/**
 * The BruteForce class contains methods to perform brute-force search for solving the Traveling Salesman Problem (TSP).
 * It generates all possible permutations of cities to find the shortest route.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteForce {

    /** Represents the data object containing information about cities. */
    public static Data data;

    /** Minimum distance found during brute-force search. */
    public static Double minDistance = Double.MAX_VALUE;

    /** Best route found during brute-force search. */
    public static int[] bestRoute = new int[Data.numberOfCities];

    /**
     * Generates all possible permutations of cities and finds the shortest route.
     *
     * @param arr The array representing the permutation of cities.
     * @param k The current index during permutation generation.
     */
    public static void permute(int[] arr, int k) {
        if (k == arr.length) {
            double distance = findRouteLength(arr);
            if ( distance < minDistance) {
                minDistance = distance;
                bestRoute = arr.clone();
            }
        }
        else {
            for (int i = k; i < arr.length; i++) {
                int temp = arr[i];
                arr[i] = arr[k];
                arr[k] = temp;
                permute(arr, k + 1);
                temp = arr[k];
                arr[k] = arr[i];
                arr[i] = temp;
            }
        }
    }

    /**
     * Calculates the total length of the route based on the given permutation of cities.
     *
     * @param route The permutation of cities representing the route.
     * @return The total length of the route.
     */
    public static double findRouteLength(int[] route) {
        double minDistance = 0;
        int city1 = 0;
        int city2 = 0;
        for (int i = 0; i < route.length -1; i++) {
            city1 = route[i];
            city2 = route[i+1];
            minDistance += Data.distanceMatrix[city1][city2];
        }
        city1 = 0;
        minDistance += Data.distanceMatrix[city1][city2];
        return minDistance;
    }

    /**
     * Adjusts the best route array to include starting and ending city.
     *
     * @param bestRoute The best route found during brute-force search.
     * @return The adjusted route array with starting and ending city.
     */
    public static int[] editedRoute(int[] bestRoute) {
        int[] edited = new int[bestRoute.length+1];
        for (int i = 0; i < bestRoute.length; i++) {
            edited[i] = bestRoute[i] + 1;
        }
        edited[bestRoute.length] = 1;
        return edited;
    }

    /**
     * Draws the route on a canvas.
     *
     * @param route The route to be drawn.
     */
    public static void drawRoute(int[] route) {
        int city1 = 0;
        int city2 = 0;
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(1000, 1000);
        for (int i = 0; i < route.length; i++) {
            if ( i != route.length - 1) {
                city1 = route[i] - 1;
                city2 = route[i + 1] - 1;
            }
            else {
                city1 = route[i];
                city2 = route[0];
            }
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.005);
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
