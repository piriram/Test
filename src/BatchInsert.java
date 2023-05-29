import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class BatchInsert {
    private static final String INSERT_SQL = "INSERT INTO "+Config.TABLE_NAME+" (장소명, 카테고리, 화장실_보유여부, 주차장_보유여부, 개설연도, 평점) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String[] categories = {"음식점", "카페", "공공기관", "의료기관", "문화시설"};
    private static final Random random = new Random();

    public static void main(String[] args) {
        createAndInsertRecords(100000);
    }//10만개 데이터 생성

    public static void createAndInsertRecords(int recordCount) {

        try (Connection conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);  // 오토 커밋을 멈춤

            for (int i = 0; i < recordCount; i++) {
                pstmt.setString(1, "장소" + (i + 1));
                pstmt.setString(2, categories[random.nextInt(categories.length)]);
                pstmt.setBoolean(3, random.nextBoolean());
                pstmt.setBoolean(4, random.nextBoolean());
                pstmt.setInt(5, 1900 + random.nextInt(123));  // 1900~2023 사이의 개설년도를 생성한다.
                pstmt.setDouble(6, random.nextDouble() * 5);  // 0.0~5.0 사이에서 평점을 생성한다.
                pstmt.addBatch();

                if (i % 1000 == 0) {  //1000개의 레코드마다 커밋을 한다.
                    pstmt.executeBatch();
                    conn.commit();
                }
            }

            pstmt.executeBatch();
            conn.commit();  // 마지막 커밋

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}