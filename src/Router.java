/**
 * Router Class
 */

class Router {
    private int routerID;
    private RoutingVector routingVector;

    Router(int routerID) {
        this.routerID = routerID;
        this.routingVector = new RoutingVector();
    }

    public int getRouterID() {
        return routerID;
    }

    public RoutingVector getRoutingVector() {
        return routingVector;
    }

    public void setRouterID(int routerID) {
        this.routerID = routerID;
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
        return Integer.hashCode(routerID);
    }

    @Override
    public String toString() {
        return String.valueOf("ROUTER" + routerID);
    }
}
