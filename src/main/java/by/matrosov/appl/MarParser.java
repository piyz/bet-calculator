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

    private static final double minOdds = 0.01;

    public static void main(String[] args) throws IOException {
        List<String> hltvList;
        List<String> marList = new ArrayList<>();
        Map<String,String> result = new HashMap<>();
        //DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Document document = Jsoup.connect("https://www.marathonbet.com/su/betting/e-Sports/?menu=1895085").userAgent("Mozilla/5.0").get();
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
                        String odds1 = gameElement.child(0).child(2).tagName("span").text();
                        String odds2 = gameElement.child(0).child(3).tagName("span").text();
                        String resultOdds = odds1 + "-" + odds2;
                        marList.add(game + "," + resultOdds);
                    }catch (Exception ignore){}
                }
            }
        });
        marList.forEach(System.out::println);

        BufferedReader reader = Files.newBufferedReader(Paths.get("d://file.txt"));
        hltvList = reader.lines().collect(Collectors.toList());

        for (String s : marList) {
            String[] arr = s.split(",");
            String[] teams = arr[0].toLowerCase()
                    .replaceAll("'", "")
                    .replaceAll("team", " ")
                    .replaceAll("gaming", " ")
                    .replaceAll("tactics", " ")
                    .replaceAll("elements pro", "epg")
                    .replaceAll("esports", " ")
                    .replaceAll("clan", " ")
                    .replaceAll("x-kom", "x kom")
                    .replaceAll("b\\.o\\.o\\.t-dream\\[s\\]cape", "boot ds")
                    .replaceAll("sapphirekelownadotcom", "subtle")
                    .replaceAll("mous \\.cs", "mousesports")
                    .replaceAll("virtus\\.pro", "virtuspro")
                    .replaceAll("ninjas in pyjamas", "nip")
                    .replaceAll("addict", " ")
                    .replaceAll("nao tem como", "no tem como")
                    .split("-");
            String[] marOdds = arr[1].split("-");
            double marOdds1 = Double.parseDouble(marOdds[0].trim());
            double marOdds2 = Double.parseDouble(marOdds[1].trim());

            String team1 = teams[0].trim();
            String team2 = teams[1].trim();

            for (String str : hltvList) {
                String[] hltvArr = str.split("!");
                String[] hltvTeams = hltvArr[0].split("-vs-");
                String[] hltvOdds = hltvArr[1].split("and");
                double hltvOdds1 = Double.parseDouble(hltvOdds[0].replaceAll(",", ".").trim());
                double hltvOdds2 = Double.parseDouble(hltvOdds[1].replaceAll(",", ".").trim());
                String trstFactr = hltvOdds[2].trim();
                String team1hltv = hltvTeams[0].replaceAll("-", " ").toLowerCase();
                String team2hltv = hltvTeams[1].replaceAll("-", " ").toLowerCase();

                if (team1hltv.contains(team1) && team2hltv.contains(team2)){
                    calculateBet(result, marOdds1, marOdds2, team1, team2, hltvOdds1, hltvOdds2, trstFactr);
                }else if (team1hltv.contains(team2) && team2hltv.contains(team1)){
                    calculateBet(result, marOdds1, marOdds2, team1, team2, hltvOdds2, hltvOdds1, trstFactr);
                }
            }
        }

        File file = new File("D:\\result.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry entry : result.entrySet()) {
            writer.write(entry.getKey() + ", " + entry.getValue());
            writer.newLine();
        }
        writer.write("--------------------------------------------");
        writer.newLine();

        for (Map.Entry entry : result.entrySet()){
            String[] value = entry.getValue().toString().replaceAll(",", ".").split(" ");
            if (Double.parseDouble(value[value.length - 4]) >= minOdds){
                writer.write(entry.getKey() + ", " + entry.getValue());
                writer.newLine();
            }
        }
        writer.close();

        /*
            int gameCount = 0;
            for (Map.Entry entry : result.entrySet()){
                String[] value = entry.getValue().toString().replaceAll(",", ".").split(" ");
                if (Double.parseDouble(value[value.length - 4]) > 0){
                    gameCount++;
                }
            }

            Date date = new Date();
            if (gameCount == 0){
                System.out.println(dateFormat.format(date) + " : NULL");
            }else {
                System.out.println(dateFormat.format(date) + ": " + gameCount + " MATCHES FOUND");
            }
            hltvList.clear();
            marList.clear();
            result.clear();
            TimeUnit.MINUTES.sleep(10);
         */
    }

    private static void calculateBet(Map<String, String> result, double marOdds1, double marOdds2, String team1, String team2, double hltvOdds1, double hltvOdds2, String trstFactr) {
        if (marOdds1 > marOdds2){
            result.put(team1 + " vs " + team2, "bet on " + team1 + " " + String.format("%.4f", kelly(marOdds1, hltvOdds1)) +
            " (odds " + marOdds1 + ")" + "-> " + trstFactr);
        }else if (marOdds1 < marOdds2){
            result.put(team1 + " vs " + team2, "bet on " + team2 + " " + String.format("%.4f", kelly(marOdds2, hltvOdds2)) +
            " (odds " + marOdds2 + ")" + "-> " + trstFactr);
        }else if (marOdds1 == marOdds2){
            result.put(team1 + " vs " + team2, "bet on " + team1 + " " + String.format("%.4f", kelly(marOdds1, hltvOdds1)) +
            " or bet on " + team2 + " " + String.format("%.2f", kelly(marOdds2, hltvOdds2)) + " (odds " + marOdds1 + ")" + "-> " + trstFactr);
        }
    }

    private static double kelly(double odds, double success){
        return (success * (odds - 1) - (1 - success)) / (odds - 1);
    }
}