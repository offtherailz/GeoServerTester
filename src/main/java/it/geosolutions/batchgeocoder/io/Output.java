package it.geosolutions.batchgeocoder.io;

import it.geosolutions.batchgeocoder.model.Location;

import java.util.List;

public interface Output {

	
	public void storeLocations(List<Location> locations);
	
	public void appendLocations(List<Location> locations);
	
	public void appendLocation(Location location);
	
}
