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
        Network network = this.network;
        PriorityQueue<TopologicalEvent> events = this.events;
    }

    private void splitHorizon(int flag) {
        Network network = this.network;
        PriorityQueue<TopologicalEvent> events = this.events;
    }

    private void poisonReverse(int flag) {
        Network network = this.network;
        PriorityQueue<TopologicalEvent> events = this.events;
    }


    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            throw new IllegalArgumentException("Please enter arguments as: <File with network topology> " +
                    "<File with topological events> <Binary flag>");
        }

        String[] routingAlgorithms = new String[]{"Regular", "Split Horizon", "Poison Reverse"};

        FileParser fileParser = new FileParser();
        String networkFile = args[0].trim();
        String eventFile = args[1].trim();
        int flag = Integer.parseInt(args[2].trim());

        Network originalNetwork = fileParser.readNetwork(networkFile);
        PriorityQueue<TopologicalEvent> originalEvents = fileParser.readTopologicalEvents(eventFile);

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
