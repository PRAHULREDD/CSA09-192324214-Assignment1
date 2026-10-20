package parking.model;

import java.util.Objects;
public class Vehicle {
    private final String registrationNumber;
    private final String type;
    public Vehicle(String registrationNumber, String type) {
        if (registrationNumber == null || registrationNumber.isBlank()) throw new IllegalArgumentException("Registration required");
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Type required");
        this.registrationNumber=registrationNumber; this.type=type;
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getType() { return type; }
    @Override public boolean equals(Object o) { return o instanceof Vehicle v && registrationNumber.equalsIgnoreCase(v.registrationNumber); }
    @Override public int hashCode() { return Objects.hash(registrationNumber.toUpperCase(java.util.Locale.ROOT)); }
}
