# CONTEXT.md - AI Agent Reference

## Purpose

**efraude** is a SEO-first fraud reporting platform where users worldwide can report scams, vote on their legitimacy, and help others avoid becoming victims. The platform emphasizes community-driven content moderation through voting and admin oversight.

## Domain Overview

### Core Entities

1. **User** - Platform accounts
   - Local auth (email/password with BCrypt)
   - Google OAuth2 login
   - Email verification required before login
   - Roles: USER, ADMIN

2. **Fraud** - Fraud reports
   - Title, description, caution (how to avoid), related URL
   - Country filter (GLOBAL or specific country)
   - Status: ACTIVE or ARCHIVED
   - SEO-friendly URLs: `/frauds/{uuid}-{slug}`

3. **Vote** - Community voting
   - One vote per user per fraud
   - Types: UP or DOWN
   - Score = upvotes - downvotes
   - "Likely Fraud" badge when score and vote count exceed thresholds

4. **Comment** - Discussions on frauds
   - Users share experiences and insights
   - Simple chronological display

## Architecture

### Stack
- **Backend**: Java 17, Spring Boot 3.2, Maven, PostgreSQL, Kafka
- **Frontend**: Thymeleaf (SSR), HTMX (interactivity), CSS
- **Auth**: Spring Security, OAuth2 Client (Google)
- **Email**: SendGrid via Kafka consumers
- **DB**: PostgreSQL with Flyway migrations
- **Dev**: Docker Compose (Postgres + Kafka + App)

### Key Design Principles

1. **SEO First** - Server-rendered pages, canonical URLs, OpenGraph tags, robots.txt
2. **Clean Separation** - API layer (`/api/**`) and Web layer (`/web/**`) in same codebase but decoupled
3. **Email Verification** - Users must verify email before login
4. **Async Email** - Kafka events trigger email sending via consumers
5. **i18n Without URL** - Language in cookie/header, NOT in path (same URL for all languages)
6. **Production Ready** - Not a prototype; clean, deployable code

### Layer Structure

```
api/              -> REST controllers and DTOs (future mobile/SPA)
web/              -> Thymeleaf controllers (SSR for SEO)
domain/           -> Business logic (services, repositories, models)
  ├── model/      -> JPA entities
  ├── repository/ -> Spring Data JPA
  └── service/    -> Business services
messaging/        -> Kafka events, producers, consumers, email
security/         -> Custom UserDetailsService, OAuth2 handlers
config/           -> Spring configurations
```

## Key Flows

### 1. Signup and Email Confirmation

```
User submits /web/auth/signup
  ↓
AuthService.registerUser()
  - Creates user with emailVerified=false
  - Generates token, saves EmailVerificationToken
  - Publishes user.registered Kafka event
  ↓
UserRegistrationConsumer receives event
  ↓
SendGridEmailService sends confirmation email
  ↓
User clicks link → /web/auth/confirm?token=xxx
  ↓
AuthService.confirmEmail(token)
  - Validates token
  - Sets user.emailVerified=true
  - Deletes token
  ↓
User can login
```

### 2. Report Fraud

```
Logged-in user → /web/frauds/create
  ↓
FraudWebController.create()
  ↓
FraudService.createFraud()
  - Generates slug from title
  - Saves fraud with status=ACTIVE
  ↓
Redirect to /web/frauds/{id}-{slug}
```

### 3. Vote and Score

```
User votes on fraud
  ↓
VoteService.upsertVote()
  - Insert or update vote (one per user per fraud)
  ↓
FraudScoringService.calculateScore()
  - score = upvotes - downvotes
  - likelyFraud = score >= threshold && totalVotes >= minThreshold
  ↓
Display on fraud detail page
```

### 4. Admin Archive

```
Admin → /web/admin/moderation
  ↓
Click Archive on fraud
  ↓
FraudService.archiveFraud()
  - Sets fraud.status = ARCHIVED
  ↓
Fraud no longer in public lists
  - Still accessible via direct link with banner
```

## Important Constraints

### Security
- **Email verification required**: Users with `emailVerified=false` cannot login
- **Google OAuth auto-verified**: Google users skip email verification
- **Role-based access**: ADMIN role for `/web/admin/**`
- **Passwords**: BCrypt hashed

### SEO
- **Server-rendered**: All public pages use Thymeleaf
- **Canonical URLs**: Always `https://efraude.com/frauds/{id}-{slug}`
- **hreflang tags**: Same URL for all languages (no language in path)
- **OpenGraph**: Proper tags for social sharing
- **robots.txt**: Allow public pages, disallow admin/api

### i18n
- **Languages**: English (default), Portuguese (pt_BR)
- **Resolution**: Cookie → Accept-Language → Default (English)
- **No URL path**: Language NOT in URL; same URL serves all languages
- **Fraud content**: NOT translated; only UI labels localized

### Voting
- **One vote per user per fraud**: Enforced by unique constraint
- **Can change vote**: Upsert allows changing UP to DOWN or vice versa
- **Can remove vote**: DELETE endpoint available
- **Score display**: Always shows current score on fraud detail

### Email
- **Async via Kafka**: Never blocks request
- **Dev-safe**: If SENDGRID_API_KEY missing, logs to console
- **Topic**: `user.registered`
- **Consumer group**: `efraude-email-service`

## Commands

### Local Development

```bash
# Start all services (recommended)
docker-compose up --build

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Rebuild from scratch
docker-compose down -v
docker-compose up --build

# Run tests
mvn test

# Run locally (requires Postgres + Kafka running)
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Database

```bash
# Migrations run automatically on startup
# Located in: src/main/resources/db/migration/V*.sql

