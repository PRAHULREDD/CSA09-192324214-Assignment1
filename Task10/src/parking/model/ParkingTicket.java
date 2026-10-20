package parking.model;
import java.time.LocalDateTime;
public class ParkingTicket {
    private final long ticketNumber;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    public ParkingTicket(long ticketNumber, Vehicle vehicle, LocalDateTime entryTime) {
        if (ticketNumber <= 0) throw new IllegalArgumentException("Ticket number must be positive");
        if (vehicle == null || entryTime == null) throw new IllegalArgumentException("Vehicle and entry time required");
        this.ticketNumber=ticketNumber; this.vehicle=vehicle; this.entryTime=entryTime;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
