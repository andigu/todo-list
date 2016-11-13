package db;

import org.apache.derby.jdbc.ClientDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Andi Gu
 */
public class DatabaseAccessor {
    private static ClientDataSource source = new ClientDataSource();
    private static final DatabaseAccessor instance = new DatabaseAccessor();

    private DatabaseAccessor() {
        source.setDatabaseName("db");
    }

    private Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static DatabaseAccessor getInstance() {
        return instance;
    }

}
