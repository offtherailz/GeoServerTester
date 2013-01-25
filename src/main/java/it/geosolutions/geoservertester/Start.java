package it.geosolutions.geoservertester;

import it.geosolutions.batchgeocoder.io.Input;
import it.geosolutions.batchgeocoder.io.SolrDictionaryRepositoryReader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Start {
	
	private static Logger LOG = Logger.getLogger(GeoServerTester.class
			.getCanonicalName());
	
	
	public static void main(String args[]) {
		Configuration conf =loadConfig();
		GeoServerTester gst = new GeoServerTester(conf);
		Input repo = new SolrDictionaryRepositoryReader(conf); 
		repo.loadLocations();
		gst.test(repo.getLocations());
	}


	private static  Configuration loadConfig() {
		Configuration conf =null;
		try {
			conf = new PropertiesConfiguration(GeoServerTester.class.getClassLoader().getResource("configuration.properties"));
		} catch (ConfigurationException e) {
			LOG.log(Level.SEVERE, "failed to load configurations");
			throw new RuntimeException(e);
		}  
		return conf;
	}
	
}
