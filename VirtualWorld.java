import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;

    private ImageStore imageStore;

    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = appTime / timeScale - scheduler.getCurrentTime();
        this.update(frameTime);
        view.drawViewport();
    }

    public void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public Entity holeSpawn(){
        Random ran = new Random();
        int randx = ran.nextInt(2) + 2;
        Random ran2 = new Random();
        int randy = ran2.nextInt(4) + 1;
        Point pressed = mouseToPoint();
        Point newHole = new Point(pressed.x + randx, pressed.y + randy);
        if (world.getOccupant(newHole).isPresent()){
            return holeSpawn();

        }
        else{
//            Entity hole = Factory.createHole(WorldLoader.HOLE_KEY, newHole, imageStore.getImageList(WorldLoader.HOLE_KEY));
            // would have liked this to be factory made but I cant downcast it to a hole for adding it
            // cant add as entity or next position won't find it since the Entity has no declared class hole
            Hole hole = new Hole (WorldLoader.HOLE_KEY, newHole, imageStore.getImageList(WorldLoader.HOLE_KEY));
            world.tryAddEntity(hole);
            return hole;
        }
    }
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);
        //spawn a working fairy
//        Fairy fairy = new Fairy(WorldLoader.FAIRY_KEY, pressed, imageStore.getImageList(WorldLoader.FAIRY_KEY),
//                1,
//                .05);
//        world.tryAddEntity(fairy);
//        fairy.scheduleActions(scheduler,world, imageStore);


        //The Very Hungry Caterpillar
        if (world.getOccupant(pressed).isEmpty())
            {
            Entity begin = holeSpawn();
        //no schedule for hole
                Entity jerry = Factory.createWorm(WorldLoader.WORM_KEY, pressed, 0.5,
        0.2, imageStore.getImageList(WorldLoader.WORM_KEY));
        world.tryAddEntity(jerry);
        ((Worm)jerry).scheduleActions(scheduler,world, imageStore);}
//
//
//        Factory.createFairy(WorldLoader.FAIRY_KEY, new Point (pressed.x, pressed.y), actionPeriod,
//        animationPeriod, List<PImage> images);
        //Entity sapling = Factory.createSapling(WorldLoader.SAPLING_KEY + "_" +
        // fairyTarget.get().getId(), tgtPos, imageStore.getImageList(WorldLoader.SAPLING_KEY));

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.println("id: " + entity.getId() + " - " + entity.getClass() + " at " + entity.getPosition());
        }

    }
    public void butterSpawn(Point p){
        Entity barry = Factory.createBut(WorldLoader.BUTTERFLY_KEY, p, 0.5,
                0.2, imageStore.getImageList(WorldLoader.BUTTERFLY_KEY));
        world.tryAddEntity(barry);
        System.out.println("here");
        ((Butterfly)barry).scheduleActions(scheduler,world, imageStore);
    }

    private Point mouseToPoint() {
        return view.getViewport().viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            ImageLoader.loadImages(in, imageStore,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            WorldLoader.load(world, in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            WorldLoader.load(world, in, imageStore, createDefaultBackground(imageStore));
        }
    }
    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            //check type
            if (entity.getClass() != Stump.class && entity.getClass() != House.class){
                ((EntityAnimated)entity).scheduleActions(scheduler, world, imageStore);}
        }
    }
    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}
