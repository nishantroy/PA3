/**
 * Tuple for Routing Table. Stores cost and # of hops to get to a certain router through a certain path.
 * Cost & # of hops may be (Double.POSITIVE_INFINITY) if link is down or no path has been found b/w routers.
 */
class Tuple {
    private double cost;
    private double numberOfHops;

    Tuple() {
        super();
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getNumberOfHops() {
        return numberOfHops;
    }

    public void setNumberOfHops(double numberOfHops) {
        this.numberOfHops = numberOfHops;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "cost=" + cost +
                ", numberOfHops=" + numberOfHops +
                '}';
    }
}
