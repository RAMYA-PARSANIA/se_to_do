import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Task Manager CLI Application
 * A command-line application for managing daily tasks efficiently.
 */
public class TaskManager {
    private String storageFile;
    private List<Task> taskList;
    private Gson gson;
    private Scanner scanner;

    public TaskManager(String storageFile) {
        this.storageFile = storageFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.taskList = retrieveTasks();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Task class to represent individual tasks
     */
    static class Task {
        private int taskId;
        private String description;
        private boolean isDone;
        private String timestamp;

        public Task(int taskId, String description, boolean isDone, String timestamp) {
            this.taskId = taskId;
            this.description = description;
            this.isDone = isDone;
            this.timestamp = timestamp;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public String getDescription() {
            return description;
        }

        public boolean isDone() {
            return isDone;
        }

        public void setDone(boolean done) {
            isDone = done;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    /**
     * Retrieve tasks from storage file
     */
    private List<Task> retrieveTasks() {
        File file = new File(storageFile);
        if (file.exists()) {
            try (Reader reader = new FileReader(storageFile)) {
                Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
                List<Task> tasks = gson.fromJson(reader, taskListType);
                return tasks != null ? tasks : new ArrayList<>();
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Persist tasks to storage file
     */
    private void persistTasks() {
        try (Writer writer = new FileWriter(storageFile)) {
            gson.toJson(taskList, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Create a new task and add it to the list
     */
    public boolean createTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        
        Task newTask = new Task(
            taskList.size() + 1,
            description.trim(),
            false,
            timestamp
        );
        
        taskList.add(newTask);
        persistTasks();
        return true;
    }

    /**
     * Display all tasks in the list
     */
    public String displayAllTasks() {
        if (taskList.isEmpty()) {
            return "Your task list is empty.";
        }

        StringBuilder result = new StringBuilder();
        result.append("\n").append("-".repeat(60)).append("\n");
        result.append("TASK MANAGER - ALL TASKS\n");
        result.append("-".repeat(60)).append("\n");

        for (Task item : taskList) {
            String marker = item.isDone() ? "✔" : "○";
            result.append(String.format("%d. [%s] %s%n", item.getTaskId(), marker, item.getDescription()));
            result.append(String.format("   Added on: %s%n", item.getTimestamp()));
        }

        result.append("-".repeat(60)).append("\n");
        return result.toString();
    }

    /**
     * Mark a specific task as completed
     */
    public boolean markAsDone(int taskId) {
        for (Task item : taskList) {
            if (item.getTaskId() == taskId) {
                item.setDone(true);
                persistTasks();
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a task from the list
     */
    public boolean removeTask(int taskId) {
        int originalCount = taskList.size();
        taskList.removeIf(item -> item.getTaskId() == taskId);

        if (taskList.size() < originalCount) {
            // Reassign IDs to maintain sequence
            for (int i = 0; i < taskList.size(); i++) {
                taskList.get(i).setTaskId(i + 1);
            }
            persistTasks();
            return true;
        }
        return false;
    }

    /**
     * Remove all tasks from the list
     */
    public boolean removeAllTasks() {
        taskList.clear();
        persistTasks();
        return true;
    }

    /**
     * Show the main menu options
     */
    private void showMenu() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("TASK MANAGER - MAIN MENU");
        System.out.println("-".repeat(60));
        System.out.println("1. Create New Task");
        System.out.println("2. Show All Tasks");
        System.out.println("3. Mark Task as Done");
        System.out.println("4. Remove Task");
        System.out.println("5. Remove All Tasks");
        System.out.println("6. Quit");
        System.out.println("-".repeat(60));
    }

    /**
     * Run the Task Manager application
     */
    public void runApplication() {
        while (true) {
            showMenu();
            System.out.print("\nSelect an option (1-6): ");
            String userInput = scanner.nextLine().trim();

            switch (userInput) {
                case "1":
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine().trim();
                    if (createTask(description)) {
                        System.out.println("✔ Task created successfully!");
                    } else {
                        System.out.println("✘ Task creation failed. Description cannot be empty.");
                    }
                    break;

                case "2":
                    System.out.println(displayAllTasks());
                    break;

                case "3":
                    try {
                        System.out.print("Enter task ID to mark as done: ");
                        int taskId = Integer.parseInt(scanner.nextLine().trim());
                        if (markAsDone(taskId)) {
                            System.out.println("✔ Task #" + taskId + " marked as done!");
                        } else {
                            System.out.println("✘ Task #" + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("✘ Invalid input. Please enter a valid number.");
                    }
                    break;

                case "4":
                    try {
                        System.out.print("Enter task ID to remove: ");
                        int taskId = Integer.parseInt(scanner.nextLine().trim());
                        if (removeTask(taskId)) {
                            System.out.println("✔ Task #" + taskId + " removed successfully!");
                        } else {
                            System.out.println("✘ Task #" + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("✘ Invalid input. Please enter a valid number.");
                    }
                    break;

                case "5":
                    System.out.print("Are you sure you want to remove all tasks? (yes/no): ");
                    String confirmation = scanner.nextLine().toLowerCase();
                    if (confirmation.equals("yes")) {
                        removeAllTasks();
                        System.out.println("✔ All tasks have been removed!");
                    } else {
                        System.out.println("Action cancelled.");
                    }
                    break;

                case "6":
                    System.out.println("\nThank you for using Task Manager!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("✘ Invalid option. Please select a number between 1-6.");
            }
        }
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager("tasks.json");
        manager.runApplication();
    }
}
