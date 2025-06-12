# biblioteca-digital

Este repositório agrupa os projetos **pré-requisito** para aprovação da disciplina de Programação Orientada a Objetos (POO) do curso de Engenharia de Computação da UniFecaf.

---

## 📂 Estrutura

- **biblioteca-api/**  
  Projeto **Back-end** em Spring Boot (Java 17 + Maven) que expõe uma API REST para CRUD de livros, usando H2 em memória.

- **biblioteca-web/**  
  Projeto **Front-end** em **Vue.js 3** (Vue CLI) que consome a API da `biblioteca-api` e oferece interface de usuário.

---

## 🛠 Pré-requisitos

- **Back-end**  
  - Java 17 (JDK)  
  - Maven 3.8+  

- **Front-end**  
  - Node.js 16+ & npm (ou Yarn)  
  - Docker & Docker Compose 

---

## ⚙️ Configuração de Ambiente

1. **Copie o template de variáveis de ambiente**  
   ```bash
   cd biblioteca-api
   cp .env-example .env



2. Ajuste (se necessário) os valores em .env
# .env-example
SERVER_PORT=8080

# H2 em memória
SPRING_DATASOURCE_URL=jdbc:h2:mem:biblioteca;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=

# JPA / Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true



3. NÃO faça commit de .env ou .env-local 
Este arquivo está incluído no .gitignore, eles podem conter informações sensíveis à segurança do projeto.
Apenas o .env-example deve ser versionado.


🚀 Como Executar:

Back-end (biblioteca-api)

Via Maven 
cd biblioteca-api
./mvnw clean spring-boot:run

A API ficará disponível em http://localhost:8080.

Via Docker

cd biblioteca-api
1. caso use Docker Compose plugin
1.1 compilar o projeto:
docker compose build --no-cache
1.2 subir o container
docker coompose up

2. caso use a versão antiga 
docker-compose up --build -d



Front-end (biblioteca-web)

Execução local

cd biblioteca-web
npm install
npm run serve

A aplicação Vue roda, por padrão, em http://localhost:8081.

Via Docker

cd biblioteca-web
1. caso use Docker Compose plugin
1.1 compilar o projeto:
docker compose build --no-cache
1.2 subir o container
docker coompose up

2. caso use a versão antiga 
docker-compose up --build -d




📋 Endpoints Principais

| Verbo  | Rota              | Descrição                |
| ------ | ----------------- | ------------------------ |
| GET    | `/api/books`      | Lista todos os livros    |
| POST   | `/api/books`      | Cria um novo livro       |
| GET    | `/api/books/{id}` | Busca livro por ID       |
| PUT    | `/api/books/{id}` | Atualiza livro existente |
| DELETE | `/api/books/{id}` | Remove livro por ID      |


🧪 Testes

Back-end:
cd biblioteca-api
./mvnw test

Front-end:

cd biblioteca-web
npm test

📦 Exemplos de Uso com cURL

# 1) Listar livros
curl http://localhost:8080/api/books

# 2) Criar livro
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
        "title":"Clean Code",
        "author":"Robert C. Martin",
        "isbn":"978-0132350884",
        "availableCopies":10
      }'


🤝 Contribuindo
1. Fork deste repositório

2. Crie uma branch de feature: git checkout -b feature/nova-funcionalidade

3. Commit suas mudanças: git commit -m "feat: descrição da sua feature"

4. Push para o branch: git push origin feature/nova-funcionalidade

5. Abra um Pull Request


📄 Licença
Este projeto é parte das atividades acadêmicas da UniFecaf; sua cópia e uso estão sujeitos às políticas da instituição.