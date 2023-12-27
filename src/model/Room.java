package model;

import java.util.Optional;

public class Room implements IRoom {

    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(final String roomNumber, final Double price, final RoomType roomType) {
        this.roomNumber = Optional.ofNullable(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room number cannot be null"));
        this.price = Optional.ofNullable(price)
                .orElseThrow(() -> new IllegalArgumentException("Price cannot be null"));
        this.roomType = Optional.ofNullable(roomType)
                .orElseThrow(() -> new IllegalArgumentException("Room type cannot be null"));
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Double getRoomPrice() {
        return price;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isFree() {
        return Double.compare(price, 0.0) == 0;
    }

    @Override
    public String toString() {
        return String.format("Room Number: %s, Price: $%.2f, Type: %s", roomNumber, price, roomType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Room)) return false;
        Room other = (Room) obj;
        return roomNumber.equals(other.roomNumber);
    }

    @Override
    public int hashCode() {
        return roomNumber.hashCode();
    }
}
