/**
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.geoservertester;

import it.geosolutions.batchgeocoder.io.Input;
import it.geosolutions.batchgeocoder.io.Output;
import it.geosolutions.batchgeocoder.model.Location;
import it.geosolutions.batchgeocoder.model.Location.TYPE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import sun.net.www.protocol.http.AuthCacheValue.Type;


/**
 * @author Lorenzo Natali
 * This class wants to perform tests on a GeoServer instance and write results on some files
 * for now it simply checks the availability of some features using a filter 
 */
public class GeoServerTester {
	/* Costants */
	private static final String GEOSERVER_URL_PARAMETER = "geoserver.url";
	private static final String TYPENAME_PARAMETER  = "typename";
	private static final String OUTPUT_FOLDER_NAME  = "folder_out";
	private static final String CONNECTION_PARAMETERS_CAPABILITIES_URL = "WFSDataStoreFactory:GET_CAPABILITIES_URL";
	private static Logger LOG = Logger.getLogger(GeoServerTester.class
			.getCanonicalName());

	
	private Configuration conf;
	private Map<String,Boolean> createdFiles = new HashMap<String,Boolean>();

	public GeoServerTester(Configuration conf) {
		this.conf = conf;

	}

	public void runTest() {
		loadInput();

	}

	private void loadInput() {
		// TODO Auto-generated method stub

	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public void test(List<Location> locations) {
		int errors=0;
		int found=0;
		int notfound=0;
		int multiple=0;
		int totin=locations.size();
		Map connectionParameters = new HashMap();
		connectionParameters.put(CONNECTION_PARAMETERS_CAPABILITIES_URL, conf.getProperty(GEOSERVER_URL_PARAMETER) );
		System.out.println(locations.size());
		
		DataStore data;
		FeatureSource<SimpleFeatureType, SimpleFeature> source;
		String typeName = conf.getString(TYPENAME_PARAMETER);
		try {
			//connection
			data = DataStoreFinder.getDataStore( connectionParameters );
			//discovery
			/*
			String typeNames[] = data.getTypeNames();
			typeName = typeNames[0];
			SimpleFeatureType schema = data.getSchema( typeName );
			*/
			
			//target
			source = data.getFeatureSource( typeName );
		
		} catch (IOException e) {
			LOG.severe("UNABLE TO INIZIALIZE CONNECTION TO THE WFS");
			return;
		}
		for(Location l : locations){
			FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
			Filter filter;
			try {
				String escapedName = Utils.escapeCQLString(l.getName());
				String escapedParent = Utils.escapeCQLString(l.getParent().getName());
				//int type = 
				filter = CQL.toFilter("name ='"+escapedName+"' and parent ='" + escapedParent + "' and type = " + l.getType().ordinal());
			} catch (CQLException e) {
				LOG.severe("UNABLE TO CREATE FILTER for" + l.getName() );
				e.printStackTrace();
				
				return;
			}
			try {
				Query query = new Query( typeName, filter );
				query.setCoordinateSystem(null);
				
					features = source.getFeatures( query );
				
				int number = features.size();
				if(number==1){
					match(l);
					found++;
				}else{
					
					if(number>1){
						multiple ++;
						notMatch(l,features,"multiple.txt");
					}else if(number<1){
						notfound++;
						notMatch(l,features,"notfound.txt");
					}
					
				}
			} catch (IOException e) {
				notMatch(l,features,"errors.txt");
				errors++;
			}
			
		}
		report(found,notfound,multiple,errors,totin);
		
	}

	private void report(int found, int notfound,int multiple, int errors,int totin) {
		System.out.println("\nFound:"+found);
		System.out.println("\nNot Found:"+notfound);
		System.out.println("\nMultiple:"+multiple);
		System.out.println("\nErrors:"+errors);
		int tot = notfound +found+multiple +errors;
		System.out.println("\nTotal:"+tot);
		int miss =totin -tot;
		System.out.println("\nMissing:"+miss );
		
	}

	private void match(Location l) {
		
	}

	private void notMatch(Location l,FeatureCollection<SimpleFeatureType, SimpleFeature> features, String fname) {
		String path = conf.getString(OUTPUT_FOLDER_NAME);
		File f;BufferedWriter writer = null;
		try {
			f = new File (path,fname);
				
			writer = new BufferedWriter(new FileWriter(f, this.getMode(f)));
			writer.write("\n[name = " +l.getName() + " parent =  " + 
			l.getParent().getName() + " type=" + l.getType().ordinal() +"]" );
			writer.write("FOUND:"+ features.size());
			
		} catch (IOException e) {LOG.severe(e.getMessage());
		}finally{
			try{if (writer != null){ writer.close();} }catch(Exception e){LOG.log(Level.FINE,e.getLocalizedMessage(),e);}
			
		}
		
		
		
		
	}
	private boolean getMode(File f){
		if(f.exists()){
			String name = f.getName();
			Boolean created = createdFiles.get(name);
			if (created!=null){
				return true;
			}else {
				createdFiles.put(name, true);
				LOG.info("Created " +name);
				
				return false;
			}
			
		}
		return false;
	}
}
