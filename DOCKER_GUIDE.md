### Option 1 : Docker Compose (Recommandé)

```bash
# Démarrer tous les services
docker-compose up -d

# Vérifier les logs
docker-compose logs -f app

# Arrêter les services
docker-compose down

# Arrêter et supprimer les volumes
docker-compose down -v
```

### Option 2 : Docker Build Manuel

```bash
# Build l'image
docker build -t task-shift-management:latest .

# Run le container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/taskshiftdb \
  -e SPRING_DATASOURCE_USERNAME=taskshift \
  -e SPRING_DATASOURCE_PASSWORD=taskshift123 \
  task-shift-management:latest
```

## 📋 Services Disponibles

Une fois démarré avec `docker-compose up -d`, vous aurez accès à :

| Service | URL | Description |
|---------|-----|-------------|
| **Application** | http://localhost:8080 | API REST principale |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentation interactive |
| **MySQL** | localhost:3307 | Base de données |
| **phpMyAdmin** | http://localhost:8081 | Interface de gestion MySQL |

### Credentials par Défaut

**MySQL:**
- User: `taskshift`
- Password: `taskshift123`
- Database: `taskshiftdb`
- Root Password: `rootpassword`

**phpMyAdmin:**
- Server: `mysql`
- Username: `taskshift`
- Password: `taskshift123`

## 🧪 Test de l'API

### 1. Créer un compte

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@taskshift.com",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### 2. Se connecter

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Réponse:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@taskshift.com",
  "role": "ADMIN"
}
```

### 3. Utiliser le token

```bash
export TOKEN="votre_token_jwt"

# Créer un employé (requiert ADMIN ou MANAGER)
curl -X POST http://localhost:8080/api/employees \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "position": "Developer",
    "status": "ACTIVE"
  }'
```

## 🔧 Configuration Avancée

### Variables d'Environnement

Vous pouvez personnaliser via un fichier `.env` :

```env
# Database
MYSQL_DATABASE=taskshiftdb
MYSQL_USER=taskshift
MYSQL_PASSWORD=taskshift123
MYSQL_ROOT_PASSWORD=rootpassword

# Application
SPRING_PROFILES_ACTIVE=docker
APP_JWT_SECRET=your-super-secret-key-here
APP_JWT_EXPIRATION=86400000

# Ports
APP_PORT=8080
MYSQL_PORT=3307
PHPMYADMIN_PORT=8081
```

Puis utilisez:
```bash
docker-compose --env-file .env up -d
```

### Production Deployment

Pour la production, modifiez `docker-compose.yml`:

```yaml
app:
  environment:
    SPRING_PROFILES_ACTIVE: prod
    SPRING_JPA_HIBERNATE_DDL_AUTO: validate  # Ne pas modifier le schéma
    SPRING_JPA_SHOW_SQL: "false"             # Désactiver les logs SQL
    APP_JWT_SECRET: ${PRODUCTION_JWT_SECRET} # Utiliser une vraie clé secrète
```

## 🔍 Troubleshooting

### Les containers ne démarrent pas

```bash
# Voir les logs détaillés
docker-compose logs

# Reconstruire les images
docker-compose up -d --build

# Nettoyer et redémarrer
docker-compose down -v
docker-compose up -d --build
```

### L'application ne se connecte pas à MySQL

```bash
# Vérifier que MySQL est prêt
docker-compose ps
docker-compose logs mysql

# Attendre le healthcheck
docker-compose up -d
# Attendre ~30 secondes pour MySQL
```

### Accéder au container

```bash
# Shell dans le container de l'application
docker-compose exec app sh

# Shell dans MySQL
docker-compose exec mysql mysql -u taskshift -p
```

## 📊 Monitoring

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Logs

```bash
# Logs en temps réel
docker-compose logs -f app

# Dernières 100 lignes
docker-compose logs --tail=100 app

# Logs MySQL
docker-compose logs -f mysql
```

## 🔄 Mise à Jour

```bash
# 1. Arrêter les services
docker-compose down

# 2. Récupérer les nouvelles modifications
git pull

# 3. Rebuild et redémarrer
docker-compose up -d --build
```

## 🧹 Nettoyage

```bash
# Supprimer les containers et volumes
docker-compose down -v

# Supprimer les images
docker rmi task-shift-management:latest

# Nettoyage complet Docker
docker system prune -a --volumes
```

## 🚀 Déploiement Cloud

### AWS ECS

```bash
# Build pour AWS
docker build -t task-shift-management:latest .
docker tag task-shift-management:latest YOUR_ECR_URI:latest
docker push YOUR_ECR_URI:latest
```

### Kubernetes

Voir le dossier `k8s/` pour les manifests Kubernetes (à venir).

## 📚 Ressources

- [Documentation Docker](https://docs.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Spring Boot Docker](https://spring.io/guides/gs/spring-boot-docker/)

---

**Besoin d'aide ?** Ouvrez une issue sur GitHub!
