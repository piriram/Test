import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitmapIndexAndSearcher {
    private String restroomFile = "화장실_보유여부.txt";
    private String parkingFile = "주차장_보유여부.txt";

    private BitSet loadBitMapIndex(String file,char number) throws IOException {
        String bitmapData = new String(Files.readAllBytes(Paths.get(file)));
        BitSet bitmapIndex = new BitSet();
        for (int i = 0; i < bitmapData.length(); i++) {
            if (bitmapData.charAt(i) == number) {
                bitmapIndex.set(i);
            }
        }
        return bitmapIndex;
    }

    private List<Integer> findRecordIds(BitSet bitmapIndex) {
        List<Integer> recordIds = new ArrayList<>();
        for (int i = 0; i < bitmapIndex.length(); i++) {
            if (bitmapIndex.get(i)) {
                recordIds.add(i + 1);
            }
        }
        return recordIds;
    }

    public void searchRecords() throws SQLException, IOException {
        BitSet restroomBitSet = loadBitMapIndex(restroomFile,'1');
        BitSet parkingBitSet = loadBitMapIndex(parkingFile,'1');
        BitSet andBitSet = (BitSet) restroomBitSet.clone();
        andBitSet.and(parkingBitSet);

        List<Integer> recordIds = findRecordIds(andBitSet);
        requireQuery(recordIds);
    }

    private static void requireQuery(List<Integer> recordIds) throws SQLException {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {

            System.out.println("ID    장소명    카테고리    화장실_보유여부    주차장_보유여부    개설연도    평점");
            // Retrieve records with matching IDs
            for (int recordId : recordIds) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM places WHERE ID = " + recordId);
                if (resultSet.next()) {
                    double 평점 = resultSet.getDouble("평점");
                    DecimalFormat 평점Format = new DecimalFormat("0.#");
                    System.out.println(resultSet.getInt("ID") + "    " + resultSet.getString("장소명") + "    " + resultSet.getString("카테고리")
                            + "    " + resultSet.getBoolean("화장실_보유여부") + "    " + resultSet.getBoolean("주차장_보유여부")
                            + "    " + resultSet.getInt("개설연도") + "    " + 평점Format.format(평점));

                    count++;
                }
            }
            System.out.println("조회된 레코드 개수: " + count);
        }
    }

    public static void main(String[] args) {
        try {
            BitmapIndexAndSearcher searcher = new BitmapIndexAndSearcher();
            searcher.searchRecords();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
