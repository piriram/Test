import java.sql.*;

public class IndexQueryRunner {
    private static final String URL = Config.URL;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String queryById = "SELECT * FROM places USE INDEX (idx_id) WHERE ID = 1";
            String queryByPlaceName = "SELECT * FROM places USE INDEX (idx_placeName) WHERE 장소명 = '장소500'";

            ResultSet resultSetById = statement.executeQuery(queryById);
            while (resultSetById.next()) {
                System.out.println("ID: " + resultSetById.getInt("ID"));
                // Print other columns as needed...
            }

            ResultSet resultSetByPlaceName = statement.executeQuery(queryByPlaceName);
            while (resultSetByPlaceName.next()) {
                System.out.println("장소명: " + resultSetByPlaceName.getString("장소명"));
                // Print other columns as needed...
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
