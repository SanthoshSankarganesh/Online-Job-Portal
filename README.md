Enterprise Online Job Portal

A robust desktop application designed to streamline the recruitment lifecycle. Built with Java Swing for a responsive user interface and MySQL for reliable data management, this system bridges the gap between employers and job seekers through a secure, role-based platform.

🚀 Key Features

👤 Job Seeker Module

Secure Access: Role-based login and registration system.

Smart Search: Filter job listings instantly by keywords or job titles.

Application Tracking: Real-time status updates (Pending, Accepted, Rejected) visible directly on the dashboard.

💼 Employer Module

Job Management: Post detailed job vacancies with title, description, and salary.

Applicant Review: View a structured table of all candidates.

Instant Decisions: Accept or reject applications with a single click, updating the candidate's status immediately.

🛡️ Admin Module

System Oversight: Monitor all active users and job postings.

Content Moderation: Ability to remove inappropriate or outdated job listings.

🛠️ Technical Architecture

Language: Java (JDK 17+)

Frontend: Java Swing (AWT) using JFrame, JTable, and GridBagLayout.

Database: MySQL Relational Database.

Connectivity: JDBC (Java Database Connectivity).

Core Concepts:

OOP: Extensive use of Inheritance (User -> Employer), Polymorphism, and Interfaces.

Multithreading: Background threads handle email simulations to keep the UI responsive.

Collections: Dynamic data handling using ArrayList and Generics.

⚙️ Installation Guide

Follow these steps to set up the project locally:

Clone the Repository

git clone [https://github.com/YOUR_USERNAME/Online-Job-Portal-Java-Swing.git](https://github.com/YOUR_USERNAME/Online-Job-Portal-Java-Swing.git)


Database Configuration

Open MySQL Workbench.

Open the script file located at database/schema.sql.

Run the script to create the job_portal_gui database and all required tables.

Link the Application

Open src/DatabaseService.java in your IDE.

Locate Line 11: private static final String PASS = "your_password";

Replace "your_password" with your actual MySQL root password.

Run the Application

Open the project in IntelliJ IDEA or Eclipse.

Ensure the mysql-connector-j.jar file (located in the lib/ folder) is added to your project's Classpath/Dependencies.

Right-click src/JobPortalApp.java and select Run.

📂 Project Structure

Online-Job-Portal-Java-Swing/
│
├── src/                        # Source Code
│   ├── JobPortalApp.java       # Main Entry Point & GUI Logic
│   ├── JobPortalModels.java    # Data Models (POJOs)
│   └── DatabaseService.java    # Database Connection & Business Logic
│
├── database/                   # SQL Scripts
│   └── schema.sql              # Database Schema Creation Script
│
├── lib/                        # External Libraries
│   └── mysql-connector-j.jar   # MySQL JDBC Driver
│
├── screenshots/                # Project Demo Images
│   ├── login.png
│   ├── employer_dashboard.png
│   └── seeker_dashboard.png
│
└── README.md                   # Project Documentation


👥 Team

Santhosh Sankarganesh - Team Leader

Mayank Pratap Singh

Shivam Upadhyay

Submitted for Project Review | Nov 2025
