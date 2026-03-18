
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReportEngine reportEngine = new ReportEngine();
        FileReader fileReader = new FileReader();
        Scanner scanner = new Scanner(System.in);

        System.out.println("ПРИЛОЖЕНИЕ ДЛЯ БУХГАЛТЕРИИ ПАРКА АТТРАКЦИОНОВ");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Считать все месячные отчёты");
            System.out.println("2 - Считать годовой отчёт");
            System.out.println("3 - Сверить отчёты");
            System.out.println("4 - Вывести информацию о месячных отчётах");
            System.out.println("5 - Вывести информацию о годовом отчёте");
            System.out.println("0 - Выход");

            System.out.print("Ваш выбор: ");
            if(scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    reportEngine.loadMonthlyReports(fileReader);
                    break;
                case 2:
                    reportEngine.loadYearlyReport(fileReader);
                    break;
                case 3:
                    reportEngine.reconcileReports();
                    break;
                case 4:
                    reportEngine.printMonthlyReports();
                    break;
                case 5:
                    reportEngine.printYearlyReport();
                    break;
                case 0:
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверная команда. Попробуйте снова.");
                }
            }
        }
    }
}