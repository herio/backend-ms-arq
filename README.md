# arq-ms-mobile

Projeto spring boot que fornece infra para projetos spring boot mobile.

Fornece a infra de:
- tratamento de exce��es (TratadorExcecaoRestController)
- swagger (SwaggerConfig)
- cliente feign (FeignErrorDecoder e FeignRequestInterceptor)
- security (SecurityConfig)
- resources e beans (AppConfig)
- TokenJWT (TokenJwtService)
- Config logs do projeto (AppCommonsRequestLoggingFilter)

Fornece endpoints para:
- Autentica��o (AutenticacaoController)
- CRUD usu�rios (UsuariosController)
- CRUD dispositivos (DispositivosController)
- Notifica��es (NotificacoesController)
- Logs (LogController)

## Pr�-requisitos para rodar localmente

Ter instalado (configurado nas vari�veis de ambiente):
- Java 11 (JAVA_HOME e PATH)

N�o precisa instalar - wrapper:
- Gradle 7.3.3


## Importando no intelliJ

File > New > Project from existing source > arq-ms-mobile 

Import project from external model > Gradle

- Use default gradle wrapper
- Gradle home > instala��o do gradle local (definido em GRADLE_HOME)
- Gradle JVM > instala��o Jdk 8 local (definido em JAVA_HOME)
- Global Gradle settings > Service directory path deve apontar para GRADLE_USER_HOME 


## Publica��o no reposit�rio local

Para publicar atualiza��o do projeto, rodar comando gradle:

    gradle publish
    
    
## Git


Baixar projeto:

    git clone https://github.com/herio/backend-ms-arq.git

Sobrescrever c�digo local com o do reposit�rio:

    git fetch --all
	git reset --hard origin/master
	
- git fetch downloads the latest from remote without trying to merge or rebase anything.
- Then the git reset resets the master branch to what you just fetched. The --hard option changes all the files in your working tree to match the files in origin/master
	
Atualizar c�digo local mantendo suas atualiza��es
  
    git status
	git stash
	git pull
	git stash pop

Ver log dos �ltimos commits:

    git log --stat

Fazer commit:

    git add . --add todas altera��es
    git commit -m "descri��o" --faz commit repo local
    git push origin master --push repo remoto
    
Criar novo reposit�rio:

    Na web > + > new repository > nome, vazio
    git clone url
    adiciona arquivos
    git add .
    git commit -m "coment�rio"
    git push iu origin master
    
Definir usu�rio padr�o para commit via linha de comando

    git config --global credential.username "seu email"

Atualizar todos os reposit�rios pelo powershell

    Get-ChildItem -Directory | foreach { Write-Host "`n■ Getting latest for $_ ↓" | git -C $_.FullName pull -v}
    
    
## Heroku

Principais comandos:
    
    heroku login
    heroku create appname
    git push heroku master //toda vez que tiver altera��o e quiser subir em produ��o
    heroku open
    heroku logs --tail
    heroku config //v� url bd
    heroku pg //v� dados bd
    heroku ps:scale web=1 //add uma inst�ncia web
    heroku config:set ENERGY="20 GeV" //criar var ambiente
    heroku config //listar var ambiente
    heroku apps //listar apps
    heroku git:remote -a noomedoapp //adicionar heroku a pasta do app
    git remote -v //verificar se heroku foi ligado a pasta do projeto
     
Criar Procfile para rodar app em prod:

    Ex. web: java $JAVA_OPTS -Dserver.port=$PORT -Dspring.profiles.active=prod -jar build/libs/arq-ms-mobile-0.0.1-SNAPSHOT.jar

Criar application-prod.yml com config de prod heroku:
     
    spring:
	 #bd
	 datasource:
	  username: ${JDBC_DATABASE_USERNAME}
	  password: ${JDBC_DATABASE_PASSWORD}
    url: ${JDBC_DATABASE_URL}
	 #flyway
	 flyway:
	  user: ${JDBC_DATABASE_USERNAME}
	  password: ${JDBC_DATABASE_PASSWORD}
	  url: ${JDBC_DATABASE_URL}


Criar add-on para postgress prod:

    heroku addons:create heroku-postgresql:hobby-dev --app nomeapp --version=9.6

Acessar BD prod:

    Ir em datastore > settings > credentials > heroku CLI > Copiar comando (Executar esse comando na raiz do projeto: pgsql deve estar configurado no PATH)
    Ex. heroku pg:psql postgresql-asymmetrical-61306 --app sgtifgo
    
Acessar BD via psql:

    psql --host=ec2-52-203-98-126.compute-1.amazonaws.com --port=5432 --username=lhnebstamvmtzs --password=1a0cfdefd51bf354b4da5cccaa420aa92a8943f862c1dd43abce2ed1d3f970e5 --dbname=deamr4lqjg7tri
    
Comandos de BD �teis

    \dt //lista tabelas 
    \dt advogado_comunitario.* //lista tabelas de um schema
    \dn //lista schemas
    \d noticias_juridicas.notificacao; //desc table    
    drop schema noticias_juridicas cascade; //remove schema
    update noticias_juridicas.notificacao set data_envio = '2019-07-01 00:00:00-00' //atualizar
    SHOW server_encoding;
    SET client_encoding TO 'UTF8'; //atualizar encoding do banco

Criar backup sql no pgadmin (caso n�o tenha flyway): 

    Clique botão direito no banco > backup > plain > gere arquivo .sql e jogue na raiz do projeto

Alterar owner do arquivo .sql  por usu�rio do banco no heroku:

    ex. replace all: 'TO postgres' por: 'TO zpsjlzbixfkzxt'

Em heroku pg:sql, mande executar o arquivo criado

    \i sgt.sql

D�vidas de comandos:
 
    \copyright para mostrar termos de distribui��o
    \h para ajuda com comandos SQL
    \? para ajuda com comandos do psql
    \g ou terminar com ponto-e-v�rgula para executar a consulta
    \q para sair
