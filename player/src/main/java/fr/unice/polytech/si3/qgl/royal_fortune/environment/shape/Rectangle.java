package fr.unice.polytech.si3.qgl.royal_fortune.environment.shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.unice.polytech.si3.qgl.royal_fortune.calculus.Mathematician;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * @author Bonnet Killian Imami Ayoub Karrakchou Mourad Le Bihan Leo
 *
 */
@JsonIgnoreProperties(value = {
		"type"
})
public class Rectangle extends Shape{

	private double width;
	private double height;
	private double orientation;
	private List<Segment> segmentList;
	Position position;
	int PRECISION=50;

	public Rectangle() {}
	
	public Rectangle(String type, double width, double height, double orientation) {
		super(type);
		this.width = width;
		this.height = height;
		this.orientation = orientation;
	}

	/**
	 * Compute the 4 segments of the rectangle
	 * @return a list that contain the 4 segment of the rectangle [H, D, B, G]
	 */
	private List<Segment> computeSegments() {
		List<Segment> rectangleSegment = new ArrayList<>();
		List<Position> rectangleCorner = computeCorner();
		Position HG = rectangleCorner.get(0);
		Position HD = rectangleCorner.get(1);
		Position BD = rectangleCorner.get(2);
		Position BG = rectangleCorner.get(3);
		rectangleSegment.add(new Segment(HG, HD));
		rectangleSegment.add(new Segment(HD, BD));
		rectangleSegment.add(new Segment(BD, BG));
		rectangleSegment.add(new Segment(BG, HG));
		return rectangleSegment;
	}

	/**
	 * Compute the 4 corners of the rectangle
	 * @return a list that contain the 4 corner of the rectangle [HG, HD, BD, BG]
	 */
	private List<Position> computeCorner() {
		List<Position> listOfPosition=new ArrayList<>();
		listOfPosition.add(Mathematician.changeBase(this.position,-width/2,height/2));
		listOfPosition.add(Mathematician.changeBase(this.position,width/2,height/2));
		listOfPosition.add(Mathematician.changeBase(this.position,width/2,-height/2));
		listOfPosition.add(Mathematician.changeBase(this.position,-width/2,-height/2));
		return listOfPosition;
	}
	/**
	 * Compute the 4 corners of the rectangle
	 * @return a list that contain the 4 corner of the rectangle [HG, HD, BD, BG]
	 */
	private List<Position> computeBeacon() {
		List<Position> listOfPosition=new ArrayList<>();
		double widthUnit=width/PRECISION;
		double heightUnit=height/PRECISION;
		for (int k=-PRECISION/5;k<PRECISION+PRECISION/5;k++){
		listOfPosition.add(Mathematician.changeBase(this.position,-width/2+k*widthUnit,height/2));
		listOfPosition.add(Mathematician.changeBase(this.position,width/2,height/2-k*widthUnit));
		listOfPosition.add(Mathematician.changeBase(this.position,width/2-k*widthUnit,-height/2));
		listOfPosition.add(Mathematician.changeBase(this.position,-width/2,-height/2+k*widthUnit));}
		return listOfPosition;
	}


	/**
	 * Compute the intersection between the current shape and a segment
	 * @param segment
	 * @return the 2 positions of the intersection
	 */
	public List<Position> computeIntersectionWith(Segment segment){
		List<Position> intersectionsPosition = new ArrayList<>();
		Optional<Position> intersection;
		for(Segment seg : segmentList) {
			 intersection = segment.computeIntersectionWith(seg);
			 if(!intersection.isEmpty()) intersectionsPosition.add(intersection.get());
		}
		return intersectionsPosition;
	}

	/**
	 * check if the given Position is in the rectangle
	 * @return
	 * @param pointA
	 */
	public boolean positionIsInTheRectangle(Position pointA) {
		return true;
	}

	@Override
	/**
	 * When we set the position the rectangle create his segments
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
		this.position.setOrientation(orientation);
		this.segmentList = computeSegments();
	}

	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getOrientation() {
		return orientation;
	}
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode oarActionJSON = mapper.createObjectNode();
		oarActionJSON.put("type", "rectangle");
		oarActionJSON.put("width", width);
		oarActionJSON.put("height", height);
		oarActionJSON.put("orientation", orientation);


		try {
			return mapper.writeValueAsString(oarActionJSON);
		} catch (JsonProcessingException e) {
			logger.log(Level.INFO, "Exception");
		}
		return "";
	}

}
