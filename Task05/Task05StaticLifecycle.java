import java.util.ArrayList;
import java.util.List;

/** Task 5: shared static counter, facility-specific occupancy, and object lifecycle. */
public class Task05StaticLifecycle {
    static class ParkingFacility {
        private static long nextTicketNumber = 1; // shared across all facilities in this JVM
        private final String name;
        private int occupiedSlots; // belongs to this facility only
        ParkingFacility(String name) { this.name=name; }
        long issueTicketNumber() { return nextTicketNumber++; }
        void park() { occupiedSlots++; }
        int occupancy() { return occupiedSlots; }
        String name() { return name; }
    }
    static class Ticket { final long id; Ticket(long id) { this.id=id; } }
    static Ticket createTicket(ParkingFacility facility) { return new Ticket(facility.issueTicketNumber()); }
    public static void main(String[] args) {
        ParkingFacility north = new ParkingFacility("North"), south = new ParkingFacility("South");
        north.park(); south.park(); south.park();
        Ticket t1=createTicket(north), t2=createTicket(south);
        System.out.println(north.name()+" occupancy="+north.occupancy()+", ticket="+t1.id);
        System.out.println(south.name()+" occupancy="+south.occupancy()+", ticket="+t2.id);
        List<Ticket> active = new ArrayList<>(); active.add(t1); active.add(t2);
        active.remove(t1); t1=null; // if no other references remain, object is eligible for GC
        System.out.println("Active tickets="+active.size()+"; next shared ID="+north.issueTicketNumber());
    }
}
