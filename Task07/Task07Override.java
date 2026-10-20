import java.util.Objects;

/** Task 7: overriding, final constants, and Object methods. */
public class Task07Override {
    static class Vehicle {
        private final String regNo;
        Vehicle(String regNo) { if (regNo==null || regNo.isBlank()) throw new IllegalArgumentException("Registration required"); this.regNo=regNo; }
        String regNo() { return regNo; }
        double calculateFee(int minutes) { return 0; }
        @Override public String toString() { return getClass().getSimpleName()+"["+regNo+"]"; }
        @Override public boolean equals(Object o) { return o instanceof Vehicle v && regNo.equalsIgnoreCase(v.regNo); }
        @Override public int hashCode() { return regNo.toUpperCase(java.util.Locale.ROOT).hashCode(); }
    }
    static class Car extends Vehicle {
        private static final double RATE = 30.0;
        Car(String regNo) { super(regNo); }
        @Override double calculateFee(int minutes) { return minutes <= 0 ? 0 : ((minutes+59)/60)*RATE; }
    }
    static class Bike extends Vehicle {
        Bike(String regNo) { super(regNo); }
        @Override double calculateFee(int minutes) { return minutes <= 0 ? 0 : ((minutes+59)/60)*10.0; }
    }
    public static void main(String[] args) {
        Vehicle car=new Car("TN01AB1111"), bike=new Bike("TN02CD2222");
        System.out.println(car+" fee for 61 min=Rs "+car.calculateFee(61));
        System.out.println(bike+" fee for 61 min=Rs "+bike.calculateFee(61));
        System.out.println("Same registration equals="+new Car("TN01AB1111").equals(new Bike("TN01AB1111")));
    }
}
