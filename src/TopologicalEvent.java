import org.jgrapht.alg.flow.PadbergRaoOddMinimumCutset;

/**
 * Topological Events for Network.
 * - Round in which event occurs
 * - Routers affected
 * - New cost of link. (-1 means link removed)
 */
public class TopologicalEvent implements Comparable<TopologicalEvent> {
    private int round;
    private int sourceRouterID;
    private int destRouterID;
    private double cost;

    TopologicalEvent(int round, int sourceRouterID, int destRouterID, double cost) {
        this.round = round;
        this.sourceRouterID = sourceRouterID;
        this.destRouterID = destRouterID;
        this.cost = cost;
    }

    public int getRound() {
        return round;
    }

    public int getSourceRouterID() {
        return sourceRouterID;
    }

    public int getDestRouterID() {
        return destRouterID;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public int compareTo(TopologicalEvent o) {
        if (o == this || o.getRound() == this.getRound()) {
            return 0;
        } else if (o.getRound() < this.getRound()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "At round #" + round + ", a link between " + sourceRouterID + " and "
                + destRouterID + " will be added, with a cost of " + cost;
    }
}
