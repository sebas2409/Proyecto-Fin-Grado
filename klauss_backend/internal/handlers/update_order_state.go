package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/dto"
	"klauss_backend/internal/logger"
)

func UpdateOrderState(ctx *gin.Context) {
	var stateDto dto.OrderState
	client := database.InitDb()
	err := ctx.BindJSON(&stateDto)
	if err != nil {
		logger.Error("Error al parsear el body debido a que: " + err.Error())
		ctx.JSON(500, gin.H{
			"message": "Error al parsear el body",
		})
		return
	}
	var order dto.Order
	id, _ := primitive.ObjectIDFromHex(stateDto.ID)
	err2 := client.
		Database("klauss_artesanal").
		Collection("orders").
		FindOneAndUpdate(ctx, bson.M{"_id": bson.M{"$eq": id}}, bson.M{"$set": bson.M{"estado": stateDto.Estado}}).
		Decode(&order)

	if err2 != nil {
		logger.Error("Error al actualizar el estado de la orden debido a que: " + err2.Error())
		ctx.JSON(500, gin.H{
			"message": "Error al actualizar el estado de la orden",
		})
		return
	} else {
		logger.Info("Estado de orden actualizado")
		ctx.JSON(200, gin.H{
			"message": "Estado de orden actualizado",
		})
		return
	}

}
