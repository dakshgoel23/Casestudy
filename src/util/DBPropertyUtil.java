

package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getPropertyString(String propertyFileName) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertyFileName)) //try with resources(automatically close stream)
        {
            properties.load(inputStream);
            return "jdbc:mysql://" + properties.getProperty("hostname") + ":" +
                    properties.getProperty("port") + "/" + properties.getProperty("dbname") + "?user=" +
                    properties.getProperty("username") + "&password=" + properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //finally block can also written to close streams
        //if(inputStream!=null) close
    }
}
