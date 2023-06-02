package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/models"
	"log"
)

func GetOrderById(ctx *gin.Context) {

	id := ctx.Param("id")
	client := database.InitDb().Database("klauss_artesanal").Collection("orders")

	orderId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		log.Printf("Error al convertir el id: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al convertir el id",
		})
		return
	}
	completeOrder := models.Order{}
	err = client.FindOne(ctx, bson.M{"_id": bson.M{"$eq": orderId}}).Decode(&completeOrder)
	if err != nil {
		log.Printf("Error al buscar la orden: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al buscar la orden",
		})
		return
	}

	ctx.JSON(200, completeOrder)

}
