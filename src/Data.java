/**
 * The Data class contains methods to handle data related to the Traveling Salesman Problem (TSP).
 * It reads city information from a file, calculates distances between cities, and initializes necessary matrices.
 */
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Data {

    /** Number of cities. */
    public static int numberOfCities;

    /** Coordinates of each city. */
    public static double[][] cityCoordinates;

    /** Matrix storing distances between cities. */
    public static double[][] distanceMatrix;

    /**
     * Sets the number of cities based on the content of the file.
     *
     * @param fileName The name of the file containing city information.
     */
    public static void setNumberOfCities(String fileName) {
        try (Scanner input = new Scanner(new File(fileName))) {
            while (input.hasNextLine()) { // finding the number of cities // finding the number of cities
                input.nextLine();
                numberOfCities++;
            }
        }
        catch (IOException e) {
                    e.printStackTrace();
                    return;
        }
    }

    /**
     * Reads city coordinates from the file and initializes the cityCoordinates array.
     *
     * @param fileName The name of the file containing city coordinates.
     */*
    public static void setCityCoordinates(String fileName) {
        try (Scanner readLine = new Scanner(new File(fileName))) {
            cityCoordinates = new double[numberOfCities][2];
            for (int i = 0; i < numberOfCities; i++) {
                String line = readLine.nextLine();
                String[] lineSplit = line.split(",");
                cityCoordinates[i][0] = Double.parseDouble(lineSplit[0]);
                cityCoordinates[i][1] = Double.parseDouble(lineSplit[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Calculates distances between cities and initializes the distanceMatrix array.
     */
    public static void setDistanceMatrix () {
        distanceMatrix = new double[numberOfCities][numberOfCities];
        for (int i = 0; i < numberOfCities; i++) { // finding distances and assigning initial pheromone value to roads;
            for (int j = 0; j < numberOfCities; j++) {
                double xi = cityCoordinates[i][0];
                double yi = cityCoordinates[i][1];
                double xj = cityCoordinates[j][0];
                double yj = cityCoordinates[j][1];
                distanceMatrix[i][j] = Math.pow((Math.pow(xi - xj, 2) + Math.pow(yi - yj, 2)), 0.5);
            }
        }
    }
}


