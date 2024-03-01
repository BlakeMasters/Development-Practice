import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity, EntityAnimated{

        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final double animationPeriod;

        public Obstacle(String id, Point position, List<PImage> images, double animationPeriod) {
            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
            this.animationPeriod = animationPeriod;
        }
        public int getImageIndex() {
            return imageIndex;
        }

        public void nextImage() {
            imageIndex = imageIndex + 1;
        }

        /**
         * @param world      - world model object
         * @param imageStore - ImageStore object holding all of the images in the project
         * @param scheduler  - idk remove warning
         */

        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
            scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
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
//    public String log(){
//        return this.id.isEmpty() ? null :
//                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
//    }
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





