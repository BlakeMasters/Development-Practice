
public interface EntityAnimated extends Entity{


//        private final String id;
//        private Point position;
//        private final List<PImage> images;
//        private int imageIndex;
//        private final double actionPeriod;
//        private final double animationPeriod;

    void nextImage();

    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    double getAnimationPeriod();

}



