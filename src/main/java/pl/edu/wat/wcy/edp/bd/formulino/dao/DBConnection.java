package pl.edu.wat.wcy.edp.bd.formulino.dao;

import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

import java.sql.DriverManager;

public class DBConnection {

    public static Sql2o createSql2o() {
        try {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:f1.db");
            return new Sql2o(dataSource);

        } catch (Exception e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }
}