# ZEPHYR-ControlVault
Zephyr ControlVault is a Java-based identity and facility access controller. It offers a cleanly abstracted user hierarchy, authentication manager, access policies, and automated activity logging backed by file storage.

**Zephyr ControlVault** is a Java-based Identity and Access Management (IAM) system that simulates how modern enterprises manage secure facility access, user roles, authentication, and audit logging.

It demonstrates clean OOP principles, role-based access policies, user hierarchy modeling, exception handling, collections, and file-based persistence — making it a highly structured, real-world inspired mini-project.

---

## 🚀 Project Overview

Zephyr ControlVault manages three categories of users:
- **Admin**
- **Employee**
- **Visitor**

Each user type has unique permissions, access behaviors, and authenticated actions.

Every access attempt, login event, and workflow is recorded with timestamps to maintain a complete audit trail.

This project goes far beyond a typical CRUD system, simulating how corporate environments enforce identity management and facility access control.

---

## 🔍 Key Features

### ✅ 1. Multi-Role Authentication
- Secure login system using username & password
- Role-based object creation (Admin/Employee/Visitor)
- Invalid credentials throw custom exceptions

### ✅ 2. Role-Based Access Control (RBAC)
Different users have different privileges:
- **Admin** → Full access to all zones
- **Employee** → Office + Lab
- **Visitor** → Lobby only (time-limited badge)

### ✅ 3. Facility Zone Access
Simulated access areas:
- Server Room (Admin only)
- Laboratory
- Office Floor
- Lobby

Polymorphism ensures each user behaves differently in the access flow.

### ✅ 4. Visitor Badge Management
- Temporary access badge
- Automatic expiry after limited uses/time
- Custom `VisitorExpiredException` for expired badges

### ✅ 5. Automated Logging System
All events are logged with timestamps, stored in:
```
data/access_log.txt
```

Includes:
- Successful access
- Denied access
- Expired badges
- Authentication events

### ✅ 6. Employee Attendance Tracking
Employees can:
- Check-in
- Check-out

Stored in:
```
data/attendance.txt
```

### ✅ 7. File-Based Data Storage
Persistent storage for:
- Users
- Access logs
- Attendance records

### ✅ 8. Clean Console UI
- Interactive menus
- Input validation
- Error handling

---

## 🧩 Tech Stack

### Language
**Java (Core + OOP)**

### Concepts Used
- Inheritance, abstraction, polymorphism
- Java Collections (HashMap, ArrayList)
- Exception handling (custom + built-in)
- File I/O (BufferedReader, BufferedWriter, FileWriter)
- Java Time API (LocalDateTime)
- Modular system design

---

## 🗂️ Project Structure
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

---

## 👥 Team Members & Responsibilities

### Member A — ARS
**Core Logic & OOP Architecture**
- Designed User Hierarchy (User, Admin, Employee, Visitor)
- Implemented Authentication System
- Built Role-Based Access Engine
- Developed Visitor Badge Expiry Logic
- Implemented Custom Exceptions
- Integrated Polymorphic Access Behavior
- Reviewed PRs & tested core logic

### Member B — Partner
**Persistence, Logging & UI**
- Implemented FileHandler (persistent storage)
- Developed LogManager (audit logging)
- Created Attendance Tracking System
- Built full Console UI and input validation
- Implemented reporting features (logs & attendance)
- Wrote README.md and collected screenshots
- Reviewed PRs & tested I/O modules

---

## 💻 How to Run the Application

### 1. Clone the Repository
```bash
git clone https://github.com/<team-name>/ZEPHYR-ControlVault.git
```

### 2. Open in IDE (VS Code / IntelliJ / Eclipse)
Ensure **JDK 8+** is installed.

### 3. Run the Main File
Navigate to:
```
src/main/Main.java
```
Run the file.

### 4. System will start with login screen
Admin credentials can be preloaded in `users.txt`.

---

## 🧪 Sample Data Format

### users.txt
```
ADM01,admin,admin123,ADMIN
EMP220,rahul123,pass456,EMPLOYEE
VIS102,guest1,guestpass,VISITOR,3
```

### access_log.txt
```
[2025-12-07 14:22:10] ADMIN admin accessed Server Room - SUCCESS
[2025-12-07 14:24:11] VISITOR vis102 tried Office Floor - DENIED (Expired)
```

### attendance.txt
```
[2025-12-07 09:00:15] EMPLOYEE rahul123 - CHECK IN
[2025-12-07 18:30:42] EMPLOYEE rahul123 - CHECK OUT
```

---

## 📸 Screenshots

### Login Screen
![Login Screen](screenshots/login.png)

### Admin Dashboard
![Admin Dashboard](screenshots/admin_dashboard.png)

### Access Control in Action
![Access Control](screenshots/access_control.png)

### Audit Logs
![Audit Logs](screenshots/audit_logs.png)

### Attendance Tracking
![Attendance](screenshots/attendance.png)

*(Add actual screenshots to a `screenshots/` folder in your repository)*

---

## 🧾 GitHub Collaboration Workflow

- Both members cloned repo
- Both worked on feature branches
- Pull Requests reviewed mutually
- Minimum 5 meaningful commits each
- Final merge into main branch

### Example branches:
- `feature-auth-system`
- `feature-ui-module`
- `feature-logging`
- `feature-attendance`
- `feature-access-control`

---

## 🎯 Learning Outcomes

Through this project, we successfully implemented:
- **Object-Oriented Design**: Clean class hierarchy with proper inheritance and polymorphism
- **SOLID Principles**: Single responsibility, open/closed, and dependency inversion
- **Exception Handling**: Custom exceptions for domain-specific errors
- **File I/O Operations**: Persistent data storage and retrieval
- **Security Concepts**: Authentication, authorization, and audit trails
- **Team Collaboration**: Git workflow, code reviews, and task distribution

---

## 🔮 Future Enhancements

Potential improvements for the system:
- Database integration (MySQL/PostgreSQL) instead of file storage
- GUI implementation using JavaFX or Swing
- Multi-factor authentication (MFA)
- Real-time access monitoring dashboard
- Email notifications for security events
- Biometric authentication simulation
- Access card/RFID simulation
- Advanced reporting and analytics

---

## 📝 License

This project is created for educational purposes as part of a Java OOP course.

---

## 🤝 Acknowledgments

- Special thanks to our course instructor for guidance
- Inspired by real-world IAM systems used in enterprise environments
- Built with dedication and collaborative effort

---

## 📧 Contact

For any queries regarding this project:
- **Member A (ARS)**: [email@example.com]
- **Member B (Partner)**: [email@example.com]

---

## 🏁 Conclusion

**Zephyr ControlVault** demonstrates:
- Strong OOP design principles
- Realistic access control modeling
- Persistent data management
- Clean modular architecture
- Collaborative Git workflow

This project reflects a professional-grade mini IAM system, far more advanced than typical beginner Java projects, showcasing our understanding of enterprise-level software design patterns and implementation.

---

**⭐ If you found this project helpful, please consider giving it a star on GitHub!**
