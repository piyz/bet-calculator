package by.matrosov.appl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarParser {
    public static void main(String[] args) throws IOException {

        List<String> hltvList = new ArrayList<>();
        List<String> marList = new ArrayList<>();
        Map<String,String> result = new HashMap<>();

        Document document = Jsoup.connect("https://www.marathonbet.com/su/betting/e-Sports/?menu=1895085").get();
        Elements matchElements = document.getElementsByAttributeValue("class", "category-container");

        matchElements.forEach(element -> {
            String csgoTag = element.child(0).child(0).child(0).child(1).child(0).child(0).tagName("span").text();
            if (csgoTag.contains("CS:GO")){
                int childNodeSize = element.child(1).child(0).child(0).childNodeSize();
                //System.out.println(childNodeSize);
                for (int i = 1; i < childNodeSize; i++) {
                    try{
                        Element gameElement = element.child(1).child(0).child(0).child(i);
                        String game = gameElement.attr("data-event-name");
                        //matchesList.add(game);
                        String odds1 = gameElement.child(0).child(2).tagName("span").text();
                        String odds2 = gameElement.child(0).child(3).tagName("span").text();
                        String resultOdds = odds1 + "-" + odds2;
                        marList.add(game + "," + resultOdds);
                        //result.put(game, resultOdds);
                    }catch (Exception ignore){}
                }
            }
        });

        BufferedReader reader = Files.newBufferedReader(Paths.get("d://file.txt"));
        hltvList = reader.lines().collect(Collectors.toList());





    }
}
