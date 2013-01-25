package it.geosolutions.batchgeocoder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the data structure for the metadata representing a location 
 * @author DamianoG
 *
 */
public class Description {

	private String[] data;
	
	public void setDescription(String[] data){
		this.data = data;
	}
	
//	public String getKey(){
//		return data[0];
//	}
	
	public List<String> getAllAlternatives(){
		List<String> list = new ArrayList<String>();
		list.add(data[1]);
		
		return list;
	}
	
	public String getId(){
		return data[0];
	}
	
	public String getName(){
		return data[1];
	}
	
	public String getAlternativeName(){
		return data[2];
	}
}
