# Assessment Manager

A full-stack web application for managing the lifecycle of university assessments — from creation and checking through marking, moderation, and feedback. Built as a team project at the **University of Sheffield, Department of Computer Science**.

The system models real-world academic workflows where coursework, exams, and tests each follow distinct multi-stage pipelines with role-based approvals, audit logging, and automatic stage progression.

## My Contributions

I was the **primary contributor** to this project, authoring **137 of ~330 commits (42%)** — nearly 3x the next contributor — spanning both the Java backend and React frontend.

**Assessment Progression Engine (core feature — built end-to-end)**
- Designed and implemented the entire multi-stage workflow system that powers the application: domain modelling, backend services, DTOs, REST endpoints, and the full React UI
- Built stage advancement and reversal logic with role-based authorization (setter, checker, moderator, external examiner, exams officer)
- Implemented the checker/setter feedback loop — checkers can reject and revert stages with feedback, triggering a pending indicator for the setter to respond
- Added automatic date-based progression for deadline and exam date triggers
- Created togglable per-stage audit logging with actor identification

**Assessment Overview & Management**
- Implemented soft delete/restore for assessments with admin-only controls
- Built admin views to display all assessments across modules
- Fixed exam creation logic and external examiner feedback flows

**Module System**
- Created the `ModuleInfo` and `ModuleCard` components with role-aware dropdowns
- Built repository methods, service layer, and controller logic for module staff views
- Fixed navigation, overflow, and visibility bugs across the module pages

**Authentication & Frontend Architecture**
- Refactored all 14+ pages to enforce token-based login gating (a single 900-line commit touching every page)
- Set up Axios HTTP client with JWT interceptors for automatic auth headers
- Optimized API call patterns and fixed routing/navbar issues throughout the app

**Backend Architecture**
- Defined core enums (`AssessmentRole`, `AssessmentStages`, `AssessmentType`) that encode the business rules for all three assessment pipelines
- Built DTOs, service methods, and exception handling for assessment progression
- Handled continuous merge/integration work to keep the team synchronized

## Tech Stack

| Layer | Technologies |
|---|---|
| **Frontend** | React 19, Vite 7, React Router 7, Axios, Bootstrap 5, React Select |
| **Backend** | Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA, Hibernate |
| **Auth** | JWT with RSA key pairs (Spring Security OAuth2 Resource Server) |
| **Database** | H2 (in-memory, development) |
| **Build** | Maven (backend), npm (frontend) |
| **Testing** | JUnit 5, Spring Boot Test |

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│  React SPA (Vite dev server :5173)                      │
│  ┌──────────┐  ┌──────────┐  ┌────────────────────┐     │
│  │  Pages   │  │Components│  │ Context (UserState)│     │
│  └────┬─────┘  └──────────┘  └────────────────────┘     │
│       │  Axios + JWT Bearer token                       │
└───────┼─────────────────────────────────────────────────┘
        │  HTTP/REST
┌───────▼────────────────────────────────────────────────┐
│  Spring Boot API (:8080)                               │
│  ┌────────────┐  ┌──────────┐  ┌───────────────────┐   │
│  │ Controllers│→ │ Services │→ │ JPA Repositories  │   │
│  └────────────┘  └──────────┘  └──────────┬────────┘   │
│  ┌──────────────────┐  ┌──────────────┐   │            │
│  │Security (JWT/RSA)│  │ DTOs + Enums │   │            │
│  └──────────────────┘  └──────────────┘   │            │
└───────────────────────────────────────────┼────────────┘
                                            │
                                    ┌───────▼───────┐
                                    │  H2 Database  │
                                    └───────────────┘
