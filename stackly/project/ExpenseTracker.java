package com.stackly.project;

import java.time.LocalDate;
import java.util.Scanner;

public class ExpenseTracker {
	 public static void main(String[] args) {

	        Scanner scanner = new Scanner(System.in);

	        Expensemanager manager = new Expensemanager();

	        int id = 1;

	        while (true) {

	            System.out.println("==============================");
	            System.out.println("PERSONAL EXPENSE TRACKER");
	            System.out.println("==============================");
                System.out.println("1. Add Expense");
	            System.out.println("2. Display All Expenses");
	            System.out.println("3. Delete Expense");
	            System.out.println("4. Calculate Total Expense");
	            System.out.println("5. Find Highest Expense");
	            System.out.println("6. Category-wise Expenses");
	            System.out.println("7. Exit");

	            System.out.print("Enter your choice: ");

	            int choice = scanner.nextInt();
	            scanner.nextLine();
                switch (choice) {

	                case 1:

	                    System.out.print("Enter description: ");
	                    String description = scanner.nextLine();

	                    System.out.print("Enter amount: ");
	                    double amount = scanner.nextDouble();

	                    System.out.println("Select Category:");

	                    Category[] categories = Category.values();

	                    for (int i = 0; i < categories.length; i++) {

	                        System.out.println((i + 1) + ". " + categories[i]);
	                    }

	                    System.out.print("Enter category number: ");
	                    int categoryChoice = scanner.nextInt();

	                    if (categoryChoice < 1 || categoryChoice > categories.length) {

	                        System.out.println("Invalid category!");
	                        break;
	                    }

	                    Category category = categories[categoryChoice -1];

	                    LocalDate date = LocalDate.now();

	                    Expense expense = new Expense(id, description,  amount, category, date );

	                    manager.addExpense(expense);

	                    id++;

	                    break;

	                case 2:

	                    manager.displayExpenses();
	                    break;

	                case 3:

	                    System.out.print("Enter Expense ID to delete: ");
                        int deleteId = scanner.nextInt();
                        manager.deleteExpense(deleteId);
                        break;

	                case 4:

	                    manager.calculateTotal();
                        break;

	                case 5:

	                    manager.findHighestExpense();
                        break;

	                case 6:

	                    manager.categoryWiseExpenses();
	                    break;

	                case 7:

	                    System.out.println("Thank you for using Expense Tracker!" );
                        scanner.close();
                        return;

	                default:
                        System.out.println("Invalid choice. Please try again.");
	            }
	        }
	    }
}
