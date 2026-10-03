package com.stackly.project;

import java.util.Scanner;

public class ticketBooking {
	public static void main(String[] args) {

	        Scanner scanner = new Scanner(System.in);

	        // Create Movie object
	        movie movie = new movie("Avengers",8.5);

	        // Create theatre object
	        // 10 rows
	        // 20 seats per row
	        // Ticket price = rs.300
	        theatre theatre = new theatre( 10, 20,300);

	        while (true) {
                System.out.println("================================");
	            System.out.println("      MOVIE TICKET BOOKING");
	            System.out.println("================================");
                System.out.println("1. Movie Details");
	            System.out.println("2. Show Available Seats");
	            System.out.println("3. Book Ticket");
	            System.out.println("4. Cancel Ticket");
	            System.out.println("5. Exit");

	            System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {

	              
	                case 1:
                         movie.displayMovieDetails();
                         break;
                   
	                case 2:
                        theatre.showAvailableSeats();
                        break;
                        
                   case 3:
                          System.out.println( "===== BOOK TICKET =====");
                          theatre.showAvailableSeats();

	                    System.out.print("Enter row number (1-10):");

	                    int row = scanner.nextInt();

	                    System.out.print( "Enter seat number (1-20):");

	                    int seat = scanner.nextInt();

	                    // Weekend input
	                    System.out.print( "Is today weekend? (yes/no):");

	                    String weekendInput =scanner.next();
                        boolean weekend = weekendInput.equalsIgnoreCase("yes");

	                    // Coupon input
	                    System.out.print("Enter coupon code (or NO): ");

	                    String coupon = scanner.next();

	                    // Convert user number to array index
	                    row = row - 1;
	                    seat = seat - 1;

	                    // Book ticket
	                    theatre.bookTicket(row,seat,weekend,coupon);
                        break;

	                // Cancel ticket
	                case 4:

	                    System.out.println(
	                            "\n===== CANCEL TICKET ====="
	                    );

	                    theatre.showAvailableSeats();

	                    System.out.print(
	                            "\nEnter row number (1-5): "
	                    );

	                    int cancelRow =
	                            scanner.nextInt();

	                    System.out.print(
	                            "Enter seat number (1-6): "
	                    );

	                    int cancelSeat =
	                            scanner.nextInt();

	                    // Convert to array index
	                    cancelRow = cancelRow - 1;
	                    cancelSeat = cancelSeat - 1;

	                    // Cancel ticket
	                    theatre.cancelTicket(
	                            cancelRow,
	                            cancelSeat
	                    );

	                    break;

	                // Exit
	                case 5:

	                    System.out.println(
	                            "\nThank you for using " +
	                            "Movie Ticket Booking System!"
	                    );

	                    scanner.close();

	                    return;

	                default:

	                    System.out.println(
	                            "Invalid choice. Please try again."
	                    );
	            }
	        }
	    }
	}