```

**Backend** follows a layered architecture: Controllers handle HTTP requests, Services contain business logic, and Repositories manage data access via Spring Data JPA. Authentication is stateless — the server issues RSA-signed JWTs on login, and a custom filter validates the token on every request.

**Frontend** is a React SPA using React Router for client-side navigation and React Context for global auth state. An Axios interceptor attaches the JWT to every API call and handles 401 responses by redirecting to login.

## Features

### Assessment Lifecycle Management
- **Three assessment types** with distinct workflows:
  - **Coursework** — 9 stages (spec creation → checking → release → marking → moderation → feedback)
  - **Exam** — 13 stages (creation → checking → officer review → external examiner → admin → marking → moderation)
  - **Test** — 8 stages (creation → checking → test takes place → marking → moderation → results)
- **Role-based stage advancement** — only users with the correct role (setter, checker, moderator, external examiner, exams officer) can advance their respective stages; admins can advance any stage
- **Stage reversal** — checkers can reject and revert stages with written feedback
- **Automatic progression** — stages tied to deadlines or exam dates advance automatically
- **Audit trail** — every stage transition is logged with the actor, timestamp, and action

### Module Management
- Create, edit, and soft-delete modules
- Assign staff to modules with specific roles (module leader, setter, checker, moderator)
- CSV bulk import for modules, staff, and assessments
- Role-aware views — admins see all modules; staff see only their assigned modules

### User Management & Access Control
- Four user roles: Admin, Academic Staff, Exams Officer, External Examiner
- JWT authentication with RSA-signed tokens and BCrypt password hashing
- First-time login password change enforcement
- Admin-only user creation and deletion
- Role management interface

### UI/UX
- Responsive design with Bootstrap
- "My Tasks" dashboard showing each user's pending actions across all assessments
- Assessment progression visualizer with status legend
- Module cards with context-aware dropdown menus

## Project Structure

```
assessment-manager/
├── client/                          # React frontend
│   ├── src/
│   │   ├── api/                     # Axios config with JWT interceptor
│   │   ├── components/              # 14 reusable components
│   │   │   ├── AssessmentInfo       # Assessment metadata card
│   │   │   ├── AssessmentStage      # Stage progression UI
│   │   │   ├── ModuleCard           # Module display with dropdown
│   │   │   ├── ModuleInfo           # Module metadata card
│   │   │   ├── Navbar / Footer      # App shell
│   │   │   └── StatusLegend         # Stage status key
│   │   ├── context/                 # React Context for auth state
│   │   └── pages/                   # 15 page components
│   │       ├── AssessmentOverview   # List assessments per module
│   │       ├── AssessmentProgression# Stage advancement interface
│   │       ├── AssessmentLogs       # Audit trail viewer
│   │       ├── Modules              # Module listing
│   │       ├── CreateModule / Edit  # Module CRUD
│   │       ├── CreateAssessment     # Assessment creation form
│   │       ├── MyTasks              # Per-user action dashboard
│   │       ├── Login / Logout       # Authentication
│   │       └── UserCreation / Roles # Admin user management
│   └── vite.config.js
│
├── server/                          # Spring Boot backend
│   ├── src/main/java/.../
│   │   ├── config/                  # Security, JWT, CORS, exception handling
│   │   ├── controller/              # 4 REST controllers
│   │   ├── domain/                  # JPA entities + enums
│   │   │   ├── assessment/          # Assessment, AssessmentStage, AssessmentStageLog
│   │   │   ├── module/              # Module, ModuleStaff, ModuleRole
│   │   │   └── user/                # User, UserRole
│   │   ├── dto/                     # 18 data transfer objects
│   │   ├── exception/               # 7 custom exceptions
│   │   ├── repository/              # 6 Spring Data JPA repositories
│   │   ├── security/                # JWT filter, UserDetails
│   │   └── service/                 # 13 service classes
│   └── src/test/java/               # 8 test classes (JUnit 5)
│
└── README.md
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- npm

### 1. Generate RSA Keys

```bash
cd server/src/main/resources
mkdir certs && cd certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
```

### 2. Start the Backend

```bash
cd server
./mvnw spring-boot:run        # Linux/Mac
.\mvnw spring-boot:run        # Windows
```

The API starts on `http://localhost:8080`. An H2 console is available at `http://localhost:8080/h2-console`. On startup, the `DataGenerator` auto-populates the database with 50+ test users and 5 modules.

### 3. Start the Frontend

```bash
cd client
npm install
npm run dev
```

The app opens at `http://localhost:5173`.

### Default Login

```
Email:    test@sheffield.ac.uk
Password: test
Role:     ADMIN
```

Other users generated by the `DataGenerator` share the same email/password pattern (check the H2 console).

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/auth/login` | Public | Authenticate and receive JWT |
| `POST` | `/api/auth/signup` | Admin | Create new user |
| `GET` | `/api/auth/me` | Any | Get current user info |
| `GET` | `/api/modules` | Admin, Exams Officer | List all modules |
| `GET` | `/api/modules/user/{id}` | Any | Get user's modules |
| `POST` | `/api/modules` | Admin | Create module |
| `PUT` | `/api/modules/{id}` | Admin | Update module |
| `DELETE` | `/api/modules/{id}` | Admin | Soft-delete module |
| `POST` | `/api/modules/uploadCsv` | Admin | Bulk import via CSV |
| `GET` | `/api/assessments` | Any | List assessments |
| `POST` | `/api/assessments` | Admin | Create assessment |
| `GET` | `/api/assessments/{id}` | Any | Get assessment details |
| `POST` | `/api/assessments/{id}/advance` | Role-based | Advance assessment stage |
| `DELETE` | `/api/assessments/{id}` | Admin | Soft-delete assessment |

## Team

| Name | Primary Contributions |
|------|----------------------|
| **Cheng An Wong** | Assessment progression engine, stage advancement/reversal, audit logging, assessment overview, module components, auth gating, frontend architecture |
| Wanlin Zhong | JWT/Spring Security framework, backend domain model, CSV import pipeline, CORS config |
| Rhys Womack | Modules page UI, module editing/deletion, responsive design |
| Connor McGough | User creation system, role management page, access control |
| Jiaying Ye | Login/logout UI, My Tasks dashboard, user deletion, password update, assessment logs |
| Benjamin Grassie | Unit test coverage for services (User, Module, Assessment) |
| Olena Shevchenko | Module creation with CSV upload, project scaffolding, frontend setup |

## License

University of Sheffield — Department of Computer Science, Team 24 (2025).
