import model.IRoom;
import model.Reservation;
import api.HotelResource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

    public static void mainMenu() {
        boolean continueRunning = true;
        while (continueRunning) {
            displayMainMenu();
            String selection = scanner.nextLine();
            continueRunning = processMenuSelection(selection);
        }
    }

    private static void displayMainMenu() {
        System.out.println("""
                Welcome to the Hotel Reservation Application
                --------------------------------------------
                1. Find and reserve a room
                2. See my reservations
                3. Create an account
                4. Admin
                5. Exit
                --------------------------------------------
                Select an option (1-5):""");
    }

    private static boolean processMenuSelection(String selection) {
        return switch (selection) {
            case "1" -> {
                findAndReserveRoom();
                yield true;
            }
            case "2" -> {
                seeMyReservation();
                yield true;
            }
            case "3" -> {
                createAccount();
                yield true;
            }
            case "4" -> {
                AdminMenu.adminMenu();
                yield true;
            }
            case "5" -> false;
            default -> {
                System.out.println("Invalid selection, please try again.");
                yield true;
            }
        };
    }

    private static void findAndReserveRoom() {
        System.out.println("Enter Check-In Date mm/dd/yyyy example 02/01/2020");
        Date checkInDate = getInputDate();
        System.out.println("Enter Check-Out Date mm/dd/yyyy example 02/21/2020");
        Date checkOutDate = getInputDate();

        Collection<IRoom> availableRooms = hotelResource.findRooms(checkInDate, checkOutDate);
        if (availableRooms.isEmpty()) {
            handleNoRoomsFound(checkInDate, checkOutDate);
        } else {
            handleRoomSelection(checkInDate, checkOutDate, availableRooms);
        }
    }

    private static Date getInputDate() {
        while (true) {
            try {
                return dateFormatter.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use mm/dd/yyyy.");
            }
        }
    }

    private static void handleNoRoomsFound(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkInDate, checkOutDate);
        if (alternativeRooms.isEmpty()) {
            System.out.println("No rooms found on alternative dates either.");
        } else {
            Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkInDate);
            Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOutDate);
            System.out.println("Alternative rooms found:");
            alternativeRooms.forEach(System.out::println);
            reserveRoom(scanner, alternativeCheckIn, alternativeCheckOut, alternativeRooms);
        }
    }

    private static void reserveRoom(Scanner scanner, Date checkInDate, Date checkOutDate, Collection<IRoom> availableRooms) {
        System.out.println("Would you like to book a room? (y/n)");
        String bookRoom = scanner.nextLine();

        if ("y".equalsIgnoreCase(bookRoom)) {
            System.out.println("Do you have an account with us? (y/n)");
            String haveAccount = scanner.nextLine();

            if ("y".equalsIgnoreCase(haveAccount)) {
                System.out.println("Enter Email format: name@domain.com");
                String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("Customer not found. You need to create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    String roomNumber = scanner.nextLine();

                    IRoom roomToBook = availableRooms.stream()
                            .filter(room -> room.getRoomNumber().equals(roomNumber))
                            .findFirst()
                            .orElse(null);

                    if (roomToBook != null) {
                        Reservation reservation = hotelResource.bookRoom(customerEmail, roomToBook, checkInDate, checkOutDate);
                        System.out.println("Reservation created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room number not available. Start reservation again.");
                    }
                }
            } else {
                System.out.println("You need to create an account to book a room.");
            }
        }
    }

    private static void handleRoomSelection(Date checkInDate, Date checkOutDate, Collection<IRoom> availableRooms) {
        System.out.println("Available rooms:");
        availableRooms.forEach(System.out::println);
        reserveRoom(scanner, checkInDate, checkOutDate, availableRooms);
    }

    private static void seeMyReservation() {
        System.out.println("Enter your Email format: name@domain.com");
        String customerEmail = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(customerEmail);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void createAccount() {
        System.out.println("Enter Email format: name@domain.com");
        String email = scanner.nextLine();
        System.out.println("First Name:");
        String firstName = scanner.nextLine();
        System.out.println("Last Name:");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createCustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error creating account: " + ex.getMessage());
        }
    }
}
