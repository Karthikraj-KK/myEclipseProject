package jdbc_project;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class dbTable {

	static final String URL = "jdbc:mysql://localhost:3306/", USER = "root", PASSWORD = "password";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL + "library_db", USER, PASSWORD);
    }

    public static void createtables() {
        String[] queries = {
        		"CREATE DATABASE library_db",

        		"CREATE TABLE library_db.books (book_id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(100) NOT NULL, author VARCHAR(100) NOT NULL, quantity INT NOT NULL DEFAULT 0)",

        		"CREATE TABLE library_db.members (member_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, phone VARCHAR(15) NOT NULL)",

        		"CREATE TABLE library_db.book_issues (issue_id INT PRIMARY KEY AUTO_INCREMENT, book_id INT NOT NULL, member_id INT NOT NULL, issue_date DATE NOT NULL, due_date DATE NOT NULL, return_date DATE DEFAULT NULL, fine DECIMAL(10,2) DEFAULT 0, FOREIGN KEY (book_id) REFERENCES library_db.books(book_id), FOREIGN KEY (member_id) REFERENCES library_db.members(member_id))"
        };

        try (Connection con = DriverManager.getConnection( URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            for (String query : queries) {
                st.executeUpdate(query);
            }

            System.out.println("Database and tables created successfully.");

        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}

	

