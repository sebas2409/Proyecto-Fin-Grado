package models

import "go.mongodb.org/mongo-driver/bson/primitive"

type User struct {
	Id    primitive.ObjectID `json:"id" bson:"_id"`
	Phone string             `json:"phone"`
	Name  string             `json:"name"`
}
