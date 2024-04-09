# DummyProject Backend

## **Funcionalidades**


- [x] Exibir os produtos existentes.
- [x] Cadastrar novos produtos.
- [x] Buscar produtos por categoria.
- [x] Buscar informações dos produtos por ID.
- [x] Editar informações dos produtos por ID.
- [x] Reduzir ou deletar produtos de acordo com o ID.


## **Tecnologias**


- Java 11
- MySQL
- Maven
- Docker
- Mapstruct
- Lombok
- OpenFeign cloud
- jUnit5 - Teste
- Jacoco
- Spring-cloud/ Spring-boot


## **Como instalar**

### Backend

- Baixe ou clone o repositório do backend usando git clone https://github.com/elizapimentel/DummyProducts


## Como executar

Execute no terminal `docker-compose up -d` para subir a imagem e container no Docker.

## Collection

<div> 

A `collection` possui uma coleção onde armazenamos as informações de retorno da API externa.

</div>

<p>

Exemplo de `Dado` cadastrado no [DummyJSON](https://dummyjson.com/docs/products):

</p>

```json
[
  {
    "id": 1,
    "title": "iPhone 9",
    "description": "An apple mobile which is nothing like apple",
    "price": 549,
    "discountPercentage": 12.96,
    "rating": 4.69,
    "stock": 94,
    "brand": "Apple",
    "category": "smartphones",
    "thumbnail": "...",
    "images": ["...", "...", "..."]
  }
]
```

##  ROTAS


O projeto foi estruturado seguindo modelo da estrura de Arquitetura de Software Rest/Restful, utilizando os protocolos HTTP - POST, GET, PUT, DELETE - CRUD.

<br>

###  Método GET

<div align = "center">

|  Método  |                               Rota                                |                        Descrição                        |
| :------: |:-----------------------------------------------------------------:|:-------------------------------------------------------:|
|  `GET`   |                http://localhost:8080/api/products                 |        Lista que retorna os itens da API externa        |
|  `GET`   |               http://localhost:8080/api/products/db               | Lista todos os itens que foram salvos no Banco de Dados |
|  `GET`   |               http://localhost:8080/api/products/ID               |                      Busca por ID                       |
|  `GET`   | http://localhost:8080/api/products/category?category=categoryName |             Busca por Categoria do produto              |

<br>
</div>

### Método POST

<div align = "center">

|  Método  |                    Rota                    |       Descrição        |
| :------: |:------------------------------------------:|:----------------------:|
|  `POST`  | http://localhost:8080/api/products/newProd | Cadastra novo registro |

<br>
</div>

###  Método PUT

<div align = "center">

|  Método  |                     Rota                     |                          Descrição                           |
| :------: |:--------------------------------------------:|:------------------------------------------------------------:|
|   `PUT`  | http://localhost:8080/api/products/update/ID |            Atualiza os dados dos clientes por ID             |

<br>
</div>

###  Método DELETE

<div align = "center">

|  Método  |                                           Rota                                            |                Descrição                |
| :------: |:-----------------------------------------------------------------------------------------:|:---------------------------------------:|
| `DELETE` | http://localhost:8080/api/products/ID?deleteWholeItem=false&quantityToRemove=<quantidade> | Diminui valor do estoque do item por ID |
| `DELETE` |             http://localhost:8080/api/products/delete/ID?deleteWholeItem=true             |         Deleta todo item por ID         |

<br>
</div>

## Dúvidas
<br>

<div align = "center">
<a href="https://www.linkedin.com/in/eliza-pimentel/">
<img alt="linkedin" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/>
</a> 
</div > 
