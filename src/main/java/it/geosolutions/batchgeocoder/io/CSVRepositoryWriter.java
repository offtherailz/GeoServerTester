package it.geosolutions.batchgeocoder.io;

import it.geosolutions.batchgeocoder.composer.OutputComposer;
import it.geosolutions.batchgeocoder.composer.PlainComposerOut;
import it.geosolutions.batchgeocoder.model.Location;

import java.awt.Canvas;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVRepositoryWriter implements Output {

	private static Logger LOG = Logger.getLogger(Canvas.class.getCanonicalName());
	private OutputComposer outComposer;
	private String outputFileName;
	
	public CSVRepositoryWriter(OutputFileType type){
		outComposer = new PlainComposerOut();
		Configuration conf = null;
		try {
			conf = new PropertiesConfiguration("configuration.properties");
		} catch (ConfigurationException e) {
			LOG.log(Level.SEVERE, "failed to load configurations");
		}
		switch (type) {
		case GEOCODED: 
			outputFileName = conf.getString("fileNameOut.geocoded");
			break;
		case DISCARDED: 
			outputFileName = conf.getString("fileNameOut.discarded");
			break;
		}
	}
	
	public void storeLocations(List<Location> locations) {
		Writer writer = null;
		CSVWriter csvWriter = null;
		try {
			writer = new FileWriter(outputFileName);
		
		csvWriter = new CSVWriter(writer, ';');
		List<String[]> strList = outComposer.composeAll(locations);;
		csvWriter.writeAll(strList);
		} 
		catch (IOException e) {
			LOG.log(Level.SEVERE,e.getLocalizedMessage(),e);
		}
		finally {
			try {
				writer.close();
				csvWriter.close();
			} catch (IOException e) {
				LOG.log(Level.FINE,e.getLocalizedMessage(),e);
			}
			
		}
	}

	public void appendLocations(List<Location> locations) {
		// TODO Auto-generated method stub
		
	}

	public void appendLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	

}
