import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class BitmapIndexGenerator {
    private static final String[] categories = new String[]{"음식점", "카페", "공공기관", "의료기관", "문화시설"};
    private Map<String, BitSet> bitmapIndexes = new HashMap<>();
    private int rowCount = 0;

    public void generateBitmapIndexes() throws SQLException, IOException, InterruptedException {
        System.out.println("화장실_보유여부,주차장_보유여부,카테고리에 대해서 비트맵 인덱스를 생성하겠습니다.");
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Config.TABLE_NAME)) {

            while (resultSet.next()) {
                updateBitmapIndex(resultSet.getString("화장실_보유여부").equals("1") ? 1 : 0, "화장실_보유여부", rowCount);
                updateBitmapIndex(resultSet.getString("주차장_보유여부").equals("1") ? 1 : 0, "주차장_보유여부", rowCount);

                String category = resultSet.getString("카테고리");
                for (String c : categories) {
                    updateBitmapIndex(category.equals(c) ? 1 : 0, "카테고리_" + c, rowCount);
                }
                rowCount++;
            }

            // Save bitmap indexes to txt files
            for (Map.Entry<String, BitSet> bitmapIndexEntry : bitmapIndexes.entrySet()) {
                saveBitmapIndexToFile(bitmapIndexEntry.getKey() + ".txt", bitmapIndexEntry.getValue());
            }
        }

        Thread.sleep(1 * 30 * 1000); // 대기 시간: 30초
    }

    private void updateBitmapIndex(int value, String field, int row) {
        BitSet bitSet = bitmapIndexes.computeIfAbsent(field, k -> new BitSet());
        bitSet.set(row, value == 1);
    }

    private void saveBitmapIndexToFile(String fileName, BitSet bitSet) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < rowCount; i++) {
                char value = bitSet.get(i) ? '1' : '0';
                writer.write(value);
            }
        }
    }

    public static void main(String[] args) {
        BitmapIndexGenerator generator = new BitmapIndexGenerator();
        try {
            generator.generateBitmapIndexes();
        } catch (SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
