package by.matrosov.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Tournaments {
    public static void main(String[] args) throws IOException {

        Map<String, Double> teams = new HashMap<>();

        Document document = Jsoup.connect("https://www.marathonbet.com/su/betting/e-Sports/?menu=1895085").userAgent("Mozilla/5.0").get();
        Element tournamentElement = document.getElementById("category3828097");

        int childNodeSize = tournamentElement.child(0).child(0).child(0).child(0).child(0).child(0).child(1).child(0).child(0).child(1).child(0).childNodeSize();
        Element tableElement = tournamentElement.child(0).child(0).child(0).child(0).child(0).child(0).child(1).child(0).child(0).child(1).child(0);
        for (int i = 1; i < childNodeSize; i++) {
            try{
                teams.put(tableElement.child(i).child(0).tagName("td").text(), Double.valueOf(tableElement.child(i).child(1).child(0).tagName("span").text()));
                teams.put(tableElement.child(i).child(2).tagName("td").text(), Double.valueOf(tableElement.child(i).child(3).child(0).tagName("span").text()));
            }catch (Exception ignore){}
        }

        final double[] count = {0};
        teams.forEach((s, d) -> count[0] = count[0] + 1/d);

        double marginSumMar = count[0] - 1;
        double marginMar = marginSumMar / teams.size();


        //------------------------------------------------------------------------start drake
        BufferedReader reader = Files.newBufferedReader(Paths.get("d://tournament.txt"));
        List<String> lounge = reader.lines().collect(Collectors.toList());

        List<String> resultLounge = new ArrayList<>();
        for (String s : lounge) {
            if (s.contains(".")) {
                resultLounge.add(s);
            }
        }

        final double[] count1 = {0};
        resultLounge.forEach(s -> {
            String[] strings = s.split(" ");
            double odds = Double.parseDouble(strings[strings.length - 1]);
            count1[0] = count1[0] + 1 / odds;
        });

        double marginSumDrake = count1[0] - 1;
        double marginDrake = marginSumDrake / resultLounge.size();
        //-----------------------------------------------------------------------end drake
        
        //compare
        List<String> result = new ArrayList<>(); //success

        double sum = 0;
        for (int i = 0; i < resultLounge.size(); i++) {
            String team = resultLounge.get(i).split(" ")[0].toLowerCase();
            String[] teamAndoddsDrake = resultLounge.get(i).split(" ");
            String oddsDrake = teamAndoddsDrake[teamAndoddsDrake.length - 1];
            for (Map.Entry entry : teams.entrySet()){
                if (entry.getKey().toString().toLowerCase().contains(team)){
                    double newOdds = (1 / Double.parseDouble(entry.getValue().toString()) - marginMar + 1 / Double.parseDouble(oddsDrake) - marginDrake) / 2;
                    result.add(team + " - " + String.format("%.4f", newOdds));
                    sum = sum + newOdds;
                }
            }
        }

        System.out.println(result);

        List<String> resultTournament = new ArrayList<>();

        for (Map.Entry entry : teams.entrySet()){
            double odds = Double.parseDouble(entry.getValue().toString());
            for (int i = 0; i < result.size(); i++) {
                String team = result.get(i).split(" - ")[0];
                double success = Double.parseDouble(result.get(i).split(" - ")[1].replaceAll(",", "."));
                if (entry.getKey().toString().toLowerCase().contains(team)){
                    resultTournament.add(team + " bet " + String.format("%.4f", kelly(odds, success)));
                }
            }
        }

        System.out.println(resultTournament);

        File file = new File("D:\\resultTournamet.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < resultTournament.size(); i++) {
            if (!resultTournament.get(i).contains("-")){
                writer.write(resultTournament.get(i));
                writer.newLine();
            }
        }
        writer.close();


    }

    private static double kelly(double odds, double success){
        return (success * (odds - 1) - (1 - success)) / (odds - 1);
    }
}
