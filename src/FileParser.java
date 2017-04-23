import org.jgrapht.graph.DefaultEdge;

import java.io.File;
import java.util.*;

/**
 * Reads in Files
 */
class FileParser {
    /**
     * Reads in the network topology and creates all the routers, links, etc.
     *
     * @param fileName File with network topology
     * @return Network of routers
     * @throws Exception FileNotFoundException etc
     */
    Network readNetwork(String fileName) throws Exception {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (Exception e) {
            throw new Exception("Error creating network. " + e.getMessage());
        }

        Network network = new Network();
        int numRouters = Integer.parseInt(scanner.nextLine());
        HashMap<Integer, Router> networkRouters = new HashMap<>();
        HashMap<Router, Map<Router, Integer>> networkLinks = new HashMap<>();

        for (int i = 1; i <= numRouters; i++) {
            Router newRouter = new Router(i);
            newRouter.setChanged(true);
            networkRouters.put(i, newRouter);
        }

        for (Router x : networkRouters.values()) {
            for (Router key : networkRouters.values()) {
                for (int i = 1; i <= numRouters; i++) {
                    if (i != key.getRouterID()) {
                        Router neighbor = networkRouters.get(i);
                        x.updateTable(key, neighbor, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                    }
                }
            }
        }

        String[] data;
        while (scanner.hasNextLine()) {
            // Read in line
            data = scanner.nextLine().split(" ");
            Router routerSource = networkRouters.get(Integer.parseInt(data[0]));
            Router routerDest = networkRouters.get(Integer.parseInt(data[1]));
            int cost = Integer.parseInt(data[2]);

            // Initialize routing tables
            routerSource.updateTable(routerDest, routerDest, cost, 1);
//            routerSource.updateTable(routerSource, routerSource, 0, 0);
            routerDest.updateTable(routerSource, routerSource, cost, 1);
//            routerDest.updateTable(routerDest, routerDest, 0, 0);

            //Initialize routing vectors
//            routerSource.updateVector(routerDest, cost, 1, routerDest);
//            routerSource.updateVector(routerSource, 0, 0, routerSource);
//            routerDest.updateVector(routerSource, cost, 1, routerSource);
//            routerDest.updateVector(routerDest, 0, 0, routerDest);

            // Store links to add to network
            if (networkLinks.containsKey(routerSource)) {
                networkLinks.get(routerSource).put(routerDest, cost);
            } else {
                Map<Router, Integer> map = new HashMap<>();
                map.put(routerDest, cost);
                networkLinks.put(routerSource, map);
            }

        }

        networkRouters.values().forEach(network::addRouter);

        for (Router source : networkLinks.keySet()) {
            Map<Router, Integer> map = networkLinks.get(source);
            for (Router dest : map.keySet()) {
                network.addLink(source, dest);
                network.setLinkWeight(source, dest, map.get(dest));
            }
        }

        for (Router source : network.getNetwork().vertexSet()) {
            for (Router neighbor : network.getNeighbors(source)) {
                source.getRoutingTable().setFastestPath(neighbor, neighbor);
            }
        }

//        network.printNetwork();
//        network.printRoutingTables();
        return network;
    }

    /**
     * Creates Priority Queue of topological events from file sorted by
     * ascending order of round in which they occur
     *
     * @param fileName Name of file with events
     * @return Priority Queue of events
     * @throws Exception FileNotFoundException etc.
     */
    PriorityQueue<TopologicalEvent> readTopologicalEvents(String fileName) throws Exception {
        PriorityQueue<TopologicalEvent> pq = new PriorityQueue<>();

        Scanner scanner;

        try {
            scanner = new Scanner(new File(fileName));
        } catch (Exception e) {
            throw new Exception("Error reading topological events. " + e.getMessage());
        }

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
}
