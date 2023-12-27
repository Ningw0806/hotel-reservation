package model;

public class FreeRoom extends Room {
    private static final double FREE_ROOM_PRICE = 0.0;

    public FreeRoom(final String roomNumber, final RoomType roomType) {
        super(roomNumber, FREE_ROOM_PRICE, roomType);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("FreeRoom { Room Number: %s, Type: %s }", getRoomNumber(), getRoomType());
    }
}
