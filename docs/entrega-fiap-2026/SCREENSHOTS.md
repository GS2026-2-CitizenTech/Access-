# Roteiro de screenshots reais para o PDF

Nenhuma captura foi realizada nesta etapa. As imagens abaixo devem ser produzidas em navegador pela equipe, usando dados fictícios e sem expor URL JDBC, usuário, senha, e-mail pessoal ou qualquer segredo.

| # | Tela/estado a capturar | Rota sugerida | Evidência visual desejada |
|---|---|---|---|
| 1 | Início | `/app` | Identidade Access+, destaques e navegação inferior mobile-first. |
| 2 | Explorar restaurantes | `/app/explorar` | Campo de busca, categorias horizontais e cards de restaurantes. |
| 3 | Detalhe do restaurante | `/app/restaurantes/{id}` | Informação autodeclarada, nota média, recursos e relatos comunitários. |
| 4 | Formulário de avaliação | `/app/restaurantes/{id}/avaliar` | Componente de cinco estrelas; registrar uma nota com meio ponto, como 3,5. |
| 5 | Recursos e relatos comunitários | detalhe do restaurante ou formulário | Status de declaração e um relato `CONFIRMADO`, `CONTESTADO` ou `NAO_VERIFICADO`, sem tratá-lo como certificação. |
| 6 | Sucesso após avaliação | detalhe após POST válido | Mensagem de sucesso retornada pelo fluxo POST/Redirect/GET. Executar apenas em ambiente autorizado. |
| 7 | Responsividade opcional | qualquer tela em viewport próximo de 390 × 844 px | Comprovação do layout mobile-first. |
| 8 | Modo público opcional | tentativa de `/usuarios` no deploy de demonstração | Resposta HTTP 403, sem expor conteúdo administrativo. |

## Orientações de composição

- Priorizar viewport mobile, pois a interface foi concebida como mobile-first.
- Manter a barra de endereço fora da imagem quando ela revelar parâmetros ou dados que não devam ir ao PDF.
- Identificar ilustrações conceituais como ilustrações; elas não representam fotografias reais dos restaurantes.
- Para a captura de sucesso, usar apenas dados fictícios aprovados e registrar que isso cria dados no ambiente utilizado.
- Não simular screenshots nem substituir a captura real por mockup na seção de evidências.
