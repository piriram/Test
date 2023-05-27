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

public class BitmapIndexOperationSearcher {


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

    public void searchRecords(String op,String file1,String bool1,String file2,String bool2) throws SQLException, IOException {
        BitSet bitset1 = Config.ConditionalBitset(file1, bool1);
        BitSet bitset2 = Config.ConditionalBitset(file2, bool2);



        BitSet resultBitSet = (BitSet) bitset2.clone();
        if("and".equals(op)||"And".equals(op)||"AND".equals(op)){
            
            resultBitSet.and(bitset1);
        } else if ("or".equals(op)||"OR".equals(op)||"Or".equals(op)) {

            resultBitSet.or(bitset1);

        }else{
            System.out.println("목록에 없는 연산자입니다.");
        }


        List<Integer> recordIds = findRecordIds(resultBitSet);
        Config.BitsetPrint(recordIds);
    }

}
