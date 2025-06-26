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
```

## 🏃‍♂️ Ejecución

### Usando Gradle Wrapper (Recomendado)

```bash
# En Windows
./gradlew bootRun

# En Linux/Mac
./gradlew bootRun
```

### Usando Java directamente

```bash
# Compilar el proyecto
./gradlew build

# Ejecutar el JAR generado
java -jar build/libs/literalura-0.0.1-SNAPSHOT.jar
```

## 📖 Uso de la Aplicación

Al ejecutar la aplicación, verás un menú interactivo con las siguientes opciones:

### 1. Buscar libro por título
- Ingresa el título del libro que deseas buscar
- La aplicación consulta la API de Gutendx
- Si encuentra el libro, lo guarda en la base de datos
- Si ya existe, te informa que está registrado

### 2. Listar libros registrados
- Muestra todos los libros almacenados en la base de datos
- Información incluye: título, autor, idioma y número de descargas

### 3. Listar autores
- Muestra todos los autores registrados
- Incluye fechas de nacimiento y muerte (si están disponibles)

### 4. Listar autores vivos en un determinado año
- Ingresa un año específico
- Muestra autores que estaban vivos en ese año
- Considera fecha de nacimiento y muerte para el cálculo

### 5. Listar libros por idioma
- Muestra estadísticas de libros agrupados por idioma
- Traduce códigos de idioma a nombres legibles

### 0. Salir
- Termina la ejecución de la aplicación

## 🔧 Funcionalidades Técnicas

### Mapeo JSON con Jackson
- Uso de `@JsonAlias` para mapear nombres de campos
- `@JsonIgnoreProperties` para ignorar campos no necesarios
- `ObjectMapper` para conversión automática

### Relaciones JPA
- Relación `@OneToMany` entre Autor y Libro
- Relación `@ManyToOne` entre Libro y Autor
- Cascade types para persistencia automática

### Consultas Derivadas
```java
// Buscar autores vivos en un año específico
@Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :año)")
List<Autor> findAutoresVivosEnAño(@Param("año") Integer año);

// Estadísticas por idioma
@Query("SELECT l.idioma, COUNT(l) FROM Libro l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
List<Object[]> countLibrosPorIdioma();
```

### Validación de Entrada
- Manejo de excepciones `InputMismatchException`
- Validación de campos obligatorios
- Limpieza de buffer del Scanner

## 🐛 Solución de Problemas Comunes

### Error de conexión a PostgreSQL
- Verificar que PostgreSQL esté ejecutándose
- Comprobar credenciales en `application.properties`
- Asegurar que la base de datos `literalura` existe

### Error al consumir la API
- Verificar conexión a internet
- La API de Gutendx a veces tiene limitaciones de rate
- Reintentar la búsqueda después de unos segundos

### Libros duplicados
- La aplicación verifica automáticamente si un libro ya existe
- Se basa en el título para evitar duplicados

## 📡 API Utilizada

**Gutendx API**: https://gutendx.com/books/

Ejemplo de consulta:
```
https://gutendx.com/books/?search=frankenstein
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

Desarrollado como parte del challenge de Alura - ONE (Oracle Next Education)

---

⭐️ Si te gustó este proyecto, ¡dale una estrella!