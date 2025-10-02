CREATE DATABASE IF NOT EXISTS payments_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE payments_management;

CREATE TABLE IF NOT EXISTS department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS agent (
    agent_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    agent_type ENUM('Worker','Responsible','Director','Intern') NOT NULL,
    department_id INT,
    is_responsible BOOLEAN NOT NULL DEFAULT FALSE,   -- ✅ new column
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_agent_department
    FOREIGN KEY (department_id)
    REFERENCES department(department_id)
                                                   ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    agent_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_type ENUM('Salary','Bonus','Compensation','Prime') NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(500) NULL,
    CONSTRAINT fk_payment_agent
    FOREIGN KEY (agent_id)
    REFERENCES agent(agent_id)
    ON DELETE CASCADE
    );
