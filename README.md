# 🚀 Enterprise Online Job Portal System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

## 📖 Project Abstract
The **Enterprise Job Portal** is a robust, full-stack desktop application designed to streamline the recruitment lifecycle. Built using **Java Swing** with a custom "Modern Dark" UI, it implements a **Model-View-Controller (MVC)** architecture to separate business logic from the user interface.

The system features **Role-Based Access Control (RBAC)**, ensuring secure environments for Administrators, Recruiters, and Job Seekers. A key innovation of this project is the implementation of **Multithreading** to handle background notifications, ensuring the UI remains responsive during heavy data operations.

---

## ✨ Key Features (Rubric Highlights)

### 1. 🛡️ Robust Architecture
* **MVC Pattern:** Clear separation between `JobPortalApp` (View), `DatabaseService` (Controller), and Data Models.
* **Security:** Implemented `PreparedStatement` to prevent SQL Injection attacks.
* **Validation:** Server-side input validation for null checks and data integrity.

### 2. ⚡ Innovation & Multithreading
* **Asynchronous Notifications:** When a candidate applies for a job, a background thread is spawned to simulate an email server. This ensures the UI does not freeze (`Thread.sleep` simulation) while the "email" is being sent.

### 3. 🎨 Modern UI/UX
* **Custom Swing Components:** Overridden `paintComponent` methods to create rounded buttons and modern input fields.
* **Dark Mode Theme:** A professional Gunmetal & Emerald color palette for reduced eye strain.
* **Responsive Layouts:** Utilizes `GridBagLayout` for dynamic component resizing.

### 4. 👥 Role-Based Dashboards
* **Recruiters:** Post vacancies, view applicant lists, and update application status (Accept/Reject).
* **Job Seekers:** Real-time job search, one-click application, and application status tracking.
* **Admin:** System monitoring and user oversight.

---

## 🛠️ Technology Stack
* **Language:** Java 21 (JDK)
* **Frontend:** Java Swing (Custom UI Library)
* **Backend Logic:** Java Servlet API & JDBC
* **Database:** MySQL 8.0
* **Version Control:** Git & GitHub
* **IDE:** IntelliJ IDEA

---

## ⚙️ Setup & Installation

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* MySQL Server
* IntelliJ IDEA

### Step 1: Clone the Repository
```bash
git clone [https://github.com/SanthoshSankarganesh/Online-Job-Portal.git](https://github.com/SanthoshSankarganesh/Online-Job-Portal.git)
