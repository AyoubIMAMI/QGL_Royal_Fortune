package fr.unice.polytech.si3.qgl.royal_fortune.target;

import fr.unice.polytech.si3.qgl.royal_fortune.calculus.Cartologue;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.Stream;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Route implements Comparable{
    private Route firstRoute;
    private Route secondRoute;
    private double value;
    private Optional<Stream> stream;
    private List<Segment> listSegment;
    private Cartologue cartologue;
    private Boolean reefCollision;

    /**
     * Create a route using a single segment
     * We try to slice the segment by its intersection with SeaEntities.<br>
     * If no intersection the route is a leaf route else we distribute the segment list between the first and the second route.
     * @param segment a segment
     */
    public Route(Segment segment,Cartologue cartologue){
        this.cartologue = cartologue;
        this.listSegment.add(segment);
        List<Segment> slicedSegments = sliceSegment(segment);
        if(slicedSegments.size() > 0){
            listSegment = slicedSegments;
            distributeSegments(listSegment);
            value=firstRoute.getValue()+ secondRoute.getValue();
        }
        else{
            value=cartologue.computeDistance(segment);
        }
    }

    /**
     * Create a route using a list of segment, we distribute all the segment between the two lower route of the current route to build other route
     * @param listSegment a list of segments
     * @param cartologue a cartologue
     */
    public Route(List<Segment> listSegment, Cartologue cartologue) {
        this.cartologue = cartologue;
        this.listSegment = listSegment;
        distributeSegments(listSegment);
        value=firstRoute.getValue()+ secondRoute.getValue();
    }

    /**
     * Cut the segment list in two part by using /2.<br>
     * If we have a list like <strong>{AB,BC,CD}</strong> the first segment AB will go in firstRoute and the other <strong>(BC,CD)</strong> in the second.<br>
     * If we have a list like <strong>{AB,BC,CD,DE}</strong> the two first segment AB and BC will go in firstRoute and the other <strong>(CD,DE)</strong> in the second.<br>
     * @param listSegment list of segments
     */
    private void distributeSegments(List<Segment> listSegment) {
        List<Segment> segmentFirstRoute = new ArrayList<>();
        List<Segment> segmentSecondRoute = new ArrayList<>();
        for(int i = 0 ; i < listSegment.size()/2;i++)segmentFirstRoute.add(listSegment.get(i));
        for(int i = listSegment.size()/2 ; i <listSegment.size() ;i++)segmentSecondRoute.add(listSegment.get(i));
        if (segmentFirstRoute.size()==1)
            firstRoute = new Route(segmentFirstRoute.get(0), cartologue);
        else
            firstRoute = new Route(segmentFirstRoute, cartologue);
        if (segmentSecondRoute.size()==1)
            secondRoute = new Route(segmentFirstRoute.get(0), cartologue);
        else
            secondRoute = new Route(segmentSecondRoute, cartologue);
    }


    /**
     * slice the segment by intersection between seaEntities using the cartologue
     * @return list of segments
     */
    private List<Segment> sliceSegment(Segment segment) {
        //use cartologue to slice segment
        return cartologue.sliceSegmentByInteraction(segment);
    }

    public List<Segment> getListSegment() {
        return listSegment;
    }

    public Route getFirstRoute() {
        return firstRoute;
    }

    public Route getSecondRoute() {
        return secondRoute;
    }

    public double getValue() {
        return value;
    }

    public Optional<Stream> getStream() {
        return stream;
    }

    public Cartologue getCartologue() {
        return cartologue;
    }

    public Boolean getReefCollision() {
        return reefCollision;
    }

    @Override
    public int compareTo(Object o) {
        if(this.value>((Route)o).getValue())
            return 1;
        else if(this.value==((Route)o).getValue())
            return 0;
        else return -1;
    }
}