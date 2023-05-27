import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

public class Config {
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";
    public static final String DBNAME = "DBSPROJECT";
    public static final String URL = "jdbc:mysql://localhost:3306/"+Config.DBNAME;
    // 다른 전역 변수들도 이곳에 추가
    public static String determineType(String input) {
        try {
            Integer.parseInt(input);
            return "Integer";
        } catch (NumberFormatException e) {
            // input is not an Integer
        }

        try {
            Double.parseDouble(input);
            return "Double";
        } catch (NumberFormatException e) {
            // input is not a Double
        }

        return "String";
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

}