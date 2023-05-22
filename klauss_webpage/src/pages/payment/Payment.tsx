import {ProductStore} from "../../store/ProductStore.ts";
import {useEffect, useState} from "react";
import {useSearchParams} from "react-router-dom";
import {
    Alert,
    AlertDescription,
    AlertIcon,
    AlertTitle,
    Box,
    Button, CloseButton,
    FormControl,
    FormLabel,
    Input,
    Text, useClipboard, useDisclosure
} from "@chakra-ui/react";

export function Payment() {

    const [params] = useSearchParams()
    const [data, setData] = useState<Order[]>([])
    const { onCopy, value, setValue, hasCopied } = useClipboard("");
    const {
        isOpen: isVisible,
        onClose,
        onOpen,
    } = useDisclosure({ defaultIsOpen: false })


    useEffect(() => {
        ProductStore.getProducts().subscribe((data) => {
            let p: Order[] = []
            data.map((product) => {
                let count = 0
                data.forEach((p) => {
                    if (p.id === product.id) {
                        count++
                    }
                })
                p.push({
                    nombre: product.name,
                    cantidad: count,
                    precio: product.price,
                })
            })
            let unique = p.filter((v, i, a) => a.findIndex(t => (t.nombre === v.nombre)) === i)
            setData(unique)
        })
    }, [])

    const calculateTotal = () => {
        let total = 0
        ProductStore.getProducts().subscribe((data) => {
            data.forEach((product) => {
                total += product.price
            })
        })
        return total.toString()
    }

    return (
        <>
            <nav style={{
                height: '100px',
                background: '#81a5ff',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                gap: '50px',
                padding: '0 20px',
            }}>
                <Text>{params.get("user")}</Text>
                <Text fontSize='xl' as='b'>Total {calculateTotal()}$</Text>
            </nav>
            <div style={{
                margin: '20px',
                display: 'flex',
                flexDirection: 'column',
                gap: '50px',
            }}>
                <FormControl>
                    <FormLabel>Número de cuenta</FormLabel>
                    <Input type='number'/>
                </FormControl>
                <FormControl>
                    <FormLabel>Nombre</FormLabel>
                    <Input type='text'/>
                </FormControl>
                <FormControl>
                    <FormLabel>CVV</FormLabel>
                    <Input type='number' maxLength={3} max={3}/>
                </FormControl>
                <Button colorScheme='blue' onClick={() => {
                    if (data.length === 0) {
                        return
                    }
                    fetch('http://192.168.1.142:8080/api/v1/order/new', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            "cliente": params.get('user'),
                            "productos": data,
                        })
                    }).then((response) => response.json()).then(async (data) => {
                        setValue(data.msg)
                        onOpen()
                    })
                }}>Pagar</Button>
            </div>
            {isVisible &&
                <Alert status='success'>
                    <AlertIcon />
                    <Box style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '10px'
                    }}>
                        <AlertTitle>Pago realizado correctamente</AlertTitle>
                        <AlertDescription>
                            Por favor copia este id con el cual podras consultar el estado de tu pedido: {value}
                        </AlertDescription>
                        <Button onClick={onCopy}>{hasCopied ? "Copiado" : "Copiar"}</Button>
                    </Box>
                    <CloseButton
                        alignSelf='flex-start'
                        position='relative'
                        right={-1}
                        top={-1}
                        onClick={onClose}
                    />
                </Alert>
            }
        </>
    )
}