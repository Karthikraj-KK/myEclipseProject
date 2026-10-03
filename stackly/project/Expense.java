package com.stackly.project;

import java.time.LocalDate;

public class Expense {
	private int id;
    private String description;
    private double amount;
    private Category category;
    private LocalDate date;
    
    
    public Expense(int id,String description,double amount,Category category,LocalDate date) {
    	 
    	this.id=id;
    	this.description=description;
    	this.amount=amount;
    	this.category=category;
    	this.date=date;
    	
    	
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "expense [id=" + id + ", description=" + description + ", amount=" + amount + ", category=" + category
				+ ", date=" + date + "]";
	}
    
}
