import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Butterfly   implements  Entity, EntityAnimated, EntityAction {


        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final double actionPeriod;
        private final double animationPeriod;


        public Butterfly(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {

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
        }

        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

            scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
            scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
        }

        public boolean moveToBut(WorldModel world, Entity target, EventScheduler scheduler) {

            return false;
        }



        public Point nextPositionBut(WorldModel world, Point destPos) {

            return position;
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





