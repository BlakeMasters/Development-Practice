import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;


public class Worm  implements  Entity, EntityAnimated, EntityAction {

        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final double actionPeriod;
        private final double animationPeriod;


        public Worm(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {

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
         * @param world - world model object
         * @param imageStore - ImageStore object holding all of the images in the project
         * @param scheduler
         */
//world.removeEntity(scheduler, this);
//scheduler.unscheduleAllEvents(this);
        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            //currently target trees until holes made progresses
            Optional<Entity> wormTarget = world.findNearest(position, new ArrayList<>(List.of(Hole.class)));
//hole needs to spawn before worm
            if (wormTarget.isPresent()) {
                //System.out.println("Here");
                Point tgtPos = wormTarget.get().getPosition();

                if (moveToWorm(world, wormTarget.get(), scheduler)) {
//private void removeEntityAt(Point pos)
                    //surely this only kills itself at the hole
                    //does scheduler even work properly
                    Point butterspawn = position;
                    world.removeEntity(scheduler, this);
                    Entity barry = Factory.createBut(WorldLoader.BUTTERFLY_KEY, butterspawn, 0.5,
                            0.2, imageStore.getImageList(WorldLoader.BUTTERFLY_KEY));
                    world.tryAddEntity(barry);
                    ((Butterfly)barry).scheduleActions(scheduler,world, imageStore);

                    //this works
                }
            }

            //System.out.println("Here2");
            scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);




        }

//            Optional<Entity> fairyTarget = world.findNearest(position, new ArrayList<>(List.of(Stump.class)));
//
//            if (fairyTarget.isPresent()) {
//                //System.out.print("here");
//                Point tgtPos = fairyTarget.get().getPosition();
//
//                if (moveToWorm(world, fairyTarget.get(), scheduler)) {
//
//                    Entity sapling = Factory.createSapling(WorldLoader.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(WorldLoader.SAPLING_KEY));
//
//                    Sapling sap = (Sapling) sapling;
//                    world.tryAddEntity(sap);
//                    sap.scheduleActions(scheduler, world, imageStore);
//                }
//            }
//
//            scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
//        }



    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
    }







        public boolean moveToWorm(WorldModel world, Entity target, EventScheduler scheduler) {
            if (position.adjacent(target.getPosition()) && target.getClass() != House.class) {
                return true;
            } else {
                Point nextPos = nextPositionWorm(world, target.getPosition());

                if (!position.equals(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
                return false;
            }
        }



        public Point nextPositionWorm(WorldModel world, Point destPos) {
            PathingStrategy recoupe = new AStarPathingStrategy();

            Predicate<Point> canPassThrough = p -> world.withinBounds(p) && world.getOccupant(p).isEmpty();
            BiPredicate<Point, Point> withinReach = (p1, p2) -> WorldModel.distanceSquared(p1,p2) == 1;
            List <Point> path = recoupe.computePath(this.position, destPos,canPassThrough,withinReach,
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



