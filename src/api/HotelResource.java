package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

public class HotelResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    private HotelResource() {
        this.customerService = CustomerService.getInstance();
        this.reservationService = ReservationService.getInstance();
    }

    private static class LazyHolder {
        static final HotelResource INSTANCE = new HotelResource();
    }

    public static HotelResource getInstance() {
        return LazyHolder.INSTANCE;
    }

    private <T> T performAction(Function<CustomerService, T> customerAction,
                                Function<ReservationService, T> reservationAction) {
        // You can add additional logic here if needed
        if (customerAction != null) {
            return customerAction.apply(customerService);
        } else {
            return reservationAction.apply(reservationService);
        }
    }

    public Customer getCustomer(String email) {
        return performAction(service -> service.getCustomer(email), null);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        performAction(service -> { service.addCustomer(email, firstName, lastName); return null; }, null);
    }

    public IRoom getRoom(String roomNumber) {
        return performAction(null, service -> service.getRoom(roomNumber));
    }

    public Reservation bookRoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("Customer does not exist with email: " + customerEmail);
            return null;
        }
        return performAction(null, service -> service.reserveARoom(customer, room, checkInDate, checkOutDate));
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("Customer does not exist with email: " + customerEmail);
            return Collections.emptyList();
        }
        return performAction(null, service -> service.getCustomersReservation(customer));
    }

    public Collection<IRoom> findRooms(Date checkIn, Date checkOut) {
        return performAction(null, service -> service.findRooms(checkIn, checkOut));
    }

    public Collection<IRoom> findAlternativeRooms(Date checkIn, Date checkOut) {
        return performAction(null, service -> service.findAlternativeRooms(checkIn, checkOut));
    }

    public Date addDefaultPlusDays(Date date) {
        return performAction(null, service -> service.addDefaultPlusDays(date));
    }
}
