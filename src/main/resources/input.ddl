CREATE TABLE departments (
                             department_id SERIAL PRIMARY KEY,
                             department_name VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
                           employee_id SERIAL PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           department_id INTEGER REFERENCES departments(department_id)
);

CREATE TABLE projects (
                          project_id SERIAL PRIMARY KEY,
                          project_name VARCHAR(100) NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE NOT NULL,
                          department_id INTEGER REFERENCES departments(department_id)
);

CREATE TABLE employee_projects (
                                   employee_project_id SERIAL PRIMARY KEY,
                                   employee_id INTEGER REFERENCES employees(employee_id),
                                   project_id INTEGER REFERENCES projects(project_id)
);

CREATE TABLE locations (
                           location_id SERIAL PRIMARY KEY,
                           location_name VARCHAR(50) NOT NULL,
                           address VARCHAR(100) NOT NULL
);

INSERT INTO departments (department_name)
VALUES ('IT'),
       ('HR');

INSERT INTO employees (first_name, last_name, email, department_id)
VALUES ('John', 'Doe', 'johndoe@email.com', 1),
       ('Jane', 'Doe', 'janedoe@email.com', 2);

INSERT INTO projects (project_name, start_date, end_date, department_id)
VALUES ('Project A', '2023-01-01', '2023-12-31', 1),
       ('Project B', '2023-01-01', '2023-12-31', 2);

INSERT INTO employee_projects (employee_id, project_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO locations (location_name, address)
VALUES ('HQ', '123 Main St'),
       ('Branch Office', '456 Maple Ave');