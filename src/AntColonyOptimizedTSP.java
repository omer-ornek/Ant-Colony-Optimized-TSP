/**
 * The AntColonyOptimized-TSP class contains the main method to execute either the Brute-Force method or the Ant Colony Optimization method
 * to find the shortest path in a given set of cities represented as coordinates.
 * The chosen method is specified by setting the value of the 'chosenMethod' variable to either 1 (Brute-Force method) or 2 (Ant Colony Optimization method).
 * The class initializes necessary parameters for both methods and executes the chosen method accordingly.
 * If the Brute-Force method is chosen, it generates all possible permutations of the cities to find the shortest path.
 * If the Ant Colony Optimization method is chosen, it utilizes ant agents to explore the search space and update pheromone levels to find the shortest path.
 * The class also provides functionality to draw the best route or the pheromone map based on the specified parameters.
 * @author Omer Taha Ornek ,Student ID: 2022400117
 * @since 30.04.2024
 */
import java.util.Arrays;
public class AntColonyOptimizedTSP{
    /**
     * The main method to execute the Brute-Force method or the Ant Colony Optimization method
     * to find the shortest path in a given set of cities.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        int chosenMethod = 1; // if 1 Brute-Force method , else if 2 Ant Colony Optimization method
        AntColony.m = 50;
        AntColony.n = 100;
        AntColony.degradationFactor = 0.8;
        AntColony.q = 0.0001;
        AntColony.initialPheromone = 0.001;
        AntColony.alpha = 1.1;
        AntColony.beta = 1.6;
        String fileName = "input01.txt";
        Data.setNumberOfCities(fileName);
        Data.setCityCoordinates(fileName);
        Data.setDistanceMatrix();
        double minDistance = Double.MAX_VALUE;
        int[] bestRoute = new int[1];

        if (chosenMethod == 1) { // Brute-Force method
            int[] initialRoute = new int[Data.numberOfCities];
            for (int i = 0; i < Data.numberOfCities; i++) {
                initialRoute[i] = i;
            }
            long start = System.currentTimeMillis();
            BruteForce.permute(initialRoute, 1); // assign k to 1 to reduce complexity since we always start from Migros.
            long end = System.currentTimeMillis();
            bestRoute = BruteForce.bestRoute;
            minDistance = BruteForce.minDistance;
            bestRoute = BruteForce.editedRoute(bestRoute);  // transforms predecessors  array into route.
            BruteForce.drawRoute(bestRoute);
            System.out.println("Method: Brute-Force Method");
            System.out.printf("Shortest Distance: " + "%.4f\n", minDistance);
            System.out.println("Shortest Path: " + Arrays.toString(bestRoute));
            System.out.println("Time it takes to find the shortest path: " + (end - start) / 1000.0);
        }

        else if (chosenMethod == 2) { // Ant colony optimization method

            int whatToDraw = 1; // if 1 draw pheromone map, else if 2 draws the best route

            long start = System.currentTimeMillis();
            AntColony.initialPheromoneMatrix(); // creating a pheromone matrix  numberOfCities * numberOfCities
            AntColony.setEdgeValueMatrix(); // creating an edgeValueMatrix  numberOfCities * numberOfCities
            for (int i = 0; i < AntColony.n; i++) {
                for (int j = 0; j < AntColony.m; j++) {
                    int startCity = (int) (Math.random() * Data.numberOfCities);
                    int[] predecessors = AntColony.traverse(startCity); // keeping track of route
                    double cycleDistance = AntColony.findCycleDistance(predecessors);
                    AntColony.updatePheromoneMatrix(predecessors, cycleDistance);
                    AntColony.updateEdgeValueMatrix();
                    if (cycleDistance < minDistance) {
                        bestRoute = null;  // to reset the array
                        bestRoute = new int[predecessors.length];
                        minDistance = cycleDistance;
                        bestRoute = predecessors;
                    }
                }
                AntColony.degrade();
                //System.out.println(minDistance);
            }
            bestRoute = AntColony.findRoute(bestRoute);
            long end = System.currentTimeMillis();
            System.out.println("Method: Ant Colony Optimization Method");
            System.out.printf("Shortest Distance: " + "%.4f\n", minDistance);
            System.out.println("Shortest Path: " + Arrays.toString(bestRoute));
            System.out.println("Time it takes to find the shortest path: " + (end - start) / 1000.0);
            if (whatToDraw == 1) { // draws pheromone matrix
                AntColony.printPheromoneMap();
            }
            else if (whatToDraw == 2) { // draws the best route
                AntColony.printBestRoute(bestRoute);
            }
        }
    }
}
