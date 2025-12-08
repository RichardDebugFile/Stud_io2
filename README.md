# STD.io - Sistema de Gestión Académica

##  Configuración de Base de Datos con Docker

### Iniciar la base de datos MySQL

```bash
docker-compose up -d
```

### Verificar que el contenedor está corriendo

```bash
docker ps
```

### Detener la base de datos

```bash
docker-compose down
```

### Detener y eliminar datos

```bash
docker-compose down -v
```

## Credenciales de Base de Datos

- **Database Name:** stud_io2
- **Username:** stdio_user
- **Password:** stdio_pass123
- **Root Password:** root123
- **Port:** 3306

## Construcción y Despliegue

```bash
mvn clean package
```

El archivo WAR generado estará en `target/stud_io2.war`
