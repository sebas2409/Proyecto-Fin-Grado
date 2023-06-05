package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/models"
	"log"
)

func UpdateStock(ctx *gin.Context) {
	dto := models.StockDto{}
	stock := models.Stock{}
	client := database.InitDb().Database("klauss_artesanal").Collection("stock")
	err := ctx.BindJSON(&dto)
	if err != nil {
		log.Printf("Error al decodificar el stock: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al decodificar el stock",
		})
		return
	}
	id, _ := primitive.ObjectIDFromHex(dto.Id)
	err = client.FindOne(ctx, bson.M{"_id": id}).Decode(&stock)
	if err != nil {
		log.Printf("Error al buscar el stock: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al buscar el stock",
		})
		return
	}
	nuevoStock := stock.Cantidad + dto.Cantidad
	_, err = client.UpdateOne(ctx, bson.M{"_id": id}, bson.M{"$set": bson.M{"cantidad": nuevoStock}})
	if err != nil {
		log.Printf("Error al actualizar el stock: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al actualizar el stock",
		})
		return
	}
	ctx.JSON(200, gin.H{
		"message": "Stock actualizado",
	})

}
