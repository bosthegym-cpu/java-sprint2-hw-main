import java.util.ArrayList;

public class MonthlyReport {
    private final int month;
    private final String monthName;
    private final ArrayList<Transaction> transactions;

    public MonthlyReport (int month, String monthName) {
        this.month = month;
        this.monthName = monthName;
        this.transactions = new ArrayList<>();
    }

    public void addTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return monthName;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
// Поиск самого прибыльного товара
    public Transaction getMostProfitableItem() {
        Transaction mostProfitable = null;
        int maxProfit = -1;

        for(Transaction transaction : transactions) {
            if(!transaction.isExpense()) {
                int profit = transaction.getTotal();
                if(profit > maxProfit) {
                    maxProfit = profit;
                    mostProfitable = transaction;
                }
            }
        }
        return mostProfitable;
    }
    // Поиск самой большой траты
    public Transaction getLargestExpense() {
        Transaction largestExpense = null;
        int maxExpense = -1;

        for(Transaction transaction : transactions) {
            if(transaction.isExpense()) {
                int expense = transaction.getTotal();
                if(expense > maxExpense) {
                    maxExpense = expense;
                    largestExpense = transaction;
                }
            }
        }
        return  largestExpense;
    }
    // Подсчет общего дохода за месяц
    public int getTotalIncome() {
        int total = 0;
        for(Transaction transaction : transactions) {
            if(!transaction.isExpense()) {
                total += transaction.getTotal();
            }
        }
        return total;
    }
    // Подсчет общего расхода за месяц
    public int getTotalExpense() {
        int total = 0;
        for(Transaction transaction : transactions) {
            if(transaction.isExpense()) {
                total += transaction.getTotal();
            }
        }
        return total;
    }


}
