# ZEPHYR-ControlVault

Zephyr ControlVault is a Java-based Identity and Access Management (IAM) system that simulates how modern enterprises manage secure facility access, user roles, authentication, and audit logging.

The project demonstrates clean Object-Oriented Programming (OOP) principles, role-based access control (RBAC), user hierarchy modeling, exception handling, collections, and file-based persistence — making it a structured, real-world inspired mini-project.

##  Project Overview

Zephyr ControlVault manages three categories of users:

- **Admin**
- **Employee**
- **Visitor**

Each user type has distinct permissions, access rules, and workflows.

All authentication events, access attempts, and attendance actions are recorded with timestamps to maintain a complete audit trail.

This project goes beyond a basic CRUD system by simulating enterprise-style identity management and facility security enforcement.

##  Key Features

###  1. Multi-Role Authentication

- Username/password-based login system
- Role-based object creation (Admin / Employee / Visitor)
- Invalid credentials handled via custom exceptions

###  2. Role-Based Access Control (RBAC)

| Role     | Allowed Access Zones                  |
|----------|---------------------------------------|
| Admin    | All zones                             |
| Employee | Laboratory, Office Floor, Lobby       |
| Visitor  | Lobby only (limited badge)            |

Access violations raise domain-specific exceptions.

###  3. Facility Zone Access

Simulated facility zones:

- **SERVER_ROOM** (Admin only)
- **LAB**
- **OFFICE_FLOOR**
- **LOBBY**

Access behavior is enforced centrally and executed polymorphically.

###  4. Visitor Badge Management

- Visitors are issued temporary access badges
- Badge validity decreases on each allowed access
- Automatic expiry enforced
- Expired badges raise `VisitorExpiredException`

###  5. Automated Logging System

All system events are logged with timestamps and severity levels.

Logs are stored in:
```
data/access_log.txt
```

Sample log entries:
```
[2025-12-16 21:55:39] [INFO] Login successful for user: [ADMIN] admin (ID: ADM01)
[2025-12-16 21:56:10] [WARN] [EMPLOYEE] emp denied access to SERVER_ROOM
[2025-12-16 21:57:05] [ERROR] Failed login attempt: User not found
```

###  6. Employee Attendance Tracking

Employees can:
- Check-in
- Check-out

Attendance records are persisted in:
```
data/attendance.txt
```

Sample format:
```
2025-12-16T22:10:31,EMP01,emp,CHECK_IN
2025-12-16T22:12:04,EMP01,emp,CHECK_OUT
```

###  7. File-Based Data Persistence

Persistent storage is used for:
- User records
- Access logs
- Attendance records

This approach keeps the system lightweight while demonstrating real I/O handling.

###  8. Clean Console Interface

- Menu-driven interaction
- Graceful error handling
- Clear separation between UI and business logic

##  Tech Stack

### Language
- Java (JDK 8+)

### Core Concepts Used
- Inheritance, abstraction, polymorphism
- Java Collections (HashMap, ArrayList)
- Custom and built-in exception handling
- File I/O (BufferedReader, BufferedWriter)
- Java Time API (LocalDateTime)
- Modular, layered system design

##  Project Structure

```
ZEPHYR-ControlVault/
│
├── src/
│   ├── users/
│   │   ├── User.java
│   │   ├── Admin.java
│   │   ├── Employee.java
│   │   └── Visitor.java
│   │
│   ├── system/
│   │   ├── AuthManager.java
│   │   ├── AccessManager.java
│   │   ├── LogManager.java
│   │   └── FileHandler.java
│   │
│   ├── exceptions/
│   │   ├── InvalidCredentialsException.java
│   │   ├── AccessDeniedException.java
│   │   └── VisitorExpiredException.java
│   │
│   └── main/
│       └── Main.java
│
├── data/
│   ├── users.txt
│   ├── access_log.txt
│   └── attendance.txt
│
└── README.md
```

##  Team Members & Responsibilities

### Member A — ARS
**Core Logic & OOP Architecture**
- Designed user hierarchy and inheritance model
- Implemented authentication logic
- Built role-based access control engine
- Developed visitor badge expiry system
- Created custom domain exceptions
- Ensured clean separation of concerns
- Reviewed PRs and tested core logic

### Member B — Partner
**Persistence, Logging & UI**
- Implemented file persistence via FileHandler
- Developed audit logging system (LogManager)
- Implemented attendance tracking
- Built console UI and input validation
- Integrated system modules
- Documented the project and collected screenshots

##  How to Run the Application

### 1. Clone the Repository
```bash
git clone https://github.com/leaderofARS/ZEPHYR-ControlVault.git
```

### 2. Compile the Project
```bash
javac -d out src/**/*.java
```

### 3. Run the Application
```bash
java -cp out main.Main
```

##  Sample Data

### users.txt
```
ADM01,admin,admin123,ADMIN
EMP01,emp,emp123,EMPLOYEE
VIS01,guest,guest123,VISITOR,2
```

##  Learning Outcomes

Through this project, we implemented:

- Object-oriented system design using inheritance and polymorphism
- Key SOLID principles (Single Responsibility, Open/Closed)
- Role-Based Access Control (RBAC)
- Custom exception handling for security-related failures
- File-based persistence and audit logging
- Clean separation between UI, logic, and data layers
- Collaborative Git workflow with feature branches and PR reviews

##  Limitations

- File-based storage instead of a database
- Console-based UI (no GUI)
- Passwords stored in plain text (no hashing)
- Single-user session simulation

These choices were intentional to focus on OOP design and system architecture.

##  Future Enhancements

- Database integration (MySQL/PostgreSQL)
- GUI using JavaFX or Swing
- Password hashing and encryption
- Multi-factor authentication (MFA)
- Real-time monitoring dashboard
- Advanced analytics and reporting
- RFID / access-card simulation

##  License

MIT License

##  Conclusion

Zephyr ControlVault demonstrates:

- Strong OOP foundations
- Realistic access-control modeling
- Persistent audit logging
- Modular, maintainable architecture
- Effective team collaboration using Git

This project represents a professional-grade mini IAM system, significantly exceeding the scope of typical beginner Java projects.

---

 **If you found this project helpful, consider giving it a star on GitHub.**
