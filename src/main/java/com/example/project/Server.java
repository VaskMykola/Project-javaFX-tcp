package com.example.project;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO LIST:
// TODO 1. multi thread exit (or patitial exit for all threads)
// TODO 2. add client part (sending receiving processing data)
// TODO 3. something else?

public class Server {
    boolean isRunning = true;
    private ServerSocket serverSocket;
    private final HashMap<String, HashMap<String, HashSet<ModuleSchedule>>> classSchedules = new HashMap<>();
    private final HashMap<String, Set<ModuleInfo>> moduleIndex = new HashMap<>();

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(1234); // Initialize serverSocket without try-with-resources
            new Thread(MenuBuilder::displayMenu).start();

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close(); // This will break the server loop by throwing a SocketException
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, HashMap<String, HashSet<ModuleSchedule>>> getClassSchedules() {
        return classSchedules;
    }

    public void addClass(String classId, ModuleSchedule schedule) {
        if (classId == null || schedule == null) return;

        classSchedules.putIfAbsent(classId, new HashMap<>());
        classSchedules.get(classId).putIfAbsent(schedule.getDay(), new HashSet<>());
        classSchedules.get(classId).get(schedule.getDay()).add(schedule);

        moduleIndex.putIfAbsent(schedule.getModuleName(), new HashSet<>());
        moduleIndex.get(schedule.getModuleName()).add(new ModuleInfo(classId, schedule.getDay()));
    }

    public void removeClass(String classId, String moduleName) {
        if (classId == null || moduleName == null) return;

        classSchedules.getOrDefault(classId, new HashMap<>()).values().forEach(schedules -> schedules.removeIf(schedule -> schedule.getModuleName().equals(moduleName)));

        Set<ModuleInfo> infos = moduleIndex.get(moduleName);
        if (infos != null) {
            infos.removeIf(info -> info.classId().equals(classId));
        }
    }

    public void displayAllClasses() {
        if (classSchedules.isEmpty()) {
            System.out.println("No classes have been added yet.");
        } else {
            System.out.println("All saved classes:");
            classSchedules.keySet().forEach(System.out::println);
        }
    }

    public void displayModulesForClass(String classId) {
        HashMap<String, HashSet<ModuleSchedule>> schedule = classSchedules.get(classId);
        if (schedule == null) {
            System.out.println("No class found with ID: " + classId);
        } else {
            System.out.println("Modules for class " + classId + ":");
            schedule.values().forEach(modules -> modules.forEach(module -> System.out.println(module.getModuleName())));
        }
    }

    public void displayScheduleForDay(String classId, String day) {
        Set<ModuleSchedule> schedules = classSchedules.getOrDefault(classId, new HashMap<>()).get(day);
        if (schedules != null && !schedules.isEmpty()) {
            System.out.println("Schedule for " + classId + " on " + day + ":");
            schedules.forEach(System.out::println);
        } else {
            System.out.println("No schedule found for " + classId + " on " + day);
        }
    }

    public void displayScheduleForModule(String moduleName) {
        Set<ModuleInfo> infos = moduleIndex.get(moduleName);
        if (infos != null && !infos.isEmpty()) {
            System.out.println("Schedule for module " + moduleName + ":");
            infos.forEach(info -> {
                System.out.println("Class ID: " + info.classId() + ", Day: " + info.day());
                classSchedules.get(info.classId()).get(info.day()).stream()
                        .filter(schedule -> schedule.getModuleName().equals(moduleName))
                        .forEach(System.out::println);
            });
        } else {
            System.out.println("No schedule found for module " + moduleName);
        }
    }

    public void addClassFromMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Class ID: ");
        String classId = scanner.nextLine();
        System.out.print("Enter Module Name: ");
        String moduleName = scanner.nextLine();
        System.out.print("Enter Day: ");
        String day = scanner.nextLine();
        System.out.print("Enter Time: ");
        String time = scanner.nextLine();
        System.out.print("Enter Room: ");
        String room = scanner.nextLine();

