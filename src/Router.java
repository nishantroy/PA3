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

    public int getRouterID() {
        return routerID;
    }

    public RoutingTable getRoutingTable() {
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


//        this.routingTable.setCost(dest, via, cost);
//        this.routingTable.setNumHops(dest, via, numHops);
//        Router fastestVia = routingTable.getFastestPath(dest);
//        if (fastestVia == null || routingTable.getCost(dest, via) < routingTable.getCost(dest, fastestVia)) {
//            routingTable.setFastestPath(dest, via);
//        }
//        setChanged(true);
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

//        this.routingTable.setCost(dest, via, cost);
//        Router fastestVia = routingTable.getFastestPath(dest);
//        if (fastestVia == null || routingTable.getCost(dest, via) < routingTable.getCost(dest, fastestVia)) {
//            routingTable.setFastestPath(dest, via);
//        }
//        setChanged(true);

        ViaMap viaMap;
        HashMap<Router, ViaMap> table = this.routingTable.getTable();
        if (!table.containsKey(dest)) {
            table.put(dest, new ViaMap());
        }
        viaMap = table.get(dest);
        viaMap.setCost(via, cost);
    }

//    /**
//     * Receives routing table from neighbor, updates routing table accordingly.
//     * TODO: NOT WORKING PROPERLY AFTER EVENT OCCURS IT SEEMS
//     * @param broadcaster Router broadcasting its routing table
//     * @return True if routing table was updated
//     */
//    boolean receiveBroadcast(Router broadcaster) {
//        setChanged(false);
//
//        // Get received routing table
//
//        RoutingTable broadcasterRoutingTable = broadcaster.getRoutingTable();
//        HashMap<Router, ViaMap> broadcast = broadcasterRoutingTable.getTable();
//
//
//        // Get cost and number of hops to broadcaster
//        Router fastestVia = routingTable.getFastestPath(broadcaster);
//        double costToBroadcaster = routingTable.getCost(broadcaster, fastestVia);
//        double hopsToBroadcaster = routingTable.getNumHops(broadcaster, fastestVia);
//
//        for (Router dest : broadcast.keySet()) {
//
//            // Get broadcast viaMap
//            ViaMap receivedViaMap = broadcast.get(dest);
//            HashMap<Router, Tuple> viaVector = receivedViaMap.getMap();
//
//            ViaMap myViaMap = getRoutingTable().getTable().get(dest);
//
//            for (Router via : viaVector.keySet()) {
//                if (getRouterID() == dest.getRouterID()) {
//                    continue;
//                }
//                Tuple broadcastInfo = viaVector.get(via);
//                double broadcastCost = broadcastInfo.getCost();
//                double broadcastHops = broadcastInfo.getNumberOfHops();
//                double totalCost = broadcastCost + costToBroadcaster;
//                double totalHops = broadcastHops + hopsToBroadcaster;
//                if (!myViaMap.hasEntry(broadcaster) || totalCost < myViaMap.getCost(broadcaster)) {
//                    setChanged(true);
//                    System.out.println("FOUND NEW PATH FROM " + this + " TO " + dest + " VIA " + via);
//                    updateTable(dest, broadcaster, totalCost, totalHops);
//                }
//
//            }
//
//        }
//
//        return isChanged();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Router router = (Router) o;

        return routerID == router.routerID;

    }

    @Override
    public int hashCode() {
        return Integer.hashCode(routerID);
    }

    @Override
    public String toString() {
        return String.valueOf("ROUTER " + routerID);
    }

//    public String getVectorPrint() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("***********************************************\n VECTOR FOR ROUTER" + getRouterID()
//                + "\n***********************************************\n");
//        sb.append(getRoutingVector().toString());
//        sb.append("\n\n");
//        return sb.toString();
//    }
}
