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

    /**
     * Basic Bellman-Ford Routing
     * TODO: NOT WORKING AFTER EVENT OCCURS! LOOPS FOREVER
     *
     * @param flag How to show output (look at pdf for details)
     */
    private void regularRouting(int flag) {
        Network network = this.network;
        SimpleWeightedGraph<Router, DefaultEdge> graph = network.getNetwork();
        PriorityQueue<TopologicalEvent> events = this.events;

        boolean notConverged = true;
        int roundNumber = 0;
        while (notConverged || !network.isConverged()) {
            notConverged = false;
            roundNumber++;
            System.out.println("ROUND NUMBER: " + roundNumber);
            // Execute events for current round
            while (!events.isEmpty() && roundNumber == events.peek().getRound()) {
                TopologicalEvent event = events.poll();
                network.executeEvent(event);
            }

            for (DefaultEdge edge : graph.edgeSet()) {
                Router R1 = graph.getEdgeSource(edge);
                Router R2 = graph.getEdgeTarget(edge);
                boolean R1changed = R1.isChanged();
                boolean R2changed = R2.isChanged();


                if (R1changed) {
                    // Broadcast from source to destination
                    if (R2.receiveBroadcast(R1)) {
                        notConverged = true;
                    }
                }

                if (R2changed) {
                    // Broadcast from destination to source
                    if (R1.receiveBroadcast(R2)) {
                        notConverged = true;
                    }
                }
            }

            if (flag == 1) {
                // Print results
                network.printRoutingVectors();
            }

            if (!events.isEmpty()) {
                notConverged = true;
            }
        }

        // Print results
        System.out.println("CONVERGED");
//        network.printRoutingTables();
        network.printFastestPaths();

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
