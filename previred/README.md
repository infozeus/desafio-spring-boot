# TaskManager

TaskManager es una API RESTful desarrollada con **Spring Boot** que permite gestionar tareas y asociarlas a estados predefinidos. Este proyecto incluye funcionalidades para crear, leer, actualizar y eliminar tareas, así como asociarlas a estados específicos.

## Características

- **Gestión de Tareas**:
  - Crear nuevas tareas.
  - Consultar todas las tareas o una tarea específica por su ID.
  - Actualizar tareas existentes.
  - Eliminar tareas.

- **Gestión de Estados**:
  - Asociar tareas a estados predefinidos (e.g., "Pendiente", "En progreso", "Completado").

- **Documentación de API**:
  - Integración con **Swagger/OpenAPI** para explorar y probar la API.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (Base de datos en memoria para desarrollo y pruebas)
- **Swagger/OpenAPI** (Documentación de API)
- **JUnit 5** y **Mockito** (Pruebas unitarias)

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java 17** o superior
- **Maven 3.8** o superior

## Instalación y Ejecución

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/taskmanager.git
   cd taskmanager
   ```

2. Compila el proyecto:
   ```bash
   mvn clean install
   ```

3. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

4. Accede a la API en:
   ```
   http://localhost:8080
   ```

5. Explora la documentación de la API en:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## Endpoints Principales

### Tareas

| Método | Endpoint         | Descripción                          | Ejemplo de Cuerpo (JSON) |
|--------|------------------|--------------------------------------|--------------------------|
| `GET`  | `/tareas/all`    | Obtener todas las tareas             | N/A                      |
| `GET`  | `/tareas/{id}`   | Obtener una tarea por su ID          | N/A                      |
| `POST` | `/tareas`        | Crear una nueva tarea                | `{ "descripcion": "Nueva tarea", "estado": { "id": "1" } }` |
| `PUT`  | `/tareas/{id}`   | Actualizar una tarea existente       | `{ "descripcion": "Tarea actualizada", "estado": { "id": "2" } }` |
| `DELETE` | `/tareas/{id}` | Eliminar una tarea por su ID         | N/A                      |

### Estados

| Método | Endpoint         | Descripción                          |
|--------|------------------|--------------------------------------|
| `GET`  | `/estados/all`   | Obtener todos los estados disponibles |

## Estructura del Proyecto

```
src
├── main
│   ├── java
│   │   └── com.nuevospa.taskmanager
│   │       ├── application
│   │       │   └── service       # Lógica de negocio
│   │       ├── config            # Configuración (e.g., Swagger)
│   │       ├── domain
│   │       │   ├── model         # Entidades JPA
│   │       │   └── dto           # Clases DTO
│   │       ├── ports
│   │       │   ├── input         # Controladores REST
│   │       │   └── output        # Repositorios
│   │       └── TaskManagerApplication.java
│   └── resources
│       ├── application.properties # Configuración de la aplicación
│       └── data.sql               # Datos iniciales para la base de datos
└── test
    └── java
        └── com.nuevospa.taskmanager
            └── ports.input.controller # Pruebas unitarias de controladores
```

## Base de Datos

El proyecto utiliza una base de datos en memoria **H2** para desarrollo y pruebas. Puedes acceder a la consola de H2 en:

```
http://localhost:8080/h2-console
```

Usa las siguientes credenciales:
- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: *(vacío)*

## Pruebas

Ejecuta las pruebas unitarias con el siguiente comando:

```bash
mvn test
```

## Contribuciones

¡Las contribuciones son bienvenidas! Si deseas contribuir, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama para tu funcionalidad o corrección de errores:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. Realiza tus cambios y haz un commit:
   ```bash
   git commit -m "Agrega nueva funcionalidad"
   ```
4. Envía tus cambios:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5. Abre un Pull Request en GitHub.

## Licencia

Este proyecto está licenciado bajo la MIT License.

## Contacto

Si tienes preguntas o sugerencias, no dudes en contactarnos:

- **Nombre**: Nuevo SPA
- **Sitio web**: [https://nuevospa.com](https://nuevospa.com)
- **Correo electrónico**: apis@nuevospa.com

---

