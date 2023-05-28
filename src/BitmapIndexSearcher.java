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

        BitSet bitmapIndex = Config.getOneBitSet(COLLUM_NAME, number);


        Config.BitsetPrint(bitmapIndex);
    }

    public void searchCategoryRecords(String CATEGORY_NAME) throws SQLException, IOException {
        BitSet bitmapIndex = Config.getCategoryBitSet(CATEGORY_NAME);
        Config.BitsetPrint(bitmapIndex);
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