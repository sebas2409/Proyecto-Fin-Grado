package models

import "go.mongodb.org/mongo-driver/bson/primitive"

type Stock struct {
	Id       primitive.ObjectID `json:"id" bson:"_id"`
	Cantidad int32              `json:"cantidad" bson:"cantidad"`
	Nombre   string             `json:"nombre" bson:"nombre"`
}
