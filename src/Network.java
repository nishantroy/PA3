import org.jgrapht.graph.*;

/**
 * Network of Routers represented by Weighted Graph
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

    public void setNetwork(SimpleWeightedGraph<Router, DefaultEdge> network) {
        this.network = network;
    }

    void addRouter(Router router) {
        if (!containsRouter(router)) {
            network.addVertex(router);
        }
    }

    void addLink(Router source, Router dest) {
        if (!containsLink(source, dest)) {
            network.addEdge(source, dest);
        }
    }

    void setLinkWeight(Router source, Router dest, int cost) {
        network.setEdgeWeight(network.getEdge(source, dest), cost);
    }

    private boolean containsRouter(Router router) {
        return network.containsVertex(router);
    }

    private boolean containsLink(Router source, Router dest) {
        return network.containsEdge(source, dest);
    }

}
