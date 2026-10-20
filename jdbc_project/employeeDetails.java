package jdbc_project;
import java.sql.*;
import java.time.LocalDate;


public class employeeDetails {
	
	
         // 1. registered employee
    public void addEmployee(String name, String department) {
        if (name == null || name.trim().isEmpty() || department == null || department.trim().isEmpty()) {
            System.out.println("Name and department are required");
            return;
        }

        try (Connection con = employeeTable.getConnection()) {
            con.setAutoCommit(false);

            try {
                int employeeId;

                String sql = "INSERT INTO employees(name, department) VALUES(?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, department.trim());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Employee ID not generated");
                        }
                        employeeId = rs.getInt(1);
                    }
                }

                sql = "INSERT INTO leave_balance(employee_id, total_leaves, remaining_leaves) VALUES(?, 12, 12)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, employeeId);
                    ps.executeUpdate();
                }

                con.commit();
                System.out.println("Employee registered successfully");
                System.out.println("Employee ID: " + employeeId);
                System.out.println("Initial leave balance: 12 days");

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Registration failed:"+e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Database error:"+ e.getMessage());
        }
    }

    // 2. view  all employees
        public void viewEmployees() {
        String sql = "SELECT e.employee_id, e.name, e.department,b.total_leaves, b.remaining_leaves FROM employees e JOIN leave_balance b ON e.employee_id = b.employee_id ORDER BY e.employee_id";

        try (Connection con = employeeTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("ID | Name | Department | Total Leaves | Remaining Leaves");

            while (rs.next()) {
                System.out.println(rs.getInt("employee_id")+"|"+rs.getString("name")+"|"+rs.getString("department")+"|"+rs.getInt("total_leaves")+"|"+rs.getInt("remaining_leaves"));
            }

        } catch (Exception e) {
            System.out.println("Error:"+ e.getMessage());
        }
    }

    // 3.update employee
    public void updateEmployee(int id, String name, String department) {
        if (id <= 0 || name == null || name.trim().isEmpty()||department == null||department.trim().isEmpty()) {
            System.out.println("Invalid employee details");
            return;
        }

        String sql ="UPDATE employees SET name=?, department=? WHERE employee_id=?";

        try (Connection con = employeeTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, department.trim());
            ps.setInt(3, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Employee ID not found.");
            }

        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }

    // 4. delete employee
    public void deleteEmployee(int id) {
        if (id <= 0) {
            System.out.println("Invalid employee ID");
            return;
        }

        String sql = "DELETE FROM employees WHERE employee_id=?";

        try (Connection con = employeeTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Employee deleted successfully");
            } else {
                System.out.println("Employee ID not found");
            }

        } catch (SQLException e) {
            System.out.println("Cannot delete employee: leave records may exist" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }
    }

    // 5. apply for leave
    public void applyLeave(int employeeId, String startText, String endText, String reason) {
        if (employeeId <= 0 || reason == null || reason.trim().isEmpty()) {
            System.out.println("Invalid employee ID or reason");
            return;
        }

        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startText);
            end = LocalDate.parse(endText);
        } catch (Exception e) {
            System.out.println("Invalid date. Use YYYY-MM-DD");
            return;
        }

        if (end.isBefore(start)) {
            System.out.println("End date cannot be before start date");
            return;
        }

        String checkSql = "SELECT employee_id FROM employees WHERE employee_id=?";
        String insertSql = "INSERT INTO leave_requests(employee_id, start_date, end_date, reason, status)VALUES(?,?,?,?, 'PENDING')";

        try (Connection con = employeeTable.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, employeeId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Employee not found.");
                        return;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, employeeId);
                ps.setDate(2, Date.valueOf(start));
                ps.setDate(3, Date.valueOf(end));
                ps.setString(4, reason.trim());
                ps.executeUpdate();

                System.out.println("Leave request submitted successfully.");
                System.out.println("Status: PENDING");
            }

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // 6. view leave requests
    public void viewLeaveRequests() {
        String sql = "SELECT r.request_id, e.name, r.start_date,r.end_date, r.reason, r.status FROM leave_requests r JOIN employees e ON r.employee_id=e.employee_id ORDER BY r.request_id DESC";

        try (Connection con = employeeTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Request ID | Employee | Start | End | Reason | Status");

            while (rs.next()) {
                System.out.println( rs.getInt("request_id")+"|" +rs.getString("name")+"|"+rs.getDate("start_date")+"|"+rs.getDate("end_date")+"|"+rs.getString("reason")+"|"+rs.getString("status"));
            }

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // 7. approve or reject leave
    public void processLeave(int requestId, boolean approve) {
        if (requestId <= 0) {
            System.out.println("Invalid request ID");
            return;
        }

        try (Connection con = employeeTable.getConnection()) {
            con.setAutoCommit(false);

            try {
                int employeeId;
                LocalDate start;
                LocalDate end;

                String sql = "SELECT employee_id, start_date, end_date FROM leave_requests WHERE request_id=? AND status='PENDING' FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, requestId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Pending request not found or already processed.");
                        }

                        employeeId = rs.getInt("employee_id");
                        start = rs.getDate("start_date").toLocalDate();
                        end = rs.getDate("end_date").toLocalDate();
                    }
                }

                if (approve) {
                    // Inclusive leave duration.
                    long days = end.toEpochDay() - start.toEpochDay() + 1;

                    sql = "SELECT remaining_leaves FROM leave_balance WHERE employee_id=? FOR UPDATE";

                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, employeeId);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new SQLException("Leave balance not found");
                            }

                            if (rs.getInt("remaining_leaves") < days) {
                                throw new SQLException("Insufficient leave balance.Required:"+days+"days");
                            }
                        }
                    }

                    sql = "UPDATE leave_balance SET remaining_leaves=remaining_leaves-? WHERE employee_id=? AND remaining_leaves>=?";

                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setLong(1, days);
                        ps.setInt(2, employeeId);
                        ps.setLong(3, days);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException("Unable to deduct leave balance");
                        }
                    }
                }

                String status = approve ? "APPROVED" : "REJECTED";

                sql = "UPDATE leave_requests SET status=? WHERE request_id=? AND status='PENDING'";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, status);
                    ps.setInt(2, requestId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Unable to process request");
                    }
                }

                con.commit();
                System.out.println("Leave request"+ status +"successfully");

            } catch (Exception e) {
                con.rollback();
                System.out.println("Operation failed:"+ e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Database error:"+ e.getMessage());
        }
    }

    // 8. view leave balance
    public void viewLeaveBalance(int employeeId) {
        if (employeeId <= 0) {
            System.out.println("Invalid employee ID");
            return;
        }

        String sql = "SELECT e.name, b.total_leaves, b.remaining_leaves FROM employees e JOIN leave_balance b ON e.employee_id=b.employee_id WHERE e.employee_id=?";

        try (Connection con = employeeTable.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Employee:" + rs.getString("name"));
                    System.out.println("Total leaves:" + rs.getInt("total_leaves"));
                    System.out.println("Remaining leaves:" +rs.getInt("remaining_leaves"));
                } else {
                    System.out.println("Employee not found.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error:"+ e.getMessage());
        }
    }
}


