import java.util.HashMap;

/**
 * Routing Table for entire Network
 */

public class RoutingTable {
    private HashMap<Router, RoutingVector> table;

    public RoutingTable() {
        this.table = new HashMap<>();
    }

    public HashMap<Router, RoutingVector> getTable() {
        return table;
    }

    public void setTable(HashMap<Router, RoutingVector> table) {
        this.table = table;
    }
}
