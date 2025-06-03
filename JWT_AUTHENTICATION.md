## Autenticação JWT

Esta API utiliza JSON Web Tokens (JWT) para proteger seus endpoints. Para acessar os endpoints protegidos, os clientes devem primeiro obter um token.

**Como obter um Token:**

1.  Envie uma requisição `POST` para o endpoint `/auth/login`.
2.  No corpo da requisição, forneça um `username` e `password` válidos.
3.  Se as credenciais estiverem corretas, a API retornará um token JWT na resposta.

**Como usar o Token:**

1.  Após obter o token, inclua-o no cabeçalho `Authorization` de todas as requisições subsequentes para endpoints protegidos.
2.  Utilize o esquema `Bearer` para incluir o token.

    **Exemplo:**
    ```
    Authorization: Bearer <seu_token_jwt_aqui>
    ```

**Expiração do Token:**

Os tokens JWT possuem um tempo de expiração. Nesta API, conforme configurado em `application.properties` (`jwt.expiration.ms=3600000`), os tokens são tipicamente válidos por 1 hora (3.600.000 milissegundos). Após a expiração do token, será necessário realizar o login novamente para obter um novo token e continuar acessando os recursos protegidos.
