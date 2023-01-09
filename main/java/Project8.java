import java.sql.DriverManager;
import java.util.ArrayList;

public class Project8 {

    private static ArrayList<Game> games = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        CSVparser.parseGames("src/main/resources/games.csv", games);
        db();
    }


    private static void db() throws Exception {
        var c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/games.db");
        var s = c.createStatement();

        s.execute("CREATE TABLE IF NOT EXISTS 'Games' " +
                "('Rank' int PRIMARY KEY, " +
                "'Name' varchar, " +
                "'Platform' varchar," +
                "'Year' int, " +
                "'Genre' varchar, " +
                "'Publisher' varchar, " +
                "'NA_Sales' real, " +
                "'EU_Sales' real, " +
                "'JP_Sales' real, " +
                "'Other_Sales' real, " +
                "'Global_Sales' real); ");


        var ps = c.prepareStatement("INSERT INTO 'Games' " +
                "('Rank','Name','Platform','Year','Genre','Publisher','NA_Sales','EU_Sales','JP_Sales','Other_Sales','Global_Sales') " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;");

        c.setAutoCommit(false);

        var batchTotal = 0;
        for (var i = 0; i < games.size(); i++) {
            var g = games.get(i);
            ps.setLong(1, g.getRank());
            ps.setString(2, g.getName());
            ps.setString(3, g.getPlatform());
            ps.setLong(4, g.getYear());
            ps.setString(5, g.getGenre());
            ps.setString(6, g.getPublisher());
            ps.setDouble(7, g.getNA_Sales());
            ps.setDouble(8, g.getEU_Sales());
            ps.setDouble(9, g.getJP_Sales());
            ps.setDouble(10, g.getOther_Sales());
            ps.setDouble(11, g.getGlobal_Sales());
            ps.addBatch();
            if (batchTotal++ >= 4096) {
                ps.executeBatch();
                ps.clearBatch();
                batchTotal = 0;
            }
        }
        if (batchTotal > 0) {
            ps.executeBatch();
        }


        //src/main/resources/task1.jpg
        //Постройте график по средним показателям глобальных продаж, объединив их по платформам.
        //select platform,avg(global_sales) as sales from Games group by platform;

        System.out.println("Выведите игру с самым высоким показателем продаж в Европе за 2000 год");
        System.out.println(s.executeQuery("select *, max(eu_sales) from Games where year=2000;").getString("name"));

        var rs = s.executeQuery("select *,max(jp_sales) from games WHERE (year BETWEEN 2000 and 2006) and genre=\"Sports\";");
        System.out.println("Выведите в консоль название игры, созданную в промежутке с 2000 по 2006 год с самым высоким показателем продаж в Японии из жанра спортивных игр.");
        System.out.println(rs.getString("name"));

        c.commit();
        ps.close();
        c.close();
    }
}
