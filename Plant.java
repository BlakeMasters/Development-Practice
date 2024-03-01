public interface Plant extends Entity{

    boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    void subtractHealth();

}
