import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Reads in Files
 */
public class FileParser {
    Network readNetwork(String fileName) throws Exception {
        Scanner scanner = new Scanner(new File(fileName));
        Network network = new Network();
        int numRouters = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= numRouters; i++) {
            network.addRouter(new Router(i));
        }
        String[] data;
        while (scanner.hasNextLine()) {
            data = scanner.nextLine().split(" ");
            Router routerSource = new Router(Integer.parseInt(data[0]));
            Router routerDest = new Router(Integer.parseInt(data[1]));
            int cost = Integer.parseInt(data[2]);
            network.addLink(routerSource, routerDest);
            network.setLinkWeight(routerSource, routerDest, cost);
        }

        return network;
    }

    PriorityQueue<TopologicalEvent> readTopologicalEvents(String fileName) throws Exception {
        PriorityQueue<TopologicalEvent> pq = new PriorityQueue<>();

        Scanner scanner = new Scanner(new File(fileName));
        String[] data;
        while (scanner.hasNextLine()) {
            data = scanner.nextLine().split(" ");
            int roundNum = Integer.parseInt(data[0]);
            int routerSource = Integer.parseInt(data[1]);
            int routerDest = Integer.parseInt(data[2]);
            int cost = Integer.parseInt(data[3]);
            pq.add(new TopologicalEvent(roundNum, routerSource, routerDest, cost));
        }

        return pq;
    }
}
