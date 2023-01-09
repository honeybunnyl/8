import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;

public class CSVparser {
    public static void parseGames(String filePath, ArrayList<Game> games) throws Exception {
        var f = new FileReader(filePath);
        var csvreader = new CSVReader(f);

        csvreader.readNext();
        var arr = csvreader.readAll();
        for (var i = 0; i < arr.size(); i++) {
            var csv = arr.get(i);
            var game = new Game();
            game.setRank(Long.parseLong(csv[0]));
            game.setName(csv[1]);
            game.setPlatform(csv[2]);
            game.setYear(csv[3]);
            game.setGenre(csv[4]);
            game.setPublisher(csv[5]);
            game.setNA_Sales(Double.parseDouble(csv[6]));
            game.setEU_Sales(Double.parseDouble(csv[7]));
            game.setJP_Sales(Double.parseDouble(csv[8]));
            game.setOther_Sales(Double.parseDouble(csv[9]));
            game.setGlobal_Sales(Double.parseDouble(csv[10]));
            games.add(game);
        }
        f.close();
        csvreader.close();
    }
}
