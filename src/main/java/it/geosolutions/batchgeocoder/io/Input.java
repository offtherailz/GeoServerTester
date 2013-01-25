package it.geosolutions.batchgeocoder.io;

import it.geosolutions.batchgeocoder.model.Location;

import java.util.List;

public interface Input {

	
	/**
	 * @return a List of Locations
	 */
	public List<Location> getLocations();
	
	/**
	 * Load Locations from the specific DataSource
	 */
	public void loadLocations();
}
