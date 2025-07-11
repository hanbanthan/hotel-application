package model;

public class Room implements IRoom {
    private final String roomNumber;
    private Double price;
    private final RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.enumeration;
    }

    @Override
    public boolean isFree() {
        return this.price == 0.0;
    }

    public void setRoomPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room number: " + roomNumber + "\nPrice: " + price + "\nRoom type: " + enumeration;
    }
}
