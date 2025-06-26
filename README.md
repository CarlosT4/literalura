# 📚 Literalura - Catálogo de Libros

Proyecto desarrollado con Spring Boot que permite gestionar un catálogo de libros consumiendo la API de Gutendx y almacenando la información en una base de datos PostgreSQL.

## 🚀 Características

- **Búsqueda de libros por título** usando la API de Gutendx
- **Persistencia de datos** con PostgreSQL y Spring Data JPA
- **Gestión de autores** con relaciones entre entidades
- **Estadísticas por idioma** usando Java Streams
- **Consultas derivadas** para filtrar autores vivos por año
- **Interfaz de consola interactiva** con validación de entrada

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson** (para mapeo JSON)
- **Gradle** (gestor de dependencias)

## 📋 Requisitos Previos

1. **Java 17** o superior
2. **PostgreSQL** instalado y ejecutándose
3. **Gradle** (incluido en el wrapper del proyecto)

## ⚙️ Configuración

### 1. Base de Datos PostgreSQL

Crear una base de datos llamada `literalura`:

```sql
CREATE DATABASE literalura;
```

### 2. Configuración de application.properties

Actualizar el archivo `src/main/resources/application.properties` con tus credenciales de PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/literalura/
│   │   ├── dto/                    # Clases DTO para mapeo JSON
│   │   │   ├── AutorDTO.java
│   │   │   ├── LibroDTO.java
│   │   │   └── RespuestaApiDTO.java
│   │   ├── entity/                 # Entidades JPA
│   │   │   ├── Autor.java
│   │   │   └── Libro.java
│   │   ├── repository/             # Repositorios JPA
│   │   │   ├── AutorRepository.java
│   │   │   └── LibroRepository.java
│   │   ├── service/                # Servicios
│   │   │   ├── ConsumoApi.java
│   │   │   ├── ConversorDatos.java
│   │   │   └── LibroService.java
│   │   └── LiteraluraApplication.java
│   └── resources/
│       └── application.properties
└── build/
    └── libs/
        └── literalura-0.0.1-SNAPSHOT.jar
```

## 🏃‍♂️ Ejecución

### 1. Compilar el proyecto

```bash
# En Windows
./gradlew build

# En Linux/Mac
./gradlew build
```

### 2. Ejecutar la aplicación

```bash
java -jar build/libs/literalura-0.0.1-SNAPSHOT.jar
```

### Alternativa usando Gradle (modo desarrollo)

Si necesitas ejecutar en modo desarrollo:

```bash
# En Windows
./gradlew bootRun

# En Linux/Mac
./gradlew bootRun
```

## 📖 Uso de la Aplicación

Al ejecutar la aplicación, verás un menú interactivo con las siguientes opciones:

```
==================================================
🔶 LITERALURA - CATÁLOGO DE LIBROS 🔶
==================================================
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores
4 - Listar autores vivos en un determinado año
5 - Listar libros por idioma
0 - Salir
==================================================
```

### 1. Buscar libro por título
- Ingresa el título del libro que deseas buscar
- La aplicación consulta la API de Gutendx
- Si encuentra el libro, lo guarda en la base de datos con su autor
- Si ya existe, te informa que está registrado
- Maneja automáticamente la creación de autores nuevos

### 2. Listar libros registrados
- Muestra todos los libros almacenados en la base de datos
- Información incluye: título, autor, idioma y número de descargas
- Formato ordenado y fácil de leer

### 3. Listar autores
- Muestra todos los autores registrados alfabéticamente
- Incluye fechas de nacimiento y muerte (si están disponibles)
- Formato: "Autor: Nombre (año_nacimiento - año_muerte)"

### 4. Listar autores vivos en un determinado año
- Ingresa un año específico (ejemplo: 1850)
- Muestra autores que estaban vivos en ese año
- Considera autores nacidos antes o en ese año Y que murieron después o que aún viven

### 5. Listar libros por idioma
- Muestra estadísticas de libros agrupados por idioma
- Traduce códigos de idioma a nombres legibles (es → Español, en → Inglés, etc.)
- Ordenado por cantidad de libros descendente

### 0. Salir
- Termina la ejecución de la aplicación con mensaje de despedida

## 🔧 Funcionalidades Técnicas

### Mapeo JSON con Jackson
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDTO {
    @JsonAlias("title")
    private String titulo;
    
    @JsonAlias("authors")
    private List<AutorDTO> autores;
    
    @JsonAlias("download_count")
    private Integer numeroDescargas;
}
```

### Relaciones JPA
```java
// En la entidad Autor
@OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Libro> libros;

// En la entidad Libro
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "autor_id")
private Autor autor;
```

### Consultas Derivadas
```java
// Buscar autores vivos en un año específico
@Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :año)")
List<Autor> findAutoresVivosEnAño(@Param("año") Integer año);

// Estadísticas por idioma
@Query("SELECT l.idioma, COUNT(l) FROM Libro l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
List<Object[]> countLibrosPorIdioma();
```

### Gestión de Transacciones
- Uso de `@Transactional` para operaciones de base de datos
- `@Transactional(readOnly = true)` para consultas de solo lectura
- Manejo automático de entidades JPA en contexto de persistencia

### Validación de Entrada
- Manejo robusto de excepciones `InputMismatchException`
- Validación de campos obligatorios y formatos
- Limpieza automática de buffer del Scanner
- Mensajes de error informativos

## 🔍 Detalles de Implementación

### Consumo de API
```java
// URL base de Gutendx
private static final String URL_BASE = "https://gutendx.com/books/";

// Búsqueda con codificación URL
String url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
```

### Manejo de Errores
- Timeout de 30 segundos para peticiones HTTP
- Manejo de interrupciones de hilo
- Logging para debugging
- Recuperación graceful ante fallos de API

### Persistencia Inteligente
- Verificación de duplicados antes de guardar
- Reutilización de autores existentes
- Creación automática de autor "Desconocido" cuando no hay información

## 📡 API Utilizada

**Gutendx API**: https://gutendex.com/books/

### Ejemplos de consulta:
```
# Buscar por título
https://gutendx.com/books/?search=frankenstein

# Respuesta JSON esperada:
{
  "count": 1,
  "results": [
    {
      "id": 84,
      "title": "Frankenstein; Or, The Modern Prometheus",
      "authors": [
        {
          "name": "Shelley, Mary Wollstonecraft",
          "birth_year": 1797,
          "death_year": 1851
        }
      ],
      "languages": ["en"],
      "download_count": 50000
    }
  ]
}
```

## 🎯 Características del Proyecto

Este proyecto implementa todas las funcionalidades requeridas del challenge:

- ✅ **Análisis de JSON con Jackson**
- ✅ **Conversión de datos DTO → Entity**
- ✅ **Interacción con usuario vía consola**
- ✅ **Consulta de libros por título**
- ✅ **Consulta de autores con fechas de vida**
- ✅ **Persistencia con PostgreSQL y JPA**
- ✅ **Estadísticas por idioma**
- ✅ **Consulta de autores vivos por año**

## 🚀 Posibles Mejoras Futuras

- Interfaz web con Spring MVC
- API REST para exposición de datos
- Búsqueda por múltiples criterios
- Sistema de favoritos
- Exportación de datos a PDF/Excel
- Cache para consultas frecuentes
- Paginación de resultados

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request


## 👨‍💻 Autor
Carlos Ttito

Desarrollado como parte del challenge de Alura - ONE (Oracle Next Education)

---

⭐️ Si te gustó este proyecto, ¡dale una estrella!

**¿Necesitas ayuda?** Abre un issue en el repositorio y te ayudaremos a resolver cualquier problema.