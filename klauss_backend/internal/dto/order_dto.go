package dto

import "klauss_backend/internal/models"

type OrderId struct {
	ID string `json:"id"`
}

type Order struct {
	Productos []models.OrderProduct `json:"productos"`
	Cliente   string                `json:"cliente"`
}

type OrderState struct {
	ID     string `json:"id"`
	Estado string `json:"estado"`
}
