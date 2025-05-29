# spring-usuario-app

Gestión de Usuarios - Spring Boot Framework

## Descripción

Aplicación web para la gestión de usuarios, desarrollada con Spring Boot, Spring Security, Thymeleaf y PostgreSQL. Permite crear, editar, eliminar, listar y filtrar usuarios, así como gestionar su autenticación y estado (activo, inactivo, revocado).

## Características principales

- Autenticación segura con Spring Security y SHA-1 + Base64.
- Gestión de usuarios: alta, edición, baja y listado.
- Filtros avanzados y tablero de estados (activos, inactivos, revocados).
- Interfaz moderna y responsiva con Thymeleaf.
- Persistencia en base de datos PostgreSQL.

## Requisitos

- Java 21
- Maven 3.9.x
- Docker (opcional, recomendado para la base de datos)
- PostgreSQL 14+ (puedes usar Docker)

## Instalación y ejecución

### 1. Clona el repositorio

```sh
git clone https://github.com/tu-usuario/spring-usuario-app.git
cd spring-usuario-app/gestiondeusuariosspringboot
```

### 2. Levanta la base de datos con Docker

```sh
docker-compose -f ../docker-compose/docker-compose.yml up -d
```

Esto creará un contenedor `PostgresUsuarios` con la base de datos `usuariosdb`, usuario `user_examen` y contraseña `n0t13n3!`.

### 3. Configura la conexión a la base de datos

El archivo `src/main/resources/application.properties` ya está configurado para conectarse al contenedor de Docker.

### 4. Compila y ejecuta la aplicación

```sh
./mvnw spring-boot:run
```

La aplicación estará disponible en [http://localhost:8080](http://localhost:8080).

### 5. Acceso

- Accede a [http://localhost:8080](http://localhost:8080)
- Inicia sesión con un usuario existente en la base de datos o crea uno nuevo si es necesario.

## Pruebas

Para ejecutar las pruebas unitarias:

```sh
./mvnw test
```

## Estructura principal del proyecto

- `src/main/java/` - Código fuente Java (controladores, servicios, entidades, configuración).
- `src/main/resources/templates/` - Vistas Thymeleaf.
- `src/main/resources/application.properties` - Configuración de la aplicación.
- `docker-compose/docker-compose.yml` - Definición del contenedor de PostgreSQL.

## Notas

- El login de usuario es único y no puede modificarse una vez creado.
- Las contraseñas se almacenan usando SHA-1 + Base64.
- El sistema implementa control de acceso y mensajes de error personalizados en el login.

---

**Autor:** Rafael Nuup  
**Licencia:** MIT
