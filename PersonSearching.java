import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.*;

import processing.core.PImage;

public class PersonSearching implements Entity, People, EntityAnimated, EntityAction{

        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final int resourceLimit;
        private int resourceCount;
        private final double actionPeriod;
        private final double animationPeriod;

        public PersonSearching(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod) {

            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
            this.resourceLimit = resourceLimit;
            this.resourceCount = resourceCount;
            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;

        }

        public int getImageIndex() {return imageIndex;}
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
            Optional<Entity> target = world.findNearest(position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

            if (target.isEmpty() || !moveToSearching(world, (Plant) target.get(), scheduler) || !transformSearching(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
            }
        }

        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
        }

        private boolean transformSearching(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
            if (resourceCount >= resourceLimit) {
                Entity dude = Factory.createPersonFull(id, position, actionPeriod, animationPeriod, resourceLimit, images);
                EntityAnimated dude2 = (EntityAnimated) dude;

                world.removeEntity(scheduler, this);
                scheduler.unscheduleAllEvents(this);

                world.tryAddEntity(dude2);
                dude2.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
        }
//tree or sapling
        public boolean moveToSearching(WorldModel world, Plant target, EventScheduler scheduler) {
            if (position.adjacent(target.getPosition())) {
                resourceCount += 1;
                target.subtractHealth();
                return true;
            } else {
                Point nextPos = nextPositionDude(world, target.getPosition());

                if (!position.equals(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
                return false;
            }
        }

//        public Point nextPositionDude(WorldModel world, Point destPos) {
//            int horiz = Integer.signum(destPos.x - position.x);
//            Point newPos = new Point(position.x + horiz, position.y);
//
//            if (horiz == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != Stump.class) {
//                int vert = Integer.signum(destPos.y - position.y);
//                newPos = new Point(position.x, position.y + vert);
//
//                if (vert == 0 || world.getOccupant(newPos).isPresent() && world.getOccupant(newPos).get().getClass() != Stump.class) {
//                    newPos = position;
//                }
//            }
//
//            return newPos;
//        }

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


