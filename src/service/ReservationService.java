package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    // Static variable reference of instance
    // of type ReservationService
    private static ReservationService instance = null;

    // Declaring a variable of type
    public Map<String, IRoom> rooms;
    public List<Reservation> reservations;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private ReservationService() {
        rooms = new HashMap<>();
        reservations = new ArrayList<>();
    }

    // Static method
    // Static method to create instance of CustomerService class
    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            return rooms.get(roomId);
        }
        return null;
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, boolean findFreeRooms) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room: rooms.values()) {
            if ((findFreeRooms && room.isFree()) || (!findFreeRooms && !room.isFree()) ) {
                if (isAvailable(room, checkInDate, checkOutDate)) {
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }

    private boolean isAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        for(Reservation reservation: reservations) {
            if(reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                if (isOverLap(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkInDate, checkOutDate)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOverLap(Date start1, Date end1, Date start2, Date end2) {
        return !(start1.after(end2) || end1.before(start2));
    }

    public Collection<IRoom> findRecommendRooms(Date checkInDate, Date checkOutDate, int num) {
        Collection<IRoom> recommendRooms = new ArrayList<>();
        for (IRoom room: rooms.values()) {
            if (isAvailable(room, addDay(checkInDate, num), addDay(checkOutDate, num))) {
                recommendRooms.add(room);
            }
        }
        return recommendRooms;
    }

    Date addDay(Date date,int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> customerReservations = new ArrayList<>();
        for(Reservation reservation: reservations) {
            if( reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        if(reservations.isEmpty()) {
            System.out.println("No reservations.");
        } else {
            for(Reservation reservation: reservations) {
                System.out.println(reservation.toString());
            }
        }
    }
}
