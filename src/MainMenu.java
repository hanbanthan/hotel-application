import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;

import java.awt.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    final static AdminResource resource = new AdminResource();
    public static void mainMenu() {
        int menuOption = 0;
        while (true) {
            System.out.println("""
                    Main Menu
                    -----------------------------------------------
                    1. Find and reserve a room
                    2. See my reservations
                    3. Create an account
                    4. Admin (open the admin menu described below)
                    5. Exit (exit the application)
                    -----------------------------------------------
                    Please select a number for menu options
                    """);

            while (true) {
                Scanner scanner = new Scanner(System.in);
                try {
                    menuOption = scanner.nextInt();
                    if (1 <= menuOption && menuOption <= 5) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid number (1-5) for menu options");
                }
            }

            switch (menuOption) {
                case 1 -> findAndReserveARoom();
                case 2 -> seeMyReservation();
                case 3 -> createAccount();
                case 4 -> AdminMenu.adminMenu();
                case 5 -> {
                    System.out.println("Good bye.");
                    return;
                }
                default -> System.out.println("Please enter a valid number (1-5) for menu options");
            }
        }
    }

    private static void findAndReserveARoom() {
        String email = emailInput();

        Customer customer = resource.getCustomer(email);

        if (customer == null) {
            System.out.println("Customer does not exist. Please create an account");
            createAccount();
        } else {
            System.out.println("Please enter your check in date (yyyy-mm-dd):");
            Date checkIn;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String checkInDate = scanner.nextLine();
                checkIn = HotelResource.parseDate(checkInDate);
                if (checkIn != null) {
                    if (checkIn.after(new Date())) {
                        System.out.println("You entered check in date: " + checkIn);
                        break;
                    } else {
                        System.out.println("Please enter a valid check in date (yyyy-mm-dd) in the future");
                    }
                } else {
                    System.out.println("Please enter a valid date (yyyy-mm-dd)");
                }
            }

            System.out.println("Please enter you check out date (yyyy-mm-dd):");
            Date checkOut;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String checkOutDate = scanner.nextLine();
                checkOut = HotelResource.parseDate(checkOutDate);
                if (checkOut != null) {
                    if (checkOut.after(checkIn)) {
                        System.out.println("You entered a check out date: " + checkOut);
                        break;
                    } else {
                        System.out.println("Please enter a valid check out date (yyyy-mm-dd) in the future");
                    }
                } else {
                    System.out.println("Please enter a valid date (yyyy-mm-dd)");
                }
            }

            boolean findFreeRooms = false;
            System.out.println("Do you want to search for free rooms? (yes/no):");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    findFreeRooms = true;
                    break;
                } else if (input.equals("no")) {
                    findFreeRooms = false;
                    break;
                } else {
                    System.out.println("Please enter 'yes' or 'no'");
                }
            }

            boolean isRecommendRoom = false;
            int daysOut = 0;
            Collection<IRoom> rooms = resource.findARoom(checkIn, checkOut, findFreeRooms);
            if (!rooms.isEmpty()) {
                System.out.println("Found " + rooms.size() + " rooms:");
                for(IRoom room: rooms) {
                    System.out.println(room.toString());
                }
            } else {
                System.out.println("No rooms available");
                System.out.println("Would you like to search for recommendations N days later?");
                System.out.println("Enter number of days out (e.g., 3):");

                Scanner scanner =  new Scanner(System.in);
                while (true) {
                    try {
                        daysOut = Integer.parseInt(scanner.nextLine());
                        if (daysOut > 0) break;
                        else System.out.println("Please enter a positive number");
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                    }
                }


                rooms = resource.recommendRooms(checkIn, checkOut, daysOut);
                if (!rooms.isEmpty()) {
                    System.out.println("We recommend you to add your checkInDate and checkOutDate by " + daysOut + " days");
                    System.out.println("Check in date will be: " + addDays(checkIn, daysOut));
                    System.out.println("Check out date will be: " + addDays(checkOut, daysOut));
                    System.out.println("After changing the date, these rooms will be available:");
                    for (IRoom room: rooms) {
                        System.out.println(room.toString());
                    }
                    System.out.println("Do you want to change your check in and check out date (y/n)?");
                    while (true) {
                        scanner = new Scanner(System.in);
                        String answer = scanner.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            isRecommendRoom = true;
                            break;
                        } else if (answer.equalsIgnoreCase("n")) {
                            break;
                        } else {
                            System.out.println("Please enter y or n");
                        }
                    }
                } else {
                    System.out.println("No rooms to recommend");
                    return;
                }
            }

            if (isRecommendRoom) {
                checkIn = addDays(checkIn, daysOut);
                checkOut = addDays(checkOut, daysOut);
            }

            System.out.println("Please enter a room number:");
            String roomNumber = "";
            while (true) {
                Scanner scanner = new Scanner(System.in);
                roomNumber = scanner.nextLine();
                final String finalRoomNumber = roomNumber;
                if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(finalRoomNumber))) {
                    break;
                } else {
                    System.out.println("Please enter a valid room number");
                }
            }

            Reservation reservation = HotelResource.bookARoom(email, resource.getRoom(roomNumber), checkIn, checkOut);
            System.out.println("Your reservation is:\n" + reservation.toString());
        }
    }

    private static String emailInput() {
        System.out.println("Please enter your email address (e.g., name@domain.com):");
        String email = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            email = scanner.nextLine();
            if (email.matches("^(.+)@(.+).(.+)$")) {
                break;
            } else {
                System.out.println("Please enter a valid email address");
            }
        }
        return email;
    }

    private static Date addDays(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    private static void seeMyReservation() {
        String email = emailInput();
        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            System.out.println("Customer does not exist. Please create an account");
            createAccount();
        } else {
            Collection<Reservation> reservations = resource.getCustomersReservations(email);
            if (!reservations.isEmpty()) {
                System.out.println("Your reservations:");
                for (Reservation reservation: reservations) {
                    System.out.println(reservation.toString());
                }
            } else {
                System.out.println("No reservations");
            }
        }

    }

    private static void createAccount() {
        System.out.println("Please enter your first name");
        String firstName = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            firstName = scanner.nextLine();
            if (firstName.matches("^[a-zA-Z]+$")) {
                break;
            } else {
                System.out.println("Please enter a valid first name");
            }
        }

        System.out.println("Please enter your last name");
        String lastName = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            lastName = scanner.nextLine();
            if (lastName.matches("^[a-zA-z]+$")) {
                break;
            } else {
                System.out.println("Please enter a valid last name");
            }

        }

        String email = emailInput();

        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            resource.createACustomer(email, firstName, lastName);
            System.out.println("Customer created successfully");
        } else {
            System.out.println("Customer already exists");
        }

    }


}
