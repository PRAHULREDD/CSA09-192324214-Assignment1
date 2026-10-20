import java.time.LocalDateTime;
import java.util.Objects;

/** Task 1: candidate domain objects and their responsibilities. */
public class Task01ProblemModel {
    static final class Vehicle {
        private final String registrationNumber;
        private final String type;
        Vehicle(String registrationNumber, String type) {
            if (registrationNumber == null || registrationNumber.isBlank()) throw new IllegalArgumentException("Registration is required");
            if (type == null || type.isBlank()) throw new IllegalArgumentException("Vehicle type is required");
            this.registrationNumber = registrationNumber;
            this.type = type;
        }
        String registrationNumber() { return registrationNumber; }
        String type() { return type; }
    }
    static final class ParkingTicket {
        private final long ticketNumber;
        private final Vehicle vehicle;
        private final LocalDateTime entryTime;
        ParkingTicket(long ticketNumber, Vehicle vehicle, LocalDateTime entryTime) {
            if (ticketNumber <= 0) throw new IllegalArgumentException("Ticket number must be positive");
            this.ticketNumber = ticketNumber;
            this.vehicle = Objects.requireNonNull(vehicle);
            this.entryTime = Objects.requireNonNull(entryTime);
        }
        long ticketNumber() { return ticketNumber; }
        Vehicle vehicle() { return vehicle; }
        LocalDateTime entryTime() { return entryTime; }
    }
    static final class ParkingSlot {
        private final int floor, slot;
        private Vehicle occupant;
        ParkingSlot(int floor, int slot) { this.floor=floor; this.slot=slot; }
        boolean isFree() { return occupant == null; }
        boolean park(Vehicle vehicle) { if (!isFree()) return false; occupant=Objects.requireNonNull(vehicle); return true; }
        Vehicle release() { Vehicle old=occupant; occupant=null; return old; }
        int floor() { return floor; }
        int slot() { return slot; }
    }
    public static void main(String[] args) {
        Vehicle v = new Vehicle("TN01AB1111", "CAR");
        ParkingTicket t = new ParkingTicket(1, v, LocalDateTime.now());
        ParkingSlot s = new ParkingSlot(0, 0);
        System.out.println("Vehicle=" + t.vehicle().registrationNumber() + ", ticket=" + t.ticketNumber());
        System.out.println("Slot allocated=" + s.park(v) + ", occupied=" + !s.isFree());
        System.out.println("Business rules: one occupant per slot; positive unique ticket numbers; valid registration.");
    }
}
