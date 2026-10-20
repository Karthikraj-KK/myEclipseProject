package jdbc_project;


import java.util.Scanner;



public class libraryManagement {
	
	
 public static void main(String[] args) {

        dbTable.createtables();

        libraryOperations lo = new libraryOperations();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Register Member");
            System.out.println("7. Issue Book");
            System.out.println("8. Return Book");
            System.out.println("9. View Issue History");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1:
                        System.out.print("Enter title: ");
                        String title = sc.nextLine();

                        System.out.print("Enter author: ");
                        String author = sc.nextLine();

                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(sc.nextLine().trim());

                        lo.addBook(title, author, quantity);
                        break;

                    case 2:
                        lo.viewBooks();
                        break;

                    case 3:
                        System.out.print("Enter title or author: ");
                        lo.searchBook(sc.nextLine());
                        break;

                    case 4:
                        System.out.print("Enter book ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Enter new title: ");
                        String newTitle = sc.nextLine();

                        System.out.print("Enter new author: ");
                        String newAuthor = sc.nextLine();

                        System.out.print("Enter new quantity: ");
                        int newQuantity = Integer.parseInt(sc.nextLine().trim());

                        lo.updateBook(id, newTitle, newAuthor, newQuantity);
                        break;

                    case 5:
                        System.out.print("Enter book ID: ");
                        lo.deleteBook(Integer.parseInt(sc.nextLine().trim()));
                        break;

                    case 6:
                        System.out.print("Enter member name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter phone: ");
                        String phone = sc.nextLine();

                        lo.addMember(name, phone);
                        break;

                    case 7:
                        System.out.print("Enter book ID: ");
                        int bookId = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Enter member ID: ");
                        int memberId = Integer.parseInt(sc.nextLine().trim());

                        lo.issueBook(bookId, memberId);
                        break;

                    case 8:
                        System.out.print("Enter issue ID: ");
                        lo.returnBook(Integer.parseInt(sc.nextLine().trim()));
                        break;

                    case 9:
                        lo.viewIssues();
                        break;

                    case 0:
                        System.out.println("Thank you!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}


