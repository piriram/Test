import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.text.DecimalFormat;

public class BitmapIndexSearcher {



    public void searchOneRecords(String COLLUM_NAME,String bool) throws SQLException, IOException {
        char number = '3';
        if (bool.equals("true")) {
            number = '1';
        } else if (bool.equals("false")) {
            number = '0';
        } else {
            System.out.println("입력값이 true 또는 false가 아닙니다.");
        }

//        String COLLUM_NAME = "주차장_보유여부";
        String BITMAP_INDEX_FILE = COLLUM_NAME+".txt";
        // Load bitmap index from file
        String bitmapData = new String(Files.readAllBytes(Paths.get(BITMAP_INDEX_FILE)));
        BitSet bitmapIndex = new BitSet();
        for (int i = 0; i < bitmapData.length(); i++) {
            if (bitmapData.charAt(i) == number) {
                bitmapIndex.set(i);
            }
        }

        List<Integer> recordIds = findRecordIds(bitmapIndex);
        int count=0;
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
    public void searchCategoryRecords(String CATEGORY_NAME) throws SQLException, IOException {
        String COLLUM_NAME = "카테고리_"+CATEGORY_NAME;
        String BITMAP_INDEX_FILE = COLLUM_NAME+".txt";
        // Load bitmap index from file
        String bitmapData = new String(Files.readAllBytes(Paths.get(BITMAP_INDEX_FILE)));
        BitSet bitmapIndex = new BitSet();
        for (int i = 0; i < bitmapData.length(); i++) {
            if (bitmapData.charAt(i) == '1') {
                bitmapIndex.set(i);
            }
        }

        List<Integer> recordIds = findRecordIds(bitmapIndex);
        int count=0;
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

    private List<Integer> findRecordIds(BitSet bitmapIndex) {
        List<Integer> recordIds = new ArrayList<>();
        for (int i = 0; i < bitmapIndex.length(); i++) {
            if (bitmapIndex.get(i)) {
                recordIds.add(i + 1);
            }
        }
        return recordIds;
    }

    public static void main(String[] args) {
        BitmapIndexSearcher searcher = new BitmapIndexSearcher();
        try {
//            searcher.searchCategoryRecords("공공기관");
            searcher.searchOneRecords("화장실_보유여부","false");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
