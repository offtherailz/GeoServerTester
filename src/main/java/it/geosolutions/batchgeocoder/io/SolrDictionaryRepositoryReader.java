package it.geosolutions.batchgeocoder.io;

import it.geosolutions.batchgeocoder.model.Description;
import it.geosolutions.batchgeocoder.model.Location;
import it.geosolutions.batchgeocoder.model.Location.TYPE;
import it.geosolutions.batchgeocoder.model.Position;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

public class SolrDictionaryRepositoryReader implements Input {

	private static Logger LOG = Logger
			.getLogger(SolrDictionaryRepositoryReader.class.getCanonicalName());
	// private static String STATE_SUFFIX = ", Italia";
	private static String FILE_PATH = "src/main/resources/";
	private List<Location> locationList;
	private Configuration conf;

	public SolrDictionaryRepositoryReader(Configuration conf) {
		locationList = new ArrayList<Location>();
		this.conf = conf;
	}

	public List<Location> getLocations() {
		return locationList;
	}

	public void loadLocations() {
		String basePath = conf.getString("basePath");
		String fileName = conf.getString("fileNameIn");

		List<String[]> allData = new ArrayList<String[]>();
		allData.addAll(buildList(basePath + fileName));

		for (String[] el : allData) {
			Location loc = new Location();
			loc.setPosition(new Position());
			Description tmpDesc = new Description();
			tmpDesc.setDescription(new String[] { el[0], el[1] });
			loc.setDescription(tmpDesc);
			Description parent = new Description();
			parent.setDescription(new String[] { el[0], el[2] });
			loc.setParent(parent);
			loc.setType(TYPE.valueOf(el[3]));
			locationList.add(loc);
		}

	}

	private List<String[]> buildList(String filename) {
		List<String[]> resultList = new ArrayList<String[]>();
		Scanner scanner = null;
		FileInputStream fstream = null;
		try {
			// scanner = new Scanner(new File(filename), "UTF-8");

			fstream = new FileInputStream(new File(filename));
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "LATIN1"));
			String strLine;
			// Read File Line By Line

			long index = 0;
			// while (scanner.hasNextLine()) {
			// String line = scanner.nextLine();
			while ((strLine = br.readLine()) != null) {
				String line = strLine;
				
				if (line.indexOf("/") > 0) {
					String[] locations = line.split(" / ");

					String[] tmpEl = new String[4];
					tmpEl[0] = String.valueOf(index++);
					tmpEl[1] = locations[1];
					tmpEl[2] = locations[0];
					tmpEl[3] = "regione";
					if (!contains(resultList, tmpEl))
						resultList.add(tmpEl.clone());

					tmpEl = new String[4];
					tmpEl[0] = String.valueOf(index++);
					tmpEl[1] = locations[2];
					tmpEl[2] = locations[1];
					tmpEl[3] = "provincia";
					if (!contains(resultList, tmpEl))
						resultList.add(tmpEl.clone());

					if (locations.length >= 4) {
						tmpEl = new String[4];
						tmpEl[0] = String.valueOf(index++);
						tmpEl[1] = locations[3];
						tmpEl[2] = locations[2];
						tmpEl[3] = "comune";
						if (!contains(resultList, tmpEl)) {
							resultList.add(tmpEl.clone());
						}
					} else {
						LOG.log(Level.WARNING, line);
					}
				}
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				if (scanner != null)
				{
					scanner.close();
				}
				
				if (fstream != null)
				{
					fstream.close();
				}
			} catch (Exception e) {
				LOG.log(Level.FINE, e.getLocalizedMessage(), e);
			}
		}
		return resultList;
	}

	private boolean contains(List<String[]> resultList, String[] tmpEl) {
		if (resultList.size()==0) return false;
		
		for (String[] el : resultList) {
			if (el.length == tmpEl.length) {
				boolean match = true;
				for (int i = 1; i < el.length - 1; i++) {
					if ((el[i] == null && tmpEl[i] != null)
							|| (el[i] == null && tmpEl[i] != null)) {
						continue;
					}
					
					if (el[i] != null && tmpEl[i] != null
							&& !el[i].equals(tmpEl[i]) ) {
						match = false;
						
					}
				}
				if(match){return true;}
			}
		}
		return false;
	}

}
