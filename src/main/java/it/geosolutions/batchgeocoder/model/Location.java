package it.geosolutions.batchgeocoder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 * @author DamianoG
 * Provide a default implementation for location Object
 */
public class Location {
	
	public enum TYPE
	{
		regione,
		provincia,
		comune
	}
	
	private Description description;
	private Description parent;
	private Position position;
	private TYPE type;

	public List<String> getLocationAsList() {
		List<String> list = new ArrayList<String>();
		list.add(description.getName());
		if(parent!=null) list.add(parent.getName());
		list.addAll(position.getPositionAsList());
		list.add(String.valueOf(type.ordinal()));
		return list;
	}

	public List<String> getAlternativeNames() {
		return description.getAllAlternatives();
	}


	public void setPosition(Position position) {
		this.position = position;
	}
	

	public void setDescription(Description description) {
		this.description = description;
	}
	

	public void setParent(Description parent) {
		this.parent = parent;
	}

	public Description getParent() {
		return parent;
	}

	public String getName(){
		return this.description.getName();
	}
	
	public void setType(TYPE type) {
		this.type = type;
	}

	public TYPE getType() {
		return type;
	}

	public Polygon getJTSBoundingBox(){
		Polygon jtsPoly = buildJTSPolygon();
		return jtsPoly;
	}
	

	private Polygon buildJTSPolygon() {
		Map<String, Double> bounds = position.getBoundingBoxPoints();
		double lat = position.getLatitude();
		double lon = position.getLongitude();
		
		Coordinate[] coords = new Coordinate[4];
		coords[0] = new Coordinate(lon, lat - bounds.get("north"));
		coords[1] = new Coordinate(lon, lat + bounds.get("south"));
		coords[2] = new Coordinate(lon - bounds.get("weast"), lat);
		coords[3] = new Coordinate(lon + bounds.get("east"), lat);
		
		GeometryFactory geometryFactory = new GeometryFactory();
		
		LinearRing ring = geometryFactory.createLinearRing(coords);
		LinearRing holes[] = null; // use LinearRing[] to represent holes
		Polygon polygon = geometryFactory.createPolygon(ring, holes);
		return polygon;
	}
	
}
