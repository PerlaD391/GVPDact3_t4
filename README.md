# Act3 — CRUD en Spring Boot con JPA y MySQL (CulturaVibe)

Este es mi proyecto de la Actividad 3 de Programación Web (equipo 13).
Hice un CRUD completo en Spring Boot conectado a una base de datos real en
MySQL, con dos entidades relacionadas entre sí. También tiene sus propias
vistas dentro del mismo proyecto y lo probé con Postman.

**Autora:** Perla Danae Gallardo Vasquez (GVPD)

## Enlaces en vivo (VPS)

- Cartelera (vistas): http://13.140.226.55:8084/eventos
- API de eventos: http://13.140.226.55:8084/api/eventos
- API de categorías: http://13.140.226.55:8084/api/categorias
- Act1 (sigue corriendo): http://13.140.226.55:8081/api/proyecto
- Act2 (sigue corriendo): http://13.140.226.55:8083/eventos

## Mis entidades y su relación

Yo elegí dos entidades: `Evento` y `Categoría`.

| Entidad | Tabla | Campos |
|---|---|---|
| `Categoria` | `categorias` | id, nombre (único), descripcion |
| `Evento` | `eventos` | id, nombre, fecha, hora, sede, direccion, ciudad, estado, precio, estadoVenta, descripcion, artistas, url |

La relación es de **muchos a uno**: muchos eventos pertenecen a una
categoría y una categoría tiene muchos eventos. En el código, `Evento`
tiene `@ManyToOne` hacia `Categoría` y la llave foránea es `categoria_id`;
`Categoría` tiene `@OneToMany` del otro lado. Cuando consultas un evento,
la respuesta trae el **nombre de su categoría** (`categoriaNombre`), no
solo su id. Los artistas se guardan aparte en la tabla `evento_artistas`
con `@ElementCollection`.

## Endpoints

### Eventos (`/api/eventos`)

| Método | Ruta | Para qué sirve | Respuesta |
|---|---|---|---|
| GET | `/api/eventos?page=0&size=12` | Listar con paginación | 200 |
| GET | `/api/eventos/categoria/{categoriaId}` | Listar por categoría | 200 |
| GET | `/api/eventos/{id}` | Ver un evento | 200 / 404 |
| POST | `/api/eventos` | Crear un evento | 201 |
| PUT | `/api/eventos/{id}` | Actualizar un evento | 200 / 404 |
| DELETE | `/api/eventos/{id}` | Eliminar un evento | 204 / 404 |

### Categorías (`/api/categorias`)

| Método | Ruta | Para qué sirve | Respuesta |
|---|---|---|---|
| GET | `/api/categorias` | Listar categorías | 200 |
| GET | `/api/categorias/{id}` | Ver una categoría | 200 / 404 |
| POST | `/api/categorias` | Crear una categoría | 201 |
| PUT | `/api/categorias/{id}` | Actualizar una categoría | 200 / 404 |
| DELETE | `/api/categorias/{id}` | Eliminar una categoría | 204 / 404 / 409 si tiene eventos |

### Vistas (Thymeleaf, en el mismo proyecto)

| Ruta | Qué se ve |
|---|---|
| `/` | Redirige a `/eventos` |
| `/eventos` | Lista paginada de eventos con su categoría |
| `/eventos/{id}` | Detalle de un evento con su categoría |

Los datos que entran se validan con `@Valid` (`@NotBlank`, `@NotNull`):
si falta algo, la API responde **400** diciendo qué campo falló. Yo uso
DTOs para controlar qué datos entran y salen; la entidad nunca se expone
directamente.

Ejemplo de lo que mando para crear un evento (POST `/api/eventos`):

```json
{
  "nombre": "Concierto de Prueba",
  "fecha": "2026-12-15",
  "hora": "18:00",
  "sede": "Teatro Prueba",
  "direccion": "Calle Prueba 5",
  "ciudad": "Puebla",
  "estado": "PUE",
  "categoriaId": 1,
  "precio": "$150",
  "estadoVenta": "onsale",
  "descripcion": "Concierto creado desde Postman",
  "artistas": ["Compañía Prueba"],
  "url": "https://www.ticketmaster.com.mx"
}
```

Y me responde **201 Created** con el evento creado, incluyendo
`categoriaNombre: "Música"`.

## Base de datos

Usé MySQL real. Las tablas se crean solas al arrancar el proyecto
(`spring.jpa.hibernate.ddl-auto=update`). La primera vez se guardan solas
5 categorías (Música, Cine, Arte, Libros, Teatro) y 6 eventos.

