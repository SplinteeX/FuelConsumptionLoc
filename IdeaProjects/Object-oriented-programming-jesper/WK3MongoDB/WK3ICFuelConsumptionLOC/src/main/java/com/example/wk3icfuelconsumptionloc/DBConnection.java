package com.example.wk3icfuelconsumptionloc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/FuelConsumption";
    private static final String USER = "root";
    private static final String PASSWORD = "salasana";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}