package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MakeDatabase {
//    public static void main(String[] args) {
//        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=intec-123");
//            Statement statement=connection.createStatement();){
//
//            int result = statement.executeUpdate("CREATE DATABASE belgium_beerdb");
//            System.out.println("CREATE DATABASE belgium_beerdb: " + result);
//
//            result = statement.executeUpdate("grant all on belgium_beerdb.* to 'intec'@'localhost'");
//            System.out.println("grant all on belgium_beerdb.* to 'intec'@'localhost': " + result);
//
//            result = statement.executeUpdate("flush privileges");
//            System.out.println("flush privileges: " + result);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
}
