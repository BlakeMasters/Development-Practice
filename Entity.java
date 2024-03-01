import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {

    //    private final String id;
//    private Point position;
//    private final List<PImage> images;
    /*
     *
     * @param world - world model object
     * @param imageStore - ImageStore object holding all of the images in the project
     * @param scheduler
     */

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    String getId();

    Point getPosition();
    void setPosition(Point pos);

    default PImage getCurrentImage() {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    default String log(){
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().x, this.getPosition().y, this.getImageIndex());
    }

    int getImageIndex();

    List<PImage> getImages();


}
