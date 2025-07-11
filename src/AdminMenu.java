import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.rmi.server.ExportException;
import java.util.*;

public class AdminMenu {
    public static  void adminMenu() {
        int menuOption = 0;
        while (true) {
            System.out.println("""
                    Main Menu
                    -----------------------------------------------
                    1. See all Customers
                    2. See all Rooms
                    3. See all Reservations
                    4. Add a Room
                    5. Populate system with test data
                    6. Back to Main Menu (returns back to the MainMenu)
                    -----------------------------------------------
                    Please select a number for menu options
                    """);

            while (true) {
                Scanner scanner = new Scanner(System.in);
                try {
                    menuOption = scanner.nextInt();
                    if (1 <= menuOption && menuOption <= 6) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid number (1-6) for menu options");
                }
            }

            switch (menuOption) {
                case 1 -> AdminResource.displayAllCustomers();
                case 2 -> AdminResource.displayAllRooms();
                case 3 -> AdminResource.displayAllReservations();
                case 4 -> AdminResource.addRoom(inputRooms());
                case 5 -> populateTestData();
                case 6 -> {
                    return;
                }
            }
        }
    }

    public static List<IRoom> inputRooms() {
        List<IRoom> rooms = new ArrayList<IRoom>();
        int numRooms = 0;
        while (true) {
            System.out.println("Please enter the number of rooms you want to add:");
            Scanner scanner = new Scanner(System.in);
            try {
                numRooms = scanner.nextInt();
                if (numRooms > 0) {
                    break;
                } else if (numRooms == 0) {
                    return  rooms;
                } else {
                    System.out.println("Please enter a valid number of rooms");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number of rooms");
            }
        }

        for (int i=0; i<numRooms; i++) {
            System.out.println("Enter room " + (i+1) + " of " + numRooms + ":");

            System.out.println("Please enter the room number:");
            Scanner scannerRoomNumber = new Scanner(System.in);
            String roomNumber = scannerRoomNumber.nextLine();

            RoomType roomType;
            while (true) {
                System.out.println("Please enter the room type (single, double):");
                Scanner scannerRoomType = new Scanner(System.in);
                String roomTypeString = scannerRoomType.nextLine();

                if (roomTypeString.equals("single")) {
                    roomType = RoomType.SINGLE;
                    break;
                } else if (roomTypeString.equals("double")) {
                    roomType = RoomType.DOUBLE;
                    break;
                } else {
                    System.out.println("Please enter a valid room type (single, double)");
                }
            }

            double roomPrice = 0.0;
            while (true) {
                System.out.println("Please enter the room price:");
                Scanner scanner = new Scanner(System.in);
                try {
                    roomPrice = scanner.nextDouble();
                    if (roomPrice >= 0) {
                        break;
                    } else {
                        System.out.println("Please enter a valid room price");
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid room price");
                }
            }

            Room room = new Room(roomNumber, roomPrice, roomType);
            rooms.add(room);
        }

        return rooms;
    }

    public static void populateTestData() {
        List<IRoom> rooms = new ArrayList<>();
        rooms.add(new Room("1", 0.0, RoomType.SINGLE));
        rooms.add(new Room("2", 100.0, RoomType.DOUBLE));
        rooms.add(new Room("3", 0.0, RoomType.SINGLE));

        AdminResource.addRoom(rooms);

        AdminResource.customerService.addCustomer("alice@gmail.com", "alice", "alice");
        AdminResource.customerService.addCustomer("bob@gmail.com", "bob", "bob");

        Customer alice = AdminResource.customerService.getCustomer("alice@gmail.com");
        Customer bob = AdminResource.customerService.getCustomer("bob@gmail.com");

        IRoom room1 = AdminResource.reservationService.getARoom("1");
        IRoom room2 = AdminResource.reservationService.getARoom("2");

        Date checkIn = HotelResource.parseDate("2025-08-08");
        Date checkOut = HotelResource.parseDate("2025-08-09");

        HotelResource.bookARoom(alice.getEmail(), room1, checkIn, checkOut);
        HotelResource.bookARoom(bob.getEmail(), room2, checkIn, checkOut);
    }
}


