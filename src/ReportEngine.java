// ReportEngine.java
import java.util.ArrayList;
import java.util.HashMap;

public class ReportEngine {
    private HashMap<Integer, MonthlyReport> monthlyReports;
    private YearlyReport yearlyReport;
    private boolean monthlyReportsLoaded = false;
    private boolean yearlyReportLoaded = false;

    // Константы для названий месяцев
    private final String[] MONTH_NAMES = {
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    };

    public ReportEngine() {
        monthlyReports = new HashMap<>();
    }

    // Метод для преобразования номера месяца в название
    public String getMonthName(int month) {
        if (month >= 1 && month <= 12) {
            return MONTH_NAMES[month - 1];
        }
        return "Неизвестный месяц";
    }

    // Метод для очистки строки от пробелов
    private String cleanString(String str) {
        return str.trim();
    }

    // Загрузка месячных отчетов
    public void loadMonthlyReports(FileReader fileReader) {
        System.out.println("Загрузка месячных отчетов...");
        monthlyReports.clear();
        monthlyReportsLoaded = false;

        // Загружаем отчеты за январь, февраль, март 2021
        for (int month = 1; month <= 3; month++) {
            String fileName = "m.20210" + month + ".csv";
            ArrayList<String> lines = fileReader.readFileContents(fileName);

            if (lines.isEmpty()) {
                System.out.println("Не удалось загрузить отчет за " + getMonthName(month));
                continue;
            }

            MonthlyReport report = new MonthlyReport(month, getMonthName(month));

            // Пропускаем заголовок (строка 0)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // Очищаем каждую часть от пробелов
                    String itemName = cleanString(parts[0]);
                    boolean isExpense = Boolean.parseBoolean(cleanString(parts[1]));
                    int quantity = Integer.parseInt(cleanString(parts[2]));
                    int unitPrice = Integer.parseInt(cleanString(parts[3]));

                    Transaction transaction = new Transaction(itemName, isExpense, quantity, unitPrice);
                    report.addTransactions(transaction);
                }
            }

