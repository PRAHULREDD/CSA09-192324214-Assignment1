package parking.app;
import parking.model.Vehicle;
import parking.model.ParkingTicket;
import parking.service.ParkingService;
public class Main {
    public static void main(String[] args) {
        ParkingService service=new ParkingService();
        ParkingTicket t=service.enter(new Vehicle("TN01AB1111", "CAR"));
        System.out.println("Ticket="+t.getTicketNumber()+", vehicle="+t.getVehicle().getRegistrationNumber());
        System.out.println("Exited="+(service.exit("TN01AB1111") != null)+", active="+service.activeCount());
    }
}
