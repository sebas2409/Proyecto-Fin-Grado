export interface Product {
    id: string
    name: string
    price: number
    url: string
    ingredients: Ingredient[]
}

export interface Ingredient {
    quantity: number
    name: string
}