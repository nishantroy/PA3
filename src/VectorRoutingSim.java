import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;
import java.util.*;

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
     *
     * @return something
     */
    private boolean regularRouting() {
        Boolean updated = false;
        Set<Router> routers = network.getNetwork().vertexSet();
        //Set<Router> routersCopy = new HashSet<>();
        HashMap<Router, RoutingTable> copiedRoutersTables = new HashMap<>();
        for (Router router: routers) {
            Router routerCopy = (Router)deepClone(router);
            copiedRoutersTables.put(router, routerCopy.getRoutingTable());
            //routersCopy.add((Router)deepClone(router));
        }

        for (Router router: routers) {
            if(!router.isChanged()) continue;
            HashSet<Router> neighbors = network.getNeighbors(router);
            RoutingTable routerRTable = router.getRoutingTable();

            for(Router neighbor:neighbors) {

                for(Map.Entry<Router, ViaMap> entry : routerRTable.getTable().entrySet()) {
                    Router dest = entry.getKey();
                    if (dest.equals(router)) continue;

                    if (routerRTable.getFastestPath(dest) != null) {
                        Router via = routerRTable.getFastestPath(dest);
                        double oldCost = copiedRoutersTables.get(router).getCost(dest, via);
                        if (oldCost != Double.POSITIVE_INFINITY) {
                            RoutingTable neighborRTable = neighbor.getRoutingTable();
                            double extraCost = neighborRTable.getCost(router, router);
                            if (!routerRTable.getFastestPath(dest).equals(neighbor)) {
                                double updatedCost = oldCost + extraCost;
                                boolean changed = neighborRTable.setCost(dest, router, updatedCost);
                                if (changed) {
                                    double count = copiedRoutersTables.get(router).getNumHops(dest, via) + 1;
                                    neighborRTable.setNumHops(dest, router, count);
                                    neighbor.setChanged(true);
                                }
                                if (!updated && changed) {
                                    updated = true;
                                }
                            }
                        }


                    }
                }

            }
        }
        return updated;

    }

    private void splitHorizon(int flag) {
        Network network = this.network;
        PriorityQueue<TopologicalEvent> events = this.events;
    }

    private void poisonReverse(int flag) {
        Network network = this.network;
        PriorityQueue<TopologicalEvent> events = this.events;
    }

    private HashSet<TopologicalEvent> getEventsByRound(int roundNumber) {
        HashSet<TopologicalEvent> roundEvents = new HashSet<>();
        while (!events.isEmpty() && roundNumber == events.peek().getRound()) {
            TopologicalEvent event = events.poll();
            roundEvents.add(event);
        }
        return roundEvents;
    }

    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

            int roundNumber = 1;
            int lastEvent = 0;
            Boolean updated = true;
            StringBuilder sb = new StringBuilder();

            if (flag == 1) {
                double[][] table = makeTable(network, true);
                sb.append("ROUND 1\n");
                sb.append(printTable(table));
                //do something with this
            }

            while (true) {
                HashSet<TopologicalEvent> eventsInRound = simulator.getEventsByRound(roundNumber);
                if (!eventsInRound.isEmpty()) {
                    network.executeEventsAndUpdate(eventsInRound);
                    lastEvent = roundNumber;
                }
                switch (algorithm) {
                    case "Regular":
                        updated = simulator.regularRouting();
                        break;
                    case "Split Horizon":
                        updated = simulator.splitHorizon();
                        break;
                    case "Poison Reverse":
                        updated = simulator.poisonReverse();
                        break;
                }

                Set<Router> routers = network.getNetwork().vertexSet();
                for (Router router: routers) {
                    router.setChanged(router.getRoutingTable().updateAllFastestPaths());
                }
                if(!updated && events.isEmpty()) {
                    break;
                }

                double[][] table = makeTable(network);
                if (flag == 1) {
                    sb.append("Round " );
                    sb.append(roundNumber);
                    sb.append("\n");
                    sb.append(printTable(table));
                }

                if (countToInf(table)) {
                    System.out.println("Count to infinity problem reached");
                    System.exit(1);
                }

                roundNumber++;
            }
            if (flag == 0) {
                double[][] table = makeTable(network);
                sb.append(printTable());
            }
            int convergenceDelay = roundNumber - lastEvent;
            sb.append("Convergence delay: ");
            sb.append(convergenceDelay);
            sb.append(" round(s)");

            StringBuilder filename = new StringBuilder();
            filename.append("output-");
            switch (algorithm) {
                case "Regular":
                    filename.append("basic");
                    break;
                case "Split Horizon":
                    filename.append("split-horizon");
                    break;
                case "Poison Reverse":
                    filename.append("split-horizon-poison-reverse");
                    break;
            }
            if (flag == 1) {
                filename.append("-detailed");
            }
            filename.append(".txt");
            //NEED TO ADD FILE OUTPUT
            System.out.println(sb.toString());
        }

    }//PUT NEWLINE AT END OF PRINT TABLE
}
