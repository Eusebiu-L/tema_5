import java.io.*;
import java.util.*;


class Interval {
    private double lowerBound;
    private double upperBound;
    private int totalNumbersTested;
    private int numbersInsideInterval;

    public Interval(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.totalNumbersTested = 0;
        this.numbersInsideInterval = 0;
    }

  
    public void testNumber(Double number) {
        totalNumbersTested++;
        if (number >= lowerBound && number <= upperBound) {
            numbersInsideInterval++;
        }
    }


    public double calculatePercentage() {
        if (totalNumbersTested == 0) {
            return 0.0;
        }
        return (double) numbersInsideInterval / totalNumbersTested * 100;
    }


    public void writeResultToFile(BufferedWriter writer) throws IOException {
        writer.write(String.format("[%.1f,%.1f]: %.2f%%\n", lowerBound, upperBound, calculatePercentage()));
    }
}
class FileManager {
    public List<Double> readNumbersFromFile(String filename) throws IOException {
        List<Double> numbers = new ArrayList<>();
        Set<Double> uniqueNumbers = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                for (String token : tokens) {
                    Double number = Double.parseDouble(token);
                    if (!uniqueNumbers.contains(number)) {
                        numbers.add(number);
                        uniqueNumbers.add(number);
                    } else {
                        System.out.println("Warning: Duplicate number found and ignored: " + number);
                    }
                }
            }
        }
        return numbers;
    }

    public List<Interval> readIntervalsFromFile(String filename) throws IOException {
        List<Interval> intervals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bounds = line.trim().replaceAll("[\\[\\]]", "").split(",");
                double lowerBound = Double.parseDouble(bounds[0]);
                double upperBound = Double.parseDouble(bounds[1]);
                intervals.add(new Interval(lowerBound, upperBound));
            }
        }
        return intervals;
    }

    public void writeStatisticsToFile(List<Interval> intervals, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Interval interval : intervals) {
                interval.writeResultToFile(writer);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        List<Interval> intervals;
        List<Double> numbers;

        try {
            intervals = fileManager.readIntervalsFromFile("intervale.dat");
            numbers = fileManager.readNumbersFromFile("numere.dat");

            
            for (Double number : numbers) {
                for (Interval interval : intervals) {
                    interval.testNumber(number);
                }
            }


            fileManager.writeStatisticsToFile(intervals, "statistica.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


