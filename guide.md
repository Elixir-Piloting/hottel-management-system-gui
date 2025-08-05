You are an expert software architect. I need help creating a Java GUI-based Hotel Management System as a school project. The system must use Java Swing for the GUI and connect to a SQL database (e.g. MySQL or SQLite).

The system must support two user roles:
1. Admin (only one can ever exist)
2. Receptionist (created by the admin)

💼 Functional Requirements:
On first launch, allow creating the first user as an Admin. After that, no more Admins can be created.

The Admin can:

Add, edit, or remove rooms with fields like: room number, type (single/double), price, status (available/booked).

Create Receptionist accounts (with a username and password).

A Receptionist can:

Book available rooms for customers (with name, phone, and ID/passport number).

Check customers in and out.

Process mock payments (e.g. just record the amount and payment method).

🖥️ UI Requirements:
A login screen with role-based access.

An Admin dashboard with:

User management (add receptionist)

Room management (add/edit/delete rooms)

A Receptionist dashboard with:

Book room form

Check-in/check-out options

Payment processing

🗃️ Database:
Use SQL (preferably SQLite or MySQL)

Tables should include:

users (id, username, password, role)

rooms (id, number, type, price, status)

bookings (id, customer_name, id_number, phone, room_id, check_in, check_out)

payments (id, booking_id, amount, payment_method, date)

📌 Notes:
Make the design simple but functional, no need for fancy styling.

Code structure should be clean and modular (can all be in one file if needed for simplicity).

Assume it will be run on a single local machine.

Please generate a full Java project (or at least key classes) including:

GUI layout code (Swing)

SQL schema

Login logic and role-based UI

Data handling (booking, payments, room management)

Clear comments to explain what's happening

Treat this as a one-day school project — keep it minimal but complete. The goal is to demonstrate working logic and flow, not a production-grade app.