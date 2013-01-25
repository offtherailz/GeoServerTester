package it.geosolutions.batchgeocoder.composer;

import it.geosolutions.batchgeocoder.model.Location;

import java.util.List;

/**
 * 
 * @author DamianoG
 * Compose the information structure for serialization, persistence...
 */
public interface OutputComposer {

	/**
	 * Compose both header and data
	 * @param locations a list of locations with nullable fields
	 * @return
	 */
	public List<String[]> composeAll(List<Location> locations);
	
	/**
	 * 
	 * @return Compose an header for the data
	 */
	public String[] composeHeader();
	
}
