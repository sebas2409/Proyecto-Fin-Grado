const {addKeyword} = require("@bot-whatsapp/bot");

const estadoPedido = addKeyword('Estado de mi pedido', {})
    .addAnswer('Por favor ingresa tu numero de pedido', {capture: true}, null)

module.exports = {
    flowEstadoPedido: estadoPedido
}