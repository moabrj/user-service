# USER MICROSERVICE

## Pequeno microservice para autenticação/autorização de usuários (USER, ADMIN) com mongodb, spring (boot, security, web, data), docker e kubernetes.

## Endpoints

- Obter todos os usuários: ip-address:8000/users
- Obter um usuário: ip-address:8000/users/{id}
- Obter lista de usuários filtrados por nome e com páginação: ip-address:8000/users/search/{nome}?page={value}&size={value}
- Para os endpoints abaixo é necessário papel de ADMIN
- O usuário admin@admin.com e password 123456 foi adicionado por default
- Cadastrar usuário: ip-address:8000/users
- Atualizar usuário: ip-address:8000/users/{id}
- Deletar usuário: ip-address:8000/users/{id}

#### Observação:

O formato json é usado como payload
```
{
    "name": "Ana",
    "email": "ana@gmail.com",
    "phone": "123456",
    "address": "Rua Y numnero x",
    "password": "123456",
    "role": [
            {
                "role": "USER"
            }
        ]
}
```

O password será enciptado antes do salvamento no banco de dados.

## Para realizar o deployment diretamente do código presente no github

1. Clone o repositório para sua máquina
2. Compile o código e gere o arquivo jar
Para construir o jar utilizado no container é necessário pular os testes.
```
mvn package -Dmaven.test.skip
```
Os testes são pulados nessa etapa, pois já foram executados em etapas anteriores, além disso, os testes fazem uso do banco de dados e se este não estiver ativo os teste falharão e não será gerado o package.

3. Execute o comando docker build -t user-service:latest diretamente da pasta da aplicação
4. Execute o comando docker-compose -f docker-compose.yaml up
A execução desse comando colocará de pé três serviços: Mongodb, Mongodb Express (para fins de testes e usado em cenário de desenvolvimento) e User-service.

## Deployment com kubernetes

1. A imagem da aplicação deve ser enviada para o reposotório (Dockerhub)
2. Execute o comando:
```
kubectl apply -f deployment.yaml
```

### Observações:

1. Os containers não foram testados em nuvem com o uso de kubernetes.
