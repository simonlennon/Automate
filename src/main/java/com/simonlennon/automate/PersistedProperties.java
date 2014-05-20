package com.simonlennon.automate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by simon.lennon on 20/05/14.
 */
public class PersistedProperties {

    private static PersistedProperties pp = null;

    private static final String FILE_NAME = "scheduler.properties";

    private Properties props = new Properties();

    private PersistedProperties() {
    }

    public synchronized static PersistedProperties getInstance() throws IOException {
        if (pp == null) {
            pp = new PersistedProperties();
            File f = new File(FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
            }
            pp.props.load(new FileInputStream(f));
        }
        return pp;
    }

    public synchronized void saveProp(String key, String value) throws IOException {
        File f = new File(FILE_NAME);
        props.put(key, value);
        props.store(new FileOutputStream(f), "Last write: key="+key+" value="+value);
    }

    public String getProp(String key) {
        Object value = props.get(key);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    public static void main(String args[]){
        try {
            getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
