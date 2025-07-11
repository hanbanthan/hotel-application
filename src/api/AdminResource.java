package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource extends HotelResource {
    public static CustomerService customerService = CustomerService.getInstance();
    public static ReservationService reservationService = ReservationService.getInstance();

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public static void addRoom(List<IRoom> rooms) {
        for (IRoom room: rooms) {
            reservationService.addRoom(room);
        }
    }

    public static Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public static Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public static void displayAllReservations() {
        reservationService.printAllReservation();
    }

    public static void displayAllCustomers() {
        if (getAllCustomers().isEmpty()) {
            System.out.println("No customers found");
        } else {
            System.out.println("Customers:");
            for (Customer customer: getAllCustomers()) {
                System.out.println(customer.toString());
            }
        }
    }

    public static void displayAllRooms() {
        if (getAllRooms().isEmpty()) {
            System.out.println("No rooms found");
        } else {
            System.out.println("Rooms: ");
            for (IRoom room: getAllRooms()) {
                System.out.println(room.toString());
            }
        }
    }

}
