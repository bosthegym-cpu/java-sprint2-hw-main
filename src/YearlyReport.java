// YearlyReport.java
import java.util.ArrayList;

public class YearlyReport {
    private int year;
    private ArrayList<YearlyRecord> records;

    public YearlyReport(int year) {
        this.year = year;
        this.records = new ArrayList<>();
    }

    public void addRecord(YearlyRecord record) {
        records.add(record);
    }

    public int getYear() {
        return year;
    }

    public ArrayList<YearlyRecord> getRecords() {
        return records;
    }

    // Получение дохода за конкретный месяц
    public int getIncomeForMonth(int month) {
        int totalIncome = 0;
        for (YearlyRecord record : records) {
            if (record.getMonth() == month && !record.isExpense()) {
                totalIncome += record.getAmount();
            }
        }
        return totalIncome;
    }

    // Получение расхода за конкретный месяц
    public int getExpenseForMonth(int month) {
        int totalExpense = 0;
        for (YearlyRecord record : records) {
            if (record.getMonth() == month && record.isExpense()) {
                totalExpense += record.getAmount();
            }
        }
        return totalExpense;
    }


    // Подсчет среднего дохода за все месяцы
    public double getAverageIncome() {
        int totalIncome = 0;
        int monthsWithIncome = 0;


        for (int month = 1; month <= 12; month++) {
            int incomeThisMonth = 0;


            for (YearlyRecord record : records) {
                if (record.getMonth() == month && !record.isExpense()) {
                    incomeThisMonth += record.getAmount();
                }
            }


            if (incomeThisMonth > 0) {
                totalIncome += incomeThisMonth;
                monthsWithIncome++;
            }
        }


        if (monthsWithIncome > 0) {
            double result = (double) totalIncome / monthsWithIncome;
            return result;
        } else {
            return 0;
        }
    }

    // Подсчет среднего расхода за все месяцы
    public double getAverageExpense() {
        int totalExpense = 0;
        int monthsWithExpense = 0;


        for (int month = 1; month <= 12; month++) {
            int expenseThisMonth = 0;


            for (YearlyRecord record : records) {
                if (record.getMonth() == month && record.isExpense()) {
                    expenseThisMonth += record.getAmount();
                }
            }


            if (expenseThisMonth > 0) {
                totalExpense += expenseThisMonth;
                monthsWithExpense++;
            }
        }


        if (monthsWithExpense > 0) {
            double result = (double) totalExpense / monthsWithExpense;
            return result;
        } else {
            return 0;
        }
    }
}