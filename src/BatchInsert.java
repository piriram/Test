import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class BatchInsert {


    private static final String INSERT_SQL = "INSERT INTO places (장소명, 카테고리, 화장실_보유여부, 주차장_보유여부, 개설연도, 평점) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String[] categories = {"음식점", "카페", "공공기관", "의료기관", "문화시설"};
    private static final Random random = new Random();

    public static void main(String[] args) {
        createAndInsertRecords(1000000);
    }

    public static void createAndInsertRecords(int recordCount) {

        try (Connection conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);  // Disable auto-commit

            for (int i = 0; i < recordCount; i++) {
                pstmt.setString(1, "장소" + (i + 1));
                pstmt.setString(2, categories[random.nextInt(categories.length)]);
                pstmt.setBoolean(3, random.nextBoolean());
                pstmt.setBoolean(4, random.nextBoolean());
                pstmt.setInt(5, 1900 + random.nextInt(123));  // 개설연도 between 1900 and 2023
                pstmt.setDouble(6, random.nextDouble() * 5);  // 평점 between 0.0 and 5.0
                pstmt.addBatch();

                if (i % 1000 == 0) {  // Commit every 1000 records
                    pstmt.executeBatch();
                    conn.commit();
                }
            }

            pstmt.executeBatch();  // Execute the remaining records, if any
            conn.commit();  // Final commit

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}