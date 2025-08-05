# Hotel Management System

![Hotel Management System](httpshttps://i.imgur.com/6v3JqY0.png)

## 🏨 About the Project

This is a comprehensive Hotel Management System built in Java using Swing for the graphical user interface. It provides a user-friendly interface for managing rooms, guests, and bookings in a hotel. The system uses a SQLite database to store and manage the hotel's data.

## ✨ Features

*   **Room Management:** Add, update, and delete rooms. View room status (available, occupied, under maintenance).
*   **Guest Management:** Add, update, and delete guest information.
*   **Booking Management:** Create, view, and manage bookings.
*   **User Authentication:** Secure login for authorized personnel.
*   **Invoice and Payment:** Generate invoices and process payments.

## 🛠️ Built With

*   [Java](https://www.java.com/)
*   [Swing](https://docs.oracle.com/javase/tutorial/uiswing/)
*   [Maven](https://maven.apache.org/)
*   [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your system:

*   **Java Development Kit (JDK) 11 or higher:** You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
*   **Apache Maven:** You can download it from the [Maven website](https://maven.apache.org/download.cgi).

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### 1. Clone the Repository

```sh
git clone https://github.com/Elixir-Piloting/hottel-management-system-gui.git
```

### 2. Set up the Database

The project uses a SQLite database. The database file `hotel_management.db` is included in the project's root directory. No special setup is required for the database.

### 3. Build the Project

Navigate to the project's root directory and use Maven to build the project:

```sh
mvn clean install
```

This command will compile the source code, run any tests, and package the application into a JAR file in the `target` directory.

### 4. Run the Application

Once the project is built, you can run the application using the following command:

```sh
java -jar target/hotel-management-system-1.0-SNAPSHOT.jar
```

This will launch the application's login screen.
### Test admin credentials are
#### username
```sh
admin
```
#### password
```sh
admin123
```


## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## ©️ Copyright

Copyright (c) 2025 Elixir-Piloting