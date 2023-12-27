# Hotel Reservation Application

Welcome to the Hotel Reservation Application, a Java-based console application for managing hotel reservations, inspire by the UDACITY course. This application allows users to find and reserve rooms, view their reservations, and create accounts. Additionally, it includes an admin panel for managing customers, rooms, and reservations.

## Features

- **Room Reservation**: Users can find and reserve rooms based on availability.
- **View Reservations**: Users can view their current reservations.
- **Account Creation**: New users can create an account for managing reservations.
- **Admin Panel**: Admin users can view all customers, rooms, and reservations, and add new rooms to the hotel.

## How to Run

To run the application, execute the `HotelApplication.java` file. This will launch the main menu of the application.

## Main Components

- `HotelApplication.java`: The entry point of the application.
- `MainMenu.java`: Handles the main menu and user interactions.
- `AdminMenu.java`: Manages the admin panel functionalities.
- `api/`: Contains `AdminResource.java` and `HotelResource.java` for handling business logic.
- `model/`: Includes classes like `Customer`, `Room`, `Reservation`, etc., representing different data models.
- `service/`: Contains `CustomerService.java` and `ReservationService.java` for service layer logic.

## How to Contribute

Contributions are welcome! Please feel free to fork the repository, make changes, and submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
