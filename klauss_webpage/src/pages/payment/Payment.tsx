import {ProductStore} from "../../store/ProductStore.ts";
import {useEffect} from "react";
import {useSearchParams} from "react-router-dom";

export function Payment() {

    const [params] = useSearchParams()

    useEffect(() => {
        ProductStore.getProducts().subscribe((data) => {
            console.log(data)
        })
    }, [])

    return (
        <div>
            <h1>Pagos - {params}</h1>
        </div>
    )
}