# Manual Flyway commands
mvn flyway:info
mvn flyway:validate
mvn flyway:migrate
```

### Building

```bash
# Build JAR
mvn clean package

# Build Docker image
docker build -t efraude:latest .

# Run Docker image
docker run -p 8080:8080 --env-file .env efraude:latest
```

## Repository Structure Map

### Key Files

| Path | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies |
| `docker-compose.yml` | Local dev environment |
| `Dockerfile` | Application container |
| `.env.example` | Environment variable template |
| `src/main/java/com/efraude/EfraudeApplication.java` | Main entry point |
| `src/main/resources/application.yml` | Spring configuration |
| `src/main/resources/db/migration/` | Flyway SQL migrations |
| `src/main/resources/messages*.properties` | i18n translations |
| `src/main/resources/templates/` | Thymeleaf HTML templates |
| `src/main/resources/static/css/style.css` | Application styles |
| `src/main/resources/static/js/app.js` | Client-side JavaScript |

### Important Packages

| Package | Contains |
|---------|----------|
| `com.efraude.api.controller` | REST API endpoints |
| `com.efraude.api.dto` | API request/response DTOs |
| `com.efraude.web.controller` | Thymeleaf page controllers |
| `com.efraude.domain.model` | JPA entities |
| `com.efraude.domain.repository` | Spring Data repositories |
| `com.efraude.domain.service` | Business logic services |
| `com.efraude.messaging.event` | Kafka event POJOs |
| `com.efraude.messaging.producer` | Kafka producers |
| `com.efraude.messaging.consumer` | Kafka consumers |
| `com.efraude.messaging.email` | Email sending logic |
| `com.efraude.security` | Custom security implementations |
| `com.efraude.config` | Spring configurations |

## Environment Variables

Critical ones for AI to know:

```bash
# Database
DB_HOST=localhost
DB_NAME=efraude
DB_USER=efraude_user
DB_PASSWORD=efraude_pass

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Email (optional, logs if not set)
SENDGRID_API_KEY=
SENDGRID_FROM_EMAIL=noreply@efraude.com

# OAuth2 (optional, disables Google login if not set)
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

# Application
BASE_URL=http://localhost:8080
FRAUD_SCORE_THRESHOLD=10
FRAUD_MIN_VOTES_THRESHOLD=5
```

## Common Tasks for AI Agents

### Adding a New Entity

1. Create entity in `domain/model/`
2. Create repository in `domain/repository/`
3. Create service in `domain/service/`
4. Create Flyway migration in `resources/db/migration/`
5. Add API controller in `api/controller/`
6. Add web controller in `web/controller/`
7. Create Thymeleaf templates in `resources/templates/`

### Adding a New Kafka Event

1. Define event POJO in `messaging/event/`
2. Add producer method in `EventProducer`
3. Create consumer in `messaging/consumer/`
4. Update Kafka config if needed

### Adding i18n Keys

1. Add key=value to `messages.properties` (English)
2. Add key=value to `messages_pt_BR.properties` (Portuguese)
3. Use in Thymeleaf: `th:text="#{key}"`

### Modifying Database Schema

1. Create new Flyway migration: `V{next}__description.sql`
2. Update corresponding entity class
3. Restart application (Flyway runs automatically)

## Testing Strategy

- **Unit tests**: Service layer logic
- **Integration tests**: Repository and database interactions
- **Security tests**: Authentication and authorization flows
- **Email**: Test with missing SENDGRID_API_KEY (logs to console)

## Known Patterns

1. **Service methods are transactional**: Use `@Transactional`
2. **DTOs for API**: Never expose entities directly via REST
3. **Web calls services**: Never repository directly
4. **Lombok**: Used for reducing boilerplate (`@Data`, `@Builder`, etc.)
5. **UUID primary keys**: All public-facing entities
6. **Timestamps**: `@CreationTimestamp` and `@UpdateTimestamp` for audit

## Constraints to Remember

- NO language in URL path (violates i18n requirement)
- Email verification MUST be enforced for local auth
- Google OAuth users are auto-verified
- One vote per user per fraud (database constraint)
- Archived frauds accessible by direct link but not in lists
- API layer must be callable independently (future extraction)
- SEO tags required on all public pages

## URLs to Know

| URL | Purpose |
|-----|---------|
| `/` | Redirects to `/web/home` |
| `/web/home` | Homepage |
| `/web/frauds` | Fraud listing (with search/filter) |
| `/web/frauds/{id}-{slug}` | Fraud detail page |
| `/web/frauds/create` | Report fraud form |
| `/web/auth/login` | Login page |
| `/web/auth/signup` | Signup page |
| `/web/auth/confirm?token=xxx` | Email confirmation |
| `/web/auth/resend` | Resend confirmation email |
| `/web/admin/moderation` | Admin fraud management |
| `/api/auth/*` | Auth API endpoints |
| `/api/frauds/*` | Fraud API endpoints |
| `/api/frauds/{id}/votes` | Voting API |
| `/api/frauds/{id}/comments` | Comments API |

## Quick Reference

### How to...

**Run the application:**
```bash
docker-compose up --build
```

**Add a new page:**
1. Create controller in `web/controller/`
2. Create template in `resources/templates/`
3. Add i18n keys to `messages*.properties`
4. Update navigation in `layout/header.html`

**Change fraud scoring logic:**
Edit `FraudScoringService.calculateScore()`

**Add new country:**
Update `fraud/create.html` and `fraud/list.html` templates

**Test email locally:**
Leave `SENDGRID_API_KEY` empty; check console logs

**Create admin user:**
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'user@example.com';
```

---

This context should help AI agents understand the codebase quickly and make informed decisions when assisting with development or debugging.
