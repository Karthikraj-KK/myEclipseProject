package jdbc_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class employeeTable {
	static final String URL = "jdbc:mysql://localhost:3306/", USER = "root", PASSWORD = "password";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection( URL + "employee_leave_db", USER, PASSWORD);
    }

    public static void createtables() {
        String[] queries = {
            "CREATE DATABASE employee_leave_db",

            "CREATE TABLE employee_leave_db.employees (employee_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, department VARCHAR(100) NOT NULL)",

            "CREATE TABLE employee_leave_db.leave_balance (employee_id INT PRIMARY KEY, total_leaves INT NOT NULL DEFAULT 12, remaining_leaves INT NOT NULL DEFAULT 12, FOREIGN KEY (employee_id) REFERENCES employee_leave_db.employees(employee_id))",

            "CREATE TABLE employee_leave_db.leave_requests (request_id INT PRIMARY KEY AUTO_INCREMENT, employee_id INT NOT NULL, start_date DATE NOT NULL, end_date DATE NOT NULL, reason VARCHAR(255) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'PENDING', FOREIGN KEY (employee_id) REFERENCES employee_leave_db.employees(employee_id))"};

        try (Connection con = DriverManager.getConnection( URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            for (String query:queries) {
                st.executeUpdate(query);
            }

            System.out.println("Database and tables created successfully.");

        } catch (Exception e) {
            System.out.println("Table creation error:"+ e.getMessage());
        }
}
}
