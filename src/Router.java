import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Router Class
 * routerID: Identifies the router
 * routingVector: The routing vector for this router
 * changed: Boolean to indicate whether the routing vector was updated.
 * Used to determine whether or not to broadcast routing vector in next round.
 */

class Router implements Serializable {
    private int routerID;
    private RoutingTable routingTable;
    private boolean changed;

    Router(int routerID) {
        this.routerID = routerID;
        this.routingTable = new RoutingTable(this);
    }

    int getRouterID() {
        return routerID;
    }

    RoutingTable getRoutingTable() {
        return routingTable;
    }


    /**
     * Checks if router's table was changed during last iteration
     * @return true if changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the changed field
     * @param changed true/false
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }


    /**
     * Updates the full routing table (path from R1 to R2 via RX)
     * @param dest R2
     * @param via RX
     * @param cost Cost to R2
     * @param numHops # of hops to R2
     */
    void updateTable(Router dest, Router via, double cost, double numHops) {
        if (getRouterID() == dest.getRouterID()) {
            return;
        }
        updateCost(dest, via, cost);
        routingTable.setNumHops(dest, via, numHops);
    }

    /**
     * Updates cost for link
     * @param dest Target router
     * @param via Router through which we are going
     * @param cost Cost of link
     */
    void updateCost(Router dest, Router via, double cost) {
        if (getRouterID() == dest.getRouterID()) {
            return;
        }

        ViaMap viaMap;
        HashMap<Router, ViaMap> table = this.routingTable.getTable();
        if (!table.containsKey(dest)) {
            table.put(dest, new ViaMap());
        }
        viaMap = table.get(dest);
        viaMap.setCost(via, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Router router = (Router) o;

        return routerID == router.routerID;

    }

    @Override
    public int hashCode() {
        return routerID;
    }

    @Override
    public String toString() {
        return String.valueOf("ROUTER " + routerID);
    }

}