Las contraseñas **no** están en el repositorio: se pasan con variables
de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`).

### Cómo instalé MySQL en el VPS

```bash
sudo apt update
sudo apt install -y mysql-server
sudo systemctl status mysql
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'TU_CONTRASENA'; FLUSH PRIVILEGES;"
```

### Cómo lo corro en mi máquina

Necesito JDK 17 (Maven no hace falta instalarlo, el proyecto trae el
Maven Wrapper) y MySQL corriendo en local con la base `culturavibe_act3`
(se crea sola). El servidor levanta en `http://localhost:8084`.

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.1"
.\mvnw.cmd clean package
java -jar target\gvpdact3-t4-0.0.1-SNAPSHOT.jar
```

Si mi MySQL local tiene contraseña, la paso así antes de correrlo:

```powershell
$env:DB_PASSWORD="tu_contrasena"
```

## Cómo lo probé (Postman)

La colección está en la carpeta `postman/` de este repo. En Postman voy
a **Import → Files** y elijo estos dos archivos:

- `postman/CulturaVibe-Act3.postman_collection.json` (las 8 peticiones)
- `postman/CulturaVibe-Act3-VPS.postman_environment.json` (para probar
  contra el VPS) o `postman/CulturaVibe-Act3-Local.postman_environment.json`
  (para probar en mi máquina)

Elijo el entorno arriba a la derecha y corro las peticiones en este orden:

1. Categorías → Listar categorias
2. Categorías → Crear categoria
3. Eventos → Listar eventos
4. Eventos → Ver evento
5. Eventos → Crear evento (201)
6. Eventos → Actualizar evento
7. Eventos → Listar por categoria (la relación: solo eventos de Música)
8. Eventos → Eliminar evento (204)

La colección de Bruno ya no está en el repo, yo lo probé todo con Postman.

También se puede probar con curl:

```bash
curl http://13.140.226.55:8084/api/eventos?page=0&size=12
curl http://13.140.226.55:8084/api/eventos/1
```

## Cómo lo desplegué en el VPS

```bash
sudo ufw allow 8084/tcp
mkdir -p /opt/springboot-act3
scp target\gvpdact3-t4-0.0.1-SNAPSHOT.jar root@13.140.226.55:/opt/springboot-act3/
```

En el VPS, dentro de `/opt/springboot-act3/`:

```bash
export DB_PASSWORD="tu_contrasena_mysql"
nohup java -jar gvpdact3-t4-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

Para verificar que las tres actividades sigan vivas:

```bash
curl http://13.140.226.55:8081/api/proyecto
curl http://13.140.226.55:8083/eventos
curl http://13.140.226.55:8084/api/eventos
```

## Capturas de pantalla

### Mis entidades y su relación

![Listado de categorías](docs/Captura%20de%20pantalla%202026-11-10%20223738.png)

### Eventos de una categoría (la relación)

![Eventos de la categoría Música](docs/Captura%20de%20pantalla%202026-11-10%20223658.png)

### Crear un evento (POST 201)

![Crear evento en Postman](docs/Captura%20de%20pantalla%202026-11-10%20223709.png)

### Listar eventos con su categoría

![Listado de eventos](docs/Captura%20de%20pantalla%202026-11-10%20223606.png)

### Detalle con el nombre de la categoría

![Detalle del evento](docs/Captura%20de%20pantalla%202026-11-10%20223910.png)

### Actualizar un evento (PUT 200)

![Actualizar evento en Postman](docs/Captura%20de%20pantalla%202026-11-10%20223718.png)

### Eliminar un evento (DELETE 204)

![Eliminar evento en Postman](docs/Captura%20de%20pantalla%202026-11-10%20223728.png)

## Cómo está organizado mi proyecto

```text
src/main/java/mx/culturavibe/act3/
├── CulturavibeAct3Application.java
├── modelo/
│   ├── Categoria.java        # @Entity, @OneToMany
│   └── Evento.java           # @Entity, @ManyToOne, @ElementCollection
├── dto/
│   ├── CategoriaDTO.java     # @Valid
│   └── EventoDTO.java        # @Valid, categoriaId + categoriaNombre
├── repositorio/
│   ├── CategoriaRepository.java
│   └── EventoRepository.java # JpaRepository + findByCategoriaId
├── servicio/
│   ├── CategoriaService.java
│   ├── EventoService.java
│   └── DataInitializer.java  # guarda 5 categorías y 6 eventos al inicio
├── controlador/
│   ├── CategoriaController.java  # REST /api/categorias
│   ├── EventoController.java     # REST /api/eventos
│   ├── EventoVistaController.java # Thymeleaf /eventos
│   └── HomeController.java
└── config/
    └── GlobalExceptionHandler.java # 404, 400, 409
```
