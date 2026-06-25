import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        UserManager um = new UserManager();

        System.out.println("МЕНЕДЖЕР ЗАДАЧ");

        while (true) {
            System.out.println("\nЧто хочешь сделать?");
            System.out.println("1. Добавить задачу (Task)");
            System.out.println("2. Добавить эпик (Epic)");
            System.out.println("3. Добавить подзадачу (Subtask)");
            System.out.println("4. Посмотреть все задачи");
            System.out.println("5. Посмотреть все эпики");
            System.out.println("6. Посмотреть все подзадачи");
            System.out.println("7. Посмотреть подзадачи эпика");
            System.out.println("8. Обновить статус задачи");
            System.out.println("9. Удалить задачу по ID");
            System.out.println("10. Удалить всё");
            System.out.println("0. Выйти");
            System.out.print("Твой выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // чистим перенос строки

            if (choice == 0) {
                System.out.println("Пока");
                break;
            }

            switch (choice) {
                case 1:
                    um.addNewTask(tm, scanner);
                    break;
                case 2:
                    um.addNewEpic(tm, scanner);
                    break;
                case 3:
                    um.addNewSubtask(tm, scanner);
                    break;
                case 4:
                    um.showAllTasks(tm);
                    break;
                case 5:
                    um.showAllEpics(tm);
                    break;
                case 6:
                    um.showAllSubtasks(tm);
                    break;
                case 7:
                    um.showEpicSubtasks(tm, scanner);
                    break;
                case 8:
                    um.updateTaskStatus(tm, scanner);
                    break;
                case 9:
                    um.deleteTaskById(tm, scanner);
                    break;
                case 10:
                    um.deleteEverything(tm, scanner);
                    break;
                default:
                    System.out.println("Такого пункта нет, тупишь?");
            }
        }
        scanner.close();
    }
}