Enterprise Online Job Portal

A comprehensive desktop application for managing the recruitment lifecycle, built with Java Swing and MySQL. This system facilitates seamless interaction between Employers, Job Seekers, and Administrators.

🚀 Features

1. 👤 Job Seeker Module

Secure Registration & Login: Role-based access control.

Search Jobs: Filter job listings by keywords.

Application Tracking: View real-time status updates (Pending/Accepted/Rejected).

2. 💼 Employer Module

Post Vacancies: Create job listings with title, description, and salary.

Manage Applicants: View a tabular list of all applicants.

Recruitment Actions: Accept or Reject candidates with a single click.

3. 🛡️ Admin Module

System Oversight: View all active jobs and users.

Content Moderation: Remove inappropriate job postings.

🛠️ Technical Stack

Language: Java (JDK 17+)

GUI Framework: Java Swing (JFrame, JTable, GridBagLayout)

Database: MySQL (Relational Schema)

Connectivity: JDBC (Java Database Connectivity)

Key Concepts: OOP (Inheritance, Polymorphism), Multithreading (Background email simulation), Collections Framework.

⚙️ Setup & Installation

Clone the Repository:

git clone [https://github.com/YOUR_USERNAME/Online-Job-Portal-Java-Swing.git](https://github.com/YOUR_USERNAME/Online-Job-Portal-Java-Swing.git)


Database Setup:

Open MySQL Workbench.

Run the script located in database/schema.sql.

This will create the job_portal_gui database and required tables.

Configure Connection:

Open src/DatabaseService.java.

Update the PASS variable on Line 11 with your MySQL root password.

Run the Application:

Compile and run JobPortalApp.java in your IDE (IntelliJ/Eclipse).

Ensure mysql-connector-j.jar is added to your project dependencies.

👥 Team Members

Santhosh Sankarganesh - Team Leader

Mayank Pratap Singh

Shivam Upadhyay

Submitted for Project Review | Nov 2025
