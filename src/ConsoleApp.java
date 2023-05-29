import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Config.setConnectionInfo();
        while (true) {
            System.out.println("========= 콘솔 창 =========");
            System.out.println("1. 테이블 생성");
            System.out.println("2. 레코드 생성");
            System.out.println("3. 비트맵 인덱스 생성");
            System.out.println("4. 비트맵 인덱스 질의");
            System.out.println("5. B+ Tree Index 생성");
            System.out.println("6. 값 찾기");
            System.out.println("7. 범위 질의(개설연도, 평점)");
            System.out.println("8. 레코드 모두 삭제");
            System.out.println("0. 종료");
            System.out.print("메뉴를 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 2:
                    BatchInsert.createAndInsertRecords(1000000);
                    System.out.println("레코드 생성이 완료되었습니다.");
                    break;
                case 3:
                    generateBitmapIndexes();
                    break;
                case 4:
                    performBitmapIndexQuery(scanner);
                    break;
                case 5:
                    BplusTreeCreator.createIndexes(scanner);
                    break;
                case 6:
                    QueryRunner.sqlQuery(scanner);
                    break;
                case 7:
                    QueryRunner.rangeQuery(scanner);
                    break;
                case 8:
                    DeleteAllData.deleteAllData();
                    System.out.println("모든 레코드가 삭제되었습니다.");
                    break;
                case 1:

                    System.out.println("DB 조작을 위한 준비 마쳤습니다.");
                    System.out.print("데이터베이스 이름을 입력하세요: ");
                    Config.DBNAME = scanner.nextLine();
                    Config.URL = "jdbc:mysql://localhost:3306/" + Config.DBNAME;
                    System.out.print("테이블 이름을 입력하세요: ");
                    Config.TABLE_NAME = scanner.nextLine();
                    System.out.println("테이블을 생성하겠습니다.");
                    CreateTable.createTable();
                    break;

                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못된 메뉴 선택입니다. 다시 선택해주세요.");
            }
        }
    }

    private static void generateBitmapIndexes() {
        BitmapIndexGenerator generator = new BitmapIndexGenerator();
        try {
            generator.generateBitmapIndexes();
            System.out.println("비트맵 인덱스 생성이 완료되었습니다.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted");
        }
    }


    private static void performBitmapIndexQuery(Scanner scanner) {
        System.out.println("========= 비트맵 인덱스 질의를 위한 컬럼개수 선택 =========");
        System.out.println("1. 컬럼개수 1개");
        System.out.println("2. 컬럼개수 2개");
        System.out.println("3. 컬럼개수 3개");
        System.out.print("선택하세요: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                performColOne(scanner);
                break;
            case 2:
                performTwoColumnBitmapQuery(scanner);
                break;
            case 3:
                performThreeColumn(scanner);
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    private static void performColOne(Scanner scanner) {
        System.out.println("========= 검색할 컬럼을 선택해주세요 =========");
        System.out.println("1. 주차장_보유여부");
        System.out.println("2. 화장실_보유여부");
        System.out.println("3. 카테고리");
        int bitChoice = scanner.nextInt();
        scanner.nextLine();
        switch (bitChoice){
            case 1:
                System.out.print("true or false를 입력해주세요. ");
                System.out.println();
                String boolChoice1 = scanner.next();
                scanner.nextLine();

                try {
                    BitmapIndexSearcher searcher = new BitmapIndexSearcher();
                    searcher.searchOneRecords("주차장_보유여부",boolChoice1);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.print("true or false를 입력해주세요. ");
                System.out.println();
                String boolChoice2 = scanner.next();
                scanner.nextLine();
                try {

                    BitmapIndexSearcher searcher = new BitmapIndexSearcher();
                    searcher.searchOneRecords("화장실_보유여부",boolChoice2);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                System.out.println("========= 검색할 항목을 입력해주세요 =========");
                System.out.println("공공기관 or 문화시설 or 음식점 or 의료기관 or 카페");
                String cateChoice = scanner.next();
                scanner.nextLine();
                try {
                    BitmapIndexSearcher searcher = new BitmapIndexSearcher();
                    searcher.searchCategoryRecords(cateChoice);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private static void performTwoColumnBitmapQuery(Scanner scanner) {
        System.out.println("========= 컬럼개수 2개 - 연산 선택 =========");
        System.out.println("1. AND 연산");
        System.out.println("2. OR 연산");
        System.out.print("선택하세요: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("컬럼명1을 입력하세요: ");
        String column1 = scanner.nextLine();
        System.out.print("컬럼명1의 값을 입력하세요 ");
        String bool1 = scanner.nextLine();
        System.out.print("컬럼명2를 입력하세요: ");
        String column2 = scanner.nextLine();
        System.out.print("컬럼명2의 값을 입력하세요 ");
        String bool2 = scanner.nextLine();

        switch (choice) {
            case 1:
                try {
                    BitmapIndexOperationSearcher searcher = new BitmapIndexOperationSearcher();
                    searcher.searchTwoRecords("and",column1,bool1,column2,bool2);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    BitmapIndexOperationSearcher searcher = new BitmapIndexOperationSearcher();
                    searcher.searchTwoRecords("or",column1,bool1,column2,bool2);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }
    private static void performThreeColumn(Scanner scanner) {
        System.out.println("========= 연산 선택 =========");
        System.out.println("1. AND 연산");
        System.out.println("2. OR 연산");
        System.out.print("선택하세요: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("화장실_보유여부의 값을 입력하세요 ");
        String bool1 = scanner.nextLine();
        System.out.print("주차장_보유여부의 값을 입력하세요 ");
        String bool2 = scanner.nextLine();
        System.out.print("카테고리의 값을 입력하세요 ");
        String bool3 = scanner.nextLine();

        switch (choice) {
            case 1:
                try {
                    BitmapIndexOperationSearcher searcher = new BitmapIndexOperationSearcher();
                    searcher.searchThreeRecords("and",bool1,bool2,bool3);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    BitmapIndexOperationSearcher searcher = new BitmapIndexOperationSearcher();
                    searcher.searchThreeRecords("or",bool1,bool2,bool3);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }
    private static void performRangeQuery(Scanner scanner) {
        System.out.println("========= 범위 질의 (개설연도, 평점) =========");
        System.out.println("컬럼 1: 개설연도");
        System.out.println("컬럼 2: 평점");
        System.out.print("첫 번째 컬럼을 선택하세요 (1 or 2): ");
        int column1 = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("두 번째 컬럼을 선택하세요 (1 or 2): ");
        int column2 = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("첫 번째 값 입력: ");
        double value1 = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        System.out.print("두 번째 값 입력: ");
        double value2 = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character


    }

    private static void performValueSearch(Scanner scanner) {
        System.out.println("========= 값 찾기 =========");
        System.out.print("컬럼명을 입력하세요. ex.ID,장소명,카테고리,화장실_보유여부,주차장_보유여부,개설연도,평점");
        String columnName = scanner.nextLine();
        System.out.print("찾는 값 입력: ");
        String value = scanner.nextLine();

//        ValueSearcher valueSearcher = new ValueSearcher();
//        valueSearcher.searchRecords(columnName, value);

    }
}
