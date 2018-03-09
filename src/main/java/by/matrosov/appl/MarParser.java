package by.matrosov.appl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
                    }catch (Exception ignore){}
                }
            }
        });
        System.out.println(marList);


        BufferedReader reader = Files.newBufferedReader(Paths.get("d://file.txt"));
        hltvList = reader.lines().collect(Collectors.toList());

        for (String s : marList) {
            String[] arr = s.split(",");
            String[] teams = arr[0].split("-");
            String[] marOdds = arr[1].split("-");
            double marOdds1 = Double.parseDouble(marOdds[0].trim());
            double marOdds2 = Double.parseDouble(marOdds[1].trim());

            String team1 = teams[0].toLowerCase().trim();
            if (team1.contains("team"))
                team1 = team1.replaceAll("team", " ").trim();
            if (team1.contains("gaming"))
                team1 = team1.replaceAll("gaming", " ").trim();
            if (team1.contains("tactics"))
                team1 = team1.replaceAll("tactics", " ").trim();
            if (team1.contains("elements pro"))
                team1 = team1.replaceAll("elements pro", "epg").trim();
            if (team1.contains("esports"))
                team1 = team1.replaceAll("esports", " ").trim();

            String team2 = teams[1].toLowerCase().trim();
            if (team2.contains("team"))
                team2 = team2.replaceAll("team", " ").trim();
            if (team2.contains("gaming"))
                team2 = team2.replaceAll("gaming", " ").trim();
            if (team2.contains("tactics"))
                team2 = team2.replaceAll("tactics", " ").trim();
            if (team2.contains("elements pro"))
                team2 = team2.replaceAll("elements pro", "epg").trim();
            if (team2.contains("esports"))
                team2 = team2.replaceAll("esports", " ").trim();


            for (String str : hltvList) {
                String[] hltvArr = str.split("!");
                String[] hltvTeams = hltvArr[0].split("-vs-");
                String[] hltvOdds = hltvArr[1].split("and");
                double hltvOdds1 = Double.parseDouble(hltvOdds[0].replaceAll(",", ".").trim());
                double hltvOdds2 = Double.parseDouble(hltvOdds[1].replaceAll(",", ".").trim());
                String team1hltv = hltvTeams[0].replaceAll("-", " ").toLowerCase();
                String team2hltv = hltvTeams[1].replaceAll("-", " ").toLowerCase();

                if (team1hltv.contains(team1) && team2hltv.contains(team2)){
                    if (marOdds1 > marOdds2){
                        result.put(team1 + " vs " + team2, "bet on " + team1 + " " + String.format("%.4f", kelly(marOdds1, hltvOdds1)));
                    }else if (marOdds1 < marOdds2){
                        result.put(team1 + " vs " + team2, "bet on " + team2 + " " + String.format("%.4f", kelly(marOdds2, hltvOdds2)));
                    }else if (marOdds1 == marOdds2){
                        result.put(team1 + " vs " + team2, "bet on " + team1 + " " + String.format("%.4f", kelly(marOdds1, hltvOdds1)) +
                        " or bet on " + team2 + " " + String.format("%.2f", kelly(marOdds2, hltvOdds2)));
                    }
                }
            }
        }

        File file = new File("D:\\result.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry entry : result.entrySet()) {
            writer.write(entry.getKey() + ", " + entry.getValue());
            writer.newLine();
        }
        writer.close();

    }

    private static double kelly(double odds, double success){
        return (success * (odds - 1) - (1 - success)) / (odds - 1);
    }


}
