DROP DATABASE IF EXISTS job_portal_gui;
CREATE DATABASE job_portal_gui;
USE job_portal_gui;

-- 1. Users Table (Added specific profile fields)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYER', 'JOB_SEEKER') NOT NULL,
    company_name VARCHAR(100), -- For Employers
    skills TEXT,               -- For Job Seekers
    experience_years INT DEFAULT 0
);

-- 2. Jobs Table
CREATE TABLE jobs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employer_id INT,
    title VARCHAR(100),
    description TEXT,
    salary DOUBLE,
    status VARCHAR(20) DEFAULT 'OPEN',
    FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Applications Table (Tracks Status)
CREATE TABLE applications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    seeker_id INT,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, ACCEPTED, REJECTED
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (seeker_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(job_id, seeker_id)
);

-- Seed Data (Default Users)
INSERT INTO users (name, email, password, role, company_name) VALUES
('System Admin', 'admin@portal.com', 'admin123', 'ADMIN', NULL),
('Tech Corp', 'hr@tech.com', 'emp123', 'EMPLOYER', 'Tech Corp Inc.');

INSERT INTO users (name, email, password, role, skills, experience_years) VALUES
('Alice Dev', 'alice@dev.com', 'user123', 'JOB_SEEKER', 'Java, SQL, Swing', 2);

INSERT INTO jobs (employer_id, title, description, salary) VALUES
(2, 'Java Architect', 'Lead the backend team.', 120000);