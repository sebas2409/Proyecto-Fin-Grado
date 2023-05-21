const {addKeyword} = require("@bot-whatsapp/bot");

const compra
    = addKeyword('Realizar una compra', {})
    .addAnswer('Para realizar una compra, por favor ingresa a nuestra tienda online: https://klaussartesanal.com/', null, null)

module.exports = {
    flowCompra: compra
}