import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BplusTreeCreator {

    public static void createIndexes(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {

            // Create B+ tree index for ID
            String createIdIndexQuery = "CREATE INDEX idx_id ON places (ID)";
            statement.executeUpdate(createIdIndexQuery);

            // Create B+ tree index for placeName
            String createPlaceNameIndexQuery = "CREATE INDEX idx_placeName ON places (장소명)";
            statement.executeUpdate(createPlaceNameIndexQuery);

            System.out.println("ID와 장소명에 대한 B+ 트리 인덱스 생성이 완료되었습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
