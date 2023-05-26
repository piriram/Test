import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BitmapIndexQuery {
    private Map<String, BitSet> bitmapIndexes = new HashMap<>();

    public static void main(String[] args) throws SQLException, IOException {
        new BitmapIndexQuery().startQueryProcess();
    }

    private void startQueryProcess() throws IOException, SQLException {
        // Load bitmap indexes from files
        loadBitmapIndexes();

        // Start console query
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Please enter your query in format: column=value");
                String[] input = scanner.nextLine().split("=");

                if (input.length != 2) {
                    System.out.println("Invalid query format. Please try again.");
                    continue;
                }

                String column = input[0];
                String value = input[1];

                BitSet resultBitSet = bitmapIndexes.get(column + "_" + value);
                if (resultBitSet == null) {
                    System.out.println("No results found.");
                    continue;
                }

                // Now we can perform the SQL query
                performSQLQuery(column, resultBitSet);
            }
        }
    }

    private void loadBitmapIndexes() throws IOException {
        bitmapIndexes.put("화장실_보유여부", BitSet.valueOf(Files.readAllBytes(Paths.get("화장실_보유여부.txt"))));
        bitmapIndexes.put("주차장_보유여부", BitSet.valueOf(Files.readAllBytes(Paths.get("주차장_보유여부.txt"))));
        bitmapIndexes.put("카테고리_음식점", BitSet.valueOf(Files.readAllBytes(Paths.get("카테고리_음식점.txt"))));
        bitmapIndexes.put("카테고리_카페", BitSet.valueOf(Files.readAllBytes(Paths.get("카테고리_카페.txt"))));
        bitmapIndexes.put("카테고리_공공기관", BitSet.valueOf(Files.readAllBytes(Paths.get("카테고리_공공기관.txt"))));
        bitmapIndexes.put("카테고리_의료기관", BitSet.valueOf(Files.readAllBytes(Paths.get("카테고리_의료기관.txt"))));
        bitmapIndexes.put("카테고리_문화시설", BitSet.valueOf(Files.readAllBytes(Paths.get("카테고리_문화시설.txt"))));
    }

    private void performSQLQuery(String column, BitSet resultBitSet) throws SQLException {
        // Assuming we have an id column to match the bitmap index
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM places WHERE id IN (");
        for (int i = resultBitSet.nextSetBit(0); i >= 0; i = resultBitSet.nextSetBit(i + 1)) {
            queryBuilder.append(i).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1).append(")");

        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())) {

            while (resultSet.next()) {
                // Print out or otherwise handle the result set
            }
        }
    }
}
