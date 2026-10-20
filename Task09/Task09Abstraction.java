/** Task 9: abstract Vehicle plus optional Chargeable capability interface. */
public class Task09Abstraction {
    interface Chargeable { double chargingFee(int kWh); default boolean needsChargingBay() { return true; } }
    abstract static class Vehicle {
        private final String regNo;
        protected Vehicle(String regNo) { this.regNo=regNo; }
        public String regNo() { return regNo; }
        public abstract double ratePerHour();
        public double feeForMinutes(int minutes) { if (minutes<0) throw new IllegalArgumentException(); return (minutes==0?0:(minutes+59)/60)*ratePerHour(); }
    }
    static class Car extends Vehicle { Car(String regNo) { super(regNo); } @Override public double ratePerHour() { return 30; } }
    static class ElectricCar extends Car implements Chargeable {
        ElectricCar(String regNo) { super(regNo); }
        @Override public double chargingFee(int kWh) { if(kWh<0) throw new IllegalArgumentException(); return 12.0*kWh; }
    }
    public static void main(String[] args) {
        Vehicle v=new ElectricCar("TN03C3");
        double total=v.feeForMinutes(130)+((Chargeable)v).chargingFee(10);
        System.out.println("Abstract type="+v.getClass().getSuperclass().getSimpleName());
        System.out.println("Charging bay required="+((Chargeable)v).needsChargingBay()+", total=Rs "+total);
    }
}
