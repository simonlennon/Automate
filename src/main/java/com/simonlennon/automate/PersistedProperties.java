package com.simonlennon.automate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by simon.lennon on 20/05/14.
 */
public class PersistedProperties {

    private static final String FILE_NAME = "automate.properties";
    private static Logger logger = LogManager
            .getLogger(PersistedProperties.class);
    private static String PROP_DIR_KEY = "automate.props.dir";
    private static PersistedProperties pp;
    private Properties props = new Properties();

    private PersistedProperties() {
    }

    public synchronized static PersistedProperties getInstance() {

        try {
            return getInstanceInternal();
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }

    }

    private synchronized static PersistedProperties getInstanceInternal() throws IOException {

        if (pp == null) {
            pp = new PersistedProperties();

            File f;
            String propDir = System.getProperty(PROP_DIR_KEY);
            if (propDir != null && propDir.trim().length() != 0) {
                f = new File(propDir, FILE_NAME);
            } else {
                f = new File(FILE_NAME);
            }

            logger.debug("Loading props from: " + f.getPath());

            if (!f.exists()) {
                f.createNewFile();
            }

            pp.props.load(new FileInputStream(f));
        }

        return pp;
    }

    public synchronized void saveProp(String key, String value) {
        String propDir = System.getProperty(PROP_DIR_KEY);
        File f = new File(propDir, FILE_NAME);
        props.put(key, value);
        try {
            props.store(new FileOutputStream(f), "Last write: key=" + key + " value=" + value);
        } catch (IOException e) {
            logger.debug("Error saving properties", e);
            throw new RuntimeException("Error saving prop", e);
        }
    }

    public String getProp(String key) {
        Object value = props.get(key);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

}
