package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/dto"
	"klauss_backend/internal/logger"
	"klauss_backend/internal/models"
	"time"
)

func NewOrder(ctx *gin.Context) {
	var order dto.Order
	var completeOrder models.Order
	var products []models.Product
	client := database.InitDb()

	err := ctx.BindJSON(&order)
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
	completeOrder.Fecha = primitive.NewDateTimeFromTime(time.Now())
	completeOrder.Estado = "RECIBIDO"
	completeOrder.Total = 0
	for _, producto := range completeOrder.Productos {
		completeOrder.Total += producto.Precio * producto.Cantidad
	}
	one, err := client.
		Database("klauss_artesanal").
		Collection("orders").
		InsertOne(ctx, completeOrder)

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
					logger.Error("No se pudo encontrar el producto en el stock por que: " + err.Error())
					return
				}
				stock.Cantidad -= producto.Ingredients[i].Quantity
				_, err2 :=
					client.Database("klauss_artesanal").
						Collection("stock").
						UpdateOne(ctx, bson.M{"nombre": bson.M{"$eq": ingredient.Name}}, bson.M{"$set": bson.M{"cantidad": stock.Cantidad}})
				if err2 != nil {
					logger.Error("No se pudo actualizar el stock por que: " + err.Error())
					return
				}
			}
		}
		logger.Info("Orden insertada: " + one.InsertedID.(primitive.ObjectID).Hex())
		ctx.JSON(202, gin.H{
			"message": "Orden recibida",
		})
	}
}
