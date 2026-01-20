# efraude - Fraud Reporting Platform

**efraude** is a SEO-friendly, community-driven platform where users can report scams and frauds worldwide, vote on them, and help others avoid becoming victims.

## 🚀 Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.1**
- **Maven** - Build and dependency management
- **PostgreSQL** - Primary database
- **Apache Kafka** - Asynchronous event processing
- **Spring Data JPA** - Data access layer
- **Flyway** - Database migrations
- **Spring Security** - Authentication and authorization
- **OAuth2 Client** - Google login integration

### Frontend
- **Thymeleaf** - Server-side template engine for SEO
- **HTMX** - Interactive UI without heavy JavaScript
- **Custom CSS** - Responsive design with orange/yellow/black theme

### Infrastructure
- **Docker Compose** - Local development environment
- **SendGrid** - Email service for confirmations

## 📋 Features

### Core Functionality
- ✅ **Anonymous Browsing** - View fraud reports without login
- ✅ **User Registration** - Local signup with email verification
- ✅ **Google OAuth2** - Quick login with Google
- ✅ **Fraud Reporting** - Verified users can report frauds
- ✅ **Voting System** - Upvote/downvote fraud reports
- ✅ **Comments** - Discussion on fraud reports
- ✅ **Search & Filter** - Find frauds by keywords and country
- ✅ **Admin Moderation** - Archive frauds
- ✅ **Internationalization** - English and Portuguese (Brazil)

### Technical Features
- ✅ **SEO Optimized** - Server-rendered pages, canonical URLs, OpenGraph tags
- ✅ **Email Verification** - Required before login
- ✅ **Fraud Scoring** - Configurable voting thresholds
- ✅ **Clean Architecture** - Separated API and Web layers
- ✅ **Kafka Events** - Asynchronous email processing
- ✅ **Security** - BCrypt passwords, role-based access control

## 🏗️ Architecture

### Project Structure

```
efraude/
├── src/main/java/com/efraude/
│   ├── EfraudeApplication.java           # Main application entry point
│   ├── api/                               # REST API layer
│   │   ├── controller/                    # API controllers
│   │   └── dto/                           # Request/Response DTOs
│   ├── config/                            # Spring configurations
│   │   ├── SecurityConfig.java            # Security and OAuth2
│   │   ├── KafkaConfig.java               # Kafka setup
│   │   ├── LocaleConfig.java              # i18n configuration
│   │   └── WebConfig.java                 # Web MVC config
│   ├── domain/                            # Core business logic
│   │   ├── model/                         # JPA entities
│   │   ├── repository/                    # Spring Data repositories
│   │   └── service/                       # Business services
│   ├── messaging/                         # Kafka infrastructure
│   │   ├── event/                         # Event DTOs
│   │   ├── producer/                      # Kafka producers
│   │   ├── consumer/                      # Kafka consumers
│   │   └── email/                         # Email service
│   ├── security/                          # Security implementations
│   └── web/                               # Web UI layer
│       └── controller/                    # Thymeleaf controllers
├── src/main/resources/
│   ├── application.yml                    # Main configuration
│   ├── application-local.yml              # Local development config
│   ├── db/migration/                      # Flyway SQL scripts
│   ├── messages.properties                # English i18n
│   ├── messages_pt_BR.properties          # Portuguese i18n
│   ├── templates/                         # Thymeleaf templates
│   │   ├── layout/                        # Base layouts
│   │   ├── auth/                          # Authentication pages
│   │   ├── fraud/                         # Fraud pages
│   │   └── admin/                         # Admin pages
│   └── static/                            # Static assets
│       ├── css/style.css                  # Application styles
│       ├── js/app.js                      # Client-side JS
│       └── robots.txt                     # SEO robots file
├── docker-compose.yml                     # Docker Compose configuration
├── Dockerfile                             # Application container
├── pom.xml                                # Maven dependencies
└── .env.example                           # Environment template
```

