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
