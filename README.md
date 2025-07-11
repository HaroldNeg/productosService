productos-service
==================

Este repositorio contiene el microservicio `productos-service` del sistema CompraApp.
Este servicio se encarga de la gestión de productos disponibles para su compra, incluyendo su creación, modificación y consulta.

Descripción del Proyecto
-------------------------
`productos-service` es responsable de administrar la información de productos: nombre, descripción, precio, categoría, etc.
Puede ser consultado directamente por el frontend o por otros microservicios como `inventario-service` mediante FeignClient.

Tecnologías utilizadas
-----------------------
- Java 21
- Spring Boot 3.5.3
- Spring Cloud 2025.0.0
- Spring Data JPA
- Spring Web
- Eureka Client
- Config Client

Características
---------------
- Registro de nuevos productos.
- Consulta de productos por ID, categoría, o todos.
- Actualización y eliminación de productos.
- Integración con `inventario-service` mediante REST o Feign.

Ejecución del Proyecto
-----------------------
1. Clonar el repositorio:
   git clone https://github.com/HaroldNeg/productosService.git

2. Entrar al directorio del proyecto:
   cd productosService

3. Compilar el proyecto con Maven:
   mvn clean install

4. Ejecutar el microservicio:
   mvn spring-boot:run

Asegúrate de tener corriendo previamente:
- config-server
- eureka-server

Configuración
-------------
En el archivo `application.yml` debes definir:
- Puerto de escucha del microservicio
- Configuración de base de datos
- Configuración para Eureka y Config Server

Ejemplo de ruta expuesta:
--------------------------
GET /api/productos/{id}  
Retorna la información de un producto por su ID.

Base de Datos
-------------
El servicio está conectado a una base de datos relacional donde almacena la información de los productos.
Asegúrate de configurar correctamente:
- URL de conexión
- Usuario y contraseña
- Dialecto del motor (MySQL, PostgreSQL, etc.)

Observabilidad
--------------
Puedes mejorar el monitoreo con:
- Spring Boot Actuator
- Sleuth + Zipkin
- Prometheus

Licencia
--------
Este proyecto está bajo una licencia de código abierto.

Contacto
--------
Para soporte técnico o sugerencias, abre un issue en el repositorio.
