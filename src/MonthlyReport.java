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
        Transaction best = null;

        for (Transaction t : transactions) {
            if(!t.isExpense()) {
                if (best == null || t.getTotal() > best.getTotal()) best = t;
            }
        }
        return best;
    }
    // Поиск самой большой траты
    public Transaction getLargesExpense() {
        Transaction worst = null;

        for (Transaction t : transactions) {
            if(!t.isExpense()) {
                if (worst == null || t.getTotal() > worst.getTotal()) worst = t;
            }
        }
        return worst;
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


    public Transaction getLargestExpense() {
        return null;
    }
}
