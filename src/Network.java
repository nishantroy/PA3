import org.jgrapht.graph.*;

import java.util.HashMap;

/**
 * Network of Routers represented by Simple Weighted Graph
 */
class Network {
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
     * Executes a topological event. Adds/Removes links as necessary, and changes all the routing tables.
     * TODO: CAUSING SOME ISSUE! INFINITE LOOP WHEN EVENT IS RUN!
     * @param event Event to execute
     */
    void executeEvent (TopologicalEvent event) {
        Router R1 = new Router(event.getSourceRouterID());
        Router R2 = new Router(event.getDestRouterID());
        double cost = event.getCost();
        double numHops = 1;
        if (cost == -1) {
            cost = Double.POSITIVE_INFINITY;
            numHops = cost;
        }
        addRouter(R1);
        addRouter(R2);
        addLink(R1, R2);
        setLinkWeight(R1, R2, cost);
        DefaultEdge edge = getNetwork().getEdge(R1, R2);
        R1 = getNetwork().getEdgeSource(edge);
        R2 = getNetwork().getEdgeTarget(edge);

        if (cost == Double.POSITIVE_INFINITY) {
            for (Router router : network.vertexSet()) {
                R1.updateTable(router, R2, cost, numHops);
                R2.updateTable(router, R1, cost, numHops);
            }
        } else {
            R1.updateTable(R2, R2, cost, 1);
            R2.updateTable(R1, R1, cost, 1);
        }

    }

    /**
     * Checks if no router was changed
     * @return True if protocol converged
     */
    boolean isConverged() {
        for (Router router : network.vertexSet()) {
            if (router.isChanged()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints out the routing vectors (shortest paths) for all routers
     */
    void printRoutingVectors() {
        StringBuilder sb = new StringBuilder();
        for (Router router : network.vertexSet()) {
//            sb.append(router.getVectorPrint());
        }
        System.out.println(sb.toString());
    }

    /**
     * Prints out the whole routing tables (path from R1 to R2 via RX) for all routers
     */
    void printRoutingTables() {
        StringBuilder sb = new StringBuilder();
        for (Router router : network.vertexSet()) {
            sb.append("TABLE FOR ROUTER #" + router + "\n\n");
            sb.append(router.getRoutingTable().toString());
        }
        System.out.println(sb.toString());
    }

    void printFastestPaths() {
        StringBuilder sb = new StringBuilder();
        for (Router router : network.vertexSet()) {
            sb.append("\nFastest path from " + router + ":\n");
            sb.append(router.getRoutingTable().printFastestPath());

        }
        System.out.println(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SOURCE\t\t|\tDEST\t\t|\tCOST\n");
        sb.append("------------|---------------|---------\n");

        for (DefaultEdge edge : network.edgeSet()) {
            Router source = network.getEdgeSource(edge);
            Router dest = network.getEdgeTarget(edge);
//            HashMap<Router, Tuple> map = source.getRoutingVector().getVector();
//            double cost = map.containsKey(dest) ? map.get(dest).getCost() : -1;
//            sb.append(source).append("\t\t|\t").append(dest).append("\t\t|\t").append(cost).append("\n");
        }
        return sb.toString();
    }

}
