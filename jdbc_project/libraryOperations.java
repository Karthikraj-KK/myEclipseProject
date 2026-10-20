package jdbc_project;

import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;


public class libraryOperations {

       // add books
         public void addBook(String title, String author, int quantity) {
         if (title == null || title.trim().isEmpty()  || author == null || author.trim().isEmpty() || quantity < 0) {
            System.out.println("Invalid book details.");
            return;
        }

        String sql = "INSERT INTO books(title, author, quantity) VALUES(?,?,?)";

        try (Connection con = dbTable.getConnection();PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, author.trim());
            ps.setInt(3, quantity);
            ps.executeUpdate();

            System.out.println("Book added successfully.");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // view books
        public void viewBooks() {
        String sql = "SELECT * FROM books ORDER BY book_id";

        try (Connection con = dbTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("ID | Title | Author | Quantity");

            while (rs.next()) {
                System.out.println( rs.getInt("book_id") + " | " +rs.getString("title") + " | " + rs.getString("author") + " | " + rs.getInt("quantity"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // search
         public void searchBook(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty");
            return;
        }

        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        try (Connection con = dbTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,"%"+ keyword.trim() +"%");
            ps.setString(2,"%"+ keyword.trim() +"%");

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.println( rs.getInt("book_id")+ "|"+ rs.getString("title") +"|"+rs.getString("author") +"|" +rs.getInt("quantity"));
                }

                if (!found) {
                    System.out.println("No books found.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // update book
    public void updateBook(int id, String title, String author, int quantity) {
        if (id <= 0 || title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty() || quantity < 0) {
            System.out.println("Invalid book details.");
            return;
        }

        String sql = "UPDATE books SET title=?, author=?, quantity=? WHERE book_id=?";

        try (Connection con = dbTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, author.trim());
            ps.setInt(3, quantity);
            ps.setInt(4, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Book updated successfully");
            } else {
                System.out.println("Book ID not found");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // delete book
    public void deleteBook(int id) {
        if (id <= 0) {
            System.out.println("Invalid book ID");
            return;
        }

        String sql = "DELETE FROM books WHERE book_id=? AND NOT EXISTS (SELECT 1 FROM book_issues WHERE book_id=?)";

        try (Connection con = dbTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Book deleted successfully");
            } else {
                System.out.println("Book not found or has issue history");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // registered member
    public void addMember(String name, String phone) {
        if (name == null || name.trim().isEmpty()|| phone == null || phone.trim().isEmpty()) {
            System.out.println("Name and phone are required.");
            return;
        }

        String sql = "INSERT INTO members(name, phone) VALUES(?,?)";

        try (Connection con = dbTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, phone.trim());
            ps.executeUpdate();

            System.out.println("Member registered successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //issue book
    public void issueBook(int bookId, int memberId) {
        if (bookId <= 0 || memberId <= 0) {
            System.out.println("Invalid book or member ID.");
            return;
        }

        try (Connection con = dbTable.getConnection()) {
            con.setAutoCommit(false);

            try {
                // Lock book row and check availability.
                try (PreparedStatement ps = con.prepareStatement("SELECT quantity FROM books WHERE book_id=? FOR UPDATE")) {
                    ps.setInt(1, bookId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getInt("quantity") < 1) {
                            throw new SQLException("Book unavailable");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("SELECT member_id FROM members WHERE member_id=?")) {
                    ps.setInt(1, memberId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Member not found.");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("UPDATE books SET quantity=quantity-1 WHERE book_id=? AND quantity>0")) {
                    ps.setInt(1, bookId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Book unavailable");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("INSERT INTO book_issues(book_id, member_id, issue_date, due_date) VALUES(?,?,?,?)")) {
                    LocalDate today = LocalDate.now();

                    ps.setInt(1, bookId);
                    ps.setInt(2, memberId);
                    ps.setDate(3, Date.valueOf(today));
                    ps.setDate(4, Date.valueOf(today.plusDays(14)));
                    ps.executeUpdate();
                }

                con.commit();
                System.out.println("Book issued. Return within 14 days");

            } catch (Exception e) {
                con.rollback();
                System.out.println("Issue failed:" + e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Database error:"+ e.getMessage());
        }
    }

    // RETURN BOOK AND CALCULATE FINE
    public void returnBook(int issueId) {
        if (issueId <= 0) {
            System.out.println("Invalid issue ID");
            return;
        }

        try (Connection con = dbTable.getConnection()) {
            con.setAutoCommit(false);

            try {
                int bookId;
                LocalDate dueDate;

                try (PreparedStatement ps = con.prepareStatement("SELECT book_id, due_date FROM book_issues WHERE issue_id=? AND return_date IS NULL FOR UPDATE")) {
                    ps.setInt(1, issueId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Issue not found or already returned.");
                        }

                        bookId = rs.getInt("book_id");
                        dueDate = rs.getDate("due_date").toLocalDate();
                    }
                }

                LocalDate today = LocalDate.now();
                long overdueDays = Math.max( 0, ChronoUnit.DAYS.between(dueDate, today));
                double fine = overdueDays * 5.0;

                try (PreparedStatement ps = con.prepareStatement( "UPDATE book_issues SET return_date=?, fine=? WHERE issue_id=? AND return_date IS NULL")) {
                    ps.setDate(1, Date.valueOf(today));
                    ps.setDouble(2, fine);
                    ps.setInt(3, issueId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Return failed.");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("UPDATE books SET quantity=quantity+1 WHERE book_id=?")) {
                    ps.setInt(1, bookId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Book record not found.");
                    }
                }

                con.commit();
                System.out.println("Book returned successfully.");
                System.out.println("Overdue days: " + overdueDays);
                System.out.println("Fine: Rs. " + fine);

            } catch (Exception e) {
                con.rollback();
                System.out.println("Return failed: " + e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // view issue history using join
    public void viewIssues() {
        String sql = "SELECT i.issue_id, b.title, m.name, i.issue_date,i.due_date, i.return_date, i.fine FROM book_issues i JOIN books b ON i.book_id=b.book_id JOIN members m ON i.member_id=m.member_id ORDER BY i.issue_id DESC";

        try (Connection con = dbTable.getConnection();PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                    "Issue ID: " + rs.getInt("issue_id") +
                    " | Book: " + rs.getString("title") +
                    " | Member: " + rs.getString("name") +
                    " | Issued: " + rs.getDate("issue_date") +
                    " | Due: " + rs.getDate("due_date") +
                    " | Returned: " + rs.getDate("return_date") +
                    " | Fine: Rs. " + rs.getBigDecimal("fine"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


