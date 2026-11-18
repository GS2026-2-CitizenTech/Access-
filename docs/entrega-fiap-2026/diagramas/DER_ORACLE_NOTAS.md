# Notas do DER Oracle

O DER em `der-oracle.mmd` foi elaborado diretamente a partir de `Database/Access_Plus.sql`.

## Unicidades e regras físicas relevantes

| Tabela | Constraint/regra |
|---|---|
| `USUARIO` | Índice único funcional `LOWER(email)`. |
| `CATEGORIA_ACESSIBILIDADE` | `codigo` único e limitado aos quatro códigos aprovados. |
| `RECURSO_ACESSIBILIDADE` | Único por combinação `categoria_id + nome`. |
| `RESTAURANTE_RECURSO` | Único por combinação `restaurante_id + recurso_id`. |
| `AVALIACAO` | `nota` entre 0 e 5, em incrementos de 0,5. |
| `AVALIACAO_CATEGORIA` | Único por `avaliacao_id + categoria_id`; nota com a mesma regra decimal. |
| `VERIFICACAO_RECURSO` | Único por `avaliacao_id + restaurante_recurso_id`. |

## Sequences Oracle

`SEQ_USUARIO`, `SEQ_CATEGORIA_ACESSIBILIDADE`, `SEQ_RECURSO_ACESSIBILIDADE`, `SEQ_RESTAURANTE`, `SEQ_RESTAURANTE_RECURSO`, `SEQ_AVALIACAO`, `SEQ_AVALIACAO_CATEGORIA` e `SEQ_VERIFICACAO_RECURSO` iniciam em 100 e incrementam de um em um.

Essas sequences não são tabelas e, por isso, não aparecem como entidades no DER.
