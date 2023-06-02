package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"klauss_backend/internal/database"
	"klauss_backend/internal/models"
	"log"
)

func GetStock(ctx *gin.Context) {
	client := database.InitDb().Database("klauss_artesanal").Collection("stock")
	cursor, err := client.Find(ctx, bson.D{})
	if err != nil {
		log.Printf("Error al buscar el stock: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al buscar el stock",
		})
		return
	}
	var stock []models.Stock
	for cursor.Next(ctx) {
		var product models.Stock
		err := cursor.Decode(&product)
		if err != nil {
			log.Printf("Error al decodificar el stock: %v", err)
			ctx.JSON(500, gin.H{
				"message": "Error al decodificar el stock",
			})
			return
		}
		stock = append(stock, product)
	}
	ctx.JSON(200, stock)
}
