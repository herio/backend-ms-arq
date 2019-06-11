# arq-ms-mobile

Projeto spring boot que fornece infra para projetos spring boot mobile.

Fornece a infra de:
- tratamento de exceções (TratadorExcecaoRestController e Arqref7ErrorHandler)
- swagger (SwaggerConfig)
- cliente feign (FeignErrorDecoder e FeignRequestInterceptor)
- security (SecurityConfig)
- resources e beans (AppConfig)
- serviço de autenticação para arqref7 e tokenjwt (AutenticacaoService)
- endpoint para acesso aos logs do projeto (LogController)
- logs de payload (AppCommonsRequestLoggingFilter)

## Pré-requisitos para rodar localmente

Ter instalado (configurado nas variáveis de ambiente):
- Java 10 (JAVA_HOME e PATH)
- Gradle 4.7 (GRADLE_HOME e GRADLE_USER_HOME)
- Maven (M2_HOME)


## Importando no intelliJ

File > New > Project from existing source > arq-ms-mobile 

Import project from external model > Gradle

- Use default gradle wrapper
- Gradle home > instalação do gradle local (definido em GRADLE_HOME)
- Gradle JVM > instalação Jdk 10 local (definido em JAVA_HOME)
- Global Gradle settings > Service directory path deve apontar para GRADLE_USER_HOME 


## Publicação no artifactory

Para publicar atualização do projeto, rodar comando gradle:

    gradle publish