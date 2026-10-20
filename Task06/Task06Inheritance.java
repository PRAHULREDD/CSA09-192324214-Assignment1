/** Task 6: hierarchical inheritance and constructor order. */
public class Task06Inheritance {
    static class Vehicle {
        protected final String regNo;
        Vehicle(String regNo) { this.regNo=regNo; System.out.println("Vehicle constructor"); }
        double ratePerHour() { return 0; }
    }
    static class Car extends Vehicle {
        Car(String regNo) { super(regNo); System.out.println("Car constructor"); }
        @Override double ratePerHour() { return 30; }
    }
    static class ElectricCar extends Car {
        ElectricCar(String regNo) { super(regNo); System.out.println("ElectricCar constructor"); }
    }
    static class TwoWheeler extends Vehicle {
        TwoWheeler(String regNo) { super(regNo); }
        @Override double ratePerHour() { return 10; }
    }
    public static void main(String[] args) {
        ElectricCar ev = new ElectricCar("TN01EV1234");
        Vehicle bike = new TwoWheeler("TN02BI1234");
        System.out.println("EV rate="+ev.ratePerHour()+", bike rate="+bike.ratePerHour());
    }
}
