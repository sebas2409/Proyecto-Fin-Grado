package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"klauss_backend/internal/database"
	"klauss_backend/internal/logger"
	"klauss_backend/internal/models"
)

func GetProducts(ctx *gin.Context) {
	client := database.InitDb()
	var products []models.Product
	cursor, err := client.Database("klauss_artesanal").Collection("products").Find(ctx, bson.D{})
	if err != nil {
		logger.Error("No se pudo obtener los productos")
		ctx.JSON(500, gin.H{
			"message": "No se pudo obtener los productos",
		})
		return
	}
	for cursor.Next(ctx) {
		var product models.Product
		err := cursor.Decode(&product)
		if err != nil {
			logger.Error("No se pudo decodificar el producto")
			ctx.JSON(500, gin.H{
				"message": "No se pudo obtener los productos",
			})
			return
		}
		products = append(products, product)
	}
	ctx.JSON(200, gin.H{
		"products": products,
	})
}
