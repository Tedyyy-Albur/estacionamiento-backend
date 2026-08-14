# 🚗 API de Gestión de Estacionamiento de Vehículos

API Backend RESTful desarrollada con **Spring Boot 3** y **Java 17** para la administración integral de acceso de vehículos a un estacionamiento. Permite gestionar entradas y salidas de vehículos, cálculo dinámico de tarifas según tipo de vehículo, reportes de pagos para residentes y reinicio mensual consolidado.

---

## 🛠️ 1. Versiones y Stack Tecnológico

| Tecnología / Herramienta | Versión | Descripción |
| :--- | :--- | :--- |
| **Java** | `17` (JDK / JRE) | Lenguaje principal de desarrollo |
| **Spring Boot** | `3.2.5` | Framework backend (Spring Web, Data JPA, Validation) |
| **Maven** | `3.9.6` | Gestor de dependencias y construcción |
| **H2 Database** | `2.2.224` | Base de datos embebida persistente en archivo local |
| **Springdoc OpenAPI** | `2.5.0` | Generación de documentación interactiva Swagger UI |
| **Lombok** | Incluido | Reducción de código boilerplate (Getters/Setters/Builders) |

---

## 📁 2. Estructura del Proyecto

El proyecto sigue una arquitectura en capas desacoplada y basada en principios **SOLID**:

```text
estacionamiento-api/
├── .mvn/                         # Wrapper de Maven
├── data/                         # Almacenamiento persistente de base de datos H2
│   └── estacionamiento.mv.db
├── src/
│   ├── main/
│   │   ├── java/com/estacionamiento/api/
│   │   │   ├── config/           # Configuración CORS y beans globales
│   │   │   │   └── CorsConfig.java
│   │   │   ├── controller/       # Endpoints REST API (Controllers)
│   │   │   │   ├── EstanciaController.java
│   │   │   │   ├── MesController.java
│   │   │   │   ├── ResidenteController.java
│   │   │   │   └── VehiculoController.java
│   │   │   ├── dto/              # Data Transfer Objects (Requests y Responses)
│   │   │   │   ├── ApiResponseDTO.java
│   │   │   │   ├── EstanciaEntradaRequestDTO.java
│   │   │   │   ├── EstanciaSalidaRequestDTO.java
│   │   │   │   ├── EstanciaResponseDTO.java
│   │   │   │   ├── HistorialMensualDTO.java
│   │   │   │   ├── PagoResidenteDTO.java
│   │   │   │   ├── VehiculoRequestDTO.java
│   │   │   │   └── VehiculoResponseDTO.java
│   │   │   ├── entity/           # Entidades JPA (Modelo de Dominio)
│   │   │   │   ├── Estancia.java
│   │   │   │   ├── HistorialMensual.java
│   │   │   │   ├── HistorialResidenteMes.java
│   │   │   │   ├── Residente.java
│   │   │   │   ├── TipoVehiculo.java (Enum: OFICIAL, RESIDENTE, NO_RESIDENTE)
│   │   │   │   └── Vehiculo.java
│   │   │   ├── exception/        # Manejo global de excepciones
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/       # Interfaces de repositorios JPA
│   │   │   │   ├── EstanciaRepository.java
│   │   │   │   ├── HistorialMensualRepository.java
│   │   │   │   ├── HistorialResidenteMesRepository.java
│   │   │   │   ├── ResidenteRepository.java
│   │   │   │   └── VehiculoRepository.java
│   │   │   ├── service/          # Capa de Lógica de Negocio
│   │   │   │   ├── EstanciaService.java
│   │   │   │   ├── VehiculoService.java
│   │   │   │   └── impl/
│   │   │   │       ├── EstanciaServiceImpl.java
│   │   │   │       └── VehiculoServiceImpl.java
│   │   │   └── strategy/         # Patrón Strategy para cálculo de tarifas
│   │   │       ├── TarifaStrategy.java
│   │   │       ├── TarifaOficialStrategy.java
│   │   │       ├── TarifaResidenteStrategy.java
│   │   │       ├── TarifaNoResidenteStrategy.java
│   │   │       └── TarifaStrategyFactory.java
│   │   └── resources/
│   │       └── application.properties # Configuración de Spring y BD
│   └── test/                     # Pruebas unitarias con JUnit 5 y Mockito
├── backup_estacionamiento.sql    # Script SQL de respaldo / carga inicial H2
├── mvnw / mvnw.cmd               # Ejecutables Maven Wrapper
└── pom.xml                       # Archivo de configuración POM Maven
```

---

## 🚀 3. Cómo Levantar el Proyecto

### Requisitos Previos
- **Java 17** o superior instalado.
- **Maven 3.8+** (opcional si utilizas el wrapper `./mvnw` incluido).

---

### Opción A: Ejecución Local con Maven

1. **Clonar / Ubicarse en el proyecto:**
   ```bash
   cd /ruta/al/proyecto
   ```

2. **Compilar y Ejecutar la aplicación:**
   - En Linux/macOS:
     ```bash
     ./mvnw spring-boot:run
     ```
   - En Windows (CMD/PowerShell):
     ```cmd
     mvnw.cmd spring-boot:run
     ```

3. **Verificar estado:**
   La API estará lista y escuchando en `http://localhost:8080`.

---

### Opción B: Ejecución con JAR Compilado

1. **Generar el paquete JAR:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Ejecutar el archivo JAR:**
   ```bash
   java -jar target/estacionamiento-api-0.0.1-SNAPSHOT.jar
   ```

---

## 📑 4. Documentación Interactiva y Consola DB

Una vez levantada la aplicación, se puede acceder a las siguientes interfaces web:

- 📖 **Swagger UI (OpenAPI 3):**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  Permite probar interactivamente todos los endpoints de la API (`/neo/...`).

- 🗄️ **Consola H2 Database:**  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  - **JDBC URL:** `jdbc:h2:file:./data/estacionamiento`
  - **User Name:** `sa`
  - **Password:** *(dejar en blanco)*

---

## 🗄️ 5. Base de Datos

El sistema utiliza la base de datos **H2** configurada en modo **archivo persistente local** (`./data/estacionamiento`) con soporte `AUTO_SERVER=TRUE`, permitiendo la lectura/escritura simultánea por la aplicación y la consola web.

### Script SQL de Respaldo Incluido
En la raíz del proyecto se incluye el archivo [`backup_estacionamiento.sql`](file:/backup_estacionamiento.sql), con la estructura completa DDL y datos de prueba DML para inicializar o restaurar la base de datos.
