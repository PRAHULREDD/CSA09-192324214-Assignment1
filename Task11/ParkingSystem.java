import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/** Task 11: compact integrated system with validation and runnable test cases. */
public class ParkingSystem {
    interface Pass { boolean isValidAt(LocalDateTime time); }
    static class MonthlyPass implements Pass {
        private final LocalDateTime validUntil;
        MonthlyPass(LocalDateTime validUntil) { this.validUntil=Objects.requireNonNull(validUntil); }
        public boolean isValidAt(LocalDateTime time) { return !time.isAfter(validUntil); }
    }
    static abstract class Vehicle {
        private final String regNo;
        private final Pass pass;
        Vehicle(String regNo, Pass pass) { if(regNo==null||regNo.isBlank()) throw new IllegalArgumentException("Registration required"); this.regNo=regNo.toUpperCase(Locale.ROOT); this.pass=pass; }
        String regNo() { return regNo; }
        boolean hasFreePass(LocalDateTime time) { return pass!=null && pass.isValidAt(time); }
        abstract double ratePerHour();
        double fee(int minutes, LocalDateTime exit) { if(minutes<0) throw new IllegalArgumentException("Negative duration"); if(hasFreePass(exit)||minutes==0)return 0; return ((minutes+59)/60)*ratePerHour(); }
    }
    static class Bike extends Vehicle { Bike(String r,Pass p){super(r,p);} double ratePerHour(){return 10;} }
    static class Car extends Vehicle { Car(String r,Pass p){super(r,p);} double ratePerHour(){return 30;} }
    static class ElectricCar extends Car { ElectricCar(String r,Pass p){super(r,p);} double fee(int minutes,LocalDateTime exit){return super.fee(minutes,exit);} double chargingFee(int kWh){if(kWh<0)throw new IllegalArgumentException();return kWh*12.0;} }
    static class Ticket { final long id; final Vehicle vehicle; final LocalDateTime entry; final int floor,slot; Ticket(long id,Vehicle v,LocalDateTime e,int f,int s){this.id=id;vehicle=v;entry=e;floor=f;slot=s;} }
    static class Facility {
        private final Vehicle[][] slots;
        private final Map<String,Ticket> active=new HashMap<>();
        private long nextTicket=1;
        Facility(int[] capacity){slots=new Vehicle[capacity.length][];for(int f=0;f<capacity.length;f++){if(capacity[f]<0)throw new IllegalArgumentException();slots[f]=new Vehicle[capacity[f]];}}
        Ticket enter(Vehicle v,LocalDateTime now){String key=v.regNo();if(active.containsKey(key))throw new IllegalArgumentException("Duplicate registration");for(int f=0;f<slots.length;f++)for(int s=0;s<slots[f].length;s++)if(slots[f][s]==null){slots[f][s]=v;Ticket t=new Ticket(nextTicket++,v,now,f,s);active.put(key,t);return t;}return null;}
        double exit(String reg,LocalDateTime now){Ticket t=active.remove(reg.toUpperCase(Locale.ROOT));if(t==null)throw new IllegalArgumentException("No active ticket");slots[t.floor][t.slot]=null;long mins=Math.max(0,Duration.between(t.entry,now).toMinutes());return t.vehicle.fee((int)mins,now);}
        boolean releaseInvalidSlot(int f,int s){if(f<0||f>=slots.length||s<0||s>=slots[f].length||slots[f][s]==null)return false;slots[f][s]=null;return true;}
        int activeCount(){return active.size();}
    }
    public static void main(String[] args) {
        Facility f=new Facility(new int[]{1,1}); LocalDateTime in=LocalDateTime.of(2026,10,20,10,0);
        Ticket a=f.enter(new Bike("TN01AB1111",null),in);
        Ticket b=f.enter(new Car("TN02CD2222",null),in.plusMinutes(1));
        System.out.println("Last free slots allocated="+(a!=null&&b!=null));
        System.out.println("Full facility returns null="+(f.enter(new Car("TN03EF3333",null),in)==null));
        System.out.println("Bike fee 60 min=Rs "+f.exit("TN01AB1111",in.plusMinutes(60)));
        System.out.println("Car fee 61 min=Rs "+f.exit("TN02CD2222",in.plusMinutes(62)));
        System.out.println("Invalid slot rejected="+(!f.releaseInvalidSlot(9,0)));
        try { f.enter(new Car("TN04GH4444",null),in); f.enter(new Car("TN04GH4444",null),in); }
        catch(IllegalArgumentException ex){System.out.println("Duplicate registration rejected=true");}
        System.out.println("Active tickets="+f.activeCount());
        // Hand calculation for this test: bike 1h x Rs10 = Rs10; car 2 started hours x Rs30 = Rs60; total Rs70.
        System.out.println("Hand-calculated sample total=Rs 70.0 (bike Rs10 + car Rs60)");
    }
}
