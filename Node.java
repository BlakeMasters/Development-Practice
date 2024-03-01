
import java.util.Objects;

public class Node{

    private final Point p;

    private int g;
    private int h;
    private int f;

    private Node prior;

    public Node(Point p,int g,int h,int f, Node prior){

        this.g = g;
        this.h = h;
        this.f = f;
        this.p = p;
        this.prior = prior;
    }

    public Point getPoint(){
        return p;
    }
    public Node getPrior(){return prior;}
    public void setPrior(Node p){prior = p;}
    public int getG() {
        return g;
    }

    public Integer getF() {
        return (Integer)f;
    }

    public void setG(int g1){
        g = g1;}

    public int getH() {
        return h;
    }

    public void setH(int h1){
        h = h1;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return this.getPoint().equals(node.getPoint());
    }

}
