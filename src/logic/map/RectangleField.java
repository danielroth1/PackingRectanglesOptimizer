package logic.map;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import logic.Rectangle;

public class RectangleField implements Serializable {

	private List<Rectangle> rectangles;
	
	public RectangleField() {
		rectangles = new LinkedList<Rectangle>();
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}

	public void setRectangles(List<Rectangle> rectangles) {
		this.rectangles = rectangles;
	}
	
	

}
