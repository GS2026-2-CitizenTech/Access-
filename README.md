# Access+

## API de restaurantes

Consultas públicas:

- `GET /restaurantes` lista somente restaurantes ativos e publicados.
- `GET /restaurantes/{id}` retorna somente um restaurante ativo e publicado.

Operações de manutenção (ainda sem autorização):

- `POST /restaurantes` cadastra um restaurante ativo e não publicado.
- `PUT /restaurantes/{id}` atualiza integralmente nome, bairro, endereço e descrição.
- `PATCH /restaurantes/{id}` atualiza somente os campos enviados.
- `PATCH /restaurantes/{id}/desativar` e `PATCH /restaurantes/{id}/reativar` alteram a ativação.
- `PATCH /restaurantes/{id}/publicar` e `PATCH /restaurantes/{id}/despublicar` alteram a publicação.
- `DELETE /restaurantes/{id}` realiza exclusão lógica ao desativar o registro; não há exclusão física.

Os endpoints de manutenção não têm autenticação/autorização nesta etapa e não devem ser considerados seguros para uso público ou produção. A autorização deve protegê-los antes do deploy.

Uma mudança de localização pode ser uma correção cadastral ou a transferência real de estabelecimento. A modelagem já possui `restauranteAnterior`, mas ainda não existe uma operação específica que diferencie os dois casos e preserve automaticamente o histórico. Essa decisão permanece pendente para não alterar arbitrariamente dados, vínculos futuros de avaliações ou o script Oracle nesta etapa.

## API de usuários

Operações administrativas (ainda sem autorização):

- `POST /usuarios` cadastra usuários dos tipos `CONSUMIDOR` ou `RESTAURANTE`.
- `GET /usuarios` lista os usuários para fins administrativos.
- `GET /usuarios/{id}` busca um usuário para fins administrativos.

O cadastro valida nome, e-mail, senha e tipo de usuário, normaliza o e-mail e impede duplicidade sem diferenciar maiúsculas de minúsculas. A senha é persistida somente como hash BCrypt e nunca é incluída nas respostas. O tipo enviado no cadastro é apenas um dado cadastral e não concede autorização. Estes endpoints precisam de autenticação e autorização antes de qualquer uso em produção.

## Recursos de acessibilidade

Consultas públicas:

- `GET /recursos-acessibilidade` lista o catálogo predefinido; use `?categoria=FISICA`, `VISUAL`, `AUDITIVA` ou `SENSORIAL` para filtrar.
- `GET /recursos-acessibilidade/{idRecurso}/restaurantes` lista somente restaurantes ativos e publicados que declararam `SIM` para o recurso.
- `GET /restaurantes/{idRestaurante}/recursos-acessibilidade` lista as declarações de um restaurante ativo e publicado. Uma lista vazia significa ausência de informação.

Operação de manutenção (ainda sem autorização):

- `PUT /restaurantes/{idRestaurante}/recursos-acessibilidade/{idRecurso}` cria ou atualiza a declaração atual para o par restaurante–recurso.

As declarações são autodeclaradas, não constituem certificação técnica e podem conter o status explícito `NAO_INFORMADO`. O catálogo não possui endpoint de criação para restaurantes. A alteração de declarações precisa de autenticação/autorização antes de ser exposta publicamente; `tipoUsuario` não é autorização.

O script Oracle definitivo ainda precisa definir os nomes físicos de tabelas/colunas, chaves estrangeiras, a restrição única restaurante–recurso e a estratégia de geração de IDs. Não houve teste com Oracle nesta etapa.

## Avaliações de restaurantes

Consultas públicas:

- `GET /restaurantes/{idRestaurante}/avaliacoes` lista avaliações somente quando o restaurante estiver ativo e publicado.
- `GET /avaliacoes/{idAvaliacao}` retorna avaliação somente quando o restaurante vinculado estiver ativo e publicado.

Cadastro (ainda sem autorização):

- `POST /restaurantes/{idRestaurante}/avaliacoes` recebe o identificador do usuário, nota geral, comentário opcional, data da visita opcional e notas opcionais por categoria.

As notas aceitam valores de 0 a 5 em incrementos de 0,5. A nota zero é válida e a ausência de nota por categoria não equivale a zero. A avaliação é um relato de experiência, não uma certificação de acessibilidade. O ID do usuário enviado no corpo não constitui autenticação; o POST deve ser protegido por autorização quando esse mecanismo for definido. Não existem endpoints públicos para editar ou excluir avaliações nesta etapa.

O script Oracle ainda precisa alinhar tabelas/colunas de avaliações, as FKs de restaurante e usuário, a constraint única avaliação–categoria e a geração de IDs. Não houve teste com Oracle.

## Verificações comunitárias de acessibilidade

Consultas públicas:

- `GET /avaliacoes/{idAvaliacao}/verificacoes` lista as verificações de uma avaliação somente quando o restaurante estiver ativo e publicado.
- `GET /verificacoes/{idVerificacao}` retorna uma verificação somente quando o restaurante da avaliação estiver ativo e publicado.

Cadastro (ainda sem autorização):

- `POST /avaliacoes/{idAvaliacao}/verificacoes` recebe a declaração de recurso, o resultado (`CONFIRMADO`, `CONTESTADO` ou `NAO_VERIFICADO`) e um comentário opcional.

Uma verificação é um relato comunitário associado a uma avaliação e a uma declaração existente do mesmo restaurante. Ela não altera a declaração original, as notas da avaliação nem representa certificação técnica. Há no máximo uma verificação para cada combinação avaliação–declaração. O POST deve receber autenticação/autorização antes de uso público.
