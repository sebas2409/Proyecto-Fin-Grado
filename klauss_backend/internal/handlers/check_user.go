package handlers

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"klauss_backend/internal/database"
	"klauss_backend/internal/logger"
	"klauss_backend/internal/models"
)

func CheckUser(ctx *gin.Context) {

	client := database.InitDb()
	var user models.User
	err := ctx.BindJSON(&user)
	if err != nil {
		logger.Error(fmt.Sprintf("no se pudo leer el body"))
		return
	}
	user.Id = primitive.NewObjectID()
	logger.Info(fmt.Sprintf("se recibio el usuario: %s", user.Name))
	err = client.Database("klauss_artesanal").Collection("users").FindOne(ctx, bson.D{
		{"phone", user.Phone},
	}).Decode(&user)

	if err != nil {
		logger.Info(fmt.Sprintf("No se encontro el usuario: %s, se procedera a guardarlo en la base de datos", user.Name))
		logger.Info(fmt.Sprintf("Se le ha asignado el id %s", user.Id.String()))
		_, err := client.Database("klauss_artesanal").Collection("users").InsertOne(ctx, user)
		if err != nil {
			logger.Error(fmt.Sprintf("No se pudo guardar el usuario debido a: %s", err.Error()))
			ctx.JSON(500, gin.H{
				"message": "No se pudo guardar el usuario",
			})
		}
		ctx.JSON(202, gin.H{
			"message": "Usuario guardado correctamente",
		})
	} else {
		logger.Info(fmt.Sprintf("El usuario: %s ya existe en la base de datos", user))
		ctx.JSON(200, gin.H{
			"message": "El usuario ya es cliente de Klauss Artesanal",
		})
	}

}
