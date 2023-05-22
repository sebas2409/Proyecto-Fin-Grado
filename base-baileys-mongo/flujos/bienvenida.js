const {addKeyword} = require("@bot-whatsapp/bot");
const compra = require('./compra').flowCompra;
const estadoPedido = require('./estado_pedido').flowEstadoPedido;
const productos = require('./productos').flowProductos;

const networkCall = (context, flowDynamic) => {
    let phone = context.from
    let user = context.pushName
    fetch('http://localhost:8080/api/v1/user/check', {
        method: 'POST',
        body: JSON.stringify({
            phone: phone,
            name: user
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors'
    }).then(res => {
        res.json().then(data => console.log(data))
    })
}

module.exports = {
    flowBienvenida: addKeyword(['hola', 'buenas', 'buenos dias', 'buenas tardes', 'buenas noches'], {})
        .addAnswer('Bienvenido a Klauss Artesanal, en que te puedo ayudar?',
            {
                capture: true,
                buttons: [{body: 'Realizar una compra'}, {body: 'Ver nuestros productos'}, {body: 'Estado de mi pedido'}]
            }, async (ctx, {flowDynamic}) => {
                networkCall(ctx, flowDynamic)
                //await flowDynamic([{body: 'Realizar una compra'}, {body: 'Ver nuestros productos'}, {body: 'Estado de mi pedido'}])
            }, [compra, productos, estadoPedido])

}