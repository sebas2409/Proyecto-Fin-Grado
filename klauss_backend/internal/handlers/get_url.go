package handlers

import (
	"fmt"
	"github.com/gin-gonic/gin"
)

func GetUrl(ctx *gin.Context) {
	phone := ctx.Param("phone")
	url := fmt.Sprintf("http://192.168.135.87:5173/?user=%s", phone)
	ctx.JSON(200, gin.H{
		"url": url,
	})
}
