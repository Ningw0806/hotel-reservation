package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdminResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    private static class SingletonHolder {
        private static final AdminResource INSTANCE = new AdminResource();
    }

    private AdminResource() {
        this.customerService = CustomerService.getInstance();
        this.reservationService = ReservationService.getInstance();
    }

    public static AdminResource getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Optional<Customer> getCustomer(String email) {
        return Optional.ofNullable(customerService.getCustomer(email));
    }

    public void addRooms(List<IRoom> rooms) {
        rooms.stream().filter(Objects::nonNull).forEach(reservationService::addRoom);
    }

    public List<IRoom> getAllRooms() {
        return new ArrayList<>(reservationService.getAllRooms());
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerService.getAllCustomers());
    }

    public void displayAllReservations() {
        reservationService.getAllReservations().forEach(System.out::println);
    }
}
