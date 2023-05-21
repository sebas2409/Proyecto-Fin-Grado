import {useEffect, useState} from "react";
import {Product} from "../../dto/Products.ts";
import {useNavigate, useSearchParams} from "react-router-dom";
import svg from '../../assets/shopping.svg'
import {
    Badge,
    Button,
    ButtonGroup,
    Card,
    CardBody,
    CardFooter,
    Heading,
    Stack,
    Text,
    Image
} from "@chakra-ui/react";
import {ProductStore} from "../../store/ProductStore.ts";

export function Home() {

    const [params] = useSearchParams()
    const user = params.get('user')
    const [items, setItems] = useState<Product[]>([])
    const [products, setProducts] = useState<Product[]>([])
    const [total, setTotal] = useState<number>(0)
    const navigate = useNavigate()

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/product/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(response => response.json())
            .then((data) => setProducts(data.products))
    }, [user])

    const calculateNumberOfSameItem = (p: Product) => {
        let numberOfItems = 0
        items.forEach((item) => {
            if (item.id === p.id) {
                numberOfItems++
            }
        })
        return numberOfItems
    }

    const Cards = () => {
        return products.map((product: Product) => {
            return (
                <div style={{
                    margin: '20px',
                }} key={product.id}>
                    <Card maxW="sm" borderRadius="lg" overflow="hidden">
                        <Image src={product.url} alt={product.name} style={{
                            height: '200px',
                        }}/>
                        <CardBody>
                            <Stack spacing={2}>
                                <Heading as="h2" size="md">{product.name}</Heading>
                                <Text as="b" fontSize="2xl" color='red.500'>${product.price}</Text>
                            </Stack>
                        </CardBody>
                        <CardFooter>
                            <div style={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                                gap: '50px',
                            }}>
                                <ButtonGroup>
                                    <Button colorScheme="blue" onClick={
                                        () => {
                                            setItems([...items, product])
                                            setTotal(total + 1)
                                        }
                                    }>Añadir al carrito</Button>
                                </ButtonGroup>
                                <Text fontSize='2xl' as='b'>
                                    x{calculateNumberOfSameItem(product)}
                                </Text>
                            </div>
                        </CardFooter>
                    </Card>
                </div>
            )
        })
    }
    const route = `/payment?user=${user}`
    return (
        <>
            <nav style={{
                height: '100px',
                background: '#81a5ff',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                gap: '50px'
            }}>
                <Text as='b' fontSize='xl'>{user}</Text>
                <div style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    gap: '10px',
                }}>
                    <img src={svg} alt="shopping car" style={{width: '30px'}} onClick={() => {
                        ProductStore.updateProducts(items)
                        navigate(route)
                    }}/>
                    <Badge colorScheme='red'>{total}</Badge>
                </div>
            </nav>
            <div>
                {Cards()}
            </div>
        </>
    )
}