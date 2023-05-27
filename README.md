<br />
<div align="center">
<h3 align="center">PRAGMA POWER-UP</h3>
  <p align="center">
    In this challenge you are going to design the backend of a system that centralizes the services and orders of a restaurant chain that has different branches in the city.
  </p>
</div>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these steps.

### Prerequisites

* JDK 17 [https://jdk.java.net/java-se-ri/17](https://jdk.java.net/java-se-ri/17)
* Gradle [https://gradle.org/install/](https://gradle.org/install/)
* MySQL [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)

### Recommended Tools
* IntelliJ Community [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* Postman [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

### Installation

1. Clone the repository
3. Create a new database in MySQL called powerup using the steps in the README of the repository ["user-microservice"](https://github.com/ridom997/user-microservice). For simplicity both microservices will use only one database.
4. Update the database connection settings
   ```yml
   # src/main/resources/application-dev.yml
   spring:
      datasource:
          url: jdbc:mysql://localhost/powerup
          username: root
          password: <your-password>
   ```
5. Run this project to create the tables of this microservices in db (Right-click the class FoodCourtMicroserviceApplication and choose Run)
6. After the tables are created execute src/main/resources/data.sql content to populate the database with an owner user.


<!-- USAGE -->
## Usage

1. Right-click the class FoodCourtMicroserviceApplication and choose Run
3. Start the user-microservice ["user-microservice"](https://github.com/ridom997/user-microservice)
4.  Open [http://localhost:8091/swagger-ui/index.html](http://localhost:8091/swagger-ui/index.html) in your web browser
5. Test the endpoints (view guide)

<!-- GUIDE -->
## GUIDE (SPANISH)
-El usuario insertado corresponde a un propietario con correo: corr@e.o, contraseña= 1234. (dicho usuario tiene id = 100) \
+ HU2: Crear un nuevo restaurante "/restaurants" Se puede proporcionar como idOwner = 100 que es el usuario previamente insertado con el script data.sql (es necesario tener el microservicio usuarios corriendo).
+ HU3: Crear un plato "/dishes", se puede usar como idOwnerRestaurant = 100, idCategory=100, idRestaurant = 100
+ HU4: Editar plato /dishes/{id}, se puede poner de path variable (id del dish) id = 100, idOwnerRestaurant = 100.
+ HU5: Para poder usar los endpoints es necesario utilizar el microservicio de  usuarios para autenticarse como admin o como propietario.
+ HU6: Se agregó un endpoint para verificar el dueño de un restaurante "/restaurants/{id}/validateOwner" es necesario proveer el id del restaurante (puede ser el 100 creado por el script), y es obligatorio proveer un token jwt de Owner o propietario.

<!-- ROADMAP -->
## Tests

- Right-click the test folder and choose Run tests with coverage
