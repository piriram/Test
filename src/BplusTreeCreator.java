import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BplusTreeCreator {
    private static final String URL = Config.URL;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;

    public void createIndexes() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Create B+ tree index for ID
            String createIdIndexQuery = "CREATE INDEX idx_id ON places (ID)";
            statement.executeUpdate(createIdIndexQuery);

            // Create B+ tree index for placeName
            String createPlaceNameIndexQuery = "CREATE INDEX idx_placeName ON places (장소명)";
            statement.executeUpdate(createPlaceNameIndexQuery);

            System.out.println("B+ 트리 인덱스 생성이 완료되었습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BplusTreeCreator creator = new BplusTreeCreator();
        creator.createIndexes();
    }
}
