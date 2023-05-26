public class Config {
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";
//    public static final String URL = "jdbc:mysql://localhost:3306/TEST1?serverTimezone=UTC";
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

}
