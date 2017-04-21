import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Vector Routing Simulation
 */
public class VectorRoutingSim {

    private Network network;
    private PriorityQueue<TopologicalEvent> events;

    private VectorRoutingSim(Network network, PriorityQueue<TopologicalEvent> events) {
        this.network = network;
        this.events = events;
    }

    private void regularRouting(int flag) {

    }

    private void splitHorizon(int flag) {

    }

    private void poisonReverse(int flag) {

    }



    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Incorrect arguments. Please try again!");
        }

        String[] routingAlgorithms = new String[]{"Regular", "Split Horizon", "Poison Reverse"};

        FileParser fileParser = new FileParser();
        String networkFile = args[0].trim();
        String eventFile = args[1].trim();
        int flag = Integer.parseInt(args[2].trim());
        PriorityQueue<TopologicalEvent> originalEvents = new PriorityQueue<>();
        Network originalNetwork = new Network();

        try {
            originalNetwork = fileParser.readNetwork(networkFile);
        } catch (Exception e) {
            System.out.println("Error creating network. Please check the file.");
        }

        try {
            originalEvents = fileParser.readTopologicalEvents(eventFile);
        } catch (Exception e) {
            System.out.println("Error reading topological events. Please check the file.");
            System.exit(1);
        }

        for (String algorithm : routingAlgorithms) {
            System.out.println("ALGORITHM: " + algorithm);
            Network network = new Network(originalNetwork);
            PriorityQueue<TopologicalEvent> events = new PriorityQueue<>(originalEvents);
            VectorRoutingSim simulator = new VectorRoutingSim(network, events);

            switch (algorithm) {
                case "Regular":
                    simulator.regularRouting(flag);
                    break;
                case "Split Horizon":
                    simulator.splitHorizon(flag);
                    break;
                case "Poison Reverse":
                    simulator.poisonReverse(flag);
                    break;
            }

        }

    }
}
