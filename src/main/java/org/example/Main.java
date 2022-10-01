package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
       /*
       String url = "jdbc:mariadb://localhost:3306/JDBCtest";
       String username = "root";
       String password = "";
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println(connection);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        */
        try{
            runTest();
        }catch (SQLException exception){
            for(Throwable t: exception){
                t.printStackTrace();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private static void runTest() throws SQLException, IOException {
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement())
        {
            statement.executeUpdate("CREATE TABLE Greetings (Message VARCHAR(20))");
            statement.executeUpdate("INSERT INTO Greetings VALUES ('Hello world!')");

            try(ResultSet result = statement.executeQuery("SELECT * FROM  Greetings")) {
                if(result.next()){
                    System.out.println(result.getString(1));
                }
            }
            //statement.executeUpdate("DROP TABLE Greetings");
        }
    }


    private static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        try(InputStream inputStream = Files.newInputStream(Paths.get("database.properties"))){
            properties.load(inputStream);
        }

        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url,username,password);
    }
}