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
        for (YearlyRecord record : records) {
            if (record.getMonth() == month && !record.isExpense()) {
                return record.getAmount();
            }
        }
        return 0;
    }

    // Получение расхода за конкретный месяц
    public int getExpenseForMonth(int month) {
        for (YearlyRecord record : records) {
            if (record.getMonth() == month && record.isExpense()) {
                return record.getAmount();
            }
        }
        return 0;
    }

    // Подсчет среднего дохода за все месяцы
    public double getAverageIncome() {
        int totalIncome = 0;
        int monthsWithIncome = 0;

        for (YearlyRecord record : records) {
            if (!record.isExpense()) {
                totalIncome += record.getAmount();
                monthsWithIncome++;
            }
        }

        if(monthsWithIncome > 0) {
            return totalIncome / monthsWithIncome;
        }   else {
            return 0;
        }

    }

    // Подсчет среднего расхода за все месяцы
    public double getAverageExpense() {
        int totalExpense = 0;
        int monthsWithExpense = 0;

        for (YearlyRecord record : records) {
            if (record.isExpense()) {
                totalExpense += record.getAmount();
                monthsWithExpense++;
            }
        }

        if(monthsWithExpense > 0) {
            return totalExpense / monthsWithExpense;
        } else {
            return 0;
        }
    }
}