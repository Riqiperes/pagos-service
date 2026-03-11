# Guía de Uso: Examen Parcial - Arquitectura de Microservicios

Esta guía detalla los pasos necesarios para levantar, configurar y probar el ecosistema de microservicios, incluyendo la infraestructura de base de datos y observabilidad.

## Requisitos Previos
- **Docker & Docker Compose**
- **Java 21**
- **Maven** (opcional, si deseas compilar localmente)

---

## Paso 1: Compilación de los Microservicios
Antes de levantar los contenedores, es necesario generar los archivos `.jar` de cada servicio. Ejecuta el siguiente comando en la raíz del proyecto (requiere Maven instalado):

```powershell
# Compilar todos los servicios
cd apigateway; ./mvnw clean package -DskipTests; cd ..
cd Eureka-server; ./mvnw clean package -DskipTests; cd ..
cd productos-service; ./mvnw clean package -DskipTests; cd ..
cd ordenes-service; ./mvnw clean package -DskipTests; cd ..
cd pagos-service; ./mvnw clean package -DskipTests; cd ..
```

---

## Paso 2: Levantar la Infraestructura
Primero levantaremos la base de datos y el emulador de AWS para habilitar la observabilidad.

```powershell
cd iac
# Levantar MongoDB y LocalStack
docker compose -f docker-compose-mongodb.yml up -d
docker compose -f docker-compose-localstack.yml up -d
```
*Nota: El contenedor de LocalStack ejecutará automáticamente el script `init-aws.sh` para crear los log-groups: `producto-log-group`, `ordenes-log-group` y `pagos-log-group`.*

---

## Paso 3: Levantar los Microservicios
Una vez que la infraestructura esté lista, levantamos los 5 servicios Spring Boot:

```powershell
docker compose -f docker-compose-services.yml up -d --build
```

---

## Paso 4: Verificación de Servicios
Puedes verificar que todo esté funcionando correctamente accediendo a los siguientes endpoints:

- **Eureka Server Dashboard**: [http://localhost:8761](http://localhost:8761) (Aquí verás los 5 servicios registrados).
- **API Gateway**: [http://localhost:8080](http://localhost:8080) (Puerta de entrada principal).
- **Productos Service**: [http://localhost:8081](http://localhost:8081)
- **Ordenes Service**: [http://localhost:8082](http://localhost:8082)
- **Pagos Service**: [http://localhost:8083](http://localhost:8083)

---

## Paso 5: Flujo de Prueba (Compra)
Todo el tráfico debe pasar por el **API Gateway (8080)**. Sigue este orden para simular una compra:

1. **Consultar Productos**: 
   `GET http://localhost:8080/productos`
2. **Crear Orden**: 
   `POST http://localhost:8080/ordenes` (Cuerpo JSON con datos de la orden)
3. **Procesar Pago**: 
   `POST http://localhost:8080/pagos` (Cuerpo JSON con datos del pago)

---

## Paso 6: Verificación de Logs (Observabilidad)
Los microservicios enviarán logs a CloudWatch (emulado en LocalStack). Puedes consultarlos usando la AWS CLI o verificando los logs del contenedor de LocalStack:

```powershell
# Listar log groups creados
docker exec localstack awslocal logs describe-log-groups
```

---

## Comandos Útiles
- **Detener todo**: `docker compose -f docker-compose-services.yml down; docker compose -f docker-compose-mongodb.yml down; docker compose -f docker-compose-localstack.yml down`
- **Ver logs de un servicio**: `docker logs -f productos-service`
