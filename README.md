# Access+

Backend do Access+, projeto acadêmico da Global Solution FIAP 2026. A aplicação ajuda consumidores a localizar restaurantes e consultar informações autodeclaradas de acessibilidade, avaliações de experiência e verificações comunitárias.

## Módulos

1. **Usuários** — cadastro de consumidores e responsáveis por restaurantes, com senha armazenada em hash BCrypt.
2. **Restaurantes** — cadastro, atualização, publicação, ativação e exclusão lógica.
3. **Recursos de acessibilidade** — catálogo de recursos, declarações autodeclaradas e filtros públicos por recurso declarado como `SIM`.
4. **Avaliações** — nota geral e notas opcionais por categoria de acessibilidade.
5. **Verificações comunitárias** — relatos associados a uma avaliação e a uma declaração de recurso do mesmo restaurante.

Uma declaração ou verificação comunitária não é certificação técnica de acessibilidade.

## Tecnologias

- Java 21
- Spring Boot, Spring Web MVC, Spring Data JPA e Hibernate
- Bean Validation
- Oracle Database 19c com driver `ojdbc11`
- Maven Wrapper
- JUnit 5, Mockito e MockMvc

## Organização do código

O projeto segue a organização estudada na FIAP:

```text
Controller -> BO -> Repository -> Entity
                    DTOs nas fronteiras HTTP
```

- `controller`: endpoints REST e conversão entre DTOs e entidades.
- `bo`: regras de negócio e validações complementares.
- `repository`: persistência com Spring Data JPA.
- `model/entity`: mapeamentos JPA para o schema Oracle.
- `model/dto`: contratos de entrada e resposta.
- `exception`: erros de domínio e tratamento HTTP centralizado.

## Pré-requisitos

- JDK 21 configurado no `PATH`.
- Oracle Database 19c com o schema Access+ já criado.
- Acesso ao repositório Database: [accessplus-database](https://github.com/hvicente21/accessplus-database).

O Maven Wrapper está incluído; não é necessário instalar Maven globalmente.

## Configuração Oracle

Defina as variáveis de ambiente antes de iniciar a aplicação. Use os mesmos dados de conexão validados no SQL Developer, sem gravar credenciais no repositório.

O driver Oracle aceita os dois formatos abaixo para `ORACLE_DB_URL`:

```powershell
# Conexão por SID
$env:ORACLE_DB_URL = "jdbc:oracle:thin:@<host>:<porta>:<sid>"

# Conexão por Service Name
$env:ORACLE_DB_URL = "jdbc:oracle:thin:@//<host>:<porta>/<service-name>"

$env:ORACLE_DB_USERNAME = "<usuario-do-schema>"
$env:ORACLE_DB_PASSWORD = "<senha-do-schema>"
```

A validação supervisionada do projeto utilizou conexão por **SID**. Host, usuário e senha reais não são publicados neste repositório.

A aplicação lê somente essas variáveis. A configuração JPA usa `spring.jpa.hibernate.ddl-auto=validate`: ela valida o schema existente e não cria nem altera tabelas automaticamente.

## Execução

No diretório do backend, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Sem as variáveis Oracle, a inicialização completa depende de uma configuração de conexão válida.

## Testes automatizados

```powershell
.\mvnw.cmd clean test
```

Última execução local: **65 testes, 0 falhas, 0 erros e 0 ignorados**. Esses testes usam JUnit, Mockito e MockMvc; não substituem testes de integração com Oracle real.

## Principais endpoints REST

| Área | Endpoints |
|---|---|
| Restaurantes | `GET /restaurantes`, `GET /restaurantes/{id}`, `POST /restaurantes`, `PUT/PATCH /restaurantes/{id}`, `PATCH` de ativação/publicação e `DELETE /restaurantes/{id}` (lógico) |
| Usuários | `POST /usuarios`, `GET /usuarios`, `GET /usuarios/{id}` |
| Recursos | `GET /recursos-acessibilidade`, `GET /recursos-acessibilidade?categoria=FISICA`, `GET /recursos-acessibilidade/{id}/restaurantes` |
| Declarações | `GET /restaurantes/{id}/recursos-acessibilidade`, `PUT /restaurantes/{id}/recursos-acessibilidade/{idRecurso}` |
| Avaliações | `POST /restaurantes/{id}/avaliacoes`, `GET /restaurantes/{id}/avaliacoes`, `GET /avaliacoes/{id}` |
| Verificações | `POST /avaliacoes/{id}/verificacoes`, `GET /avaliacoes/{id}/verificacoes`, `GET /verificacoes/{id}` |

Consultas públicas retornam somente restaurantes ativos e publicados. As notas aceitam valores de `0` a `5` em incrementos de `0,5`. As categorias são `FISICA`, `VISUAL`, `AUDITIVA` e `SENSORIAL`; os estados de declaração são `SIM`, `NAO` e `NAO_INFORMADO`; os resultados comunitários são `CONFIRMADO`, `CONTESTADO` e `NAO_VERIFICADO`.

## Banco de dados

O DDL, DML e DQL pertencem ao repositório Database. Em um schema novo, execute os scripts nesta ordem:

1. `Access_Plus.sql` — cria a estrutura e sequences.
2. `Access_Plus_DML.sql` — insere dados fictícios de demonstração.
3. `Access_Plus_DQL.sql` — consultas acadêmicas independentes.

O DQL é independente do backend Java: ele pode ser executado no SQL Developer depois que o schema e os dados de demonstração estiverem disponíveis. A aplicação Java não executa esses scripts.

As entidades usam tabelas, colunas, FKs e sequences Oracle explícitas, incluindo `SEQ_USUARIO`, `SEQ_RESTAURANTE`, `SEQ_RECURSO_ACESSIBILIDADE`, `SEQ_RESTAURANTE_RECURSO`, `SEQ_AVALIACAO`, `SEQ_AVALIACAO_CATEGORIA` e `SEQ_VERIFICACAO_RECURSO`.

## Evidências de integração Oracle

Durante integração supervisionada no Oracle 19c da FIAP, foram validadas leituras de restaurantes, recursos e avaliações, além de cadastros fictícios de usuário, restaurante, declaração, avaliação e verificação comunitária. Os identificadores fictícios `100` foram usados nas gravações de integração. As evidências e suas limitações estão em [docs/EVIDENCIAS_INTEGRACAO_ORACLE.md](docs/EVIDENCIAS_INTEGRACAO_ORACLE.md).

## Limitações conhecidas do MVP

- Não há autenticação nem autorização. Endpoints de escrita e consultas administrativas de usuários não são seguros para exposição pública.
- O identificador de usuário recebido no cadastro de avaliação não comprova identidade ou permissão.
- O backend atual recebe o identificador de usuário no cadastro de avaliações. A RN15 de `REGRAS_NEGOCIO.md` descreve inicialmente um fluxo por nome de exibição sem autenticação; essa divergência documental permanece pendente e não altera o contrato atual da API.
- Não há edição ou exclusão pública de avaliações e verificações.
- Verificações comunitárias são relatos e não alteram automaticamente a declaração do restaurante.
- Uma operação específica para transferência real de estabelecimento e histórico de localização ainda depende de definição da equipe.
- Os testes automatizados não cobrem todos os cenários possíveis de Oracle; as evidências reais registradas não representam validação para produção.

Não inclua credenciais em `application.properties`, arquivos de teste, documentação ou commits.
