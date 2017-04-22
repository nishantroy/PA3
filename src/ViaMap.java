import java.util.HashMap;

/**
 * Sub-map in Routing Table
 */
public class ViaMap {
    private HashMap<Router, Tuple> map;

    public ViaMap() {
        this.map = new HashMap<>();
    }

    public HashMap<Router, Tuple> getMap() {
        return map;
    }

     boolean hasEntry(Router router) {
        return map.containsKey(router);
    }

    double getCost(Router via) {
        Tuple tuple = map.get(via);
        return tuple.getCost();
    }

    void setCost(Router via, double cost) {
        if (!hasEntry(via)) {
            map.put(via, new Tuple());
        }
        Tuple tuple = map.get(via);
        tuple.setCost(cost);
    }

    double getNumberOfHops(Router router) {
        Tuple tuple = map.get(router);
        return tuple.getNumberOfHops();
    }

    void setNumberOfHops(Router via, double numHops) {
        if (!hasEntry(via)) {
            map.put(via, new Tuple());
        }
        Tuple tuple = map.get(via);
        tuple.setNumberOfHops(numHops);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VIA\t\t|\tCOST\t\t|\tHOPS\n");
        sb.append("-----------------------------------------------\n");
        for (Router key : map.keySet()) {
            Tuple tuple = map.get(key);
            double cost = tuple.getCost();
            double hops = tuple.getNumberOfHops();
            sb.append(key.getRouterID()+"\t\t|\t"+cost+"\t\t\t|\t"+hops+"\n\n");
        }
        return sb.toString();
    }
}
