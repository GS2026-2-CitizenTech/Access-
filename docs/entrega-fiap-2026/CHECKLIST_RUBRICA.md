# Checklist da rubrica FIAP

| Critério oficial | Evidência preparada | Situação para o PDF |
|---|---|---|
| Capa, nome, integrantes e RMs | Capa-base em `DOCUMENTACAO_TECNICA.md`. | Pendente: preencher integrantes e RMs aprovados. |
| Índice | Índice sugerido no texto-base. | Pendente: gerar paginação após a diagramação do PDF. |
| Descrição, justificativa e objetivos | Seções 1 e 2 do texto-base. | Pronto para revisão de conteúdo. |
| Diagrama de classes | `diagramas/uml-classes.mmd` e pré-visualização Markdown. | Pronto; revisar legibilidade na exportação PDF. |
| Camadas Java | `diagramas/camadas.mmd`. | Complementar ao diagrama de domínio. |
| Protótipos/telas | Roteiro em `SCREENSHOTS.md`. | Pendente: capturas reais. |
| Tabela de endpoints HTTP | `ENDPOINTS.md`, com GET, POST, PUT, PATCH e DELETE. | Pronto para revisão. |
| DER Oracle | `diagramas/der-oracle.mmd`, baseado em `Access_Plus.sql`. | Pronto; revisar renderização/legibilidade. |
| Ferramentas, versões e execução | Seções 7 e 8 do texto-base. | Pronto; executar somente em ambiente autorizado. |
| GitHub, deploy e Docker | Seção 6 do texto-base. | Confirmar URL/estado final imediatamente antes da entrega. |
| Testes | Seção 9 registra 81 aprovados como última evidência informada. | Pendente apenas se a equipe quiser atualizar a evidência. |
| Segurança e limitações | Seção 10 do texto-base. | Pronto para revisão. |

## Informações ainda necessárias para fechar o PDF

1. Nomes completos e RMs de todos os integrantes.
2. Capturas reais das telas indicadas, incluindo a mensagem de sucesso se houver ambiente de teste autorizado.
3. Definição visual do PDF: logotipo institucional, tipografia, paginação e autoria da capa.
4. Confirmação final da URL pública e, se desejado, QR Code gerado pela equipe na fase de diagramação.
5. Caso o PDF exija imagem estática em vez de Mermaid renderizado pelo GitHub, exportar os arquivos `.mmd` para SVG/PNG em ferramenta aprovada e revisar a resolução.
