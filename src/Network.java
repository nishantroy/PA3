import org.jgrapht.graph.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Network of Routers represented by Simple Weighted Graph
 */
class Network implements Serializable{
    private SimpleWeightedGraph<Router, DefaultEdge> network;

    Network() {
        super();
        this.network = new SimpleWeightedGraph<>(DefaultEdge.class);
    }

    Network(Network network) {

        this.network = network.getNetwork();
    }

    public SimpleWeightedGraph<Router, DefaultEdge> getNetwork() {
        return network;
    }

    /**
     * Adds router to the network
     * @param router Router to add
     */
    void addRouter(Router router) {
        if (!containsRouter(router)) {
            network.addVertex(router);
        }
    }

    /**
     * Adds link between 2 routers
     * @param source Router 1
     * @param dest Router 2
     */
    void addLink(Router source, Router dest) {
        addRouter(source);
        addRouter(dest);
        if (!containsLink(source, dest)) {
            network.addEdge(source, dest);
        }
    }

    /**
     * Sets cost of link
     * @param source Router 1
     * @param dest Router 2
     * @param cost Cost of link
     */
    void setLinkWeight(Router source, Router dest, double cost) {
        network.setEdgeWeight(network.getEdge(source, dest), cost);
    }

    /**
     * Checks if network contains a certain router
     * @param router Router to check for
     * @return True if network contains this router
     */
    private boolean containsRouter(Router router) {
        return network.containsVertex(router);
    }

    /**
     * Checks if network contains a link
     * @param source Router 1
     * @param dest Router 2
     * @return True if network contains link between Router 1 and 2
     */
    private boolean containsLink(Router source, Router dest) {
        return network.containsEdge(source, dest);
    }


    /**
     * Executes topological events. Adds/Removes links as necessary, and changes all the routing tables.
     * @param events Event list to execute
     */
    void executeEventsAndUpdate (HashSet<TopologicalEvent> events) {
        for (TopologicalEvent event: events) {
            Router R1 = new Router(event.getSourceRouterID());
            Router R2 = new Router(event.getDestRouterID());
            double cost = event.getCost();
            if (cost == -1) {
                cost = Double.POSITIVE_INFINITY;
            }

            addRouter(R1);
            addRouter(R2);

            if (network.containsVertex(R1)) {
                Set<Router> routers = network.vertexSet();

                for (Router router : routers) {
                    if (R1.equals(router)) {
                        R1 = router;
                        break;
                    }
                }
            }

            if (network.containsVertex(R2)) {
                Set<Router> routers = network.vertexSet();

                for (Router router : routers) {
                    if (R2.equals(router)) {
                        R2 = router;
                        break;
                    }
                }
            }

            addLink(R1, R2);
            setLinkWeight(R1, R2, cost);
            DefaultEdge edge = getNetwork().getEdge(R1, R2);
            R1 = network.getEdgeSource(edge);
            R2 = network.getEdgeTarget(edge);

            if (cost == Double.POSITIVE_INFINITY) {
                for (Router router : network.vertexSet()) {
                    // Cost to go from R1 to every other router in the network VIA R2 is now infinity
                    R1.updateTable(router, R2, cost, Double.POSITIVE_INFINITY);
                    // Cost to go from R2 to every other router in the network VIA R1 is now infinity
                    R2.updateTable(router, R1, cost, Double.POSITIVE_INFINITY);
                }
            } else {
                R1.updateTable(R2, R2, cost, 1);
                R2.updateTable(R1, R1, cost, 1);
            }
            R1.setChanged(true);
            R2.setChanged(true);

            HashSet<Router> r1Neigbors = getNeighbors(R1);
            HashSet<Router> r2Neigbors = getNeighbors(R2);

            updateNeighbors(R1, R2, r1Neigbors, cost);
            updateNeighbors(R2, R1, r2Neigbors, cost);

            }
    }

    void updateNeighbors(Router R1, Router R2, HashSet<Router> neighbors, double cost) {
        for (Router neighbor : neighbors) {

            if (neighbor.equals(R2)) continue;

            double costNeighborToR2 = getLinkWeight(neighbor, R2);
            double costNeighborToR1 = getLinkWeight(neighbor, R1);

            if (costNeighborToR2 != Double.POSITIVE_INFINITY ) {
                double costUpdate;
                if (cost != Double.POSITIVE_INFINITY) {
                    costUpdate = cost + costNeighborToR2;
                } else {
                    costUpdate = Double.POSITIVE_INFINITY;
                }
                neighbor.updateCost(R1, R2, costUpdate);
                neighbor.setChanged(true);
            }

            if (costNeighborToR1 != Double.POSITIVE_INFINITY ) {
                double costUpdate;
                if (cost != Double.POSITIVE_INFINITY) {
                    costUpdate = cost + costNeighborToR1;
                } else {
                    costUpdate = Double.POSITIVE_INFINITY;
                }
                neighbor.updateCost(R2, R1, costUpdate);
                neighbor.setChanged(true);
            }

        }
    }

    /**
     * Returns all neighboring routers
     * @param router Router to fetch neighbors for
     * @return Set of neighbors of supplied router
     */
    HashSet<Router> getNeighbors(Router router) {
        Set<DefaultEdge> outgoingEdges = network.edgesOf(router);
        HashSet<Router> neighbors = new HashSet<>();
        for (DefaultEdge edge : outgoingEdges) {
            Router R2 = network.getEdgeTarget(edge);
            Router R1 = network.getEdgeSource(edge);
            if (!R2.equals(router)) {
                neighbors.add(R2);
            }

            if (!R1.equals(router)) {
                neighbors.add(R1);
            }
        }
        return neighbors;
    }

    /**
     * Cost of link between 2 routers
     * @param R1 Router 1
     * @param R2 Router 2
     * @return Link weight (double) between the 2 routers
     */
    private double getLinkWeight(Router R1, Router R2) {
        try {
            return network.getEdgeWeight(network.getEdge(R1, R2));
        } catch (Exception e) {
            return Double.POSITIVE_INFINITY;
        }
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SOURCE\t\t|\tDEST\t\t|\tCOST\n");
        sb.append("------------|---------------|---------\n");

        for (DefaultEdge edge : network.edgeSet()) {
            Router source = network.getEdgeSource(edge);
            Router dest = network.getEdgeTarget(edge);
            double cost = network.getEdgeWeight(edge);
            sb.append(source).append("\t\t|\t").append(dest).append("\t\t|\t").append(cost).append("\n");
        }
        return sb.toString();
    }

}