### Layer Separation

The codebase follows a clean separation between API and Web layers:

- **API Layer** (`/api/**`) - RESTful endpoints for future mobile or SPA clients
- **Web Layer** (`/web/**`) - Server-rendered HTML pages for SEO
- **Domain Layer** - Shared business logic (services, repositories, models)
- **Messaging Layer** - Kafka events and email sending

This architecture allows the API to be extracted into a separate service in the future if needed.

## 🚦 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.9+**
- **Docker & Docker Compose**

### 1. Clone and Configure

```bash
git clone <repository-url>
cd efraude

# Copy environment template
cp .env.example .env

# Edit .env with your configuration
# At minimum, set SENDGRID_API_KEY if you want email to work
# Otherwise, emails will be logged to console
```

### 2. Run with Docker Compose

The easiest way to run the entire stack locally:

```bash
# Start all services (Postgres, Kafka, Zookeeper, Application)
docker compose up

# The application will be available at http://localhost:8080
```

Docker Compose will:
- Start PostgreSQL on port 5432
- Start Kafka + Zookeeper
- Build and run the Spring Boot application
- Run Flyway migrations automatically
- Wait for all dependencies to be healthy before starting the app

### 3. Alternative: Run Locally (without Docker)

If you prefer to run services separately:

```bash
# Start only Postgres and Kafka
docker compose up postgres kafka zookeeper

# Run the Spring Boot app locally
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Access the Application

- **Web UI**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

### 5. Create Your First Admin User

To access admin features, manually update a user in the database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```

## 🔐 Environment Variables

Key environment variables (see `.env.example` for full list):

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `DB_HOST` | PostgreSQL host | No | localhost |
| `DB_PORT` | PostgreSQL port | No | 5432 |
| `DB_NAME` | Database name | No | efraude |
| `DB_USER` | Database user | No | efraude_user |
| `DB_PASSWORD` | Database password | No | efraude_pass |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka connection | No | localhost:9092 |
| `SENDGRID_API_KEY` | SendGrid API key | No* | (logs to console) |
| `SENDGRID_FROM_EMAIL` | Sender email address | No | noreply@efraude.com |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID | No** | (disabled) |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 secret | No** | (disabled) |
| `BASE_URL` | Application base URL | No | http://localhost:8080 |

*If not set, emails are logged to console instead of sent
**If not set, Google login button won't work

## 📧 Email Configuration

### SendGrid Setup

1. Create a free SendGrid account at https://sendgrid.com
2. Generate an API key from Settings > API Keys
3. Add the API key to your `.env` file:
   ```
   SENDGRID_API_KEY=SG.your_api_key_here
   SENDGRID_FROM_EMAIL=noreply@yourdomain.com
   ```
4. Verify your sender email in SendGrid

### Development Mode (No SendGrid)

If `SENDGRID_API_KEY` is not set, the application will log emails to console instead of sending them. This is useful for local development and testing.

## 🔑 Google OAuth2 Setup

1. Go to https://console.cloud.google.com
2. Create a new project or select existing
3. Navigate to "APIs & Services" > "Credentials"
4. Create OAuth 2.0 Client ID
5. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
6. Copy Client ID and Secret to `.env`:
   ```
   GOOGLE_CLIENT_ID=your_client_id
   GOOGLE_CLIENT_SECRET=your_client_secret
   ```

## 📊 Database Migrations

Database schema is managed by Flyway. Migrations are located in `src/main/resources/db/migration/`.

### Migration Files
- `V1__create_users_table.sql` - Users and auth
- `V2__create_email_verification_tokens_table.sql` - Email verification
- `V3__create_frauds_table.sql` - Fraud reports
- `V4__create_votes_table.sql` - Voting system
- `V5__create_comments_table.sql` - Comments

Migrations run automatically on application startup.

## 🎯 Key Flows

### 1. User Registration & Email Confirmation

