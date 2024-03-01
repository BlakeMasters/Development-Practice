import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.*;

import processing.core.PImage;
public class Hole implements Entity{




    /**
     * An entity that exists in the world. See EntityKind for the
     * different kinds of entities that exist.
     */


    private final String id;
    private Point position;
    private final List<PImage> images;
    private final int imageIndex;


    public Hole(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    /**
     *
     * @param world - world model object
     * @param imageStore - ImageStore object holding all of the images in the project
     * @param scheduler
     */


//        public PImage getCurrentImage() {
//            return this.images.get(this.imageIndex % this.images.size());
//
//        }
    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
//        public String log(){
//            return this.id.isEmpty() ? null :
//                    String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
//        }
    //everything needs log/current image
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

    public int getImageIndex() {
        return imageIndex;
    }

}


