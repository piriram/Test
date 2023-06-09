import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class Config {
    public static String USERNAME = "root";
    public static String PASSWORD = "1234";
    public static String DBNAME = "DBSPROJECT";
    public static String TABLE_NAME = "places";
    public static String URL = "jdbc:mysql://localhost:3306/"+Config.DBNAME;
    // 다른 전역 변수들도 이곳에 추가
    public static void setConnectionInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 이름을 입력하세요: ");
        USERNAME = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        PASSWORD = scanner.nextLine();

    }

    static BitSet getCategoryBitSet(String CATEGORY_NAME) throws IOException {
        String COLLUM_NAME = "카테고리_"+ CATEGORY_NAME;
        String BITMAP_INDEX_FILE = COLLUM_NAME+".txt";
        // Load bitmap index from file
        String bitmapData = new String(Files.readAllBytes(Paths.get(BITMAP_INDEX_FILE)));
        BitSet bitmapIndex = new BitSet();
        for (int i = 0; i < bitmapData.length(); i++) {
            if (bitmapData.charAt(i) == '1') {
                bitmapIndex.set(i);
            }
        }
        return bitmapIndex;
    }
    static BitSet getOneBitSet(String COLLUM_NAME, char number) throws IOException {
        String BITMAP_INDEX_FILE = COLLUM_NAME +".txt";
        System.out.println(BITMAP_INDEX_FILE);
        // Load bitmap index from file
        String bitmapData = new String(Files.readAllBytes(Paths.get(BITMAP_INDEX_FILE)));
//        System.out.println(bitmapData);
        BitSet bitmapIndex = new BitSet();
        for (int i = 0; i < bitmapData.length(); i++) {
            if (bitmapData.charAt(i) == number) {
                bitmapIndex.set(i);

            }
        }
        return bitmapIndex;
    }
    static BitSet ConditionalBitset(String file1, String bool1) throws IOException {
        char number1='3';
        BitSet bitset1 = new BitSet();
        if (bool1.equals("true")) {
            number1 = '1';
            bitset1 = Config.getOneBitSet(file1,number1);
        } else if (bool1.equals("false")) {
            number1 = '0';
            bitset1 = Config.getOneBitSet(file1,number1);
        } else {
            if(file1.equals("카테고리")) {
                String newCollum = file1+"_"+bool1;
                System.out.println(newCollum);

                bitset1 = Config.getOneBitSet(newCollum,'1');
            }
        }
        return bitset1;
    }
    static void BitsetPrint(BitSet bitmapIndex) throws SQLException {
        List<Integer> recordIds = findRecordIds(bitmapIndex);
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {
             System.out.println("ID    장소명    카테고리    화장실_보유여부    주차장_보유여부    개설연도    평점");
            // Retrieve records with matching IDs
            for (int recordId : recordIds) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID = " + recordId);
                if (resultSet.next()) {
                    double 평점 = resultSet.getDouble("평점");
                    DecimalFormat 평점Format = new DecimalFormat("0.#");
                    System.out.println(resultSet.getInt("ID") + "    " + resultSet.getString("장소명") + "    " + resultSet.getString("카테고리")
                            + "    " + resultSet.getBoolean("화장실_보유여부") + "    " + resultSet.getBoolean("주차장_보유여부")
                            + "    " + resultSet.getInt("개설연도") + "    " + 평점Format.format(평점));
                }
            }
            System.out.println("조회된 레코드 개수: " + bitmapIndex.cardinality());
        }
    }
    static List<Integer> findRecordIds(BitSet bitmapIndex) {
        List<Integer> recordIds = new ArrayList<>();
        for (int i = 0; i < bitmapIndex.length(); i++) {
            if (bitmapIndex.get(i)) {
                recordIds.add(i + 1);
            }
        }
        return recordIds;
    }

}