# Pré-visualizações Mermaid

Este arquivo é a versão renderizável dos arquivos `.mmd` quando aberto em GitHub ou visualizador Mermaid compatível. Os arquivos `.mmd` permanecem como fonte editável para exportação futura.

## UML de domínio

```mermaid
classDiagram
direction LR
class Usuario {
  +Long idUsuario
  +String nome
  +String email
  +String senhaHash
  +TipoUsuario tipoUsuario
  +LocalDateTime dataCadastro
}
class Restaurante {
  +Long idRestaurante
  +String nome
  +String endereco
  +String bairro
  +String descricao
  +boolean publicado
  +boolean ativo
  +String cidade
  +String telefone
  +LocalDateTime dataCadastro
}
class CategoriaAcessibilidadeEntity {
  +Long idCategoria
  +CategoriaAcessibilidade codigo
  +String nome
  +String descricao
}
class RecursoAcessibilidade {
  +Long idRecurso
  +String nome
  +String descricao
}
class RestauranteRecurso {
  +Long idRestauranteRecurso
  +String descricaoAdicional
  +StatusDeclaracao statusDeclaracao
  +LocalDateTime dataAtualizacao
  +boolean autodeclarado
}
class Avaliacao {
  +Long idAvaliacao
  +BigDecimal notaGeral
  +String comentario
  +LocalDate dataVisita
  +LocalDateTime dataCriacao
}
class AvaliacaoCategoria {
  +Long idAvaliacaoCategoria
  +BigDecimal nota
}
class VerificacaoRecurso {
  +Long idVerificacao
  +ResultadoVerificacao resultado
  +String comentario
  +LocalDateTime dataVerificacao
}
class TipoUsuario {
  <<enumeration>>
  CONSUMIDOR
  RESTAURANTE
}
class CategoriaAcessibilidade {
  <<enumeration>>
  FISICA
  VISUAL
  AUDITIVA
  SENSORIAL
}
class StatusDeclaracao {
  <<enumeration>>
  SIM
  NAO
  NAO_INFORMADO
}
class ResultadoVerificacao {
  <<enumeration>>
  CONFIRMADO
  CONTESTADO
  NAO_VERIFICADO
}
Usuario "1" <-- "0..*" Restaurante : responsavel
Restaurante "0..1" <-- "0..*" Restaurante : anterior
CategoriaAcessibilidadeEntity "1" <-- "0..*" RecursoAcessibilidade : categoria
Restaurante "1" <-- "0..*" RestauranteRecurso : restaurante
RecursoAcessibilidade "1" <-- "0..*" RestauranteRecurso : recurso
Usuario "1" <-- "0..*" Avaliacao : usuario
Restaurante "1" <-- "0..*" Avaliacao : restaurante
Avaliacao "1" *-- "0..4" AvaliacaoCategoria : notasCategorias
CategoriaAcessibilidadeEntity "1" <-- "0..*" AvaliacaoCategoria : categoria
Avaliacao "1" <-- "0..*" VerificacaoRecurso : avaliacao
RestauranteRecurso "1" <-- "0..*" VerificacaoRecurso : declaracao
Usuario --> TipoUsuario
CategoriaAcessibilidadeEntity --> CategoriaAcessibilidade
RestauranteRecurso --> StatusDeclaracao
VerificacaoRecurso --> ResultadoVerificacao
```

## Arquitetura em camadas

```mermaid
flowchart LR
  mvc[PaginaController MVC] --> form[FormularioAvaliacaoBO]
  rest[Controllers REST] --> bo[BOs]
  form --> bo
  bo --> repo[Repositories Spring Data JPA]
  repo --> entity[Entidades JPA]
  entity --> oracle[(Oracle)]
  rest --> dto[DTOs]
  mvc --> thymeleaf[Templates Thymeleaf]
  rest --> handler[GlobalExceptionHandler]
```

## DER Oracle

```mermaid
erDiagram
  USUARIO {
    NUMBER id_usuario PK
    VARCHAR2 nome
    VARCHAR2 email UK
    VARCHAR2 senha
    VARCHAR2 tipo_usuario
    TIMESTAMP data_cadastro
  }
  CATEGORIA_ACESSIBILIDADE {
    NUMBER id_categoria PK
    VARCHAR2 codigo UK
    VARCHAR2 nome
    VARCHAR2 descricao
  }
  RECURSO_ACESSIBILIDADE {
    NUMBER id_recurso PK
    VARCHAR2 nome
    VARCHAR2 descricao
    NUMBER categoria_id FK
  }
  RESTAURANTE {
    NUMBER id_restaurante PK
    VARCHAR2 nome
    VARCHAR2 descricao
    VARCHAR2 endereco
    VARCHAR2 bairro
    VARCHAR2 cidade
    VARCHAR2 telefone
    TIMESTAMP data_cadastro
    NUMBER ativo
    NUMBER publicado
    NUMBER restaurante_anterior_id FK
    NUMBER usuario_id FK
  }
  RESTAURANTE_RECURSO {
    NUMBER id_restaurante_recurso PK
    VARCHAR2 descricao_adicional
    VARCHAR2 status_declaracao
    TIMESTAMP data_atualizacao
    NUMBER autodeclarado
    NUMBER restaurante_id FK
    NUMBER recurso_id FK
  }
  AVALIACAO {
    NUMBER id_avaliacao PK
    NUMBER nota
    VARCHAR2 comentario
    TIMESTAMP data_avaliacao
    DATE data_visita
    NUMBER usuario_id FK
    NUMBER restaurante_id FK
  }
  AVALIACAO_CATEGORIA {
    NUMBER id_avaliacao_categoria PK
    NUMBER avaliacao_id FK
    NUMBER categoria_id FK
    NUMBER nota
  }
  VERIFICACAO_RECURSO {
    NUMBER id_verificacao PK
    VARCHAR2 resultado
    VARCHAR2 comentario
    TIMESTAMP data_verificacao
    NUMBER avaliacao_id FK
    NUMBER restaurante_recurso_id FK
  }
  USUARIO ||--o{ RESTAURANTE : responsavel
  RESTAURANTE o|--o{ RESTAURANTE : anterior
  CATEGORIA_ACESSIBILIDADE ||--o{ RECURSO_ACESSIBILIDADE : classifica
  RESTAURANTE ||--o{ RESTAURANTE_RECURSO : declara
  RECURSO_ACESSIBILIDADE ||--o{ RESTAURANTE_RECURSO : declarado
  USUARIO ||--o{ AVALIACAO : registra
  RESTAURANTE ||--o{ AVALIACAO : recebe
  AVALIACAO ||--o{ AVALIACAO_CATEGORIA : detalha
  CATEGORIA_ACESSIBILIDADE ||--o{ AVALIACAO_CATEGORIA : recebe_nota
  AVALIACAO ||--o{ VERIFICACAO_RECURSO : possui
  RESTAURANTE_RECURSO ||--o{ VERIFICACAO_RECURSO : referencia
```
