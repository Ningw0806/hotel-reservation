package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReservationService {

    private static final int RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS = 7;

    public Date addDefaultPlusDays(final Date date) {
        return addDaysToDate(date, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS);
    }


    private static class ReservationServiceHolder {
        private static final ReservationService INSTANCE = new ReservationService();
    }

    private final Map<String, IRoom> roomMap;
    private final Map<Customer, Set<Reservation>> customerReservationsMap;

    private ReservationService() {
        this.roomMap = new TreeMap<>();
        this.customerReservationsMap = new HashMap<>();
    }

    public static ReservationService getInstance() {
        return ReservationServiceHolder.INSTANCE;
    }

    public void addRoom(final IRoom room) {
        String roomNumber = room.getRoomNumber();
        if (!roomNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Room number must be numeric.");
        }
        if (roomMap.containsKey(roomNumber)) {
            throw new IllegalArgumentException("Room number " + roomNumber + " already exists.");
        }
        roomMap.put(roomNumber, room);
    }


    public IRoom getRoom(final String roomNumber) {
        return roomMap.get(roomNumber);
    }

    public Collection<IRoom> getAllRooms() {
        return roomMap.values();
    }

    public Reservation reserveARoom(final Customer customer, final IRoom room,
                                    final Date checkInDate, final Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        customerReservationsMap.computeIfAbsent(customer, k -> new HashSet<>()).add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(final Date checkInDate, final Date checkOutDate) {
        return findAvailableRooms(checkInDate, checkOutDate);
    }

    public Collection<IRoom> findAlternativeRooms(final Date checkInDate, final Date checkOutDate) {
        return findAvailableRooms(addDaysToDate(checkInDate, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS),
                addDaysToDate(checkOutDate, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS));
    }

    private Collection<IRoom> findAvailableRooms(final Date checkInDate, final Date checkOutDate) {
        Set<String> bookedRoomNumbers = getAllReservations().stream()
                .filter(reservation -> reservationOverlaps(reservation, checkInDate, checkOutDate))
                .map(reservation -> reservation.getRoom().getRoomNumber())
                .collect(Collectors.toSet());

        return roomMap.values().stream()
                .filter(room -> !bookedRoomNumbers.contains(room.getRoomNumber()))
                .collect(Collectors.toList());
    }

    private Date addDaysToDate(final Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private boolean reservationOverlaps(final Reservation reservation, final Date checkInDate, final Date checkOutDate) {
        return !checkInDate.after(reservation.getCheckOutDate()) && !checkOutDate.before(reservation.getCheckInDate());
    }

    public Collection<Reservation> getCustomersReservation(final Customer customer) {
        return customerReservationsMap.getOrDefault(customer, Collections.emptySet());
    }

    public void printAllReservations() {
        getAllReservations().forEach(System.out::println);
    }

    public Collection<Reservation> getAllReservations() {
        return customerReservationsMap.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
