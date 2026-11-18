# Evidências de integração Oracle

## Escopo da validação

As evidências desta página foram obtidas em integração supervisionada com **Oracle Database 19c da FIAP**. Não há credenciais, hosts, portas ou nomes de serviço registrados neste documento.

O schema Access+ contém as tabelas `USUARIO`, `CATEGORIA_ACESSIBILIDADE`, `RECURSO_ACESSIBILIDADE`, `RESTAURANTE`, `RESTAURANTE_RECURSO`, `AVALIACAO`, `AVALIACAO_CATEGORIA` e `VERIFICACAO_RECURSO`, com sequences Oracle para os identificadores técnicos.

## Testes automatizados locais

Os testes locais executam JUnit, Mockito e MockMvc. Eles validam regras de negócio, Bean Validation, conversão de DTOs e respostas HTTP simuladas. Não iniciam uma conexão Oracle nem comprovam a persistência no banco compartilhado.

Último resultado registrado nesta revisão: 65 testes executados, sem falhas, erros ou testes ignorados.

## Testes reais via HTTP

As seguintes operações foram observadas durante a integração supervisionada:

| Operação | Evidência observada |
|---|---|
| `GET /restaurantes` | Inicialmente retornou quatro restaurantes públicos. |
| `GET /recursos-acessibilidade` | Retornou cinco recursos. |
| `GET /restaurantes/1/avaliacoes` | Retornou a avaliação do restaurante 1 com notas por categoria. |
| `POST /usuarios` | Criou o usuário fictício de ID `100`. |
| `POST /restaurantes` | Criou o restaurante fictício de ID `100`, vinculado ao responsável `100`. |
| Declaração de recurso | Atualizou/criou a declaração fictícia `RESTAURANTE_RECURSO` de ID `100`, com estado `SIM`. |
| `PATCH /restaurantes/100/publicar` | Publicou o restaurante fictício `100`. |
| `POST /restaurantes/100/avaliacoes` | Criou a avaliação fictícia de ID `100` com duas notas por categoria. |
| `POST /avaliacoes/100/verificacoes` | Criou a verificação fictícia de ID `100`, com resultado `CONFIRMADO`. |
| `GET /avaliacoes/100/verificacoes` | Retornou HTTP `200`. |
| `GET /verificacoes/100` | Retornou HTTP `200`. |

Os códigos HTTP de criação e atualização não foram anotados nas evidências recebidas; por isso não são declarados como resultados observados neste documento.

## Sequences exercitadas

Os cadastros fictícios com ID `100` exercitaram as sequences associadas aos módulos correspondentes:

- `SEQ_USUARIO`
- `SEQ_RESTAURANTE`
- `SEQ_RESTAURANTE_RECURSO`
- `SEQ_AVALIACAO`
- `SEQ_AVALIACAO_CATEGORIA` pelo cascade das duas notas por categoria
- `SEQ_VERIFICACAO_RECURSO`

Não foi afirmado que as sequences foram consultadas diretamente; a evidência é a geração dos identificadores nos cadastros supervisionados.

## Conferência no SQL Developer

Foram realizadas consultas `SELECT` no SQL Developer para confirmar:

- A avaliação `100` e suas duas notas por categoria.
- A verificação comunitária `100` persistida com resultado `CONFIRMADO`.

Os textos exatos das consultas não foram preservados nas evidências fornecidas. Portanto, este documento não reproduz SQL nem apresenta screenshots ou logs que não estejam disponíveis.

## Limitações e pendências

- A validação real cobriu fluxos representativos, não todos os cenários possíveis, erros de concorrência ou todas as constraints do Oracle.
- Não foram executadas operações destrutivas, DDL ou DML de recarga nesta revisão.
- O MVP não possui autenticação/autorização; operações de escrita exigem proteção antes de uso público.
- Registros e IDs de demonstração devem continuar sendo tratados como dados fictícios de integração.
