Biblioteca Digital – Sistema Integrado de Gestão de Biblioteca

Tecnologias
Back-end: Java 17, Spring Boot, JPA/Hibernate e H2
Front-end: Vue 3 com TypeScript
Contêineres: Docker e Docker Compose

Como executar

Back-end
– Navegue até a pasta do projeto:
cd biblioteca-api
– Compile e execute o Spring Boot:
./mvnw clean install
./mvnw spring-boot:run
A API ficará disponível em http://localhost:8080/api/v1

Front-end
– Navegue até a pasta do Vue:
cd biblioteca-web
– Instale dependências e execute o servidor de desenvolvimento:
npm install
npm run serve
O aplicativo Vue rola em http://localhost:8081

Front-end via Docker
– Navegue até biblioteca-web
– Com Docker Compose v2 (plugin):
docker compose build --no-cache
docker compose up
– Com Docker Compose v1:
docker-compose up --build -d

Endpoints Principais

Verbo Rota Descrição
GET /api/v1/books Lista todos os livros
POST /api/v1/books Cria um novo livro
GET /api/v1/books/{id} Busca livro por ID
PUT /api/v1/books/{id} Atualiza livro existente
DELETE /api/v1/books/{id} Remove livro por ID

GET /api/v1/categories Lista todas as categorias
POST /api/v1/categories Cria uma nova categoria
GET /api/v1/categories/{id} Busca categoria por ID
PUT /api/v1/categories/{id} Atualiza categoria existente
DELETE /api/v1/categories/{id} Remove categoria por ID

GET /api/v1/members Lista todos os membros
POST /api/v1/members Cria um novo membro
GET /api/v1/members/{id} Busca membro por ID
PUT /api/v1/members/{id} Atualiza membro existente
DELETE /api/v1/members/{id} Remove membro por ID

GET /api/v1/loans Lista todos os empréstimos
POST /api/v1/loans Cria um novo empréstimo
PATCH /api/v1/loans/{id}/return Marca como devolvido

Testes

Back-end
cd biblioteca-api
./mvnw test

Front-end
cd biblioteca-web
npm test

Exemplos de uso com cURL

Criar categoria
curl -X POST http://localhost:8080/api/v1/categories -H "Content-Type: application/json" -d {"name":"Segurança Cibernética"}

Criar livro
curl -X POST http://localhost:8080/api/v1/books -H "Content-Type: application/json" -d {"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","availableCopies":5,"category":{"id":"<UUID-da-categoria>"}}

Criar membro
curl -X POST http://localhost:8080/api/v1/members -H "Content-Type: application/json" -d {"name":"Fulano de Tal","email":"fulano@exemplo.com","phone":"(11)98765-4321"}

Criar empréstimo
curl -X POST http://localhost:8080/api/v1/loans -H "Content-Type: application/json" -d {"memberId":"<UUID-do-membro>","bookId":"<UUID-do-livro>"}

Devolução
curl -X PATCH http://localhost:8080/api/v1/loans/<UUID-do-empréstimo>/return

Este documento pode ser complementado com detalhes de variáveis de ambiente, instruções de deploy e referências aos diagramas de arquitetura.
