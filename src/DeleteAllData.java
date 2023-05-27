import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteAllData {
    public static void deleteAllData(String dbName, String tableName) {
        String url = "jdbc:mysql://localhost:3306/" + dbName + "?serverTimezone=UTC";
        String username = "root";
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
            System.out.println("MySQL 데이터베이스에 연결되었습니다!");

            String deleteQuery = "DELETE FROM " + tableName;
            Statement statement = connection.createStatement();

            int deletedRows = statement.executeUpdate(deleteQuery);

            System.out.println(tableName + " 테이블에서 " + deletedRows + "개의 행이 삭제되었습니다.");

            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 또는 쿼리 실행에 실패했습니다.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        deleteAllData("TEST1", "places");
    }
}
