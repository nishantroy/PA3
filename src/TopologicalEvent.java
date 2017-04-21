import org.jgrapht.alg.flow.PadbergRaoOddMinimumCutset;

/**
 * Topological Events for Network
 */
public class TopologicalEvent implements Comparable<TopologicalEvent> {
    private int round;
    private int sourceRouterID;
    private int destRouterID;
    private int cost;

    TopologicalEvent(int round, int sourceRouterID, int destRouterID, int cost) {
        this.round = round;
        this.sourceRouterID = sourceRouterID;
        this.destRouterID = destRouterID;
        this.cost = cost;
    }

    private int getRound() {
        return round;
    }

    public int getSourceRouterID() {
        return sourceRouterID;
    }

    public int getDestRouterID() {
        return destRouterID;
    }

    public int getCost() {
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
}
