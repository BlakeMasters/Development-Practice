import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface People {


    Point getPosition();

    default Point nextPositionDude(WorldModel world, Point destPos) {

        PathingStrategy strat = new AStarPathingStrategy();

        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && !(world.getOccupant(p).isPresent() && world.getOccupant(p).get().getClass() != Stump.class);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> WorldModel.distanceSquared(p1,p2) == 1;
        List<Point> path = strat.computePath(getPosition(), destPos,canPassThrough,withinReach,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? getPosition() : path.getFirst();

//        int horiz = Integer.signum(destPos.x - getPosition().x);
//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
//
//        if (horiz == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != Stump.class) {
//            int vert = Integer.signum(destPos.y - getPosition().y);
//            newPos = new Point(getPosition().x, getPosition().y + vert);
//
//            if (vert == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != Stump.class) {
//                newPos = getPosition();
//            }
//        }
//
//        return newPos;
    }



}
