import java.util.Scanner;

public class UserManager {
     void addNewTask(TaskManager tm, Scanner sc) {
        System.out.print("Название задачи: ");
        String name = sc.nextLine();
        System.out.print("Описание: ");
        String desc = sc.nextLine();
        System.out.println("Статус (1 - NEW, 2 - IN_PROGRESS, 3 - DONE): ");
        int statusChoice = sc.nextInt();
        sc.nextLine();

        TaskStatus status = getStatusByChoice(statusChoice);
        Task newTask = new Task(name, desc, status);
        int id = tm.addTask(newTask);
        System.out.println("Задача добавлена! ID = " + id);
    }

     void addNewEpic(TaskManager tm, Scanner sc) {
        System.out.print("Название эпика: ");
        String name = sc.nextLine();
        System.out.print("Описание: ");
        String desc = sc.nextLine();

        Epic newEpic = new Epic(name, desc);
        int id = tm.addEpic(newEpic);
        System.out.println("Эпик добавлен! ID = " + id);
    }

     void addNewSubtask(TaskManager tm, Scanner sc) {
        System.out.print("Название подзадачи: ");
        String name = sc.nextLine();
        System.out.print("Описание: ");
        String desc = sc.nextLine();
        System.out.println("Статус (1 - NEW, 2 - IN_PROGRESS, 3 - DONE): ");
        int statusChoice = sc.nextInt();
        System.out.print("ID эпика, к которому относится: ");
        int epicId = sc.nextInt();
        sc.nextLine();

        TaskStatus status = getStatusByChoice(statusChoice);
        Subtask newSub = new Subtask(name, desc, status, epicId);
        int id = tm.addSubtask(newSub);

        if (id != -1) {
            System.out.println("Подзадача добавлена! ID = " + id);
        } else {
            System.out.println("Ошибка! Эпик с ID " + epicId + " не найден!");
        }
    }

    void showAllTasks(TaskManager tm) {
        System.out.println("\nВСЕ ЗАДАЧИ");
        var tasks = tm.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Нет ни одной задачи");
        } else {
            System.out.println(tasks);
        }
    }

    void showAllEpics(TaskManager tm) {
        System.out.println("\nВСЕ ЭПИКИ");
        var epics = tm.getAllEpics();
        if (epics.isEmpty()) {
            System.out.println("Нет ни одного эпика");
        } else {
            System.out.println(epics);
        }
    }

    void showAllSubtasks(TaskManager tm) {
        System.out.println("\nВСЕ ПОДЗАДАЧИ");
        var subtasks = tm.getAllSubtasks();
        if (subtasks.isEmpty()) {
            System.out.println("Нет ни одной подзадачи");
        } else {
            System.out.println(subtasks);
        }
    }

    void showEpicSubtasks(TaskManager tm, Scanner sc) {
        System.out.print("Введи ID эпика: ");
        int epicId = sc.nextInt();
        sc.nextLine();

        var subtasks = tm.getAllSubtasksForEpic(epicId);
        if (subtasks.isEmpty()) {
            System.out.println("У этого эпика нет подзадач");
        } else {
            System.out.println("Подзадачи эпика " + epicId + ":");
            System.out.println(subtasks);
        }
    }

    void updateTaskStatus(TaskManager tm, Scanner sc) {
        System.out.println("Что обновляем?");
        System.out.println("1. Обычную задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        int type = sc.nextInt();
        sc.nextLine();

        System.out.print("Введи ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Новый статус (1 - NEW, 2 - IN_PROGRESS, 3 - DONE): ");
        int statusChoice = sc.nextInt();
        sc.nextLine();
        TaskStatus newStatus = getStatusByChoice(statusChoice);

        if (type == 1) {
            Task oldTask = tm.getTask(id);
            if (oldTask != null) {
                Task updated = new Task(oldTask.taskName, oldTask.taskDescription, newStatus);
                tm.updateTask(updated, id);
                System.out.println("Статус задачи обновлен!");
            } else {
                System.out.println("Задача не найдена!");
            }
        }
        else if (type == 2) {
            Epic oldEpic = tm.getEpic(id);
            if (oldEpic != null) {
                Epic updated = new Epic(oldEpic.taskName, oldEpic.taskDescription);
                // Статус эпика сам пересчитается
                tm.updateEpic(updated, id);
                System.out.println("Эпик обновлен (статус пересчитается автоматом)!");
            } else {
                System.out.println("Эпик не найден!");
            }
        }
        else if (type == 3) {
            Subtask oldSub = tm.getSubtask(id);
            if (oldSub != null) {
                Subtask updated = new Subtask(oldSub.taskName, oldSub.taskDescription, newStatus, oldSub.getEpicID());
                tm.updateSubtask(updated, id);
                System.out.println("Статус подзадачи обновлен! Статус эпика пересчитался.");
            } else {
                System.out.println("Подзадача не найдена!");
            }
        }
    }

    void deleteTaskById(TaskManager tm, Scanner sc) {
        System.out.println("Что удаляем?");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик (и все его подзадачи)");
        System.out.println("3. Подзадачу");
        int type = sc.nextInt();
        sc.nextLine();

        System.out.print("Введи ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean success = false;
        if (type == 1) success = tm.deleteTask(id);
        else if (type == 2) success = tm.deleteEpic(id);
        else if (type == 3) success = tm.deleteSubtask(id);

        if (success) {
            System.out.println("Удалено!");
        } else {
            System.out.println("Не найдено!");
        }
    }

    void deleteEverything(TaskManager tm, Scanner sc) {
        System.out.print("Точно всё удалить? (1 - ДА, 0 - НЕТ): ");
        int confirm = sc.nextInt();
        sc.nextLine();

        if (confirm == 1) {
            tm.deleteAllTasks();
            tm.deleteAllEpics();
            tm.deleteAllSubtasks();
            System.out.println("Всё удалено! Ты псих");
        } else {
            System.out.println("Фух, передумал");
        }
    }

    static TaskStatus getStatusByChoice(int choice) {
        if (choice == 1) return TaskStatus.NEW;
        if (choice == 2) return TaskStatus.IN_PROGRESS;
        return TaskStatus.DONE;
    }
}
