
public class MonthTotalPerYear {
    private int month;
    private int income;
    private int expense;

    public MonthTotalPerYear(int month, int income, int expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
    }

    public int getMonth() {
        return month;
    }

    public int getIncome() {
        return income;
    }

    public int getExpense() {
        return expense;
    }

    public int getProfit() {
        return income - expense;
    }
}