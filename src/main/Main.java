package main;

import system.*;
import users.*;
import exceptions.AccessDeniedException;
import exceptions.InvalidCredentialsException;
import exceptions.VisitorExpiredException;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static final String USER_FILE = "data/users.txt";
    private static final String LOG_FILE = "data/access_log.txt";
    private static final String ATTENDANCE_FILE = "data/attendance.txt";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AuthManager authManager = new AuthManager(USER_FILE);
        AccessManager accessManager = new AccessManager();
        LogManager logManager = new LogManager(LOG_FILE);
        FileHandler fileHandler = new FileHandler();

        authManager.loadUsersFromFile();

        System.out.println("=== ZEPHYR ControlVault ===");

        try {
            // LOGIN
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = authManager.login(username, password);
            logManager.info("Login successful for user: " + user);

            boolean running = true;

            while (running) {
                System.out.println("\nChoose Action:");
                System.out.println("1. SERVER_ROOM");
                System.out.println("2. LAB");
                System.out.println("3. OFFICE_FLOOR");
                System.out.println("4. LOBBY");

                if (user instanceof Employee) {
                    System.out.println("5. Check In");
                    System.out.println("6. Check Out");
                }

                System.out.println("0. Logout");
                System.out.print("Choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                try {
                    switch (choice) {

                        case 0:
                            running = false;
                            logManager.info("User logged out: " + user);
                            break;

                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            AccessZone zone = mapChoiceToZone(choice);
                            accessManager.processAccess(user, zone);
                            logManager.info(user + " accessed " + zone);
                            break;

                        case 5:
                            if (user instanceof Employee) {
                                Employee emp = (Employee) user;
                                String inRecord = emp.checkIn();
                                fileHandler.appendToFile(
                                        ATTENDANCE_FILE,
                                        LocalDateTime.now() + "," + inRecord
                                );
                                logManager.info("Employee checked in: " + emp);
                            } else {
                                System.out.println("Only employees can check in.");
                            }
                            break;

                        case 6:
                            if (user instanceof Employee) {
                                Employee emp = (Employee) user;
                                String outRecord = emp.checkOut();
                                fileHandler.appendToFile(
                                        ATTENDANCE_FILE,
                                        LocalDateTime.now() + "," + outRecord
                                );
                                logManager.info("Employee checked out: " + emp);
                            } else {
                                System.out.println("Only employees can check out.");
                            }
                            break;

                        default:
                            System.out.println("Invalid menu choice.");
                    }

                } catch (VisitorExpiredException e) {
                    logManager.warning(user + " denied access (badge expired)");
                    System.out.println("Access denied: Visitor badge expired.");

                } catch (AccessDeniedException e) {
                    logManager.warning(user + " denied access");
                    System.out.println("Access denied: " + e.getMessage());
                }
            }

        } catch (InvalidCredentialsException e) {
            logManager.error("Failed login attempt: " + e.getMessage());
            System.out.println("Login failed: " + e.getMessage());

        } catch (Exception e) {
            logManager.error("Unexpected system error: " + e.getMessage());
            System.out.println("Unexpected error occurred.");

        } finally {
            scanner.close();
        }
    }

    // Zones only. NO attendance here.
    private static AccessZone mapChoiceToZone(int choice) {
        switch (choice) {
            case 1: return AccessZone.SERVER_ROOM;
            case 2: return AccessZone.LAB;
            case 3: return AccessZone.OFFICE_FLOOR;
            case 4: return AccessZone.LOBBY;
            default:
                throw new IllegalArgumentException("Invalid access zone choice");
        }
    }
}
