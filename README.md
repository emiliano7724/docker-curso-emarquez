# Proyecto Docker Curso

Este repositorio contiene el código fuente y la configuración necesaria para ejecutar una aplicación desarrollada como parte de un curso. La aplicación ha sido construida utilizando **Java 21** junto con el framework **Spring Boot** y **PostgreSQL** como base de datos.
Correr la primera vez  docker compose up --build
Esto levantara los dos containers incluyendo bd y ddl de la tabla.
La endpoint es muy sencillo y sigue lo requerido por la consigna.

## Tecnologías Utilizadas
EL stack tecnologico es el elegido porque es el que utilizo en mi trabajo actual. Incluso la idea del multistages para mejorar la velocidad lo aprendi en mi trabajo.
La diferencia es que no se usa compose. Solo se dockeriza la app. 
- **Java 21**: Lenguaje de programación utilizado para el desarrollo de la aplicación.
- **Spring Boot**: Framework que facilita la creación de aplicaciones Java basadas en Spring.
- **PostgreSQL**: Base de datos relacional utilizada para almacenar la información de la aplicación.

## Entorno de Desarrollo

Todo el proyecto se desarrolló y ejecutó en un entorno Windows utilizando Docker instalado directamente en WSL (Windows Subsystem for Linux), sin la necesidad de Docker Desktop. Para la gestión y ejecución de los contenedores, se utilizó el comando `docker compose` dentro de WSL.
El IDE utilizado es Intellij IDEA.
## Endpoints

La aplicación expone publicamente el siguiente endpoint:

1. **GET /api/mi_endpoint**:
    - **Descripción**: Este endpoint recibe un parámetro llamado `abcParam` a través de la URL y procesa el valor recibido.
    - **Ejemplo de uso**: `localhost:80/api/mi_endpoint?abcParam=123`
    - **Parámetros**:
        - `abcParam`: Un parámetro de tipo numérico que se utiliza en el procesamiento interno del endpoint.
    - **Respuesta**:
        - Si el valor de `abcParam` es `"123"`, la respuesta será:
          ```json
          {
              "recibido": "123",
              "status": "OK",
              "message": "Parámetro procesado correctamente, retorno: XYZ"
          }
          ```
          Además, este valor se grabará en la base de datos junto con la fecha y hora actuales.
        - Para cualquier otro valor de `abcParam`, la respuesta será similar a:
          ```json
          {
              "recibido": "456",
              "status": "ERROR",
              "message": "Parámetro desconocido"
          }
          ```

## Dockerfile

**Explicación**:

1. **Etapa de Construcción (build stage)**:
    - `ARG BASE_IMAGE=jelastic/maven:3.9.5-openjdk-21`: Define la imagen base con Maven y OpenJDK 21, que se utiliza para compilar la aplicación.
    - `FROM $BASE_IMAGE AS build`: Se utiliza la imagen base definida como la etapa de construcción.
    - `WORKDIR /workspace/app`: Establece el directorio de trabajo dentro del contenedor.
    - `COPY mvnw .`, `COPY .mvn .mvn`, `COPY pom.xml .`, `COPY src src`: Copia los archivos de Maven y el código fuente de la aplicación al contenedor.
    - `RUN chmod +x mvnw`: Da permisos de ejecución al script `mvnw`.
    - `RUN ./mvnw install -DskipTests`: Compila la aplicación utilizando Maven, omitiendo las pruebas.
    - `RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)`: Desempaqueta el JAR generado por Maven para preparar las dependencias para la siguiente etapa.

2. **Etapa de Producción (production stage)**:
    - `FROM $BASE_IMAGE`: Utiliza nuevamente la imagen base, esta vez para crear la imagen final de producción.
    - `VOLUME /tmp`: Define un volumen temporal.
    - `ARG DEPENDENCY=/workspace/app/target/dependency`: Define la ruta donde se almacenan las dependencias desempaquetadas.
    - `COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib`, `COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF`, `COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app`: Copia las dependencias, metadatos y clases compiladas desde la etapa de construcción al contenedor de producción.
    - `USER root`: Cambia al usuario root para las siguientes operaciones.
    - `COPY startup.sh /etc/startup.sh`: Copia un script de inicio personalizado al contenedor.
    - `RUN chmod +x /etc/startup.sh`: Da permisos de ejecución al script de inicio.
    - `ENTRYPOINT ["/etc/startup.sh"]`: Establece el punto de entrada del contenedor, que ejecutará el script de inicio.

Este Dockerfile sigue una estrategia de múltiples etapas para crear una imagen eficiente y optimizada, separando la compilación y la ejecución de la aplicación en diferentes fases.

## Docker Compose

**Explicación**:

- **Version**: Se utiliza la versión `3.8` de Docker Compose.

- **Servicios**:
    - **app**:
        - Construye la aplicación a partir del Dockerfile ubicado en el directorio actual (`build: .`).
        - Expone el puerto `8080` dentro del contenedor a través del puerto `80` en el host.
        - Configura variables de entorno para conectar la aplicación Spring Boot a la base de datos PostgreSQL.
        - Define que el servicio `app` depende del servicio `db`, asegurando que la base de datos esté lista antes de iniciar la aplicación.
        - Se conecta a la red `demodockercurso_my-network`.

    - **db**:
        - Utiliza la imagen oficial de PostgreSQL en su versión 15.
        - Configura la base de datos PostgreSQL con un nombre, usuario y contraseña definidos.
        - Expone el puerto `5432` de PostgreSQL a través del puerto `5433` en el host.
        - Monta un volumen persistente para almacenar los datos de la base de datos en el host.
        - Se conecta a la misma red que el servicio `app`.

- **Redes**:
    - Se define una red personalizada `demodockercurso_my-network` para permitir la comunicación entre los servicios `app` y `db`.

- **Volúmenes**:
    - El volumen `db_data` se utiliza para almacenar los datos de PostgreSQL, asegurando persistencia entre reinicios de los contenedores.

Este archivo Compose facilita la gestión de los servicios de la aplicación, permitiendo levantar tanto la aplicación como la base de datos con un solo comando, mientras asegura que ambos puedan comunicarse a través de una red interna.

## Dificultad Encontrada
Se me hizo costoso la comunicacion con la base de datos ya que el container db no podia levantarlo. Ni siquiera de manera individual. Luego de renegar un buen rato pude saber que era porque en mi local ya tenia un postgre corriendo y encima en el mismo puerto 5433.
Por lo que al darme cuenta lo mapie a 5433 y ya no tuve problema.
