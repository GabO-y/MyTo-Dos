# Como Rodar o Projeto

Para rodar este projeto localmente:

### Pré-requisitos

* **JDK 24** 
* **Node.js e npm**
* Uma IDE com suporte a Spring Boot (vou ensinar com Intellij por ser mais simples).

### Backend

Link do Intellij: https://www.jetbrains.com/idea/download/?section=linux, o Intellij Community já é o suficiente
1. Abra o diretório com o repositório, nela deve ter o **./backend** e o **./my-todo-frontend**
2. Vá no **backend** e siga até o arquivo de run: **./backend/MyTo-Dos/src/main/java/rz/springboot/mytodos/MyToDosApplication.java**

A partir daqui, o Intellij já deve ter começado ou vai começar a ajeitar as dependencias, caso ele termine, tente rodar no botão de play verde, no canto superior, caso não rode, pode ser as dependencias que nao carregaram direito, a versão errada do Java, ou um espaço que deixa mal formatado os diretórios

- **Espaço onde não devia**: na pasta que contém  as pastas de backend e frontend, verifique se há um espaço desnecessário, 

    tipo: [MyToDosProject ] ->  [MyToDosProject]

- **Versão errada do Java**: a versão tem que se a 24, geralmente, se é a primeira vez que você está usando, ele pede pra configurar o JDK, basta selecionar a 24 e esperar baixa.

Se não for o caso, você precisa:    

ir nas 3 barrinhas no canto superior esquedo, do lado do ícone do Intellij

lá tem a opção de **Project Structere**
 
lá você consegue mexer a vesão a versão do Java, na parte de SDK, procure por alguma versão 24, baixe é deixei como a versão do projeto

Depois tente rodar novamente

-**Dependencias o Maven**: caso ele não ajeite por padrão, na barra lateral direita, tem um ícone de M, clicando nele, abrirá uma janelinha com algumas funções, a primeira dela no canto superior esquerdo (duas flechinas se apontando em circulo), lá tem a opção de **Reload All Maven Project**, clique nessa opção

Por fim e precaução, ainda no diretório backend/MyTo-Dos, no final, tem um arquivo chamado **pom.xml**, abra ele e procure por 
```
<source>23</source>
<target>23</target>
```

Esse trecho fica lá em baixo, recomendo usar a ferramenta de pesquisa com Ctrl+f

Caso realmente esteja 23, mude os dois para 24

Esses foram os problemas que passei tentando rodar em outras maquinas


 Agora inicie, o backend será iniciado na porta padrão `8080`.

### Frontend

Aqui será necessário o npm instalado

1.  Navegue até o diretório `my-todo-frontend` do projeto.

2.  Instale as dependências com:
    ```bash
    npm install
    ```
3.  Inicie a aplicação com:
    ```bash
    npm run dev
    ```
    O frontend será executado em `http://localhost:5173`.

---


# MyTo-Dos

## 📝 Descrição do Projeto

**MyTo-Dos** é uma aplicação **fullstack** para gerenciamento de tarefas numa base de relação de cards e tasks, desenvolvida em **Spring Boot** e um frontend o em **React**. 

O projeto utiliza **JWT** para autenticação e uma **arquitetura RESTful** para a comunicação entre o frontend e o backend.

---

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 24:** Linguagem de programação.
* **Spring Boot:** Framework para o desenvolvimento do backend.
* **Spring Security:** Para autenticação e autorização via JWT.
* **H2-Database:** Banco de dados em memória para agilizar o desenvolvimento e os testes.
* **Maven:** Gerenciador de dependências.

### Frontend
* **React + Vite:** Biblioteca e ferramenta de build para o desenvolvimento da interface do usuário.
* **axios:** Cliente HTTP para comunicação com a API.

---


## 🗺️ Rotas Principais da API

Todas as rotas do backend se iniciam de ```http://localhost:8080/``` 

Primeiramente, é necessário ter um token Jwt para as requisições, que você consegue, acessando as rotas **auth** que são abertas, se você estiver usando o Postman, pode fazer a requisição no ```auth/register```, pegar o token e usar nas próximas requisições, indo em **Authorization**, selececionando **bearer token** e colando seu token lá

Também pode pegar o token do admin:
* username: ```admin```
* password: ```admin123```



Rotas
-
### Autenticação (`/auth`)
* `POST /auth/register`: Cria um novo usuário.
    * **Body:** `{ "username": "...", "password": "..." }`
* `POST /auth/authenticate`: Gera um token JWT para um usuário.
    * **Body:** `{ "username": "...", "password": "..." }`
    * **Resposta:** `{ "token": "..." }`

### Cards (`/cards`)
* `GET /cards`: Lista todos os cards do usuário autenticado.
* `POST /cards`: Cria um novo card.
* `GET /cards/{id}`: Retorna um card específico pelo ID.
* `PUT /cards`: Edita um card existente.
* `DELETE /cards/{id}`: Exclui um card pelo ID.

### Tasks (`/tasks`)
* `GET /tasks`: Lista todas as tarefas do usuário autenticado (paginado).
* `GET /tasks/{id}`: Retorna uma tarefa específica pelo ID.
* `POST /tasks`: Cria uma nova tarefa.
* `PUT /tasks`: Edita uma tarefa existente.
* `DELETE /tasks/{id}`: Exclui uma tarefa pelo ID.

---

## Banco de Dados

Foi utilizado o H2-Database, por ser em memória, sendo rápido e fácil de configurar

Acesse ele em 

```
http://localhost:8080/h2-console
```

De inicio, será mostrado uma tela pra se conectar ao banco

No **main/resources/application.properties** contém as propriedades do banco, mas pra simplificar
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:mytodosdb
User Name: sa
Password: (Não tem)
```

Com essas mesmas opções, você será capaz de acessar e utiliza-lo

---

## Interface

Ela tem no total, 3 telas:

### Login e Registro

ambas pedem a mesma coisa, um **username** e uma **password**, com isso, você pega seu token JWT usado no backend que lhe permite fazer as requições

você consegue também, ir de um para o outro


## board

Essa tela é a principal, aqui que você visualiza suas tarefas, cria elas, etc...

As tarefas são dividas por **Cards**, eles são como grupos que você pode dividir para tarefas de determinado tipo

As **Tasks** serão visualizadas nele 

### Para adicionar:

- **Card**: botão azul, no canto superior esquerdo
- **Task**: Dentro do card, no lado esquedo, tem um botão de +

### Para remover

- **Card**: Dentro do Card, há um botão de x no canto superior direito
- **Task**: Também dentro do Card, em cima de seu nome, no ícone de lixeira

### Para editar: 
- **Card**: Basta clicar em seu nome e confirmar
- **Task**: Clique duas vezes no seu nome e corfirme

### Expandir a descrição:
- **Card**: Clique em alguma área do cartão, com entre os botões de adicionar **Task** e apagar **Card** (o '+', e o 'x'), pode ser na parte de baixo também, ele deve aparecer

- **Task**: Clique uma vez sobre a **Task**, caso tenha, ele expande

### Concluir a **Taks**:
Basta clicar no ícone de "V", que ela ficará com um traço em sí, indicando a conclusão

### Logout

No canto superior direito, há um botão vermelho para sair de sua conta

    
