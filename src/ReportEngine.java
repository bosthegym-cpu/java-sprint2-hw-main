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
            String fileName = String.format("m.2021%02d.csv", month); // 202101, 202102...
            String monthName = getMonthName(month);

            ArrayList<String> lines = fileReader.readFileContents(fileName);

            if (lines.isEmpty() || lines.size() < 2) {
                System.out.println("Не удалось загрузить отчет за " + monthName + " (файл пустой или отсутствует)");
                continue;
            }

            MonthlyReport report = new MonthlyReport(month, getMonthName(month));

            // Пропускаем заголовок (строка 0)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                Transaction transaction = parseMonthlyLine(line, monthName);

                if (transaction != null) report.addTransactions(transaction);
            }


            monthlyReports.put(month, report);
            System.out.println("Загружен отчет за " + getMonthName(month));
        }

        monthlyReportsLoaded = !monthlyReports.isEmpty();
        if (monthlyReportsLoaded) {
            System.out.println("Загрузка месячных отчетов завершена.");
        } else System.out.println("Ни один месячный отчет не удалось загрузить.");
    }

    private Transaction parseMonthlyLine(String line, String monthName) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            System.out.println("Ошибка в месяце " + monthName + ": неверное количество полей в строке -> " + line);
            return null;
        }

        try {
            String itemName = parts[0].trim();
            String expenseStr = parts[1].trim();
            String qtrStr = parts[2].trim();
            String priceStr = parts[3].trim();

            boolean isExpense = Boolean.parseBoolean(expenseStr);
            int quantity = Integer.parseInt(qtrStr);
            int unitPrice = Integer.parseInt(priceStr);

            if (quantity < 0 || unitPrice < 0) {
                System.out.println("Предупреждение в месяце " + monthName + ": отрицательное количество или цена -> " + line);
            }
            return new Transaction(itemName, isExpense, quantity, unitPrice);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка парсинга чисел в месяце " + monthName + ": " + line);
            return null;
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка при разборе строки в месяце " + monthName + ": " + line);
            return null;
        }
    }

    // Загрузка годового отчета
    public void loadYearlyReport(FileReader fileReader) {
        System.out.println("Загрузка годового отчета...");

        String fileName = "y.2021.csv";
        ArrayList<String> lines = fileReader.readFileContents(fileName);

        if (lines.isEmpty() || lines.size() < 2) {
            System.out.println("Не удалось загрузить годовой отчет (файл пустой или отсутствует)");
            yearlyReportLoaded = false;
            return;
        }

        yearlyReport = new YearlyReport(2021);

        // Пропускаем заголовок (строка 0)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            YearlyRecord record = parseYearlyLine(line);

            if (record != null) {
                yearlyReport.addRecord(record);
            }
        }

        yearlyReportLoaded = yearlyReport.getRecords() != null && !yearlyReport.getRecords().isEmpty();
        if (yearlyReportLoaded) {
            System.out.println("Годовой отчет загружен.");
        } else {
            System.out.println("Не удалось загрузить годовой отчет (нет данных).");
        }
    }

    private YearlyRecord parseYearlyLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            System.out.println("Ошибка в годовом отчете: неверное количество полей в строке -> " + line);
            return null;
        }

        try {
            String monthStr = parts[0].trim();
            String amountStr = parts[1].trim();
            String expenseStr = parts[2].trim();

            int month = Integer.parseInt(monthStr);
            int amount = Integer.parseInt(amountStr);
            boolean isExpense = Boolean.parseBoolean(expenseStr);

            if (amount < 0) {
                System.out.println("Предупреждение в годовом отчете: отрицательная сумма -> " + line);
            }

            if (month < 1 || month > 12) {
                System.out.println("Предупреждение в годовом отчете: некорректный номер месяца -> " + line);
            }

            return new YearlyRecord(month, amount, isExpense);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка парсинга чисел в годовом отчете: " + line);
            return null;
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка при разборе строки в годовом отчете: " + line);
            return null;
        }
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


}