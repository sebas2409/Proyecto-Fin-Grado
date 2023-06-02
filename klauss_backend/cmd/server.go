package main

import (
	"fmt"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"klauss_backend/internal/handlers"
	"klauss_backend/internal/logger"
)

func StartServer() {
	server := gin.Default()
	server.Use(cors.Default())
	server.Use(gin.Recovery())
	server.Use(gin.Logger())

	userRoute := server.Group("/api/v1/user")
	{
		userRoute.POST("/check", handlers.CheckUser)
	}

	productsRoute := server.Group("/api/v1/product")
	{
		productsRoute.GET("/all", handlers.GetProducts)
	}

	orderRoute := server.Group("/api/v1/order")
	{
		orderRoute.GET("/all", handlers.GetOrders)
		orderRoute.GET("/state/:id", handlers.GetOrderStateById)
		orderRoute.POST("/new", handlers.NewOrder)
		orderRoute.POST("/state", handlers.UpdateOrderState)
		orderRoute.GET("/:id", handlers.GetOrderById)
	}

	stockRoute := server.Group("/api/v1/stock")
	{
		stockRoute.GET("/all", handlers.GetStock)
	}

	server.GET("/api/v1/url/:phone", handlers.GetUrl)

	err := server.Run()
	if err != nil {
		logger.Error(fmt.Sprintf("No se pudo iniciar el servidor: %s", err.Error()))
		return
	}
}
