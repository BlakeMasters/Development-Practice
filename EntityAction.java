public interface EntityAction extends Entity {

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);


}
