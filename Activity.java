public class Activity implements Action {

    /**
     * An action that can be taken by an entity.
     * Actions can be either an activity (involving movement, gaining health, etc)
     * or an animation (updating the image being displayed).
     */

    private final EntityAction entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(EntityAction entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {

            if (entity instanceof Sapling || entity instanceof Tree || entity instanceof Fairy || entity instanceof PersonSearching
                    || entity instanceof PersonFull || entity instanceof Worm)
            {entity.executeActivity(world, imageStore, scheduler);}

//                        entity.executeTreeActivity(world, imageStore, scheduler);
//                        break;
//                    case FAIRY:
//                        entity.executeFairyActivity(world, imageStore, scheduler);
//                        break;
//                    case PERSON_SEARCHING:
//                        entity.executePersonSearchingActivity(world, imageStore, scheduler);
//                        break;
//                    case PERSON_FULL:
//                        entity.executePersonFullActivity(world, imageStore, scheduler);
//                        break;
//                    default:
//                        throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", entity.getKind()));
//                }
    }
}

