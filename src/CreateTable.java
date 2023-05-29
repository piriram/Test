import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void createTable() {
        String CREATE_TABLE_SQL =
                "CREATE TABLE " + Config.TABLE_NAME + " (" +
                        "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                        "장소명 VARCHAR(50), " +
                        "카테고리 VARCHAR(10), " +
                        "화장실_보유여부 BOOLEAN, " +
                        "주차장_보유여부 BOOLEAN, " +
                        "개설연도 INT, " +
                        "평점 DOUBLE," +
                        "UNIQUE (장소명)" +
                        ")";
        try (Connection conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(CREATE_TABLE_SQL);
            System.out.println("테이블을 성공적으로 생성했습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
