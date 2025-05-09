-- Drop tables if they exist (for reusability in in-memory mode)
DROP TABLE IF EXISTS LeaveRequest;
DROP TABLE IF EXISTS Attendance;
DROP TABLE IF EXISTS Compensation;
DROP TABLE IF EXISTS GovernmentIdentification;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Employee;

-- Government Identification Table
CREATE TABLE GovernmentIdentification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Philhealth BIGINT UNIQUE NOT NULL,
    TIN VARCHAR(20) UNIQUE NOT NULL,
    Pagibig BIGINT UNIQUE NOT NULL,
    SSS VARCHAR(20) UNIQUE NOT NULL
);

-- Compensation Table
CREATE TABLE Compensation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    basic_salary DECIMAL(10,2) NOT NULL CHECK (basic_salary >= 0),
    rice_subsidy DECIMAL(10,2) DEFAULT 0 CHECK (rice_subsidy >= 0),
    phone_allowance DECIMAL(10,2) DEFAULT 0 CHECK (phone_allowance >= 0),
    clothing_allowance DECIMAL(10,2) DEFAULT 0 CHECK (clothing_allowance >= 0),
    gross_semi_monthly_rate DECIMAL(10,2) NOT NULL CHECK (gross_semi_monthly_rate >= 0),
    hourly_rate DECIMAL(10,2) NOT NULL CHECK (hourly_rate >= 0)
);

-- User Table
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password CHAR(60) NOT NULL
);

-- Employee Table (Primary Table) with Consolidated Personal and Employment Info
CREATE TABLE Employee (
    employeeNum INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    address TEXT NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    status VARCHAR(50) NOT NULL,
    position VARCHAR(100) NOT NULL,
    immediate_supervisor VARCHAR(255) NOT NULL,
    governmentIdentificationId INT UNIQUE, 
    compensationId INT UNIQUE, 
    userId INT UNIQUE,
    FOREIGN KEY (governmentIdentificationId) REFERENCES GovernmentIdentification(id) ON DELETE CASCADE,
    FOREIGN KEY (compensationId) REFERENCES Compensation(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

-- Attendance Table
CREATE TABLE Attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeNum INT NOT NULL,
    date DATE NOT NULL,
    time_in TIME NOT NULL,
    time_out TIME,
    FOREIGN KEY (employeeNum) REFERENCES Employee(employeeNum) ON DELETE CASCADE
);

-- Leave Request Table
CREATE TABLE LeaveRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeNum INT,
    startDate TIMESTAMP,
    endDate TIMESTAMP,
    notes TEXT,
    leave_type VARCHAR(50),
    status VARCHAR(50),
    FOREIGN KEY (employeeNum) REFERENCES Employee(employeeNum) ON DELETE CASCADE
);

ALTER TABLE employee ALTER COLUMN employeeNum RESTART WITH 10001;

-- Indexes for performance
CREATE INDEX idx_employee_position ON Employee(position);
CREATE INDEX idx_attendance_employee ON Attendance(employeeNum, date);
CREATE INDEX idx_leave_employee ON LeaveRequest(employeeNum, startDate);
