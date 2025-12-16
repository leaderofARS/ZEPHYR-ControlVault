package main;

import system.*;
import users.User;
import exceptions.AccessDeniedException;
import exceptions.InvalidCredentialsException;
import exceptions.VisitorExpiredException;

import java.util.Scanner;

public class Main {

    private static final String USER_FILE = "data/users.txt";
    private static final String LOG_FILE  = "data/access_log.txt";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Core managers
        AuthManager authManager = new AuthManager(USER_FILE);
        AccessManager accessManager = new AccessManager();
        LogManager logManager = new LogManager(LOG_FILE);

        // Load users
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

            // ACCESS MENU
            boolean running = true;
            while (running) {
                System.out.println("\nChoose Access Zone:");
                System.out.println("1. SERVER_ROOM");
                System.out.println("2. LAB");
                System.out.println("3. OFFICE_FLOOR");
                System.out.println("4. LOBBY");
                System.out.println("0. Logout");

                System.out.print("Choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 0) {
                    running = false;
                    logManager.info("User logged out: " + user);
                    break;
                }

                AccessZone zone = mapChoiceToZone(choice);

                try {
                    accessManager.processAccess(user, zone);
                    logManager.info(user + " accessed " + zone);

                } catch (VisitorExpiredException e) {
                    logManager.warning(user + " denied access (badge expired)");
                    System.out.println("Access denied: Visitor badge expired.");

                } catch (AccessDeniedException e) {
                    logManager.warning(user + " denied access to " + zone);
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

    // Helper: menu → AccessZone
    private static AccessZone mapChoiceToZone(int choice) {
        switch (choice) {
            case 1: return AccessZone.SERVER_ROOM;
            case 2: return AccessZone.LAB;
            case 3: return AccessZone.OFFICE_FLOOR;
            case 4: return AccessZone.LOBBY;
            default:
                throw new IllegalArgumentException("Invalid menu choice");
        }
    }
}
