# Endpoints HTTP e rotas MVC

Status indicados correspondem ao comportamento esperado pelos controllers e pelo `GlobalExceptionHandler`. Sem autenticação, as operações administrativas ainda exigem proteção antes de qualquer exposição fora do modo público somente leitura.

## Endpoints REST

| Método | Rota | Finalidade | Entrada | Resposta esperada |
|---|---|---|---|---|
| GET | `/restaurantes` | Lista restaurantes públicos. | — | `200` e lista de `RestauranteRespostaDTO`; somente ativos/publicados. |
| GET | `/restaurantes/{id}` | Consulta restaurante público por ID. | `id` na rota. | `200` e `RestauranteRespostaDTO`; `404` se não visível/inexistente. |
| POST | `/restaurantes` | Cadastra restaurante. | `RestauranteDTO`: nome, bairro, endereço, descrição, cidade/telefone opcionais, `idResponsavel`. | `201` e `RestauranteRespostaDTO`. |
| PUT | `/restaurantes/{id}` | Atualiza integralmente campos permitidos. | `RestauranteDTO`. | `200` e `RestauranteRespostaDTO`; `400`/`404` quando aplicável. |
| PATCH | `/restaurantes/{id}` | Atualiza parcialmente campos permitidos. | `RestauranteAtualizacaoDTO`. | `200` e `RestauranteRespostaDTO`. |
| PATCH | `/restaurantes/{id}/desativar` | Desativa logicamente. | `id` na rota. | `200` e `RestauranteRespostaDTO`. |
| PATCH | `/restaurantes/{id}/publicar` | Publica restaurante ativo. | `id` na rota. | `200` e `RestauranteRespostaDTO`. |
| PATCH | `/restaurantes/{id}/despublicar` | Remove publicação. | `id` na rota. | `200` e `RestauranteRespostaDTO`. |
| PATCH | `/restaurantes/{id}/reativar` | Reativa restaurante. | `id` na rota. | `200` e `RestauranteRespostaDTO`. |
| DELETE | `/restaurantes/{id}` | Executa exclusão lógica por desativação. | `id` na rota. | `204`; não remove fisicamente o registro. |
| POST | `/usuarios` | Cadastra usuário. | `UsuarioCadastroDTO`: nome, e-mail, senha, tipo. | `201` e `UsuarioRespostaDTO`, sem senha/hash. |
| GET | `/usuarios` | Lista usuários administrativos. | — | `200` e lista de `UsuarioRespostaDTO`; não publicar sem autorização. |
| GET | `/usuarios/{id}` | Consulta usuário administrativa. | `id` na rota. | `200` e `UsuarioRespostaDTO`; `404` quando inexistente. |
| GET | `/recursos-acessibilidade` | Lista catálogo. | Query opcional `categoria` (`FISICA`, `VISUAL`, `AUDITIVA`, `SENSORIAL`). | `200` e lista de `RecursoAcessibilidadeRespostaDTO`. |
| GET | `/recursos-acessibilidade/{idRecurso}/restaurantes` | Filtra restaurantes públicos que declararam o recurso como `SIM`. | `idRecurso` na rota. | `200` e lista de `RestauranteRespostaDTO`. |
| GET | `/restaurantes/{idRestaurante}/recursos-acessibilidade` | Lista declarações públicas do restaurante. | `idRestaurante`. | `200` e lista de `RestauranteRecursoRespostaDTO`. |
| PUT | `/restaurantes/{idRestaurante}/recursos-acessibilidade/{idRecurso}` | Cria ou atualiza declaração atual. | `RestauranteRecursoDTO`: `statusDeclaracao`. | `200` e `RestauranteRecursoRespostaDTO`. |
| POST | `/restaurantes/{idRestaurante}/avaliacoes` | Cadastra avaliação pública. | `AvaliacaoCadastroDTO`: `idUsuario`, `notaGeral`, comentário/data opcionais, notas por categoria opcionais. | `201` e `AvaliacaoRespostaDTO`. |
| GET | `/restaurantes/{idRestaurante}/avaliacoes` | Lista avaliações públicas de restaurante público. | `idRestaurante`. | `200` e lista de `AvaliacaoRespostaDTO`. |
| GET | `/avaliacoes/{idAvaliacao}` | Consulta avaliação pública. | `idAvaliacao`. | `200` e `AvaliacaoRespostaDTO`; `404` quando não visível/inexistente. |
| POST | `/avaliacoes/{idAvaliacao}/verificacoes` | Cadastra verificação comunitária. | `VerificacaoRecursoCadastroDTO`: `idRestauranteRecurso`, resultado, comentário opcional. | `201` e `VerificacaoRecursoRespostaDTO`. |
| GET | `/avaliacoes/{idAvaliacao}/verificacoes` | Lista verificações públicas de uma avaliação. | `idAvaliacao`. | `200` e lista de `VerificacaoRecursoRespostaDTO`. |
| GET | `/verificacoes/{idVerificacao}` | Consulta verificação pública. | `idVerificacao`. | `200` e `VerificacaoRecursoRespostaDTO`. |

### Erros REST tratados

- `400 Bad Request`: Bean Validation, argumentos inválidos, enum/parâmetro/corpo inválido.
- `404 Not Found`: restaurante, usuário, recurso, declaração, avaliação ou verificação inexistente/não pública.
- `409 Conflict`: e-mail duplicado ou verificação duplicada para a mesma avaliação + declaração.

## Rotas MVC Thymeleaf

| Método | Rota | Template/efeito | Entrada |
|---|---|---|---|
| GET | `/app` e `/app/` | `inicio.html` | — |
| GET | `/app/explorar` | `explorar.html` | Query opcional `busca`, `recurso`. |
| GET | `/app/restaurantes/{idRestaurante}` | `detalhe-restaurante.html` | ID do restaurante público. |
| GET | `/app/restaurantes/{idRestaurante}/avaliar` | `avaliar-restaurante.html` | ID do restaurante público. |
| POST | `/app/restaurantes/{idRestaurante}/avaliar` | Salva avaliação/formulário e redireciona para detalhes (PRG); em erro, reapresenta o template. | `FormularioAvaliacaoDTO`, incluindo perfil de demonstração, notas, comentário, data e verificações opcionais. |
| GET | `/app/avaliacoes` | `avaliacoes.html` | — |
| GET | `/app/sobre` | `sobre.html` | — |

No modo público de demonstração descrito pela equipe, operações de escrita e as consultas administrativas de usuários retornam `403` no servidor.
