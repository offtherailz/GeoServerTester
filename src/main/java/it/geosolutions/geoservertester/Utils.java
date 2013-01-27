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

import org.apache.commons.lang.StringUtils;

/**
 * @author Lorenzo Natali
 *
 */
public  class Utils {
	
	/**
	 * Escapes a string duplicating apex,as cql language requires
	 * @param string the string to escape
	 * @return the escaped string
	 */
	static String escapeCQLString(String string) {

		String[] components = string.split("'");
		return  StringUtils.join(components, "''");
	}

}
