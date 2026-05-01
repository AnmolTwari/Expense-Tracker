# ExpenseTracker

ExpenseTracker is a Spring Boot web application for tracking income and expenses with user registration, login, password reset by email, dashboard summaries, search and filtering, and a responsive Thymeleaf UI.

## Features

- User registration and login with session-based access
- Forgot password flow with email reset link
- Add, edit, and delete income or expense transactions
- Dashboard totals for income, expense, and balance
- Search and category filtering on the dashboard
- Chart visualization for income vs expense
- Responsive form and dashboard styling

## Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Data JPA / Hibernate
- Thymeleaf
- H2 file-based database for local persistence
- Maven Wrapper

## Project Structure

```text
ExpenseTracker/
├── src/main/java/com/example/ExpenseTracker/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── src/main/resources/
│   ├── application.properties
│   ├── static/css/style.css
│   └── templates/
├── .env
├── .gitignore
├── pom.xml
├── mvnw.cmd
└── README.md
```

## Prerequisites

- Java 21 installed
- Git installed
- Windows PowerShell or Command Prompt

You do not need MySQL for the current setup. The app uses a local H2 database file and loads runtime values from `.env`.

## How to Start the App

From the project root, run:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

- App: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

## Other Useful Commands

Build the project:

```powershell
.\mvnw.cmd clean package
```

Run tests:

```powershell
.\mvnw.cmd test
```

Run the packaged JAR:

```powershell
java -jar target/ExpenseTracker-0.0.1-SNAPSHOT.jar
```

## Configuration

The main configuration lives in [src/main/resources/application.properties](src/main/resources/application.properties).

This project also loads a local [.env](.env) file from the repository root. That file is ignored by git and is used for machine-specific values such as database and mail settings.

If you need to change secrets or local runtime settings, edit [.env](.env) instead of committing values directly into source control.

Current important settings:

- H2 file-backed database for persistence across restarts
- Session timeout set to 24 hours
- Email settings for the forgot-password flow

If you change database or mail settings, restart the app after editing that file.

## Main Pages

- `/login` - login page
- `/register` - registration page
- `/forgot-password` - request reset email
- `/reset-password` - set a new password from the email link
- `/` - dashboard with totals, chart, search, and filters
- `/new` - add transaction
- `/edit/{id}` - edit transaction

## Data Model

- `User` stores username, email, password, role, reset token, and token expiry
- `Expense` stores title, amount, date, category, and owning user

## Notes

- The dashboard only shows the logged-in user’s transactions
- Search and category filtering are available on the dashboard
- Passwords are currently stored as plain text in the app code path; BCrypt is still a recommended future improvement

## Troubleshooting

- If the app says the database is in use, close any other running Java/Spring Boot process and start again
- If password reset email fails, verify the SMTP username and app password in `application.properties`
- If the H2 console does not open, ensure the app is running on port 8080

## Suggested Improvements

- Add BCrypt password hashing
- Add date-range filtering on the dashboard
- Add CSV export
- Add confirmation dialogs for delete actions
- Add better validation messages on forms
