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

    private class outputTuple {
        Router via;
        double numberOfHops;

        outputTuple() {
            super();
        }

        outputTuple(Router via, double numberOfHops) {
            this.via = via;
            this.numberOfHops = numberOfHops;
        }

        public Router getVia() {
            return via;
        }

        void setVia(Router via) {
            this.via = via;
        }

        public double getNumberOfHops() {
            return numberOfHops;
        }

        void setNumberOfHops(double numberOfHops) {
            this.numberOfHops = numberOfHops;
        }
    }

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
        for (Router router : routers) {
            Router routerCopy = (Router) deepClone(router);
            copiedRoutersTables.put(router, routerCopy.getRoutingTable());
            //routersCopy.add((Router)deepClone(router));
        }

        for (Router router : routers) {
            if (!router.isChanged()) continue;
            HashSet<Router> neighbors = network.getNeighbors(router);
            RoutingTable routerRTable = router.getRoutingTable();

            for (Router neighbor : neighbors) {

                for (Map.Entry<Router, ViaMap> entry : routerRTable.getTable().entrySet()) {
                    Router dest = entry.getKey();
                    if (dest.equals(router)) continue;

                    if (routerRTable.getFastestPath(dest) != null) {
                        Router via = routerRTable.getFastestPath(dest);
                        double oldCost = copiedRoutersTables.get(router).getCost(dest, via);
                        if (oldCost != Double.POSITIVE_INFINITY) {
                            RoutingTable neighborRTable = neighbor.getRoutingTable();
                            double extraCost = neighborRTable.getCost(router, router);
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


//                    } else {
//                        routerRTable.setFastestPath();
//                    }
                    }

                }
            }

        }
        return updated;

    }


    private boolean splitHorizon() {
        Boolean updated = false;
        Set<Router> routers = network.getNetwork().vertexSet();
        //Set<Router> routersCopy = new HashSet<>();
        HashMap<Router, RoutingTable> copiedRoutersTables = new HashMap<>();
        for (Router router : routers) {
            Router routerCopy = (Router) deepClone(router);
            copiedRoutersTables.put(router, routerCopy.getRoutingTable());
            //routersCopy.add((Router)deepClone(router));
        }

        for (Router router : routers) {
            if (!router.isChanged()) continue;
            HashSet<Router> neighbors = network.getNeighbors(router);
            RoutingTable routerRTable = router.getRoutingTable();

            for (Router neighbor : neighbors) {

                for (Map.Entry<Router, ViaMap> entry : routerRTable.getTable().entrySet()) {
                    Router dest = entry.getKey();
                    if (dest.equals(router)) continue;

                    if (routerRTable.getFastestPath(dest) != null) {
                        Router via = routerRTable.getFastestPath(dest);
                        double oldCost = copiedRoutersTables.get(router).getCost(dest, via);
                        if (oldCost != Double.POSITIVE_INFINITY) {
                            RoutingTable neighborRTable = neighbor.getRoutingTable();
                            double extraCost = neighborRTable.getCost(router, router);
                                if(!routerRTable.getFastestPath(dest).equals(neighbor)){
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

    private boolean poisonReverse() {
        Boolean updated = false;
        Set<Router> routers = network.getNetwork().vertexSet();
        //Set<Router> routersCopy = new HashSet<>();
        HashMap<Router, RoutingTable> copiedRoutersTables = new HashMap<>();
        for (Router router : routers) {
            Router routerCopy = (Router) deepClone(router);
            copiedRoutersTables.put(router, routerCopy.getRoutingTable());
            //routersCopy.add((Router)deepClone(router));
        }

        for (Router router : routers) {
            if (!router.isChanged()) continue;
            HashSet<Router> neighbors = network.getNeighbors(router);
            RoutingTable routerRTable = router.getRoutingTable();

            for (Router neighbor : neighbors) {

                for (Map.Entry<Router, ViaMap> entry : routerRTable.getTable().entrySet()) {
                    Router dest = entry.getKey();
                    if (dest.equals(router)) continue;

                    if (routerRTable.getFastestPath(dest) != null) {
                        Router via = routerRTable.getFastestPath(dest);
                        double oldCost = copiedRoutersTables.get(router).getCost(dest, via);
                        if (oldCost != Double.POSITIVE_INFINITY) {
                            RoutingTable neighborRTable = neighbor.getRoutingTable();
                            double extraCost = neighborRTable.getCost(router, router);
                            double updatedCost;
                            if(!routerRTable.getFastestPath(dest).equals(neighbor)){
                                updatedCost = oldCost + extraCost;
                            } else {
                                updatedCost = Double.POSITIVE_INFINITY;
                            }

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


//                    } else {
//                        routerRTable.setFastestPath();
//                    }
                    }

                }
            }

        }
        return updated;
    }

    private HashSet<TopologicalEvent> getEventsByRound(int roundNumber) {
        HashSet<TopologicalEvent> roundEvents = new HashSet<>();
        while (!events.isEmpty() && roundNumber == events.peek().getRound()) {
            TopologicalEvent event = events.poll();
            roundEvents.add(event);
        }
        return roundEvents;
    }

    private static Object deepClone(Object object) {
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

    private HashMap<Router, HashMap<Router, outputTuple>> createTable() {

        HashMap<Router, HashMap<Router, outputTuple>> out = new HashMap<>();

        Network network = this.network;

        for (Router router : network.getNetwork().vertexSet()) {
            HashMap<Router, outputTuple> map = new HashMap<>();
            Router emptyVia = new Router(-1);
            double emptyHops = -1;

            for (Router dest : network.getNetwork().vertexSet()) {
                map.put(dest, new outputTuple(emptyVia, emptyHops));
            }
            out.put(router, map);
        }

        for (Router router : network.getNetwork().vertexSet()) {
            HashMap<Router, outputTuple> map = out.get(router);
            HashMap<Router, Router> fastestPaths = router.getRoutingTable().getAllFastestPaths();
            map.put(router, new outputTuple(router, 0));
            for (Router dest : fastestPaths.keySet()) {
                Router via = fastestPaths.get(dest);
                double hops = router.getRoutingTable().getNumHops(dest, via);
                map.put(dest, new outputTuple(via, hops));
            }
            out.put(router, map);
        }
        return out;
    }

    private String createPrintable(HashMap<Router, HashMap<Router, outputTuple>> table) {
        StringBuilder sb = new StringBuilder();
        for (Router source : table.keySet()) {
            sb.append(source.getRouterID()).append("\t\t");
            HashMap<Router, outputTuple> map = table.get(source);
            for (Router dest : map.keySet()) {
                outputTuple tuple = map.get(dest);
                Router via = tuple.getVia();
                double numberOfHops = tuple.getNumberOfHops();
                sb.append(via.getRouterID()).append(",").append(numberOfHops).append("\t\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean countToInf() {
        HashMap<Router, HashMap<Router, outputTuple>> table = createTable();

        for (Router source : table.keySet()) {
            for (Router dest : table.get(source).keySet()) {
                if (table.get(source).get(dest).getNumberOfHops() > 100) {
                    return true;
                }
            }
        }
        return false;
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
            Network networkCopy = (Network)deepClone(originalNetwork);
            Network network = new Network(networkCopy);
            //Network network = new Network(originalNetwork);
            PriorityQueue<TopologicalEvent> eventsCopy = (PriorityQueue<TopologicalEvent>)deepClone(originalEvents);
            //PriorityQueue<TopologicalEvent> events = new PriorityQueue<>(originalEvents);
            PriorityQueue<TopologicalEvent> events = new PriorityQueue<>(eventsCopy);

            VectorRoutingSim simulator = new VectorRoutingSim(network, events);


            int roundNumber = 1;
            int lastEvent = 0;
            Boolean updated = true;
            StringBuilder sb = new StringBuilder();

            if (flag == 1) {
                sb.append("Round ").append(roundNumber).append("\n");
                String table = simulator.createPrintable(simulator.createTable());

                sb.append(table).append("\n");

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


                if (flag == 1) {
                    sb.append("Round " );
                    sb.append(roundNumber+1);
                    sb.append("\n");
                    HashMap<Router, HashMap<Router, outputTuple>> networkTable = simulator.createTable();
                    String table = simulator.createPrintable(networkTable);
                    sb.append(table).append("\n");
                }

                if (simulator.countToInf()) {
                    System.out.println("Count to infinity problem reached");
                    System.exit(1);
                }
                roundNumber++;
            }
            if (flag == 0) {
                String table = simulator.createPrintable(simulator.createTable());
                sb.append(table).append("\n");
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
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename.toString()));
            bw.write(sb.toString());
            bw.flush();
            bw.close();

//            System.out.println(sb.toString());
        }

    }
}
