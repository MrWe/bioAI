import java.awt.geom.Point2D;

public class Direction {
    static Point2D UP(){
        return new Point2D.Double(0,-1);
    }
    static Point2D LEFT(){
        return new Point2D.Double(-1,0);
    }
    static Point2D DOWN(){
        return new Point2D.Double(0,1);
    }
    static Point2D RIGHT(){
        return new Point2D.Double(1,0);
    }
}