            monthlyReports.put(month, report);
            System.out.println("Загружен отчет за " + getMonthName(month));
        }

        monthlyReportsLoaded = true;
        System.out.println("Загрузка месячных отчетов завершена.");
    }

    // Загрузка годового отчета
    public void loadYearlyReport(FileReader fileReader) {
        System.out.println("Загрузка годового отчета...");

        String fileName = "y.2021.csv";
        ArrayList<String> lines = fileReader.readFileContents(fileName);

        if (lines.isEmpty()) {
            System.out.println("Не удалось загрузить годовой отчет.");
            yearlyReportLoaded = false;
            return;
        }

        yearlyReport = new YearlyReport(2021);

        // Пропускаем заголовок (строка 0)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length >= 3) {
                try {
                    // Очищаем каждую часть от пробелов перед парсингом
                    int month = Integer.parseInt(cleanString(parts[0]));
                    int amount = Integer.parseInt(cleanString(parts[1]));
                    boolean isExpense = Boolean.parseBoolean(cleanString(parts[2]));

                    YearlyRecord record = new YearlyRecord(month, amount, isExpense);
                    yearlyReport.addRecord(record);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка при парсинге строки: " + line);
                    System.out.println("Проблемное значение: " + e.getMessage());
                }
            }
        }

        yearlyReportLoaded = true;
        System.out.println("Годовой отчет загружен.");
    }

    // Сверка отчетов
    public void reconcileReports() {
        if (!monthlyReportsLoaded || !yearlyReportLoaded) {
            System.out.println("Сначала необходимо загрузить все отчеты!");
            return;
        }

        System.out.println("Сверка отчетов...");
        boolean hasErrors = false;

        for (int month = 1; month <= 3; month++) {
            MonthlyReport monthly = monthlyReports.get(month);
            if (monthly == null) continue;

            int monthlyIncome = monthly.getTotalIncome();
            int monthlyExpense = monthly.getTotalExpense();

            int yearlyIncome = yearlyReport.getIncomeForMonth(month);
            int yearlyExpense = yearlyReport.getExpenseForMonth(month);

            if (monthlyIncome != yearlyIncome) {
                System.out.println("Несоответствие по доходу в месяце " + getMonthName(month));
                System.out.println("  Месячный отчет: " + monthlyIncome);
                System.out.println("  Годовой отчет: " + yearlyIncome);
                hasErrors = true;
            }

            if (monthlyExpense != yearlyExpense) {
                System.out.println("Несоответствие по расходу в месяце " + getMonthName(month));
                System.out.println("  Месячный отчет: " + monthlyExpense);
                System.out.println("  Годовой отчет: " + yearlyExpense);
                hasErrors = true;
            }
        }

        if (!hasErrors) {
            System.out.println("Сверка прошла успешно! Все данные совпадают.");
        } else {
            System.out.println("Обнаружены несоответствия в отчетах.");
        }
    }

    // Вывод информации о месячных отчетах
    public void printMonthlyReports() {
        if (!monthlyReportsLoaded) {
            System.out.println("Сначала необходимо загрузить месячные отчеты!");
            return;
        }

        System.out.println("\nИНФОРМАЦИЯ О МЕСЯЧНЫХ ОТЧЕТАХ\n");

        for (int month = 1; month <= 3; month++) {
            MonthlyReport report = monthlyReports.get(month);
            if (report == null) continue;

            System.out.println("Месяц: " + report.getMonthName());

            Transaction mostProfitable = report.getMostProfitableItem();
            if (mostProfitable != null) {
                System.out.println("  Самый прибыльный товар: " + mostProfitable.getItemName() +
                        ", сумма: " + mostProfitable.getTotal());
            } else {
                System.out.println("  Нет данных о прибыльных товарах");
            }

            Transaction largestExpense = report.getLargestExpense();
            if (largestExpense != null) {
                System.out.println("  Самая большая трата: " + largestExpense.getItemName() +
                        ", сумма: " + largestExpense.getTotal());
            } else {
                System.out.println("  Нет данных о тратах");
            }

            System.out.println();
        }
    }

    // Вывод информации о годовом отчете
    public void printYearlyReport() {
        if (!yearlyReportLoaded) {
            System.out.println("Сначала необходимо загрузить годовой отчет!");
            return;
        }

        System.out.println("\n===== ИНФОРМАЦИЯ О ГОДОВОМ ОТЧЕТЕ =====\n");
        System.out.println("Год: " + yearlyReport.getYear());

        for (int month = 1; month <= 3; month++) {
            int income = yearlyReport.getIncomeForMonth(month);
            int expense = yearlyReport.getExpenseForMonth(month);
            int profit = income - expense;

            System.out.println("Прибыль за " + getMonthName(month) + ": " + profit);
        }

        System.out.println("\nСредний расход за все месяцы: " + yearlyReport.getAverageExpense());
        System.out.println("Средний доход за все месяцы: " + yearlyReport.getAverageIncome());
    }

    // Получение итогов по месяцам для годового отчета
    public ArrayList<MonthTotalPerYear> getMonthlyTotals() {
        ArrayList<MonthTotalPerYear> totals = new ArrayList<>();

        if (!monthlyReportsLoaded) {
            return totals;
        }

        for (int month = 1; month <= 3; month++) {
            MonthlyReport report = monthlyReports.get(month);
            if (report != null) {
                MonthTotalPerYear total = new MonthTotalPerYear(
                        month,
                        report.getTotalIncome(),
                        report.getTotalExpense()
                );
                totals.add(total);
            }
        }

        return totals;
    }

    public boolean isMonthlyReportsLoaded() {
        return monthlyReportsLoaded;
    }

    public boolean isYearlyReportLoaded() {
        return yearlyReportLoaded;
    }
}