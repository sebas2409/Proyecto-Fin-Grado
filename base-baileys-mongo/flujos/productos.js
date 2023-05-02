const {addKeyword} = require("@bot-whatsapp/bot");
const axios = require("axios");
const {get} = require("axios");

const getProducts = async () => {
    const {products} = await axios.get('http://localhost:8080/api/v1/product/all').then(i => i.data)
    return products.map(i => `${i.name} - *$ ${i.price}*`)
}

const productos =
    addKeyword('Ver nuestros productos', {})
        .addAnswer("Estos son nuestros productos: ", null, async (ctx, {flowDynamic}) => {
            const products = await getProducts()
            await flowDynamic(products)
        })


module.exports = {
    flowProductos: productos
}