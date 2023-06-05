package handlers

import (
	"fmt"
	"github.com/gin-gonic/gin"
	pushnotifications "github.com/pusher/push-notifications-go"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/dto"
	"klauss_backend/internal/logger"
	"klauss_backend/internal/models"
	"time"
)

const instanceId = "7de718c8-4fbe-4413-b02d-4bca1430dcc8"
const key = "8851289F2124013D40C6FA0C3A3B635E9AC539FF40B2DAADC5BF4A697D3E0A59"

func NewOrder(ctx *gin.Context) {
	var order dto.Order
	var completeOrder models.Order
	var products []models.Product
	client := database.InitDb()

	beamsClient, err := pushnotifications.New(instanceId, key)
	if err != nil {
		fmt.Println("Could not create Beams Client:", err.Error())
	}

	err = ctx.BindJSON(&order)
	if err != nil {
		logger.Error("No se pudo leer el body porque: " + err.Error())
		return
	}

	for _, producto := range order.Productos {
		cursor, err := client.Database("klauss_artesanal").
			Collection("products").
			Find(ctx, bson.M{"nombre": bson.M{"$eq": producto.Nombre}})
		if err != nil {
			logger.Error("No se pudo encontrar el producto")
			return
		}
		for cursor.Next(ctx) {
			var product models.Product
			err := cursor.Decode(&product)
			if err != nil {
				logger.Error("No se pudo decodificar el producto")
				return
			}
			for i, ingredient := range product.Ingredients {
				product.Ingredients[i].Quantity = ingredient.Quantity * producto.Cantidad
			}
			products = append(products, product)
		}
	}

	completeOrder.Cliente = order.Cliente
	completeOrder.Productos = order.Productos
	completeOrder.Id = primitive.NewObjectID()
	completeOrder.Fecha = time.Now().Format("2006-01-02 15:04")
	completeOrder.Estado = "RECIBIDO"
	completeOrder.Total = 0
	for _, producto := range completeOrder.Productos {
		completeOrder.Total += producto.Precio * producto.Cantidad
	}
	one, err := client.
		Database("klauss_artesanal").
		Collection("orders").
		InsertOne(ctx, completeOrder)
	id := one.InsertedID.(primitive.ObjectID).Hex()
	if err != nil {
		logger.Error("No se pudo insertar la orden")
		return
	} else {

		for _, producto := range products {
			for i, ingredient := range producto.Ingredients {
				var stock models.Stock
				err := client.
					Database("klauss_artesanal").
					Collection("stock").
					FindOne(ctx, bson.M{"nombre": bson.M{"$eq": ingredient.Name}}).
					Decode(&stock)

				if err != nil {
					logger.Error(fmt.Sprintf("No se pudo encontrar el producto %s en el stock por que: ", ingredient.Name) + err.Error())
					return
				}
				stock.Cantidad -= producto.Ingredients[i].Quantity
				_, err2 :=
					client.Database("klauss_artesanal").
						Collection("stock").
						UpdateOne(ctx, bson.M{"nombre": bson.M{"$eq": ingredient.Name}}, bson.M{"$set": bson.M{"cantidad": stock.Cantidad}})
				if err2 != nil {
					logger.Error("No se pudo actualizar el stock por que: " + err2.Error())
					return
				}
			}
		}
		logger.Info(fmt.Sprintf("Se inserto la orden con id: %s", id))

		publishRequest := map[string]interface{}{
			"fcm": map[string]interface{}{
				/* para que se envie una notificacion en segundo plano personalizada quitar tod0 lo de la notificacion */

				"notification": map[string]interface{}{
					"title": "Nuevo PEDIDO",
					"body":  "HAY UN NUEVO PEDIDO",
				},
				//"data": map[string]interface{}{
				//	"cuerpoMensaje": "mensaje bienvenida",
				//	"producto1":     "llantas",
				//	"producto2":     "neumaticos",
				//},
			},
		}

		pubId, err := beamsClient.PublishToInterests([]string{"hello"}, publishRequest)
		if err != nil {
			fmt.Println(err)
		} else {
			fmt.Println("Publish Id:", pubId)
		}

		ctx.JSON(202, gin.H{
			"msg": id,
		})
	}
}
