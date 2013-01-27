/**
 *  Copyright (C) 2007 - 2013 GeoSolutions S.A.S.
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
import it.geosolutions.batchgeocoder.io.SolrDictionaryRepositoryReader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
/**
 * Runs a simple test using a GeoServerTester Instance
 * @author Lorenzo Natali
 *
 */
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

	/**
	 * Loads the configuration 
	 * @return the configuration
	 */
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
