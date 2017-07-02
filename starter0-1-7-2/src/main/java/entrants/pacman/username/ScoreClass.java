package entrants.pacman.username;

/**
 * Created by Anton on 01-Jul-17.
 */
public class ScoreClass {
    private double distanceMeasure = 0;
    private int chainSize = 0;
    private double distanceToFirst = 0;
    private double distanceToLast = 0;
    private int firstNodeIndex = 0;
    private int lastNodeIndex = 0;

    public ScoreClass(){}

    public ScoreClass(double distance, int size, double distanceToFirst, double distanceToLast,
                      int firstNodeIndex, int lastNodeIndex){
        this.distanceMeasure = distance;
        this.chainSize = size;
        this.distanceToLast = distanceToLast;
        this.distanceToFirst = distanceToFirst;
        this.firstNodeIndex = firstNodeIndex;
        this.lastNodeIndex = lastNodeIndex;
    }

    public double getDistance(){
        return  this.distanceMeasure;
    }

    public int getSize(){
        return this.chainSize;
    }

    public int getClosestNode(){
        if(this.distanceToFirst > this.distanceToLast){
            return this.firstNodeIndex;
        } else {
            return this.lastNodeIndex;
        }
    }

    public int getFarthestNode(){
        if(this.distanceToFirst > this.distanceToLast){
            return this.lastNodeIndex;
        } else {
            return this.firstNodeIndex;
        }
    }

    public double getDensity(){
        double closestNode = this.distanceToFirst > this.distanceToLast ? this.distanceToFirst : this.distanceToLast;
        double farthest = this.distanceToFirst > this.distanceToLast ? this.distanceToLast : this.distanceToFirst;

        double score = (closestNode * this.chainSize) / farthest;

//        double density = ((this.distanceToFirst + this.distanceToLast) / this.chainSize - this.distanceToFirst) /
//                (this.distanceToLast - this.distanceToFirst);
        return score;
    }



}
