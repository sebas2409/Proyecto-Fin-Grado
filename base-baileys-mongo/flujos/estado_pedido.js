const {addKeyword} = require("@bot-whatsapp/bot");

const estadoPedido = addKeyword('Estado de mi pedido', {})
    .addAnswer('Por favor ingresa tu numero de pedido', {capture: true}, (ctx, {flowDynamic}) => {
        let mensaje = ctx.body
        fetch(`http://localhost:8080/api/v1/order/state/${mensaje}`, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json().then(data => {
            flowDynamic(`El estado de tu pedido es: *${data.estado.toUpperCase()}* el tiempo aproximado de entrega es de *30 minutos*`)
        }))
    })

module.exports = {
    flowEstadoPedido: estadoPedido
}