        addClass(classId, new ModuleSchedule(moduleName, day, time, room));
        System.out.println("Class added successfully.");
    }

    public void removeClassFromMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Class ID to remove: ");
        String classId = scanner.nextLine();
        System.out.print("Enter Module Name to remove: ");
        String moduleName = scanner.nextLine();

        removeClass(classId, moduleName);
        System.out.println("Class removed successfully.");
    }

    public void displayScheduleForDayFromMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Class ID to display schedule for a day: ");
        String classId = scanner.nextLine();
        System.out.print("Enter Day: ");
        String day = scanner.nextLine();

        displayScheduleForDay(classId, day);
    }

    public void displayScheduleForModuleFromMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Module Name to display schedule: ");
        String moduleName = scanner.nextLine();

        displayScheduleForModule(moduleName);
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private Server server;

        public ClientHandler(Socket socket, Server server) {
            this.clientSocket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // TODO PASTE CODE FOR processing client requests HERE
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ModuleSchedule {
    private final String moduleName;
    private final String day;
    private final String time;
    private final String room;

    ModuleSchedule(String moduleName, String day, String time, String room) {
        this.moduleName = moduleName;
        this.day = day;
        this.time = time;
        this.room = room;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", moduleName, day, time, room);
    }

    @Override
    public int hashCode() {
        int result = moduleName.hashCode();
        result = 31 * result + day.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + room.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleSchedule that = (ModuleSchedule) o;
        return moduleName.equals(that.moduleName) && day.equals(that.day) && time.equals(that.time) && room.equals(that.room);
    }
}

record ModuleInfo(String classId, String day) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleInfo that = (ModuleInfo) o;
        return classId.equals(that.classId) && day.equals(that.day);
    }
}

class Menu {
    List<MenuItem> items = new ArrayList<>();
    String title;

    public Menu(String title) {
        this.title = title;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void display() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(title);
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i).label);
            }
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            boolean validInput = false;
            int choice = -1;
            while (!validInput) {
                try {
                    String userInput = scanner.nextLine();
                    choice = Integer.parseInt(userInput);
                    validInput = true;
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: an integer is required. Try again.");
                }
            }
            if (choice == 0) {
                break;
            } else if (choice > 0 && choice <= items.size()) {
                MenuItem selectedItem = items.get(choice - 1);
                if (selectedItem.subMenu != null) {
                    selectedItem.subMenu.display();
                } else {
                    selectedItem.func_to_run.run();
                }
            } else {
                System.out.println("Invalid option. Please select a number between 0 and " + items.size() + ".");
            }
        }
    }
}

class MenuItem {
    String label;
    Runnable func_to_run;
    Menu subMenu;

    public MenuItem(String label, Runnable func_to_run) {
        this.label = label;
        this.func_to_run = func_to_run;
    }

    public MenuItem(String label, Menu subMenu) {
        this.label = label;
        this.subMenu = subMenu;
    }
}

class MenuBuilder {
    private static final Server server = new Server();

    public static void buildMenu() {
        Menu serverMenu = new Menu("Server Menu");
        serverMenu.addItem(new MenuItem("Show All Classes", server::displayAllClasses));
        serverMenu.addItem(new MenuItem("Add Class", server::addClassFromMenu));
        serverMenu.addItem(new MenuItem("Remove Class", server::removeClassFromMenu));
        serverMenu.addItem(new MenuItem("Display Schedule for Day", server::displayScheduleForDayFromMenu));
        serverMenu.addItem(new MenuItem("Display Schedule for Module", server::displayScheduleForModuleFromMenu));
        serverMenu.addItem(new MenuItem("Show Modules for a Class", MenuBuilder::buildClassesSubMenu));
        serverMenu.display();
    }

    private static void buildClassesSubMenu() {
        Menu classesMenu = new Menu("Select a Class");
        server.getClassSchedules().keySet()
                .forEach(classId -> classesMenu
                        .addItem(new MenuItem(classId, () ->
                                server.displayModulesForClass(classId))));
        classesMenu.display();
    }

    private void showExitConfirmation() { // TODO 1
        Menu exitMenu = new Menu("You are trying to quit the app completely. Are you sure?");
        exitMenu.addItem(new MenuItem("Yes", server::stopServer));
    }


    public static void displayMenu() {
        buildMenu();
    }
}
