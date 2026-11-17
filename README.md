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
