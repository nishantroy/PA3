import java.util.HashMap;

/**
 * Routing Table for each Router
 */

class RoutingTable {


    /**
     * All possible paths to a router
     */
    private HashMap<Router, ViaMap> table;

    /**
     * To get to some router, which one of my neighbors gives me the fastest path
     */
    private HashMap<Router, Router> fastestPath;

    RoutingTable() {
        this.table = new HashMap<>();
        this.fastestPath = new HashMap<>();
    }

    HashMap<Router, ViaMap> getTable() {
        return table;
    }

    /**
     * Checks if routing table has an entry for some router via another router
     * @param dest Destination router
     * @param via Router via which we're looking for a path
     * @return True if path exists
     */
    boolean hasEntry(Router dest, Router via) {
        return table.get(dest).hasEntry(via);
    }

    /**
     * Cost to reach a router through another router
     * @param dest Destination router
     * @param via Router via which we want to go
     * @return Cost of path to destination via another router (double)
     */
    double getCost(Router dest, Router via) {
        return table.get(dest).getCost(via);
    }

    /**
     * Sets cost
     * @param dest Destination to set cost for
     * @param via Router we are going through
     * @param cost Cost of path
     */
    void setCost(Router dest, Router via, double cost) {
        ViaMap viaMap;
        if (!table.containsKey(dest)) {
            table.put(dest, new ViaMap());
        }
        viaMap = table.get(dest);
        viaMap.setCost(via, cost);
    }

    /**
     * # of hops in path
     * @param dest Destination router
     * @param via Router we are going through
     * @return # of hops (double)
     */
    double getNumHops(Router dest, Router via) {
        return table.get(dest).getNumberOfHops(via);
    }

    /**
     * Set # of hops in path
     * @param dest Destination router
     * @param via Router we are going through
     * @param numHops # of hops (double)
     */
    void setNumHops(Router dest, Router via, double numHops) {
        ViaMap viaMap;
        if (!table.containsKey(dest)) {
            table.put(dest, new ViaMap());
        }
        viaMap = table.get(dest);
        viaMap.setNumberOfHops(via, numHops);

    }

    Router getFastestPath(Router dest) {
        return fastestPath.get(dest);
    }

    void setFastestPath(Router dest, Router via) {
        fastestPath.put(dest, via);
    }

    void updateAllFastestPaths() {
        for (Router dest : table.keySet()) {
            HashMap<Router, Tuple> map = table.get(dest).getMap();
            Router currFastest = null;
            for (Router via : map.keySet()) {
                if (currFastest == null) {
                    currFastest = via;
                } else {
                    Tuple info = map.get(via);
                    double cost = info.getCost();
                    double currCost = getCost(dest, currFastest);
                    if (cost < currCost) {
                        currFastest = via;
                    }
                }
            }
            fastestPath.put(dest, currFastest);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Router key : table.keySet()) {
            sb.append("Dest " + key + " Via:\n*********************\n\n");
            sb.append(table.get(key).toString());

        }
        return sb.toString();
    }

    String printFastestPath() {
        StringBuilder sb = new StringBuilder();
        for (Router router : fastestPath.keySet()) {
            double cost = getCost(router, fastestPath.get(router));
            sb.append("To " + router + " is via " + fastestPath.get(router) + " with cost " + cost + "\n");
        }
        return sb.toString();
    }
}
