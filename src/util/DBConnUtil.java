

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    public static Connection getConnection() {
        String connectionString = DBPropertyUtil.getPropertyString("resources/db.properties");//also can use in function parameter
        Connection connection = null;
        try {
        	//class.forname line not added
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
