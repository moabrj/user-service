# user-service

Para construir o jar utilizado no container é necessário pular os testes.
```
mvn package -Dmaven.test.skip
```
Os testes são pulados nessa etapa, pois já foram executados em etapas anteriores, além disso, os testes fazem uso do banco de dados e se este não estiver ativo os teste falharão e não será gerado o package.
