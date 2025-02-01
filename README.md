🚍 Bus Voyage Management System

This project is a Bus Voyage Management System. Users can start bus trips, sell tickets, process ticket refunds, and view trip details.

📌 Features

Initialize different types of bus voyages:

Minibus (2 seats per row)

Standard Bus (2+2 seating arrangement)

Premium Bus (1+2 seating arrangement)

Sell tickets for available seats.

Refund tickets (except for minibuses).

Display the details of existing voyages.

🛠️ Technologies Used

Java 8
Object-Oriented Programming principles.

📂 Project Structure

📁 BusVoyageManagement
│── 📂 src
│   ├── 📄 Main.java
│   ├── 📄 Voyage.java
│   ├── 📄 Bus.java
│   ├── 📄 StandardBus.java
│   ├── 📄 PremiumBus.java
│   ├── 📄 Minibus.java
│   ├── 📄 TicketManager.java
│   ├── 📄 VoyageManager.java
│── 📄 README.md
│── 📄 sample input output

📝 Sample Commands

INIT_VOYAGE	Standard	7	Ankara	Istanbul	12	400	12
SELL_TICKET	7	10_11
REFUND_TICKET	7	10
