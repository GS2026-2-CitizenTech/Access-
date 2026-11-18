# Access+ — documentação técnica final

## Capa — dados a preencher

| Campo | Conteúdo |
|---|---|
| Disciplina | Domain Driven Design — Java |
| Entrega | Global Solution — 2º semestre de 2026 |
| Projeto | Access+ |
| Instituição | FIAP |
| Integrantes e RMs | **Preencher com os dados aprovados pela equipe.** |
| Repositório backend | [GS2026-2-CitizenTech/Access-](https://github.com/GS2026-2-CitizenTech/Access-) |
| Repositório Database | [hvicente21/accessplus-database](https://github.com/hvicente21/accessplus-database) |
| Demonstração pública | [access-v4nk.onrender.com/app](https://access-v4nk.onrender.com/app) |

## Índice sugerido para o PDF

1. Apresentação, problema, justificativa e objetivos
2. Público-alvo, escopo e funcionamento
3. Arquitetura e módulos Java
4. Diagrama de classes
5. Modelo relacional Oracle (DER)
6. Endpoints REST e rotas MVC
7. Protótipos/telas
8. Execução, testes e deploy
9. Limitações, segurança e pendências

## 1. Apresentação do Access+

O Access+ é uma plataforma colaborativa de acessibilidade gastronômica. A solução permite consultar restaurantes públicos, os recursos de acessibilidade que foram autodeclarados pelos estabelecimentos, avaliações de consumidores e verificações comunitárias ligadas a esses recursos.

O problema abordado é a dificuldade de encontrar informação estruturada e contextualizada sobre acessibilidade antes de visitar um restaurante. A plataforma não apresenta declarações ou verificações como certificação técnica: o estabelecimento informa seus recursos e a comunidade pode registrar relatos sobre o que observou.

### Justificativa

Pessoas com necessidades de acessibilidade podem encontrar barreiras físicas, visuais, auditivas e sensoriais ao planejar experiências gastronômicas. Centralizar informações autodeclaradas, avaliações e relatos comunitários reduz a incerteza na decisão de visita, preservando a origem de cada informação.

### Objetivos

- Disponibilizar catálogo de recursos de acessibilidade por categoria.
- Exibir apenas restaurantes ativos e publicados nas consultas públicas.
- Permitir que responsáveis declarem recursos sem que isso seja tratado como certificação.
- Registrar avaliações com nota geral e notas opcionais por categoria em incrementos de 0,5.
- Associar verificações comunitárias a uma avaliação e a uma declaração de recurso do mesmo restaurante.
- Oferecer uma interface mobile-first em Spring MVC/Thymeleaf para demonstração acadêmica.

### Público-alvo

- Consumidores que desejam planejar uma visita considerando necessidades de acessibilidade.
- Responsáveis por restaurantes que autodeclaram recursos disponíveis.
- Comunidade que compartilha experiências, avaliações e verificações não certificadoras.

## 2. Funcionamento dos módulos

| Módulo | Responsabilidade implementada |
|---|---|
| Usuários | Cadastro de consumidor ou responsável por restaurante; senha armazenada com hash BCrypt e nunca retornada nos DTOs de resposta. |
| Restaurantes | Cadastro com responsável, atualização, ativação, publicação, despublicação e exclusão lógica. Novos registros iniciam ativos e não publicados. |
| Recursos de acessibilidade | Catálogo predefinido, associado às categorias `FISICA`, `VISUAL`, `AUDITIVA` e `SENSORIAL`; recurso não é criado pelo restaurante. |
| Declarações | Relação única restaurante + recurso, com status `SIM`, `NAO` ou `NAO_INFORMADO`, data de atualização e origem autodeclarada. |
| Avaliações e verificações | Avaliação tem nota geral, comentário/data de visita opcionais e até quatro notas por categoria. Verificação comunitária tem resultado `CONFIRMADO`, `CONTESTADO` ou `NAO_VERIFICADO` e não modifica automaticamente a declaração original. |

## 3. Arquitetura Java

O backend segue a organização adotada nas aulas:

```text
Controller → BO → Repository → Entity
              ↘ DTOs nas fronteiras HTTP
```

- **Controllers REST** expõem recursos de restaurantes, usuários, catálogo, declarações, avaliações e verificações.
- **PaginaController** oferece as rotas MVC para os templates Thymeleaf e o formulário de avaliação.
- **BOs** concentram regras como visibilidade pública, intervalos de nota, e-mail único, vínculo avaliação/declaracão e unicidade das verificações.
- **Repositories** usam Spring Data JPA.
- **Entities** usam tabelas, FKs e sequences Oracle declaradas explicitamente.
- **GlobalExceptionHandler** traduz validação, argumentos inválidos, inexistência e conflitos de duplicidade em respostas HTTP coerentes.

As fontes editáveis do diagrama de domínio e da visão de camadas estão em [diagramas](diagramas/README.md).

## 4. Integração Oracle

O DDL lido em `Access_Plus.sql` possui oito tabelas e oito sequences iniciadas em 100. O DDL declara compatibilidade conservadora com Oracle 11g; a evidência de integração do projeto registra validação supervisionada no Oracle 19c da FIAP.

As entidades utilizam `GenerationType.SEQUENCE` com `allocationSize = 1`. A configuração usa variáveis de ambiente `ORACLE_DB_URL`, `ORACLE_DB_USERNAME` e `ORACLE_DB_PASSWORD`, com `spring.jpa.hibernate.ddl-auto=validate`. Assim, o Hibernate valida o schema existente e não cria ou altera tabelas automaticamente.

Os formatos JDBC documentados são:

```text
# SID
jdbc:oracle:thin:@<host>:<porta>:<sid>

# Service Name
jdbc:oracle:thin:@//<host>:<porta>/<service-name>
```

Não há host, usuário, senha ou outro segredo neste documento.

## 5. Front-end Thymeleaf

As telas são renderizadas por templates Thymeleaf e usam HTML, CSS e JavaScript puro. A navegação e os cards foram concebidos como mobile-first. O formulário de avaliação possui componente de cinco estrelas que aceita incrementos de 0,5, distinção entre nota zero e categoria não informada, e controles acessíveis por rádio.

As rotas MVC são documentadas em [ENDPOINTS.md](ENDPOINTS.md). O roteiro de imagens reais necessárias ao PDF está em [SCREENSHOTS.md](SCREENSHOTS.md).

## 6. Deploy com Docker e Render

Foi inspecionada a configuração da branch remota `origin/feature/deploy-render`. Ela usa Docker multi-stage: Maven com JDK 21 na etapa de build e JRE 21 na imagem final, que executa o JAR Spring Boot. O serviço usa `server.port=${PORT:8080}`; o Render fornece `PORT` e a execução local mantém a porta 8080 por padrão.

Para a demonstração pública, a configuração de deploy prevê `ACCESSPLUS_DEMO_PUBLICO_SOMENTE_LEITURA=true`. Segundo a evidência da equipe, esse modo mantém leitura pública e bloqueia operações de escrita e consultas administrativas de usuários com HTTP 403. Isso é uma proteção temporária de demonstração, não substitui autenticação/autorização.

## 7. Tecnologias e versões observadas

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem e runtime do backend. |
| Spring Boot 4.1.2 | Versão declarada no `pom.xml` observado. |
| Spring Web MVC e Thymeleaf | Rotas MVC e templates HTML. |
| Spring Data JPA / Hibernate | Persistência e mapeamento JPA. |
| Bean Validation | Validação de DTOs e formulário MVC. |
| Oracle Database 19c | Banco informado para a integração FIAP. |
| `ojdbc11` | Driver JDBC Oracle em runtime. |
| Maven Wrapper | Build e testes. |
| JUnit 5, Mockito e MockMvc | Testes automatizados. |
| Docker / Render | Empacotamento e publicação do serviço. |

## 8. Execução local

1. Instalar JDK 21 e clonar o repositório backend.
2. Criar ou acessar um schema Oracle compatível usando o repositório Database, conforme autorização da equipe.
3. Definir as variáveis de ambiente sem gravar credenciais no código:

   ```powershell
   $env:ORACLE_DB_URL = "jdbc:oracle:thin:@<host>:<porta>:<sid>"
   $env:ORACLE_DB_USERNAME = "<usuario-do-schema>"
   $env:ORACLE_DB_PASSWORD = "<senha-do-schema>"
   ```

4. Executar testes automatizados:

   ```powershell
   .\mvnw.cmd clean test
   ```

5. Iniciar a aplicação:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

6. Abrir `http://localhost:8080/app`.

Para a demonstração somente leitura, definir `ACCESSPLUS_DEMO_PUBLICO_SOMENTE_LEITURA=true` na configuração de deploy correspondente. Não use credenciais reais em capturas, documentação ou commits.

## 9. Testes e evidências

O último relatório informado pela equipe registra **81 testes aprovados, 0 falhas e 0 erros**. Esse número é evidência da última execução conhecida e **não foi reexecutado nesta etapa documental**.

Os testes existentes cobrem regras de BO, controllers REST, MockMvc, renderização do formulário, validações, coleções JPA, verificações comunitárias e o bloqueio público de escrita/consultas administrativas no perfil de demonstração.

As evidências de integração Oracle estão em [EVIDENCIAS_INTEGRACAO_ORACLE.md](../EVIDENCIAS_INTEGRACAO_ORACLE.md). A URL pública fornecida pela equipe é [access-v4nk.onrender.com/app](https://access-v4nk.onrender.com/app); a navegação pública e o HTTP 403 para consultas administrativas foram informados como validados.

## 10. Limitações acadêmicas e de segurança

- Não existe autenticação, JWT, sessão ou autorização por perfil no MVP.
- O ID de usuário recebido no cadastro de avaliação não prova identidade ou permissão.
- O modo público somente leitura é uma barreira temporária de deploy; não equivale a controle de acesso completo.
- Declarações e verificações comunitárias não são certificação técnica de acessibilidade.
- Não há edição/exclusão pública de avaliações ou verificações.
- A definição de transferência real de estabelecimento e preservação de histórico de localização depende de decisão futura da equipe.
- A divergência entre a RN15 (nome de exibição inicial) e o contrato atual de avaliações (ID de usuário) permanece documentada no README do projeto.
