
# **API**

- La API se **desarrolló** con **Spring Boot** (Java versión 17), usando **Spring DATA JPA** para la comunicación con la base de datos, y **Swagger** para consumir la API.
- Se utilizó una base de datos SQL, más precisamente **MySQL**.
- Se utilizó **JUnit** y **Mockito** para realizar pruebas unitarias de los métodos.
- En el archivo `application.properties` se definen las configuraciones para la base de datos donde se deben estipular los datos de login de la misma, además de su nombre (se debe crear en caso de que no exista la base de datos). Además, se añadió una variable más llamada `spring.frontend.url` que funcionará por si se desea consumir la API desde una interfaz de usuario, por lo cual se le debería brindar la URL del cliente para poder consumirla.

## **Desarrollo**

- Se creó una única entidad (Tabla) para la manipulación y gestión de la información con la base de datos llamada **DnaRecord**.
- En la carpeta **controllers** se define la configuración para el consumo de la API, predefiniendo las URLs que podrán consumir la API, brindando así una capa de seguridad. Además, se definen los **endpoints** para el consumo de la API.
- En la carpeta **service** se define la clase **RecordsService** que implementa la interfaz **IRecordService** y se utiliza para gestionar operaciones relacionadas con secuencias de ADN (DNA) en la base de datos. Una explicación de cada método:

    ### **Constructor**:

    - `RecordsService(IRecordsDao recordsDao)`: Inicializa el servicio con una instancia del DAO (**recordsDao**).

    ### **Métodos CRUD**:

    - `findAll()`: Obtiene todas las secuencias de ADN almacenadas en la base de datos. (Si bien no se utiliza, puede quedar indicado para una posible próxima implementación)
    - `findById(Long id)`: Busca y devuelve una secuencia de ADN por su ID, o `null` si no se encuentra. (Si bien no se utiliza, puede quedar indicado para una posible próxima implementación)
    - `save(String[] dna)`: Guarda una nueva secuencia de ADN en la base de datos. Primero verifica si la secuencia ya existe para evitar duplicados. Si no existe, verifica si es mutante y luego guarda la secuencia con el resultado.
    - `delete(Long id)`: Elimina una secuencia de ADN de la base de datos por su ID. (Si bien no se utiliza, puede quedar indicado para una posible próxima implementación)

    ### **Estadísticas**:

    - `getStats()`: Calcula y devuelve estadísticas de ADN en un `Map`, incluyendo el número de secuencias mutantes, humanas y el ratio entre ellas.

    ### **Verificación de ADN**:

    - `isMutant(String[] dna)`: Verifica si una secuencia de ADN es mutante. Convierte la secuencia a una matriz, imprime la matriz y busca secuencias de cuatro letras iguales en direcciones horizontal, vertical y diagonal.
    - `verificarSecuencia(char[][] matriz, int fila, int columna, int direccionFila, int direccionColumna)`: Es un método auxiliar que verifica si hay una secuencia de cuatro letras iguales en la dirección especificada.

## **Conexión**

- En un entorno de producción, para consumir la API se lo puede hacer utilizando **Swagger** o a través de una interfaz de usuario. Ya está configurado para ser consumida de este modo, la url a utilizar es https://mercadolibre.fly.dev/swagger-ui/index.html.
- En el caso de que se trabaje en un entorno de desarrollo, simplemente bastará con tener la base de datos creada para que así el servidor cree las entidades en la base de datos y se pueda consumir la API sin problema.
- De manera predeterminada esta configurado para permitir peticiones http/https al localhost:* (cualquier puerto).

## **Base de datos**

- Script base de datos:

CREATE DATABASE IF NOT EXISTS mercadolibre;
USE mercadolibre;

- Las Tablas se generan de manera automatica al levantar el servidor.