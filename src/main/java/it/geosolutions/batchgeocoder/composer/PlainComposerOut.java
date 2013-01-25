package it.geosolutions.batchgeocoder.composer;

import it.geosolutions.batchgeocoder.model.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Basic composer
 * @author DamianoG
 *
 */
public class PlainComposerOut implements OutputComposer{
	private static Logger LOG = Logger.getLogger(OutputComposer.class
			.getCanonicalName());
	
	public List<String[]> composeAll(List<Location> locations) {
		List<String[]> strList = new ArrayList<String[]>();
		strList.add(composeHeader());
		for(Location el : locations){
			try{
				String[] arr = new String[el.getLocationAsList().size()];
				strList.add(el.getLocationAsList().toArray(arr));
			}catch(NullPointerException e){
				LOG.warning("Cannot add location "+ el);
			}
		}
		return strList;
	}
	
	public String[] composeHeader(){
		String [] header = new String[9];
		header[0] = "NAME";
		header[1] = "PARENT";
		header[2] = "LATITUDE";
		header[3] = "LONGITUDE";
		header[4] = "NORTH";
		header[5] = "SOUTH";
		header[6] = "EAST";
		header[7] = "WEST";
		header[8] = "TYPE";
		return header;
	}

}
