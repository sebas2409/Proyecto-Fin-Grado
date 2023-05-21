import {Product} from "../dto/Products.ts";
import {BehaviorSubject} from "rxjs";

const products$ = new BehaviorSubject<Product[]>([]);
export const ProductStore = {
    updateProducts: (products: Product[]) => {
        products$.next(products);
    },
    getProducts: () => products$.asObservable(),
    clearProducts: () => {
        products$.next([])
    },
}