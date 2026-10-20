import java.time.LocalDateTime;
import java.util.Objects;

/** Task 4: constructors, this(), and copy construction. */
public class Task04Constructors {
    static class ParkingTicket {
        private final long ticketNumber;
        private final String registrationNumber;
        private final LocalDateTime entryTime;
        ParkingTicket() { this(1, "UNKNOWN", LocalDateTime.now()); }
        ParkingTicket(long ticketNumber, String registrationNumber) { this(ticketNumber, registrationNumber, LocalDateTime.now()); }
        ParkingTicket(long ticketNumber, String registrationNumber, LocalDateTime entryTime) {
            if (ticketNumber <= 0) throw new IllegalArgumentException("Ticket number must be positive");
            if (registrationNumber == null || registrationNumber.isBlank()) throw new IllegalArgumentException("Registration required");
            this.ticketNumber=ticketNumber; this.registrationNumber=registrationNumber; this.entryTime=Objects.requireNonNull(entryTime);
        }
        // A copy receives a new ticket number but copies vehicle and entry time for demonstration.
        ParkingTicket(ParkingTicket other, long newTicketNumber) { this(newTicketNumber, other.registrationNumber, other.entryTime); }
        @Override public String toString() { return "Ticket{" + ticketNumber + ", " + registrationNumber + ", " + entryTime + "}"; }
    }
    public static void main(String[] args) {
        ParkingTicket a = new ParkingTicket();
        ParkingTicket b = new ParkingTicket(2, "TN01AB1111");
        ParkingTicket c = new ParkingTicket(b, 3);
        System.out.println(a); System.out.println(b); System.out.println(c);
        System.out.println("Reference variables point to objects; objects are allocated on the heap.");
    }
}
