package main;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Mysql implementation is OK.");
        } catch (Exception ex) {
            System.out.println("Mysql implementation error.");
        }
    }
}
