package parking.service;
import parking.model.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
public class ParkingService {
    private final Map<String, ParkingTicket> active = new HashMap<>();
    private long nextTicket=1;
    public ParkingTicket enter(Vehicle vehicle) {
        if (active.containsKey(vehicle.getRegistrationNumber().toUpperCase(java.util.Locale.ROOT))) throw new IllegalArgumentException("Vehicle already parked");
        ParkingTicket ticket=new ParkingTicket(nextTicket++, vehicle, LocalDateTime.now());
        active.put(vehicle.getRegistrationNumber().toUpperCase(java.util.Locale.ROOT), ticket);
        return ticket;
    }
    public ParkingTicket exit(String registrationNumber) {
        if (registrationNumber == null) return null;
        return active.remove(registrationNumber.toUpperCase(java.util.Locale.ROOT));
    }
    public int activeCount() { return active.size(); }
}
