package by.matrosov.appl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public static void main(String[] args) throws IOException {
        List<String> matchesList = new ArrayList<>();
        Map<String,String> egbResult = new HashMap<>();
        Map<String,String> betwayResult = new HashMap<>();
        List<String> hltvList = new ArrayList<>();

        Document document = Jsoup.connect("https://www.hltv.org").userAgent("Mozilla/5.0").get();
        Elements matchElements = document.getElementsByAttributeValue("class", "hotmatch-box a-reset");
        matchElements.forEach(element -> {
            String url = element.attr("href");
            matchesList.add(url);
        });

        for (int i = 0; i < matchesList.size(); i++) {
            String[] s = matchesList.get(i).split("/");
            String teamsName = s[3];

            Document document1 = Jsoup.connect("https://www.hltv.org" + matchesList.get(i)).get();
            Elements oddsElements = document1.getElementsByAttributeValueContaining("class", "geoprovider");
            oddsElements.forEach(element -> {
                try{
                    if (element.className().contains("_egb")){
                        double odds1 = Double.parseDouble(element.child(1).text());
                        double odds2 = Double.parseDouble(element.child(3).text());
                        egbResult.put(teamsName, String.format("%.2f", odds1) + " and " + String.format("%.2f", odds2));
                    }
                    if (element.className().contains("_betway")){
                        double odds1 = Double.parseDouble(element.child(1).text());
                        double odds2 = Double.parseDouble(element.child(3).text());
                        betwayResult.put(teamsName, String.format("%.2f", odds1) + " and " + String.format("%.2f", odds2));
                    }
                }catch (Exception ignore){}
            });
        }

        File file = new File("D:\\resultFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("-------------------EGB-------------------------");
        writer.newLine();
        for (Map.Entry entry : egbResult.entrySet()) {
            writer.write(entry.getKey() + "! " + entry.getValue());
            writer.newLine();
        }
        writer.write("-------------------BetWay----------------------");
        writer.newLine();
        for (Map.Entry entry : betwayResult.entrySet()) {
            writer.write(entry.getKey() + "! " + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }
}