1. User signs up at `/web/auth/signup`
2. `AuthService` creates user with `emailVerified=false`
3. `AuthService` publishes `user.registered` Kafka event
4. `UserRegistrationConsumer` receives event and sends confirmation email via SendGrid
5. User clicks link in email → `/web/auth/confirm?token=xxx`
6. `AuthService` validates token and sets `emailVerified=true`
7. User can now login

### 2. Fraud Reporting

1. Logged-in user navigates to `/web/frauds/create`
2. Submits fraud report form
3. `FraudService` generates SEO-friendly slug
4. Fraud saved with status `ACTIVE`
5. User redirected to `/web/frauds/{id}-{slug}`

### 3. Voting and Scoring

1. User votes on fraud detail page
2. Vote stored (upsert to allow changing vote)
3. `FraudScoringService` calculates:
   - Score = upvotes - downvotes
   - Likely Fraud badge if score >= threshold AND total votes >= min threshold
4. Thresholds configurable via environment variables

### 4. Admin Moderation

1. Admin accesses `/web/admin/moderation`
2. Views all frauds with Archive button
3. Archives fraud → status changed to `ARCHIVED`
4. Archived frauds excluded from public lists but still accessible via direct link

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## 🔧 Configuration

### Fraud Scoring Thresholds

Adjust in `.env`:

```
FRAUD_SCORE_THRESHOLD=10      # Score needed for "Likely Fraud" badge
FRAUD_MIN_VOTES_THRESHOLD=5   # Minimum votes needed for badge
```

### Locale/Language

Language is resolved in this order:
1. Cookie preference (set via `?lang=en` or `?lang=pt`)
2. `Accept-Language` header
3. Default to English

Users can switch language using the language selector in the header.

## 📦 Building for Production

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/efraude-1.0.0.jar

# Or build Docker image
docker build -t efraude:latest .
docker run -p 8080:8080 --env-file .env efraude:latest
```

## 🎨 Customization

### Adding a New Kafka Consumer

1. Create event class in `com.efraude.messaging.event`
2. Update `EventProducer` to publish the event
3. Create consumer in `com.efraude.messaging.consumer`:
   ```java
   @Component
   @RequiredArgsConstructor
   public class MyConsumer {
       @KafkaListener(topics = "my.topic", groupId = "my-group")
       public void handle(MyEvent event) {
           // Process event
       }
   }
   ```

### Adding Internationalization

1. Add keys to `messages.properties` (English)
2. Add translations to `messages_pt_BR.properties` (Portuguese)
3. Use in Thymeleaf: `th:text="#{my.key}"`
4. Use in Java: `@Autowired MessageSource messageSource;`

### Changing Theme Colors

Edit CSS variables in `src/main/resources/static/css/style.css`:

```css
:root {
    --primary-orange: #FF6B35;
    --secondary-yellow: #F7931E;
    --dark-bg: #1A1A1A;
}
```

## 🐛 Troubleshooting

### Docker Compose Issues

```bash
# View logs
docker compose logs -f app

# Restart services
docker compose restart

# Clean rebuild
docker compose down -v
docker compose up --build
```

### Email Not Sending

- Check `SENDGRID_API_KEY` is valid
- Verify sender email in SendGrid dashboard
- Check application logs for errors
- If key is missing, emails will log to console

### Database Connection Failed

- Ensure PostgreSQL is running
- Check `DB_*` environment variables
- Verify credentials match `docker-compose.yml`

### Kafka Connection Issues

- Ensure Kafka and Zookeeper are healthy
- Check `KAFKA_BOOTSTRAP_SERVERS` variable
- Review logs: `docker compose logs kafka`

## 📝 License

This project is provided as-is for educational and production use.

## 🤝 Contributing

Contributions are welcome! Please ensure:
- Code follows existing patterns
- Tests are included
- Documentation is updated
- Commit messages are clear

## 📞 Support

For issues and questions, please open a GitHub issue.

---

Built with ❤️ to fight fraud worldwide
