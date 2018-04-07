package by.matrosov.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        double margin = count[0] - 1;
        System.out.println(margin);




    }
}
