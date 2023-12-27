import model.IRoom;
import model.Room;
import model.RoomType;
import model.Customer;
import api.AdminResource;

import java.util.Collections;
import java.util.Scanner;
import java.util.Collection;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        boolean running = true;
        while (running) {
            showMenuOptions();
            String choice = scanner.nextLine();
            running = handleMenuSelection(choice);
        }
    }

    private static void showMenuOptions() {
        System.out.println("\nAdmin Menu Options:");
        System.out.println("1. View all Customers");
        System.out.println("2. View all Rooms");
        System.out.println("3. View all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Return to Main Menu");
        System.out.print("Please select an option: ");
    }

    private static boolean handleMenuSelection(String choice) {
        switch (choice) {
            case "1":
                displayAllCustomers();
                break;
            case "2":
                displayAllRooms();
                break;
            case "3":
                displayAllReservations();
                break;
            case "4":
                addRoom();
                break;
            case "5":
                return false;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
        return true;
    }

    private static void addRoom() {
        while (true) {
            System.out.println("Enter room number:");
            String roomNumber = scanner.nextLine();

            if (!roomNumber.matches("\\d+")) {
                System.out.println("Invalid room number. Room number must be numeric.");
                continue;
            }

            System.out.println("Enter price per night:");
            double roomPrice = enterRoomPrice();

            System.out.println("Enter room type (1 for single bed, 2 for double bed):");
            RoomType roomType = enterRoomType();

            try {
                Room room = new Room(roomNumber, roomPrice, roomType);
                adminResource.addRooms(Collections.singletonList(room));
                System.out.println("Room " + roomNumber + " added successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            System.out.println("Add another? (Y/N)");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                break;
            }
        }
    }

    private static double enterRoomPrice() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid double number for price:");
            return enterRoomPrice();
        }
    }

    private static RoomType enterRoomType() {
        String input = scanner.nextLine();
        if ("1".equals(input)) {
            return RoomType.SINGLE;
        } else if ("2".equals(input)) {
            return RoomType.DOUBLE;
        } else {
            System.out.println("Invalid input. Please enter 1 for single bed or 2 for double bed:");
            return enterRoomType();
        }
    }

    private static void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms are available.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void displayAllReservations() {
        adminResource.displayAllReservations();
    }
}
