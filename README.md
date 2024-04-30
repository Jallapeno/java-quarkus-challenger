# PAN generator java quarkus v1.0.0

## Para rodar local

1 - rode o seguinte comando ``` $ docker network create dock-dbpans-test ``` esse comando cria uma network necessária para caso queira executar tudo no docker.

2 - rode o seguinte comando ``` $ docker-compose up -d ``` para subir o banco de dados postgres no docker.

3 - dentro de cada MS <b>/generator</b> e <b>/report</b> procure em /src/main/resources e altere o <b>application.properties</b> na linha que possui ``` quarkus.datasource.jdbc.url=jdbc:postgresql://dbpans:5432/desafiobb ``` altere para ``` quarkus.datasource.jdbc.url=jdbc:postgresql://127.0.0.1:5432/desafiobb ```.

4 - crie o executavel nativo para rodar com o GraalVM;

4.1 - entre na pasta do serviço <b>/generator</b> ``` $ cd generator/ ```;

4.2 - comando para criar o executavel ``` mvn clean package -Pnative -DskipTests ```;

4.3 - navegue até a pasta target e execute ``` ./generator-1.0.0-SNAPSHOT-runner ```;

4.4 - faça o mesmo para o serviço na pasta <b>/report</b>.

## Para executar tudo no docker

Obs: deixe <b>application.properties</b> na forma original caso tenha alterado se não apenas ignora.
<hr>

1 - navegue até a pasta do serviço <b>/generator</b> ``` $ cd generator/ ```.

2 - rode o seguinte comando dentro da pasta - ``` $ ./mvnw clean package -Dquarkus.container-image.build=true -DskipTests ```
para gerar a imagem docker de generator.

3 - entre na pasta do serviço <b>/report</b>  ``` $ cd ../report/ ```.

4 - rode o seguinte comando dentro da pasta - ``` $ ./mvnw clean package -Dquarkus.container-image.build=true -DskipTests ``` para gerar a imagem docker de report.

5 - Edite o ``` docker-compose.yml ``` na raiz do repositório;

5.1 - escomente as linhas e troque o usuário de cada <i>imagem</i> se necessário.

6 - suba o container inteiro ``` docker-compose up ```.

## CURL das rotas desse projeto

para utilizar na AWS: use o ip: <b>15.229.26.255</b>

<hr>

curl --request POST \
  --url <http://localhost:8085/v1/pans> \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.1' \
  --data '{
 "bin": 666666,
 "quantity": 10
}'

<hr>

curl --request GET \
  --url <http://localhost:8086/v1/pans/2024/04/30> \
  --header 'User-Agent: insomnia/8.6.1'
