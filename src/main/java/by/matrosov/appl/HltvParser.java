package by.matrosov.appl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HltvParser {
    public static void main(String[] args) throws IOException {
        List<String> matchesList = new ArrayList<>();
        List<Double> firstTeamOdds = new ArrayList<>();
        List<Double> secondTeamOdds = new ArrayList<>();
        Map<String,String> result = new HashMap<>();

        Document document = Jsoup.connect("https://www.hltv.org").userAgent("Mozilla/5.0").get();
        Elements matchElements = document.getElementsByAttributeValue("class", "hotmatch-box a-reset");
        matchElements.forEach(element -> {
            String url = element.attr("href");
            matchesList.add(url);
        });
        //matchesList.forEach(System.out::println);

        for (int i = 0; i < matchesList.size(); i++) {
            String[] s = matchesList.get(i).split("/");
            String teamsName = s[3];

            Document document1 = Jsoup.connect("https://www.hltv.org" + matchesList.get(i)).get();
            Elements oddsElements = document1.getElementsByAttributeValueContaining("class", "geoprovider");
            oddsElements.forEach(element -> {
                try{
                    if (element.className().contains("_egb") ||
                            element.className().contains("_betway") ||
                            element.className().contains("_lootbet") ||
                            element.className().contains("day geoprovider_ggbet") ||
                            element.className().contains("day geoprovider_thunderpick") ||
                            element.className().contains("_csgopositive") ||
                            element.className().contains("_bet365") ||
                            element.className().contains("_bet188") ||
                            element.className().contains("day geoprovider_1xbet") ||
                            element.className().contains("day geoprovider_esporbet") ||
                            element.className().contains("day geoprovider_pinnacle") ||
                            element.className().contains("day geoprovider_xbet")){
                        double odds1 = Double.parseDouble(element.child(1).text());
                        double odds2 = Double.parseDouble(element.child(3).text());
                        double margin = (1/odds1 + 1/odds2 - 1) / 2;
                        double odds1Result = 1/odds1 - margin;
                        double odds2Result = 1/odds2 - margin;
                        firstTeamOdds.add(odds1Result);
                        secondTeamOdds.add(odds2Result);
                    }
                }catch (Exception ignore){

                }
            });

            double firstSum = 0;
            double secondSum = 0;
            for (Double firstTeamOdd : firstTeamOdds) {
                firstSum = firstSum + firstTeamOdd;
            }
            for (Double secondTeamOdd : secondTeamOdds) {
                secondSum = secondSum + secondTeamOdd;
            }
            double firstSumResult = firstSum / firstTeamOdds.size();
            double secondSumResult = secondSum / secondTeamOdds.size();

            result.put(teamsName, String.format("%.2f", firstSumResult) + " and "
                    + String.format("%.2f", secondSumResult) + " and "
                    + firstTeamOdds.size() + "/" + secondTeamOdds.size());
            firstTeamOdds.clear();
            secondTeamOdds.clear();
        }


        File file = new File("D:\\file.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry entry : result.entrySet()) {
            writer.write(entry.getKey() + "! " + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }
}
