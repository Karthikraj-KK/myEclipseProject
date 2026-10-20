package jdbc_project;

import java.util.Scanner;

public class employeeLeaveManagement {

  public static void main(String[] args) {

        employeeTable.createtables();

        employeeDetails ed = new employeeDetails();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===== EMPLOYEE LEAVE MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Apply Leave");
            System.out.println("6. View Leave Requests");
            System.out.println("7. Approve Leave");
            System.out.println("8. Reject Leave");
            System.out.println("9. View Leave Balance");
            System.out.println("10. Exit");
            System.out.print("Enter your choice:");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid menu number");
                continue;
            }

            try {
                switch (choice) {

                    case 1:
                        System.out.print("Enter employee name:");
                        String name = sc.nextLine();

                        System.out.print("Enter department:");
                        String department = sc.nextLine();

                       ed.addEmployee(name, department);
                        break;

                    case 2:
                        ed.viewEmployees();
                        break;

                    case 3:
                        System.out.print("Enter employee ID:");
                        int updateId = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Enter new name:");
                        String newName = sc.nextLine();

                        System.out.print("Enter new department:");
                        String newDepartment = sc.nextLine();

                       ed.updateEmployee(updateId, newName, newDepartment);
                        break;

                    case 4:
                        System.out.print("Enter employee ID to delete:");
                        int deleteId = Integer.parseInt(sc.nextLine().trim());

                        ed.deleteEmployee(deleteId);
                        break;

                    case 5:
                        System.out.print("Enter employee ID:");
                        int employeeId = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Enter start date (YYYY-MM-DD):");
                        String startDate = sc.nextLine().trim();

                        System.out.print("Enter end date (YYYY-MM-DD):");
                        String endDate = sc.nextLine().trim();

                        System.out.print("Enter reason:");
                        String reason = sc.nextLine();

                        ed.applyLeave(employeeId, startDate, endDate, reason);
                        break;

                    case 6:
                        ed.viewLeaveRequests();
                        break;

                    case 7:
                        System.out.print("Enter request ID to approve:");
                        int approveId = Integer.parseInt(sc.nextLine().trim());

                        ed.processLeave(approveId, true);
                        break;

                    case 8:
                        System.out.print("Enter request ID to reject:");
                        int rejectId = Integer.parseInt(sc.nextLine().trim());

                        ed.processLeave(rejectId, false);
                        break;

                    case 9:
                        System.out.print("Enter employee ID:");
                        int balanceId = Integer.parseInt(sc.nextLine().trim());

                        ed.viewLeaveBalance(balanceId);
                        break;

                    case 10:
                        System.out.println("Thank you for using the system");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value");
            }
        }
    }
}


