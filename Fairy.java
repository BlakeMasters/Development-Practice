import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class Fairy implements Entity,EntityAnimated,EntityAction{

    /**
     * An entity that exists in the world. See EntityKind for the
     * different kinds of entities that exist.
     */

        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final double actionPeriod;
        private final double animationPeriod;


        public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {

            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;

            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;

        }
        public int getImageIndex() {
            return imageIndex;
        }
        public void nextImage() {
            imageIndex = imageIndex + 1;
        }

        /**
         *
         * @param world - world model object
         * @param imageStore - ImageStore object holding all of the images in the project
         * @param scheduler
         */

        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> fairyTarget = world.findNearest(position, new ArrayList<>(List.of(Stump.class)));

            if (fairyTarget.isPresent()) {
                Point tgtPos = fairyTarget.get().getPosition();

                if (moveToFairy(world, fairyTarget.get(), scheduler)) {

                    Entity sapling = Factory.createSapling(WorldLoader.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(WorldLoader.SAPLING_KEY));

                    Sapling sap = (Sapling) sapling;
                    world.tryAddEntity(sap);
                    sap.scheduleActions(scheduler, world, imageStore);
                }
            }

            scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
        }





        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

                    scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
                    scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
        }







        public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
            if (position.adjacent(target.getPosition()) && target.getClass() != House.class) {
                world.removeEntity(scheduler, target);
                return true;
            } else {
                Point nextPos = nextPositionFairy(world, target.getPosition());

                if (!position.equals(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
                return false;
            }
        }




        public Point nextPositionFairy(WorldModel world, Point destPos) {
            PathingStrategy strat = new AStarPathingStrategy();

            Predicate<Point> canPassThrough = p -> world.withinBounds(p) && !(world.getOccupant(p).isPresent() && world.getOccupant(p).get().getClass() != House.class);
            BiPredicate<Point, Point> withinReach = (p1,p2) -> WorldModel.distanceSquared(p1,p2) == 1;
            List <Point> path = strat.computePath(this.position, destPos,canPassThrough,withinReach,
                    PathingStrategy.CARDINAL_NEIGHBORS);
            return path.isEmpty() ? this.position : path.getFirst();//?

//            int horiz = Integer.signum(destPos.x - position.x);
//            Point newPos = new Point(position.x + horiz, position.y);
//
//            if (horiz == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != House.class) {
//                int vert = Integer.signum(destPos.y - position.y);
//                newPos = new Point(position.x, position.y + vert);
//
//                if (vert == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != House.class) {
//                    newPos = position;
//                }
//            }
//
//            return newPos;
        }



//        public PImage getCurrentImage() {
//            return this.images.get(this.imageIndex % this.images.size());
//
//        }

        public double getAnimationPeriod() {

                    return animationPeriod;
        }

        /**
         * Helper method for testing. Preserve this functionality while refactoring.
         */
//        public String log(){
//            return this.id.isEmpty() ? null :
//                    String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
//        }

        public String getId() {
            return id;
        }

        public Point getPosition() {
            return position;
        }
        public void setPosition(Point pos) {
            this.position = pos;
        }
    public List<PImage> getImages(){
        return this.images;
    }
    }

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
