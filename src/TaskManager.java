import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private int counter = 1;

    // TASK
    public int addTask(Task t) {
        t.setTaskID(counter);
        tasks.put(counter, t);
        return counter++;
    }

    public HashMap<Integer, Task> getAllTasks() { return tasks; }
    public Task getTask(int id) { return tasks.get(id); }
    public void deleteAllTasks() { tasks.clear(); }
    public boolean deleteTask(int id) { return tasks.remove(id) != null; }
    public void updateTask(Task newTask, int oldId) {
        newTask.setTaskID(oldId);
        tasks.put(oldId, newTask);
    }

    // EPIC
    public int addEpic(Epic e) {
        e.setTaskID(counter);
        tasks.put(counter, e);
        return counter++;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        HashMap<Integer, Epic> result = new HashMap<>();
        for (var entry : tasks.entrySet()) {
            if (entry.getValue() instanceof Epic) {
                result.put(entry.getKey(), (Epic) entry.getValue());
            }
        }
        return result;
    }

    public Epic getEpic(int id) {
        Task t = tasks.get(id);
        return (t instanceof Epic) ? (Epic) t : null;
    }

    public void deleteAllEpics() {
        tasks.entrySet().removeIf(entry -> entry.getValue() instanceof Epic);
    }

    public boolean deleteEpic(int id) {
        Task t = tasks.get(id);
        if (t instanceof Epic) {
            Epic epic = (Epic) t;
            for (Object subId : epic.getSubtaskList()) {
                tasks.remove(subId);
            }
            tasks.remove(id);
            return true;
        }
        return false;
    }

    public void updateEpic(Epic newEpic, int oldId) {
        newEpic.setTaskID(oldId);
        recalcEpicStatus(newEpic);
        tasks.put(oldId, newEpic);
    }

    private void recalcEpicStatus(Epic epic) {
        HashMap<Integer, Subtask> subs = getAllSubtasksForEpic(epic.getTaskID());

        if (subs.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = true;
        boolean hasProgress = false;

        for (Subtask s : subs.values()) {
            if (s.getTaskStatus() != TaskStatus.DONE) allDone = false;
            if (s.getTaskStatus() == TaskStatus.IN_PROGRESS) hasProgress = true;
        }

        if (allDone) epic.setTaskStatus(TaskStatus.DONE);
        else if (hasProgress) epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        else epic.setTaskStatus(TaskStatus.NEW);
    }

    public HashMap<Integer, Subtask> getAllSubtasksForEpic(int epicId) {
        HashMap<Integer, Subtask> result = new HashMap<>();
        Task t = tasks.get(epicId);
        if (!(t instanceof Epic)) return result;

        Epic epic = (Epic) t;
        for (Object subId : epic.getSubtaskList()) {
            Task subTask = tasks.get(subId);
            if (subTask instanceof Subtask) {
                result.put((Integer) subId, (Subtask) subTask);
            }
        }
        return result;
    }

    // SUBTASK
    public int addSubtask(Subtask s) {
        s.setTaskID(counter);
        Task epicTask = tasks.get(s.getEpicID());
        if (epicTask instanceof Epic) {
            Epic epic = (Epic) epicTask;
            epic.getSubtaskList().add(counter);
            tasks.put(counter, s);
            recalcEpicStatus(epic);
            return counter++;
        }
        return -1;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        HashMap<Integer, Subtask> result = new HashMap<>();
        for (var entry : tasks.entrySet()) {
            if (entry.getValue() instanceof Subtask) {
                result.put(entry.getKey(), (Subtask) entry.getValue());
            }
        }
        return result;
    }

    public Subtask getSubtask(int id) {
        Task t = tasks.get(id);
        return (t instanceof Subtask) ? (Subtask) t : null;
    }

    public void deleteAllSubtasks() {
        tasks.entrySet().removeIf(entry -> entry.getValue() instanceof Subtask);
        for (var entry : tasks.entrySet()) {
            if (entry.getValue() instanceof Epic) {
                ((Epic) entry.getValue()).getSubtaskList().clear();
                recalcEpicStatus((Epic) entry.getValue());
            }
        }
    }

    public boolean deleteSubtask(int id) {
        Task t = tasks.get(id);
        if (t instanceof Subtask) {
            Subtask sub = (Subtask) t;
            int epicId = sub.getEpicID();
            Task epicTask = tasks.get(epicId);
            if (epicTask instanceof Epic) {
                Epic epic = (Epic) epicTask;
                epic.getSubtaskList().remove((Integer) id);
                recalcEpicStatus(epic);
            }
            tasks.remove(id);
            return true;
        }
        return false;
    }

    public void updateSubtask(Subtask newSub, int oldId) {
        newSub.setTaskID(oldId);
        tasks.put(oldId, newSub);
        Task epicTask = tasks.get(newSub.getEpicID());
        if (epicTask instanceof Epic) {
            recalcEpicStatus((Epic) epicTask);
        }
    }
}