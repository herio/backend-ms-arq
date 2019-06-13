# arq-ms-mobile

Projeto spring boot que fornece infra para projetos spring boot mobile.

Fornece a infra de:
- tratamento de exceções (TratadorExcecaoRestController)
- swagger (SwaggerConfig)
- cliente feign (FeignErrorDecoder e FeignRequestInterceptor)
- security (SecurityConfig)
- resources e beans (AppConfig)
- TokenJWT (TokenJwtService)
- Config logs do projeto (AppCommonsRequestLoggingFilter)

Fornece endpoints para:
- Autenticação (AutenticacaoController)
- CRUD usuários (UsuariosController)
- CRUD dispositivos (DispositivosController)
- Notificações (NotificacoesController)
- Logs (LogController)

## Pré-requisitos para rodar localmente

Ter instalado (configurado nas variáveis de ambiente):
- Java 8 (JAVA_HOME e PATH)
- Gradle 4.7 (GRADLE_HOME e GRADLE_USER_HOME)
- Maven (M2_HOME)


## Importando no intelliJ

File > New > Project from existing source > arq-ms-mobile 

Import project from external model > Gradle

- Use default gradle wrapper
- Gradle home > instalação do gradle local (definido em GRADLE_HOME)
- Gradle JVM > instalação Jdk 8 local (definido em JAVA_HOME)
- Global Gradle settings > Service directory path deve apontar para GRADLE_USER_HOME 


## Publicação no repositório local

Para publicar atualização do projeto, rodar comando gradle:

    gradle publish
    
    
## Git

Sobrescrever local:

    git fetch --all
    
    git reset --hard origin/master

- git fetch downloads the latest from remote without trying to merge or rebase anything.
- Then the git reset resets the master branch to what you just fetched. The --hard option changes all the files in your working tree to match the files in origin/master

Criar repositório baseado em projeto existente: 
https://help.github.com/en/articles/adding-an-existing-project-to-github-using-the-command-line

Comandos úteis:

    git status --vê se tem alteração
    git rm arquivo --apaga arquivo
    git add . --add todas alterações
    git commit -m "removendo arquivos" --faz commit repo local
    git push -u origin master --push repo remoto
    git push heroku master //toda vez que tiver alteração e quiser subir em produção
    