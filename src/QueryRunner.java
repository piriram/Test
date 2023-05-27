import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class QueryRunner {

    public static void sqlQuery(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {

            System.out.println("조회하려는 컬럼을 선택해주세요: ");
            System.out.println("1. ID\n2. 장소명\n3. 카테고리\n4. 화장실_보유여부\n5. 주차장_보유여부\n6. 개설연도\n7. 평점");
            int option = scanner.nextInt();
            String countQuery = null;
            String query = "";
            String text = "";

            switch (option) {
                case 1:
                    System.out.println("ID를 입력하세요: ");
                    int id = scanner.nextInt();
                    text = "WHERE ID = " + id;
                    query = "SELECT * FROM places USE INDEX (idx_id) " + text;
                    countQuery = "SELECT COUNT(*) FROM places USE INDEX (idx_id) " + text;
                    break;
                case 2:
                    System.out.println("장소명을 입력하세요: ");
                    String 장소명 = scanner.next();
                    text = "WHERE 장소명 = '" + 장소명 + "'";
                    query = "SELECT * FROM places USE INDEX (idx_placeName) " + text;
                    countQuery = "SELECT COUNT(*) FROM places USE INDEX (idx_placeName)" + text;

                    break;
                case 3:
                    System.out.println("카테고리를 입력하세요: ");
                    String 카테고리 = scanner.next();
                    text = "FROM places WHERE 카테고리 = '" + 카테고리 + "'";
                    query = "SELECT * " + text;
                    countQuery = "SELECT COUNT(*) " + text;
                    break;
                case 4:
                    System.out.println("화장실 보유 여부를 입력하세요 (true/false): ");
                    boolean 화장실_보유여부 = scanner.nextBoolean();
                    text = "FROM places WHERE 화장실_보유여부 = " + 화장실_보유여부;
                    query = "SELECT * " + text;
                    countQuery = "SELECT COUNT(*) FROM places WHERE 화장실_보유여부 = " + 화장실_보유여부;
                    break;
                case 5:
                    System.out.println("주차장 보유 여부를 입력하세요 (true/false): ");
                    boolean 주차장_보유여부 = scanner.nextBoolean();
                    text = "FROM places WHERE 주차장_보유여부 = " + 주차장_보유여부;
                    query = "SELECT * " + text;
                    countQuery = "SELECT COUNT(*) FROM places WHERE 주차장_보유여부 = " + 주차장_보유여부;
                    break;
                case 6:
                    System.out.println("개설연도를 입력하세요: ");
                    int 개설연도 = scanner.nextInt();
                    text = "FROM places WHERE 개설연도 = " + 개설연도;
                    query = "SELECT * " + text;
                    countQuery = "SELECT COUNT(*) FROM places WHERE 개설연도 = " + 개설연도;
                    break;
                case 7:
                    System.out.println("평점을 입력하세요: ");
                    double 평점 = scanner.nextDouble();
                    text = "FROM places WHERE 평점 = " + 평점;
                    query = "SELECT * " + text;
                    countQuery = "SELECT COUNT(*) FROM places WHERE 평점 = " + 평점;
                    break;
            }
            ResultSet resultSet1 = statement.executeQuery(query);
            System.out.println("ID    장소명    카테고리    화장실_보유여부    주차장_보유여부    개설연도    평점");
            while (resultSet1.next()) {
                double 평점 = resultSet1.getDouble("평점");
                DecimalFormat 평점Format = new DecimalFormat("0.#");
                System.out.println(resultSet1.getInt("ID") + "    " + resultSet1.getString("장소명") + "    " + resultSet1.getString("카테고리")
                        + "    " + resultSet1.getBoolean("화장실_보유여부") + "    " + resultSet1.getBoolean("주차장_보유여부")
                        + "    " + resultSet1.getInt("개설연도") + "    " + 평점Format.format(평점));
            }
            if (countQuery != null) {
                ResultSet resultSet2 = statement.executeQuery(countQuery);

                if (resultSet2.next()) {
                    int count = resultSet2.getInt(1);
                    System.out.println("조회된 레코드 개수: " + count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rangeQuery(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement statement = connection.createStatement()) {
            System.out.println("범위 질의를 수행할 컬럼을 선택해주세요: ");
            System.out.println("1. 개설연도\n2. 평점");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("시작 연도를 입력하세요: ");
                    int startYear = scanner.nextInt();
                    System.out.println("종료 연도를 입력하세요: ");
                    int endYear = scanner.nextInt();
                    String query1 = "SELECT * FROM places WHERE 개설연도 >= " + startYear + " AND 개설연도 <= " + endYear;
                    ResultSet resultSet1 = statement.executeQuery(query1);
                    System.out.println("ID    장소명    카테고리    화장실_보유여부    주차장_보유여부    개설연도    평점");
                    while (resultSet1.next()) {
                        double 평점 = resultSet1.getDouble("평점");
                        DecimalFormat 평점Format = new DecimalFormat("0.#");
                        System.out.println(resultSet1.getInt("ID") + "    " + resultSet1.getString("장소명") + "    " + resultSet1.getString("카테고리")
                                + "    " + resultSet1.getBoolean("화장실_보유여부") + "    " + resultSet1.getBoolean("주차장_보유여부")
                                + "    " + resultSet1.getInt("개설연도") + "    " + 평점Format.format(평점));
                    }
                    break;
                case 2:
                    System.out.println("최소 평점을 입력하세요: ");
                    double minRating = scanner.nextDouble();
                    System.out.println("최대 평점을 입력하세요: ");
                    double maxRating = scanner.nextDouble();
                    String query2 = "SELECT * FROM places WHERE 평점 >= " + minRating + " AND 평점 <= " + maxRating;
                    ResultSet resultSet2 = statement.executeQuery(query2);
                    System.out.println("ID    장소명    카테고리    화장실_보유여부    주차장_보유여부    개설연도    평점");
                    while (resultSet2.next()) {
                        double 평점 = resultSet2.getDouble("평점");
                        DecimalFormat 평점Format = new DecimalFormat("0.#");
                        System.out.println(resultSet2.getInt("ID") + "    " + resultSet2.getString("장소명") + "    " + resultSet2.getString("카테고리")
                                + "    " + resultSet2.getBoolean("화장실_보유여부") + "    " + resultSet2.getBoolean("주차장_보유여부")
                                + "    " + resultSet2.getInt("개설연도") + "    " + 평점Format.format(평점));
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
