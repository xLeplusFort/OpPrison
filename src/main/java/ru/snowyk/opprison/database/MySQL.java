package ru.snowyk.opprison.database;

import org.bukkit.ChatColor;

import java.sql.*;

public class MySQL {
    private String HOST;
    private String DATABASE;
    private String USER;
    private String PASSWORD;
    public static String TABLE = "OpPrison";
    private static Connection con;

    public MySQL(String host, String database, String user, String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
        this.connect();
        this.TryToCreateTable();
    }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            System.out.println(ChatColor.GREEN + "MySQL enable");
        } catch (SQLException var2) {
            System.out.println(ChatColor.RED + "MySQL disable");
        }

    }

    public void close() {
        try {
            if (con != null) {
                con.close();
                System.out.println(ChatColor.GREEN + "MySQL close");
            }
        } catch (SQLException var2) {
            SQLException e = var2;
            System.out.println(ChatColor.RED + "MySQL close");
            e.printStackTrace();
        }

    }

    public void TryToCreateTable() {
        try {
            Statement state = con.createStatement();
            state.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.DATABASE + "." + TABLE + " (id INT NOT NULL AUTO_INCREMENT,PlayerName TEXT(20),rank INT NOT NULL,pickaxe TEXT(20),blocks DOUBLE NOT NULL,money DOUBLE NOT NULL,multiplier DOUBLE NOT NULL,prestige DOUBLE NOT NULL,tokens DOUBLE NOT NULL,PRIMARY KEY(id));");
            System.out.println("Create Table");
        } catch (SQLException var2) {
            SQLException e = var2;
            e.printStackTrace();
        }

    }

    public static Connection connection() {
        return con;
    }

    public static int getIntegercolumn(String name, String sign) {
        int gettitle = 0;

        try {
            PreparedStatement statement = connection().prepareStatement("SELECT * FROM " + TABLE + " WHERE PlayerName=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            gettitle = results.getInt(sign);
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        }

        return gettitle;
    }

    public static double getDounlecolumn(String name, String sign) {
        double gettitle = 0.0;

        try {
            PreparedStatement statement = connection().prepareStatement("SELECT * FROM " + TABLE + " WHERE PlayerName=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            gettitle = results.getDouble(sign);
        } catch (SQLException var6) {
            SQLException e = var6;
            e.printStackTrace();
        }

        return gettitle;
    }

    public static String getStringcolumn(String name, String sign) {
        String gettitle = "";

        try {
            PreparedStatement statement = connection().prepareStatement("SELECT * FROM " + TABLE + " WHERE PlayerName=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            gettitle = results.getString(sign);
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        }

        return gettitle;
    }

    public static void setIntegercolumn(String name, String column, int number) {
        try {
            PreparedStatement statement = connection().prepareStatement("UPDATE " + TABLE + " SET " + column + " =? WHERE PlayerName=?");
            statement.setInt(1, number);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException var4) {
            SQLException e = var4;
            e.printStackTrace();
        }

    }

    public static void setDoulblecolumn(String name, String column, double number) {
        try {
            PreparedStatement statement = connection().prepareStatement("UPDATE " + TABLE + " SET " + column + " =? WHERE PlayerName=?");
            statement.setDouble(1, number);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        }

    }

    public static void setStringcolumn(String name, String column, String number) {
        try {
            PreparedStatement statement = connection().prepareStatement("UPDATE " + TABLE + " SET " + column + " =? WHERE PlayerName=?");
            statement.setString(1, number);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException var4) {
            SQLException e = var4;
            e.printStackTrace();
        }

    }
}
