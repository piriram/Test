import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {
    private static final String[] categories = new String[]{"음식점", "카페", "공공기관", "의료기관", "문화시설"};
    private static final String[] files = {"주차장_보유여부.txt", "카테고리_공공기관.txt", "카테고리_문화시설.txt",
            "카테고리_음식점.txt", "카테고리_의료기관.txt", "카테고리_카페.txt", "화장실_보유여부.txt"};

    private Map<String, BitSet> bitmapIndexes = new HashMap<>();
    private int rowCount = 0;

    public void loadIndexes() throws IOException {
        for (String file : files) {
            String data = new String(Files.readAllBytes(Paths.get(file)));
            BitSet bitSet = new BitSet();
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == '1') bitSet.set(i);
            }
            bitmapIndexes.put(file.replace(".txt", ""), bitSet);
            rowCount = Math.max(rowCount, bitSet.length());
        }
    }

    public void createAndInsertRecords(int count) throws SQLException {
        BatchInsert.createAndInsertRecords(count);
    }

    public void executeBitmapIndexQuery() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("주차장 보유여부 입력(1/0): ");
        boolean parking = Boolean.parseBoolean(scanner.nextLine());
        System.out.println("화장실 보유여부 입력(1/0): ");
        boolean restroom = Boolean.parseBoolean(scanner.nextLine());
        System.out.println("카테고리 입력(음식점/카페/공공기관/의료기관/문화시설): ");
        String category = "카테고리_" + scanner.nextLine();

        BitSet parkingBitSet = bitmapIndexes.get(parking ? "주차장_보유여부" : "");
        BitSet restroomBitSet = bitmapIndexes.get(restroom ? "화장실_보유여부" : "");
        BitSet categoryBitSet = bitmapIndexes.get(category);

        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM places")) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            int index = 0;
            while (resultSet.next() && index < rowCount) {
                if ((parkingBitSet == null || parkingBitSet.get(index)) &&
                        (restroomBitSet == null || restroomBitSet.get(index)) &&
                        (categoryBitSet == null || categoryBitSet.get(index))) {
                    StringBuilder recordBuilder = new StringBuilder("Record: ");
                    for (int i = 1; i <= columnCount; i++) {
                        recordBuilder.append(metaData.getColumnLabel(i)).append(": ").append(resultSet.getString(i)).append(", ");
                    }
                    String record = recordBuilder.toString();
                    System.out.println(record.substring(0, record.length() - 2));
                }
                index++;
            }
        }
    }

    public void executeRangeQuery() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("시작 범위 입력: ");
        int startRange = Integer.parseInt(scanner.nextLine());
        System.out.println("종료 범위 입력: ");
        int endRange = Integer.parseInt(scanner.nextLine());

        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM places WHERE ID BETWEEN " + startRange + " AND " + endRange)) {

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("ID") + "    " + resultSet.getString("장소명") + "    " +
                        resultSet.getString("카테고리") + "    " + resultSet.getBoolean("화장실_보유여부") + "    " +
                        resultSet.getBoolean("주차장_보유여부") + "    " + resultSet.getInt("개설연도") + "    " +
                        String.format("%.1f", resultSet.getDouble("평점")));
            }
        }
    }

    public void executeValueSearch() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("검색할 값 입력: ");
        String searchValue = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM places WHERE 장소명 LIKE '%" + searchValue + "%'")) {

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("ID") + "    " + resultSet.getString("장소명") + "    " +
                        resultSet.getString("카테고리") + "    " + resultSet.getBoolean("화장실_보유여부") + "    " +
                        resultSet.getBoolean("주차장_보유여부") + "    " + resultSet.getInt("개설연도") + "    " +
                        String.format("%.1f", resultSet.getDouble("평점")));
            }
        }
    }

    public void deleteAllRecords() throws SQLException {
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM places");
        }
    }

    public static void main(String[] args) {
        ConsoleApp consoleApp = new ConsoleApp();
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Console App =====");
        System.out.println("1. 레코드 생성");
        System.out.println("2. 비트맵 인덱스 질의");
        System.out.println("3. 범위 질의");
        System.out.println("4. 값 찾기");
        System.out.println("5. 레코드 모두 삭제");
        System.out.println("0. 종료");

        boolean exit = false;

        while (!exit) {
            System.out.println("원하는 작업을 선택하세요 (0-5): ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    try {
                        System.out.println("레코드 생성 중...");
                        consoleApp.createAndInsertRecords(100000);
                        System.out.println("레코드 생성이 완료되었습니다.");
                    } catch (SQLException e) {
                        System.out.println("레코드 생성 중 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        consoleApp.executeBitmapIndexQuery();
                    } catch (SQLException e) {
                        System.out.println("비트맵 인덱스 질의 중 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        consoleApp.executeRangeQuery();
                    } catch (SQLException e) {
                        System.out.println("범위 질의 중 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        consoleApp.executeValueSearch();
                    } catch (SQLException e) {
                        System.out.println("값 찾기 중 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        System.out.println("모든 레코드를 삭제합니다...");
                        consoleApp.deleteAllRecords();
                        System.out.println("모든 레코드 삭제가 완료되었습니다.");
                    } catch (SQLException e) {
                        System.out.println("레코드 삭제 중 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }

        System.out.println("프로그램을 종료합니다. 감사합니다!");
    }
}
