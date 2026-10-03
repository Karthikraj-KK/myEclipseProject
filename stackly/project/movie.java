package com.stackly.project;

public class movie {
	 private String movieName;
	    private double rating;

	  
	    public movie(String movieName, double rating) {
	        this.movieName = movieName;
	        this.rating = rating;
	    }
	    
	    public String getMovieName() {
			return movieName;
		}

		public void setMovieName(String movieName) {
			this.movieName = movieName;
		}

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public void displayMovieDetails() {

	        System.out.println("===== MOVIE DETAILS =====");

	        System.out.println("Movie Name : " + movieName);
	        System.out.println("Rating     : " + rating + " / 10");
	    }
}
