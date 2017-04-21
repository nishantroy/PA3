import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Vector Routing Simulation
 */
public class VectorRoutingSim {

    private Network readNetwork(String fileName) throws Exception {
        Scanner scanner = new Scanner(new File(fileName));
        Network network = new Network();
        int numRouters = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= numRouters; i++) {
            network.addRouter(new Router(i));
        }
        String[] data;
        while (scanner.hasNextLine()) {
            data = scanner.nextLine().split(" ");
            Router routerSource = new Router(Integer.parseInt(data[0]));
            Router routerDest = new Router(Integer.parseInt(data[1]));
            int cost = Integer.parseInt(data[2]);
            network.addLink(routerSource, routerDest);
            network.setLinkWeight(routerSource, routerDest, cost);
        }

        return network;
    }

    private PriorityQueue<TopologicalEvent> readTopologicalEvents(String fileName) throws Exception {
        PriorityQueue<TopologicalEvent> pq = new PriorityQueue<>();

        Scanner scanner = new Scanner(new File(fileName));
        String[] data;
        while (scanner.hasNextLine()) {
            data = scanner.nextLine().split(" ");
            int roundNum = Integer.parseInt(data[0]);
            int routerSource = Integer.parseInt(data[1]);
            int routerDest = Integer.parseInt(data[2]);
            int cost = Integer.parseInt(data[3]);
            pq.add(new TopologicalEvent(roundNum, routerSource, routerDest, cost));
        }

        return pq;
    }

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Incorrect arguments. Please try again!");
        }
        
        String[] routingAlgorithms = new String[]{"Regular", "Split Horizon", "Poison Reverse"};

        VectorRoutingSim simulator = new VectorRoutingSim();

        String networkFile = args[0].trim();
        String eventFile = args[1].trim();
        int flag = Integer.parseInt(args[2].trim());
        PriorityQueue<TopologicalEvent> originalEvents = new PriorityQueue<>();
        Network originalNetwork = new Network();

        try {
            originalNetwork = simulator.readNetwork(networkFile);
        } catch (Exception e) {
            System.out.println("Error creating network. Please check the file.");
        }

        try {
            originalEvents = simulator.readTopologicalEvents(eventFile);
        } catch (Exception e) {
            System.out.println("Error reading topological events. Please check the file.");
            System.exit(1);
        }

        for (String algorithm : routingAlgorithms) {
            System.out.println("ALGORITHM: " + algorithm);
            Network network = new Network(originalNetwork);
            PriorityQueue<TopologicalEvent> events = new PriorityQueue<>(originalEvents);
        }

    }
}
