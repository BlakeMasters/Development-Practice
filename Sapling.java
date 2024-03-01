import processing.core.PImage;

import java.util.List;

public class Sapling implements Entity, EntityAnimated, Plant, EntityAction{
        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;

        private final double actionPeriod;
        private final double animationPeriod;
        private int health;
        private final int healthLimit;

        public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {

            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;
            this.health = health;
            this.healthLimit = healthLimit;
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
            health++;
            if (transformPlant(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
            }
        }

        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
        }

        public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
                return !transformSapling(world, scheduler, imageStore);
        }

        //transform sapling and tree can be refactored with the extra abstract method but im not comfortable enough
        private boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
            if (health <= 0) {
                Entity stump = Factory.createStump(WorldLoader.STUMP_KEY + "_" + id, position, imageStore.getImageList(WorldLoader.STUMP_KEY));

                world.removeEntity(scheduler, this);

                world.tryAddEntity(stump);

                return true;
            } else if (health >= healthLimit) {
                Entity tree = Factory.createTreeWithDefaults(WorldLoader.TREE_KEY + "_" + id, position, imageStore.getImageList(WorldLoader.TREE_KEY));

                Tree tree2 = (Tree) tree;
                world.removeEntity(scheduler, this);

                world.tryAddEntity(tree2);

                tree2.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
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

    public void subtractHealth(){
        health = health -1;
    }
}


