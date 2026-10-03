package com.stackly.project;

public class theatre {

	    // false = available
	    // true = booked
	    private boolean[][] seats;
        private double ticketPrice;

	    public theatre(int rows, int columns, double ticketPrice) {
           seats = new boolean[rows][columns];
           this.ticketPrice = ticketPrice;
	    }

	    
	    public double getTicketPrice() {
	        return ticketPrice;
	    }

	   
	    public void setTicketPrice(double ticketPrice) {
	        this.ticketPrice = ticketPrice;
	    }

	    // Show available seats
	    public void showAvailableSeats() {
               System.out.println("===== SEAT LAYOUT =====");
                for (int i = 0; i < seats.length; i++) {
                 System.out.print("Row " + (i + 1) + ": ");
                for (int j = 0; j < seats[i].length; j++) {
                   if (seats[i][j] == false) {
                    System.out.print("[O]");
                   } else{

	                    System.out.print("[X]");
	                }
	            }

	            System.out.println();
	        }

	        System.out.println("[O] = Available");
	        System.out.println("[X] = Booked");
	    }

	    // Book ticket
	    public void bookTicket(int row,int seat,boolean weekend,String coupon) {

	        // Check whether seat number is valid
	        if (!isValidSeat(row, seat)) {
                 System.out.println("Invalid row or seat number!");
	            return;
	        }

	        // Check whether seat is already booked
	        if (seats[row][seat]) {
                System.out.println("Sorry! This seat is already booked.");
	            return;
	        }

	        // Calculate price
	        double finalPrice = calculatePrice(weekend,coupon);

	        
	        seats[row][seat] = true;

	        System.out.println("\n===== BOOKING SUCCESSFUL =====");
            System.out.println( "Row :" + (row + 1));
            System.out.println( "Seat:" + (seat + 1));
            System.out.println("Ticket Price:rs" + finalPrice);
	    }

	    // Cancel ticket
	    public void cancelTicket(int row, int seat) {

	        // Check valid seat
	        if (!isValidSeat(row, seat)) {
	        	System.out.println("Invalid row or seat number!");
                return;
	        }

	        // Check whether seat is booked
	        if (!seats[row][seat]) {
                   System.out.println("This seat is not booked.");
                  return;
	        }

	        // Cancel ticket
	        seats[row][seat] = false;
            System.out.println("Ticket cancelled successfully!");
	    }

	    // Calculate ticket price
	    private double calculatePrice( boolean weekend,String coupon) {
                double price = ticketPrice;
	        if (weekend) {
                 price = price + (price * 0.20);
                System.out.println("Weekend pricing applied: 20% extra");
	        }
	        if (coupon.equalsIgnoreCase("MOVIE10")) {
                  price = price - (price * 0.10);
                  System.out.println("MOVIE10 coupon applied: 10% discount");

	        } else if (!coupon.equalsIgnoreCase("NO")) {
             System.out.println("Invalid coupon code");
	        }
            return price;
	    }

	 
	    private boolean isValidSeat(int row, int seat) {
                   return row >= 0 &&row < seats.length &&seat >= 0 &&seat < seats[row].length;
	    }
	}


