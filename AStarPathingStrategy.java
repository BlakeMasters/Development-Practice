import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math.*;

class AStarPathingStrategy implements PathingStrategy{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        //1
        HashMap<Point, Node> nodeMap = new HashMap<>();
        //nodeMap.put(any point val, node to store);
        PriorityQueue<Node> prio = new PriorityQueue<>(Comparator.comparingInt(Node::getF));

        //2
        Node curr1 = new Node (start, 0, ManhattanDistance(start, end), ManhattanDistance(start, end), null);
        //prio.add(curr1);


        //3
        Node last = pathLoop(nodeMap, prio, potentialNeighbors, curr1, end, canPassThrough,
                withinReach);
        while (last != null && last.getPrior() != null && !(last.equals(curr1))){
            path.add(last.getPoint());
            last = last.getPrior();
            //System.out.println(last.getPoint());
        }

        Collections.reverse(path);
        //System.out.println(path);
        //supposedly flips list
        //while (ManhattanDistance(curr.getPoint(), end)>1{} maybe?

        return path;
    }

    public Node pathLoop(HashMap<Point, Node> nodeMap,
                         PriorityQueue<Node> prio, Function<Point, Stream<Point>> potentialNeighbors,
                         Node curr, Point end, Predicate<Point> canPassThrough,
                         BiPredicate<Point, Point> withinReach){

        List<Point> neighbors = potentialNeighbors.apply(curr.getPoint()).collect(Collectors.toList());
        //System.out.println(neighbors);
        //System.out.println(ManhattanDistance(curr.getPoint(), end));

        for (Point point : neighbors) {
            //System.out.println(point);
            if ( !(nodeMap.containsKey(point)) && canPassThrough.test(point)) {
                //a
                int newG = curr.getG() + 1;
                //System.out.println(newG);

                //b
                Node neighbor = new Node(point, newG, ManhattanDistance(point, end),ManhattanDistance(point, end)+newG, curr);
                //System.out.println(neighbor.getPrior());
                //System.out.println(neighbor.getH());
                if (prio.contains(neighbor)) {
                    Node removed = prio.stream().filter(node -> node.equals(neighbor)).findFirst()
                            .orElse(null);

                    removed.setPrior(curr);
                    //System.out.println(removed.getPoint());


                    if (removed != null && (removed.getG() > newG)) {
                        removed.setG(newG);
                        //removed.setPrior(curr);
                        prio.add(removed);
                    } else {
                        continue;
                    }
                }
                //c
                //d&e

                //f
                prio.add(neighbor);
            }
        }
        //4
        nodeMap.put(curr.getPoint(), curr); //this puts current, but should it put neighbor
        //5


        //System.out.println(prio);
        if (prio.isEmpty()){return null;}
        Node newHead = prio.remove();
        //newHead.setPrior(curr);

        //could be problem
        //prio.add(newHead);

        //could be problem, unlikely
        if (newHead.getPoint()!= null && withinReach.test(newHead.getPoint(), end))
        {return newHead;}
        else if (ManhattanDistance(newHead.getPoint(), end)>1){
            return pathLoop(nodeMap, prio, potentialNeighbors, newHead, end, canPassThrough,
                    withinReach);}
        else{return null;}

    }

    public static int ManhattanDistance(Point h, Point t){

        return (Math.abs(h.x - t.x) + Math.abs(h.y - t.y));
    }

}