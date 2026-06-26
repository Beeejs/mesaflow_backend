# MesaFlow API

API backend desarrollada en Java con Spring Boot para la gestión de reservas de establecimientos gastronómicos.

El módulo implementado permite crear reservas, confirmar o cancelar reservas existentes y consultar reservas por usuario o por establecimiento, aplicando validaciones de negocio como disponibilidad de mesas, permisos de usuario, estado de la reserva y configuración del establecimiento.

---

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Lombok
* Swagger / OpenAPI
* JUnit 5
* Mockito
* MockMvc

---

## Requisitos previos

Antes de ejecutar el proyecto, se debe tener instalado:

* Java JDK 21 o superior
* Maven o Maven Wrapper incluido en el proyecto
* MySQL
* Git
* Un IDE recomendado: VS Code, IntelliJ IDEA o Eclipse

---

## Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd mesaflow_backend_seguimiento
```

---

## Configuración de base de datos

Crear una base de datos en MySQL:

```sql
CREATE DATABASE mesaflow;
```

Luego configurar el archivo:

```text
src/main/resources/application.properties
```

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mesaflow
spring.datasource.username=root
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jackson.deserialization.fail-on-unknown-properties=true
```

Importante: modificar `username` y `password` según la configuración local de MySQL.

---

## Instalación de dependencias

El proyecto utiliza Maven. Para descargar dependencias y compilar:

### En Linux / Git Bash / Mac

```bash
./mvnw clean install
```

### En Windows PowerShell

```powershell
.\mvnw clean install
```

Si se tiene Maven instalado globalmente, también puede ejecutarse:

```bash
mvn clean install
```

---

## Ejecutar la aplicación

### Con Maven Wrapper

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw spring-boot:run
```

### Desde el IDE

Ejecutar la clase principal:

```text
MesaflowApiApplication.java
```

Por defecto, la API se ejecuta en:

```text
http://localhost:8080
```

---

## Documentación Swagger / OpenAPI

La API cuenta con documentación interactiva mediante Swagger.

Una vez iniciada la aplicación, acceder a:

```text
http://localhost:8080/swagger-ui/index.html
```

También se puede consultar la documentación OpenAPI en formato JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## Ejecutar tests

El proyecto incluye tests automatizados para validar endpoints principales del módulo de reservas.

Para ejecutar todos los tests:

```bash
./mvnw test
```

En Windows PowerShell:

```powershell
.\mvnw test
```

Resultado esperado:

```text
BUILD SUCCESS
```

---

## Colección de Postman

Se puede utilizar una colección de Postman para probar manualmente los endpoints principales:

```text
Reservas
├── Crear
├── Estado
├── Consulta Usuario
└── Consulta Establecimiento
```

Los bodies de ejemplo se encuentran documentados en Swagger y pueden cargarse manualmente en Postman como `raw JSON`.
En el apartado de "Docs" del endpoint se encuentra un resumen detallado del mismo.

---

## Estructura general del proyecto

```text
src
├── main
│   ├── java
│   │   └── com.mesaflow.mesaflow_api
│   │       ├── Controller
│   │       ├── DTOs
│   │       ├── Enums
│   │       ├── Model
│   │       ├── Repository
│   │       └── Service
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.mesaflow.mesaflow_api
```