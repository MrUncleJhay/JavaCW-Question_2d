# Exhibition Registration System

Desktop application for registering exhibition participants — Java 21, Swing, SQLite, Maven, MVC architecture.

## Prerequisites

- **JDK 21.** Check both `java -version` *and* `javac -version` separately — on a
  machine with more than one JDK installed, they can point to different versions
  independently of each other.
- **VS Code** with the **Extension Pack for Java** (`vscjava.vscode-java-pack`),
  which bundles the Java language server and Maven for Java.

## Running it in VS Code

1. Unzip this project and open the `exhibition-registration` folder
   (`File > Open Folder`).
2. Wait for the Java extension to finish importing the project — the status bar
   shows progress while it resolves the `sqlite-jdbc` dependency automatically.
3. Open `src/main/java/vu/exhibition/ui/MainFrame.java`.
4. Click **Run** on the code lens shown above
   `public static void main(String[] args)`.
5. The application window should appear. A `participants.db` SQLite file is
   created automatically — usually in the project root, since that's normally
   the working directory VS Code runs from — the first time you register
   someone or open View All.

If VS Code is using the wrong JDK: `Ctrl+Shift+P` → `Java: Configure Java
Runtime`, and set this project to JDK 21.

## Project structure

```
src/main/java/vu/exhibition/
    model/       Participant (plain data holder)
    dao/         ParticipantDAO — insert(), getAll()
    database/    DatabaseConnection — singleton, creates the table if missing
    ui/          MainFrame, ViewParticipantsDialog
    util/        ValidationUtils, ConfigLoader
src/main/resources/
    config.properties   (db.path=participants.db)
```

## Notes

- Email is enforced unique at the database level. Registering a duplicate
  shows a specific dialog rather than a generic error.
- View All is deliberately read-only — there's no `update()` method on the
  DAO, so an editable table would let you "change" a value that's silently
  discarded the next time the dialog is reopened.
- If `config.properties` can't be found on the classpath for any reason,
  `ConfigLoader` falls back to `participants.db` and prints a warning rather
  than failing outright.
