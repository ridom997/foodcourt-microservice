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
En caso de usar un endpoint sin la autorización necesaria saldrá de respuesta "Wrong credentials or role not allowed".
### HU2:
+ Es necesario ejecutar del data.sql la seccion "-- hu-2"
  + El usuario insertado corresponde a un propietario con correo: corr@e.o, contraseña= 1234. (dicho usuario tiene id = 100).
+ Crear un nuevo restaurante "/restaurants" Se puede proporcionar como idOwner = 100 que es el usuario previamente insertado con el script data.sql.
+ Para que la petición se cumpla correctamente hay que tener el microservicio user-microservice activo.
+ Desde la Hu-5 se debe estar autenticado como administrador (admin) para usar el endpoint.
### HU3: 
+ Es necesario ejecutar del data.sql la seccion "-- hu-3".
+ Al hacer el paso anterior ya se tiene habilitada 1 categoria en base de datos (idCategory = 100) y un restaurante (idRestaurant = 100 con idOwnerRestaurant = 100).
+ Crear un plato "/dishes", se pueden usar las variables del paso anterior.
+ Desde la Hu-5 se debe estar autenticado como el propietario (owner) del restaurante para usar el endpoint.
### HU4: 
+ Es recomendable ejecutar del data.sql la seccion "-- hu-4" para tener ya listo un dish a modificar.
+ Editar plato /dishes/{id}, Si se hizo el paso anterior poner las variables (path variable id del dish > id = 100 y idOwnerRestaurant = 100).
+ Desde la Hu-5 se debe estar autenticado como el propietario (owner) del restaurante para usar el endpoint.
### HU5: 
+ Para poder usar los endpoints es necesario utilizar el microservicio de usuarios para autenticarse como administrador o como propietario.
### HU6: 
+ Se agregó un endpoint para verificar el dueño de un restaurante "/restaurants/{id}/validateOwner" es necesario proveer el id del restaurante (puede ser el 100 creado por el script sql). 
+ Se debe estar autenticado como propietario (owner).
+ Este endpoint valida si el idUser (presente en el token jwt) es el dueño del restaurante que llega el path {id} 
### HU7:
+ Editar estado de plato (dish) "/dishes/{id}/status?active=BOOLEAN" reemplazar BOOLEAN por true o false.
+ Es necesario usar el endpoint autenticado como propietario (owner), y que dicho propietario sea el dueño del restaurante al cual se le va actualizar el plato.
+ En caso de tener los datos del data.sql se puede usar como identificador del plato (id = 100).
### HU9:
+ Para consumir el endpoint "/restaurants" es necesario estar autenticado como cliente.
+ Es necesario proveer 2 request params
  + page: numero de la pagina. (empieza desde 0)
  + size: tamaño de la pagina. (mayor a 0)
+ En caso de no encontrar una lista de restaurantes ya sea por la configuracion del request param, o por falta de datos en base de datos, la respuesta sera un 404.
### HU10:
+ Es necesario ejecutar del data.sql la seccion "-- HU-10". para agregar más categorias y un plato al restaurante con id 100.
+ Para consumir el endpoint "/restaurants/{idRestaurant}/dishes" es necesario estar autenticado como cliente y reemplazar {idRestaurant) por el id del restaurante.
+ Es necesario proveer 2 request params
    + page: numero de la pagina. (empieza desde 0)
    + size: tamaño de la pagina. (mayor a 0)
+ El request param "idCategory" es opcional e indica el id de la categoria por el cual se va a filtrar la lista de platos (dishes)
<!-- ROADMAP -->
## Tests

- Right-click the test folder and choose Run tests with coverage
