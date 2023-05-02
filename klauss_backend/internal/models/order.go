package models

import "go.mongodb.org/mongo-driver/bson/primitive"

type Order struct {
	Id        primitive.ObjectID `json:"id" bson:"_id"`
	Productos []Product          `json:"products" bson:"productos"`
	Total     int32              `json:"total" bson:"total"`
	Cliente   string             `json:"cliente" bson:"cliente"`
	Estado    string             `json:"estado" bson:"estado"`
	Fecha     primitive.DateTime `json:"fecha" bson:"fecha"`
}

type OrderProduct struct {
	Nombre   string `json:"nombre" bson:"nombre"`
	Precio   int32  `json:"precio" bson:"precio"`
	Cantidad int32  `json:"cantidad" bson:"cantidad"`
}
