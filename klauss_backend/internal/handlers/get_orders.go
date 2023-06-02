package handlers

import (
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"klauss_backend/internal/database"
	"klauss_backend/internal/models"
	"log"
)

func GetOrders(ctx *gin.Context) {
	client := database.InitDb().Database("klauss_artesanal").Collection("orders")
	cursor, err := client.Find(ctx, bson.D{})
	if err != nil {
		log.Printf("Error al buscar las ordenes: %v", err)
		ctx.JSON(500, gin.H{
			"message": "Error al buscar las ordenes",
		})
		return
	}
	var orders []models.Order
	for cursor.Next(ctx) {
		var order models.Order
		err := cursor.Decode(&order)
		if err != nil {
			log.Printf("Error al decodificar la orden: %v", err)
			ctx.JSON(500, gin.H{
				"message": "Error al decodificar la orden",
			})
			return
		}
		orders = append(orders, order)
	}
	ctx.JSON(200, orders)
}
