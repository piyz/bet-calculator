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

        Document document = Jsoup.connect("https://www.hltv.org").get();
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
                    if (element.className().contains("_egb")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.05);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.05);
                    } else if (element.className().contains("_betway")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.042);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.042);
                    } else if (element.className().contains("_lootbet")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.035);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.035);
                    }else if (element.className().contains("day geoprovider_ggbet")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.041);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.041);
                    }else if (element.className().contains("day geoprovider_thunderpick")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1));  //?
                        secondTeamOdds.add(Double.parseDouble(odds2));
                    }else if (element.className().contains("_csgopositive")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1)); //?
                        secondTeamOdds.add(Double.parseDouble(odds2));
                    }else if (element.className().contains("_bet365")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.04);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.04);
                    }else if (element.className().contains("_bet188")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.031);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.031    );
                    }else if (element.className().contains("day geoprovider_1xbet")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.027);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.027);
                    }else if (element.className().contains("day geoprovider_esporbet")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.031);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.031);
                    }else if (element.className().contains("day geoprovider_pinnacle")){
                        String odds1 = element.child(1).text();
                        String odds2 = element.child(3).text();
                        firstTeamOdds.add(Double.parseDouble(odds1) - 0.024);
                        secondTeamOdds.add(Double.parseDouble(odds2) - 0.024);
                    }
                }catch (Exception ignore){

                }
            });

            double firstSum = 0;
            double secondSum = 0;
            for (Double firstTeamOdd : firstTeamOdds) {
                //System.out.println(1 / firstTeamOdd);
                firstSum = firstSum + 1 / firstTeamOdd;
            }
            for (Double secondTeamOdd : secondTeamOdds) {
                //System.out.println(1 / secondTeamOdd);
                secondSum = secondSum + 1 / secondTeamOdd;
            }
            double firstSumResult = firstSum / firstTeamOdds.size();
            double secondSumResult = secondSum / secondTeamOdds.size();

            result.put(teamsName, String.format("%.2f", firstSumResult) + " and " + String.format("%.2f", secondSumResult));
            firstTeamOdds.clear();
            secondTeamOdds.clear();
        }


        File file = new File("D:\\file.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry entry : result.entrySet()) {
            writer.write(entry.getKey() + ", " + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }
}
