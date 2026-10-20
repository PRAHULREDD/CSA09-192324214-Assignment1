import java.util.Scanner;

/** Task 2: data types, jagged arrays, and safe integer billing arithmetic. */
public class Task02DataRepresentation {
    public static int billableHours(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Minutes cannot be negative");
        return minutes == 0 ? 0 : (minutes + 59) / 60; // ceiling division for non-negative minutes
    }
    public static void main(String[] args) {
        int[] slotsPerFloor = {20, 35, 50};
        String[][] slots = new String[slotsPerFloor.length][];
        for (int floor=0; floor<slotsPerFloor.length; floor++) slots[floor] = new String[slotsPerFloor[floor]];
        int minutes = 61;
        int correctHours = billableHours(minutes);
        int incorrectHours = minutes / 60; // integer truncation: incorrect for 61 minutes
        double hourlyRate = 30.0;
        System.out.println("Floors=" + slots.length + ", capacities=" + java.util.Arrays.toString(slotsPerFloor));
        System.out.println("61 minutes: correct fee=Rs " + (correctHours * hourlyRate));
        System.out.println("Incorrect minutes/60 fee=Rs " + (incorrectHours * hourlyRate));
        System.out.println("Types: slot/floor/duration=int; registration=String; timestamp=LocalDateTime; money=long paise or BigDecimal.");
    }
}
