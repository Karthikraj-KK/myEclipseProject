package com.stackly.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

    public class Expensemanager {
	 
           private ArrayList<Expense> expenses = new ArrayList<>();

    
    public void addExpense(Expense expense) {

        expenses.add(expense);	
        System.out.println("Expense added successfully!");
    }

    
    public void displayExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("ALL EXPENSES");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

 
    public void deleteExpense(int id) {

        boolean removed = expenses.removeIf(expense -> expense.getId() == id);

        if (removed) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

  
    public void calculateTotal() {

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
          System.out.println("Total Expense:rs." + total);
    }

   
    public void findHighestExpense() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        Expense highest = expenses.stream().max((e1, e2) ->Double.compare( e1.getAmount(), e2.getAmount() )).get();

        System.out.println("===== HIGHEST EXPENSE =====");
        System.out.println(highest);
    }

  
    public void categoryWiseExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        HashMap<Category, Double> categoryMap = new HashMap<>();

        for (Expense expense : expenses) {

            Category category = expense.getCategory();

            double currentAmount = categoryMap.getOrDefault(category, 0.0);

            categoryMap.put(category,currentAmount + expense.getAmount());
        }

        System.out.println("===== CATEGORY-WISE EXPENSES =====");

        for (Map.Entry<Category, Double>entry: categoryMap.entrySet()) {

            System.out.println(entry.getKey()+":rs" +entry.getValue());
        }
    }
}
