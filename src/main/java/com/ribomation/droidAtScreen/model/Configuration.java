/*
 * Copyright (C) 2011 Jason von Nieda <jason@vonnieda.org>
 * 
 * This file is part of OpenPnP.
 * 
 * OpenPnP is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * OpenPnP is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with OpenPnP. If not, see
 * <http://www.gnu.org/licenses/>.
 * 
 * For more information about OpenPnP visit http://openpnp.org
 */

package com.ribomation.droidAtScreen.model;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Configuration extends AbstractModelObject {

    private static Configuration instance;

    private static final String PREF_LENGTH_DISPLAY_FORMAT_WITH_UNITS =
            "Configuration.lengthDisplayFormatWithUnits";
    private static final String PREF_LENGTH_DISPLAY_FORMAT_WITH_UNITS_DEF = "%.3f%s";

    private File configurationDirectory;
    private Preferences prefs;

    public static Configuration get() {
        if (instance == null) {
            throw new Error("Configuration instance not yet initialized.");
        }
        return instance;
    }

    public static synchronized void initialize(File configurationDirectory) {
        instance = new Configuration(configurationDirectory);
        instance.setLengthDisplayFormatWithUnits(PREF_LENGTH_DISPLAY_FORMAT_WITH_UNITS_DEF);
    }

    private Configuration(File configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
        this.prefs = Preferences.userNodeForPackage(Configuration.class);
    }

    public File getConfigurationDirectory() {
        return configurationDirectory;
    }

    public void setLengthDisplayFormatWithUnits(String format) {
        prefs.put(PREF_LENGTH_DISPLAY_FORMAT_WITH_UNITS, format);
    }
    
    /**
     * Gets a File reference for the resources directory belonging to the given class. The directory
     * is guaranteed to exist.
     * 
     * @param forClass
     * @return
     * @throws IOException
     */
    public File getResourceDirectory(Class forClass) throws IOException {
        File directory = new File(configurationDirectory, forClass.getCanonicalName());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * Gets a File reference for the named file within the configuration directory. forClass is used
     * to uniquely identify the file and keep it separate from other classes' files.
     * 
     * @param forClass
     * @param name
     * @return
     */
    public File getResourceFile(Class forClass, String name) throws IOException {
        return new File(getResourceDirectory(forClass), name);
    }

    /**
     * Creates a new file with a unique name within the configuration directory. forClass is used to
     * uniquely identify the file within the application and a unique name is generated within that
     * namespace. suffix is appended to the unique part of the filename. The result of calling
     * File.getName() on the returned file can be used to load the same file in the future by
     * calling getResourceFile(). This method uses File.createTemporaryFile() and so the rules for
     * that method must be followed when calling this one.
     * 
     * @param forClass
     * @param suffix
     * @return
     * @throws IOException
     */
    public File createResourceFile(Class forClass, String prefix, String suffix)
            throws IOException {
        File directory = new File(configurationDirectory, forClass.getCanonicalName());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = File.createTempFile(prefix, suffix, directory);
        return file;
    }

}
