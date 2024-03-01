public class Animation implements Action{

    /**
     * An action that can be taken by an entity.
     * Actions can be either an activity (involving movement, gaining health, etc)
     * or an animation (updating the image being displayed).
     */

        private final EntityAnimated entity;
        private final int repeatCount;

        public Animation(EntityAnimated entity, int repeatCount) {
            this.entity = entity;
            this.repeatCount = repeatCount;
        }
        public void executeAction(EventScheduler scheduler) {
            entity.nextImage();

            if (repeatCount != 1) {

                scheduler.scheduleEvent(entity, Factory.createAnimationAction(entity, Math.max(repeatCount - 1, 0)), entity.getAnimationPeriod());
            }
        }

}
