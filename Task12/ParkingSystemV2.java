import java.time.*;
import java.util.*;

/** Task 12: extended design using jagged floors, a Pass abstraction and per-type pricing. */
public class ParkingSystemV2 {
    interface Pass { boolean isValidAt(LocalDateTime time); }
    static final class MonthlyPass implements Pass { private final LocalDate validThrough; MonthlyPass(LocalDate d){validThrough=Objects.requireNonNull(d);} public boolean isValidAt(LocalDateTime t){return !t.toLocalDate().isAfter(validThrough);} }
    static final class OneDayVisitorPass implements Pass { private final LocalDate date; OneDayVisitorPass(LocalDate d){date=Objects.requireNonNull(d);} public boolean isValidAt(LocalDateTime t){return date.equals(t.toLocalDate());} }
    interface PricingRule { double fee(Vehicle vehicle, int minutes, LocalDateTime entry, LocalDateTime exit); }
    static class StandardPricing implements PricingRule { public double fee(Vehicle v,int minutes,LocalDateTime in,LocalDateTime out){if(minutes<=0)return 0;return ((minutes+59)/60)*v.ratePerHour();} }
    static class Vehicle {
        private final String regNo; private final Pass pass;
        Vehicle(String r,Pass p){if(r==null||r.isBlank())throw new IllegalArgumentException("Registration required");regNo=r.toUpperCase(Locale.ROOT);pass=p;}
        String regNo(){return regNo;} boolean freeAt(LocalDateTime t){return pass!=null&&pass.isValidAt(t);} double ratePerHour(){return 30;}
    }
    static final class TwoWheeler extends Vehicle { TwoWheeler(String r,Pass p){super(r,p);} @Override double ratePerHour(){return 10;} }
    static class Car extends Vehicle { Car(String r,Pass p){super(r,p);} }
    static final class ElectricCar extends Car implements Chargeable { ElectricCar(String r,Pass p){super(r,p);} public double chargingFee(int kWh){if(kWh<0)throw new IllegalArgumentException();return kWh*12.0;} }
    interface Chargeable { double chargingFee(int kWh); }
    static final class EBike extends Vehicle { EBike(String r,Pass p){super(r,p);} @Override double ratePerHour(){return 0;} double flatFee(){return 25.0;} }
    static final class DeliveryVan extends Vehicle { DeliveryVan(String r,Pass p){super(r,p);} }
    static final class PricingV2 implements PricingRule {
        public double fee(Vehicle v,int minutes,LocalDateTime in,LocalDateTime out){
            if(v.freeAt(out)||minutes<=0)return 0;
            if(v instanceof EBike e)return e.flatFee();
            int hours=(minutes+59)/60;
            if(v instanceof DeliveryVan){
                // Charge double car rate only for time overlapping 09:00-17:00.
                long overlap=overlapMinutes(in,out,LocalTime.of(9,0),LocalTime.of(17,0));
                long normal=Math.max(0,minutes-overlap);
                return ((normal+59)/60)*v.ratePerHour()+((overlap+59)/60)*v.ratePerHour()*2.0;
            }
            return hours*v.ratePerHour();
        }
        private long overlapMinutes(LocalDateTime start,LocalDateTime end,LocalTime from,LocalTime to){
            long total=0; LocalDate d=start.toLocalDate();
            while(!d.isAfter(end.toLocalDate())){LocalDateTime a=d.atTime(from),b=d.atTime(to);LocalDateTime x=start.isAfter(a)?start:a,y=end.isBefore(b)?end:b;if(y.isAfter(x))total+=Duration.between(x,y).toMinutes();d=d.plusDays(1);}return total;
        }
    }
    static final class Ticket {final Vehicle v;final LocalDateTime in;final int floor,slot;Ticket(Vehicle v,LocalDateTime in,int f,int s){this.v=v;this.in=in;floor=f;slot=s;}}
    static final class Facility {
        private final Vehicle[][] slots; private final Map<String,Ticket> active=new HashMap<>(); private final PricingRule pricing; private final Map<String,Double> dailyRevenue=new HashMap<>();
        Facility(int[] capacity,PricingRule pricing){slots=new Vehicle[capacity.length][];for(int f=0;f<capacity.length;f++){if(capacity[f]<0)throw new IllegalArgumentException();slots[f]=new Vehicle[capacity[f]];}this.pricing=Objects.requireNonNull(pricing);}
        Ticket enter(Vehicle v,LocalDateTime now){if(active.containsKey(v.regNo()))throw new IllegalArgumentException("Duplicate registration");for(int f=0;f<slots.length;f++)for(int s=0;s<slots[f].length;s++)if(slots[f][s]==null){slots[f][s]=v;Ticket t=new Ticket(v,now,f,s);active.put(v.regNo(),t);return t;}return null;}
        double exit(String reg,LocalDateTime now){Ticket t=active.remove(reg.toUpperCase(Locale.ROOT));if(t==null)throw new IllegalArgumentException("No active vehicle");slots[t.floor][t.slot]=null;int mins=(int)Math.max(0,Duration.between(t.in,now).toMinutes());double fee=pricing.fee(t.v,mins,t.in,now);dailyRevenue.merge(now.toLocalDate().toString(),fee,Double::sum);return fee;}
        double revenue(LocalDate d){return dailyRevenue.getOrDefault(d.toString(),0.0);} int activeCount(){return active.size();}
    }
    public static void main(String[] args){
        int[] capacities={20,25,30,35,40}; // different slot counts: jagged array is appropriate
        Facility f=new Facility(capacities,new PricingV2()); LocalDateTime base=LocalDateTime.of(2026,10,20,10,0);
        System.out.println("Floors="+capacities.length+", capacities="+Arrays.toString(capacities));
        f.enter(new EBike("TN01EB1111",null),base);
        System.out.println("E-bike flat fee=Rs "+f.exit("TN01EB1111",base.plusMinutes(20)));
        f.enter(new DeliveryVan("TN02DV2222",null),LocalDateTime.of(2026,10,20,8,30));
        System.out.println("Delivery van crossing 09:00-17:00 fee=Rs "+f.exit("TN02DV2222",LocalDateTime.of(2026,10,20,10,30)));
        f.enter(new Vehicle("TN03VS3333",new OneDayVisitorPass(base.toLocalDate())),base);
        System.out.println("Valid one-day visitor pass fee=Rs "+f.exit("TN03VS3333",base.plusHours(3)));
        System.out.println("Active vehicles="+f.activeCount()+", revenue today=Rs "+f.revenue(base.toLocalDate()));
    }
}
