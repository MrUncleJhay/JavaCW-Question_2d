# Exhibition Registration System

A production-ready Java desktop application for managing exhibition participant registrations, rebuilt from legacy NetBeans/Access codebase to modern Java 21 with Maven, Swing, and SQLite.

## 📁 Repository Structure
```
exhibition-registration-system/
├── exhibition-registration_v3.0/ # ✅ CURRENT WORKING VERSION
│ ├── src/ # Full source code
│ ├── pom.xml # Maven configuration
│ └── README.md # Project documentation
├── exhibition-registration_v2.0/ # Archived - intermediate version
└── JavaGUI_v1.0/ # Archived - original NetBeans version
```

## 🚀 Quick Start

### Prerequisites
- **Java 21** (JDK 21 or later)
- **VS Code** with Maven extension (or any Maven-compatible IDE)
- **SQLite** (embedded - no separate installation needed)

### Run the Application
1. Navigate to the working version:
   ```bash
   cd exhibition-registration_v3.0
   ```

2. Build and run with Maven:
   ```bash
   mvn clean compile exec:java
   ```
Or use VS Code Maven extension:
- Open the exhibition-registration_v3.0 folder
- Run the MainFrame class directly

### Package as JAR
```bash
mvn package
java -jar target/exhibition-registration-1.0.jar
```

## ✨ Features
- **Participant Registration** – Full Name, Email, Contact Number, Exhibition Category
- ***Validation** – Real-time validation with user-friendly error messages
- **SQLite Database** – Lightweight, file-based storage (```participants.db```)
- **View All Participants** – JTable with scrollable list of all registrations
- **MVC Architecture** – Clean separation of Model, View, and Controller layers
- **No external DB setup** – Database auto-initializes on first run

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **UI** | Swing (GridBagLayout, no drag-drop) |
| **Build** | Maven |
| **Database** | SQLite (sqlite-jdbc) |
| **Architecture** | MVC (Model-View-Controller) |

## 📦 Project Structure (v3.0)
```
exhibition-registration_v3.0/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/exhibition/
│   │   │       ├── model/          # Participant POJO
│   │   │       ├── dao/            # ParticipantDAO (CRUD)
│   │   │       ├── database/       # DatabaseConnection (singleton)
│   │   │       ├── ui/             # MainFrame + ViewParticipantsDialog
│   │   │       └── util/           # ValidationUtils + ConfigLoader
│   │   └── resources/
│   │       └── config.properties   # DB path configuration
│   └── test/                       # (Optional test files)
├── pom.xml                         # Maven dependencies & build config
└── README.md                       # This file
```
## 🔧 Configuration
Database path can be customized in ```src/main/resources/config.properties```:
```properties
db.path=participants.db
```
## 📝 License
This project is for educational/demonstration purposes.

**⚠️ Note**: The ```exhibition-registration_v3.0``` folder contains the complete, working application. The other folders (```v2.0```, ```JavaGUI_v1.0```) are preserved for historical reference only.
