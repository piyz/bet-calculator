package by.matrosov.appl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

        //----------------------------------------------------------------------------hm?
        BufferedReader reader = Files.newBufferedReader(Paths.get("d://file.txt"));
        List<String> oldFile = reader.lines().collect(Collectors.toList());
        Map<String, String> finalResult = new HashMap<>();
        //need to compare oldFile with result(map)
        //and write to a newFile
        for (Map.Entry entry : result.entrySet()){

            String teams = entry.getKey().toString();
            String[] oddsAndTrustFactor = entry.getValue().toString().split("and");
            String trustFactor = oddsAndTrustFactor[2].trim();
            double odds1 = Double.parseDouble(entry.getValue().toString().split("and")[0].replaceAll(",", ".").trim());
            double odds2 = Double.parseDouble(entry.getValue().toString().split("and")[1].replaceAll(",", ".").trim());

            double minOdds;
            if (odds1 > odds2){
                minOdds = odds2;
            }else if (odds1 < odds2){
                minOdds = odds1;
            }else {
                minOdds = odds1;
            }

            int checkFlag = 0;
            for (String s : oldFile) {
                if (s.contains(teams)){
                    checkFlag = 1;
                    double oldodds1 = Double.parseDouble(String.valueOf(s.split("!")[1].split("and")[0].replaceAll(",", ".").trim()));
                    double oldodds2 = Double.parseDouble(String.valueOf(s.split("!")[1].split("and")[1].replaceAll(",", ".").trim()));
                    int oldTrustFactor = Integer.parseInt(String.valueOf(s.split("!")[1].split("and")[2].trim().charAt(0)));
                    double oldMinOdds;
                    if (oldodds1 > oldodds2){
                        oldMinOdds = oldodds2;
                    }else if (oldodds1 < oldodds2){
                        oldMinOdds = oldodds1;
                    }else {
                        oldMinOdds = oldodds1;
                    }

                    if (Integer.parseInt(String.valueOf(trustFactor.charAt(0))) > oldTrustFactor){
                        finalResult.put(entry.getKey().toString(), entry.getValue().toString());
                    }else {
                        //check odds
                        if (oldMinOdds < minOdds){
                            finalResult.put(entry.getKey().toString(), entry.getValue().toString());
                        }else {
                            finalResult.put(s.split("!")[0].trim(), s.split("!")[1].trim());
                        }
                    }
                }
            }
            if (checkFlag == 0) finalResult.put(entry.getKey().toString(), entry.getValue().toString());
        }

        File file = new File("D:\\file.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry entry : finalResult.entrySet()) {
            if (entry.getKey().toString().contains("-vs-")){
                writer.write(entry.getKey() + "! " + entry.getValue());
                writer.newLine();
            }
        }
        writer.close();
    }
}
