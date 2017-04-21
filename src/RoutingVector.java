import java.util.HashMap;

/**
 * Routing Vector maintained by each Router
 */
class RoutingVector {
    private HashMap<Router, Tuple> vector;

    private class Tuple {
        private Router nextHop;
        private int cost;

        public Tuple() {
        }

        public Router getNextHop() {
            return nextHop;
        }

        public void setNextHop(Router nextHop) {
            this.nextHop = nextHop;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }

    RoutingVector() {
        this.vector = new HashMap<>();
    }

    public HashMap<Router, Tuple> getVector() {
        return vector;
    }

    public void setVector(HashMap<Router, Tuple> vector) {
        this.vector = vector;
    }
}
