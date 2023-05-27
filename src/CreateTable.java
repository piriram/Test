import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+Config.DBNAME;
    private static final String USER = "root";
    private static final String PASS = "1234";
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE places (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "장소명 VARCHAR(50), " +
                    "카테고리 VARCHAR(10), " +
                    "화장실_보유여부 BOOLEAN, " +
                    "주차장_보유여부 BOOLEAN, " +
                    "개설연도 INT, " +
                    "평점 DOUBLE," +
                    "UNIQUE (장소명)"+
                    ")";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(CREATE_TABLE_SQL);
            System.out.println("Table created successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
