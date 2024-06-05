/**
 * The AntColony class contains methods to perform Ant Colony Optimization (ACO) for solving the Traveling Salesman Problem (TSP).
 * It initializes and manages the pheromone matrix, edge value matrix, and other parameters required for the ACO algorithm.
 */
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class AntColony {

    /** Represents the data object containing information about cities. */
    public static Data data;

    /** Initial pheromone level on the edges. */
    public static double initialPheromone;

    /** Matrix storing the pheromone levels on the edges between cities. */
    public static double[][] pheromoneMatrix;

    /** Matrix storing the edge values based on pheromone levels and distances between cities. */
    public static double[][] edgeValueMatrix;

    /** Number of iterations for ant traversal. */
    public static int n;

    /** Number of ants in the colony. */
    public static int m;

    /** Pheromone degradation factor. */
    public static double degradationFactor;

    /** Pheromone update factor. */
    public static double q;

    /** Pheromone importance factor. */
    public static double alpha;

    /** Heuristic information importance factor. */
    public static double beta;

    /**
     * Initializes the pheromone matrix with initial pheromone level.
     */
    public static void initialPheromoneMatrix() {
        pheromoneMatrix = new double[Data.numberOfCities][Data.numberOfCities];
        for (int i = 0; i < Data.numberOfCities; i++) {
            for (int j = 0 ; j < Data.numberOfCities; j++ ) {
                pheromoneMatrix[i][j] = initialPheromone;
            }
        }
    }

    /**
     * Initializes the edge value matrix based on pheromone levels and distances between cities.
     */
    public static void setEdgeValueMatrix() {
        edgeValueMatrix = new double[Data.numberOfCities][Data.numberOfCities];
        for (int i = 0; i < Data.numberOfCities; i++) {
            for (int j = 0 ; j < Data.numberOfCities; j++ ) {
                if (i != j)
                    edgeValueMatrix[i][j] = Math.pow(pheromoneMatrix[i][j],alpha) / Math.pow(Data.distanceMatrix[i][j],beta);
            }
        }
    }

    /**
     * Traverses from a start city to find a path (cycle) using ant agents.
     *
     * @param startCity The index of the starting city.
     * @return An array representing the predecessors of each city in the cycle.
     */
    public static int[] traverse(int startCity) { // returns the path of the cycle
        boolean[] visited = new boolean[Data.numberOfCities];
        int[] predecessors = new int[Data.numberOfCities];
        visited[startCity] = true;
        int startCityInitial = startCity; // to complete cycle keep the first city info
        int counter = 0;
        while (counter < Data.numberOfCities-1) {  // finding the path of the current ant
            if ( nextCity(startCity,visited) == -1)
                break;
            int afterCity = nextCity(startCity,visited);
            visited[afterCity] = true;
            predecessors[afterCity] = startCity;
            startCity = afterCity; // at last iteration of while start city is also the last city to connect to original start city
            counter++;
        }
        predecessors[startCityInitial] = startCity;
        return predecessors;
    }

    /**
     * Finds the next city to visit based on the current city and pheromone levels.
     *
     * @param currentCity The index of the current city.
     * @param visited An array indicating visited cities.
     * @return The index of the next city to visit.
     */
    public static int nextCity(int currentCity, boolean[] visited) {
        double totalEdgeValues = 0;
        double[][] probabilities;
        int counter = 0;
        for (int i = 0; i < edgeValueMatrix.length; i++) {
            if (!visited[i]) {
                totalEdgeValues += edgeValueMatrix[currentCity][i];
                counter++;
            }
        }
        probabilities = new double[counter+1][2]; // to keep the index too, create a 2-dimensional array
        double probability;
        int counterForProbabilityList = 0;
        for (int i = 0; i < edgeValueMatrix.length; i++) {
            if (!visited[i]) {
                probability = edgeValueMatrix[currentCity][i] / totalEdgeValues;
                probabilities[counterForProbabilityList+1][0] = probabilities[counterForProbabilityList][0] + probability;
                probabilities[counterForProbabilityList+1][1] = i;
                counterForProbabilityList++;
            }
        }
        double randomNumber = Math.random();
        for (int i = 0; i < counter; i++) {
            if (probabilities[i][0] < randomNumber &&  randomNumber < probabilities[i+1][0])
                return (int) probabilities[i+1][1];
        }
        return -1; // if all cities are visited
    }

    /**
     * Calculates the total distance of the cycle based on predecessors array.
     *
     * @param predecessors An array representing predecessors of each city in the cycle.
     * @return The total distance of the cycle.
     */
    public static double findCycleDistance(int[] predecessors) {
        int numberOfCities = Data.distanceMatrix.length;
        double cycleLength = 0;
        for (int k = 0; k < numberOfCities; k++) { // calculating the total cycle distance
            int index = predecessors[k];
            cycleLength += Data.distanceMatrix[index][k];
        }
        return cycleLength;
    }

    /**
     * Updates the pheromone matrix based on the cycle found by an ant.
     *
     * @param predecessors An array representing predecessors of each city in the cycle.
     * @param distance The total distance of the cycle.
     */
    public static void updatePheromoneMatrix(int[] predecessors, double distance) {
        for (int i = 0; i < predecessors.length; i++) {
            pheromoneMatrix[i][predecessors[i]] += q/distance;
            pheromoneMatrix[predecessors[i]][i] += q/distance;
        }
    }

    /**
     * Updates the edge value matrix based on the updated pheromone levels.
     */
    public static void updateEdgeValueMatrix() {
        for (int i = 0; i < Data.numberOfCities; i++) {
            for (int j = 0 ; j < Data.numberOfCities; j++ ) {
                if (i != j)
                    edgeValueMatrix[i][j] = Math.pow(pheromoneMatrix[i][j],alpha) / Math.pow(Data.distanceMatrix[i][j],beta);
            }
        }
    }

    /**
     * Degrades pheromone levels on edges based on the degradation factor.
     */
    public static void degrade() {
        for (int i = 0; i < Data.numberOfCities; i++) {
            for (int j = 0 ; j < Data.numberOfCities; j++ ) {
                pheromoneMatrix[i][j] *= degradationFactor;
                edgeValueMatrix[i][j] = Math.pow(pheromoneMatrix[i][j],alpha) / Math.pow(Data.distanceMatrix[i][j],beta);
            }
        }
    }

    /**
     * Converts predecessors array into a route array.
     *
     * @param predecessors An array representing predecessors of each city in the cycle.
     * @return An array representing the route of the cycle.
     */
    public static int[] findRoute(int[] predecessors) {
        int[] route = new int[predecessors.length+1];
        int counter = 1;
        int wanted = 0;
        route[0] = wanted + 1;
        wanted = predecessors[wanted];
        while (counter < predecessors.length+1) {
            route[counter] = wanted +1;
            wanted = predecessors[wanted];
            counter++;
        }
        return route;
    }

    /**
     * Draws the pheromone map.
     */
    public static void printPheromoneMap() {
        int n = Data.numberOfCities;
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(1000,1000);
        for (int i = 0; i < n;  i++) {
            for (int j = 0; j <= i ; j++) {
                double thickness = pheromoneMatrix[i][j];
                StdDraw.setPenRadius(thickness);
                StdDraw.line(Data.cityCoordinates[i][0],Data.cityCoordinates[i][1], Data.cityCoordinates[j][0],Data.cityCoordinates[j][1]);
            }
        }
        for (int i = 0; i < Data.numberOfCities; i++) {
            StdDraw.setPenColor(Color.LIGHT_GRAY);
            if (i == 0) StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.filledCircle(Data.cityCoordinates[i][0],Data.cityCoordinates[i][1],0.02);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.text(Data.cityCoordinates[i][0],Data.cityCoordinates[i][1]-0.001, String.valueOf(i+1));
        }
        StdDraw.show();
    }

    /**
     * Draws the best route found during optimization.
     *
     * @param bestRoute An array representing the best route.
     */
    public static void printBestRoute(int[] bestRoute) {
        int city1 = 0;
        int city2 = 0;
        int n = Data.numberOfCities;
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(1000,1000);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.005);
        for (int i = 0; i < bestRoute.length; i++) {
            if ( i != bestRoute.length - 1) {
                city1 = bestRoute[i] - 1;
                city2 = bestRoute[i + 1] - 1;
            }
            else {
                city1 = bestRoute[i];
                city2 = bestRoute[0];
            }
            StdDraw.line(Data.cityCoordinates[city1][0],Data.cityCoordinates[city1][1], Data.cityCoordinates[city2][0],Data.cityCoordinates[city2][1]);
        }
        for (int i = 0; i < Data.numberOfCities; i++) {
            StdDraw.setPenColor(Color.LIGHT_GRAY);
            if (i == 0) StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.filledCircle(Data.cityCoordinates[i][0],Data.cityCoordinates[i][1],0.02);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.text(Data.cityCoordinates[i][0],Data.cityCoordinates[i][1]-0.001, String.valueOf(i+1));
        }
        StdDraw.show();
    }
}
