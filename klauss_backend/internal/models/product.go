package models

import "go.mongodb.org/mongo-driver/bson/primitive"

type Product struct {
	Id          primitive.ObjectID `json:"id" bson:"_id"`
	Name        string             `json:"name" bson:"nombre"`
	Price       int32              `json:"price" bson:"precio"`
	Url         string             `json:"url" bson:"url"`
	Ingredients []Ingredient       `json:"ingredients" bson:"ingredientes"`
}

type Ingredient struct {
	Quantity int32  `json:"quantity" bson:"cantidad"`
	Name     string `json:"name" bson:"nombre"`
}
