const {addKeyword} = require("@bot-whatsapp/bot");
const axios = require("axios");
const {get} = require("axios");
const compra = require('./compra').flowCompra;
const getProducts = async () => {
    const {products} = await axios.get('http://localhost:8080/api/v1/product/all').then(i => i.data)
    return products.map(i => `${i.name} \n Precio:  *$ ${i.price}*`)
}

const getURL = async (phone) => {
    const {url} = await axios.get(`http://localhost:8080/api/v1/url/${phone}`).then(i => i.data)
    return url
}

const productos =
    addKeyword('Ver nuestros productos', {})
        .addAnswer("Estos son nuestros productos: ", null, async (ctx, {flowDynamic}) => {
            const products = await getProducts()
            await flowDynamic(products)
        })
        .addAnswer('Si deseas comprar escribe *comprar*', {capture: true}, async (ctx, {flowDynamic}) => {
            const url = await getURL(ctx.from)
            await flowDynamic([{body: 'Para realizar una compra, por favor ingresa a nuestra tienda online'}, {body: url}])
        })


module.exports = {
    flowProductos: productos
}