package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/logger"
	"klauss_backend/internal/models"
)

func GetOrderStateById(ctx *gin.Context) {
	var completeOrder models.Order
	id := ctx.Param("id")

	client := database.InitDb()
	orderId, err := primitive.ObjectIDFromHex(id)
	err = client.Database("klauss_artesanal").Collection("orders").FindOne(ctx, bson.M{"_id": bson.M{"$eq": orderId}}).Decode(&completeOrder)
	if err != nil {
		logger.Error("No se pudo encontrar la orden")
		ctx.JSON(500, gin.H{
			"message": "No se pudo encontrar la orden",
		})
		return
	}
	logger.Info("Petición de estado de orden recibida y encontrada")
	ctx.JSON(200, gin.H{
		"estado": completeOrder.Estado,
	})
}
