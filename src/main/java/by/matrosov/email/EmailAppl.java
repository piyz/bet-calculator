package by.matrosov.email;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EmailAppl {

    private static final double minOdds = 0.01;

    //-----------for MarParser------------------
    private static List<String> hltvList;
    private static List<String> marList = new ArrayList<>();
    private static Map<String,String> result = new HashMap<>();

    //----------for hltvParser------------------
    private static List<String> matchesList = new ArrayList<>();
    private static List<Double> firstTeamOdds = new ArrayList<>();
    private static List<Double> secondTeamOdds = new ArrayList<>();
    private static Map<String,String> hltvResult = new HashMap<>();

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        while (true){
            try{
                Document document = Jsoup.connect("https://www.marathonbet.com/su/betting/e-Sports/?menu=1895085").userAgent("Mozilla/5.0").get();
                Elements matchElements = document.getElementsByAttributeValue("class", "category-container");

                matchElements.forEach(element -> {
                    String csgoTag = element.child(0).child(0).child(0).child(1).child(0).child(0).tagName("span").text();
                    if (csgoTag.contains("CS:GO")){
                        int childNodeSize = element.child(1).child(0).child(0).childNodeSize();
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

                //parse from hltv
                Document hltvDocument = Jsoup.connect("https://www.hltv.org").userAgent("Mozilla/5.0").get();
                Elements matchElements1 = hltvDocument.getElementsByAttributeValue("class", "hotmatch-box a-reset");
                matchElements1.forEach(element -> {
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
                        }catch (Exception ignore){}
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

                    hltvResult.put(teamsName, String.format("%.2f", firstSumResult) + " and "
                            + String.format("%.2f", secondSumResult) + " and "
                            + firstTeamOdds.size() + "/" + secondTeamOdds.size());
                    firstTeamOdds.clear();
                    secondTeamOdds.clear();
                }

                File file = new File("D:\\file.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (Map.Entry entry : hltvResult.entrySet()) {
                    if (entry.getKey().toString().contains("-vs-")){
                        writer.write(entry.getKey() + "! " + entry.getValue());
                        writer.newLine();
                    }
                }
                writer.close();

                //reading from fileHltv and go to final part
                BufferedReader reader1 = Files.newBufferedReader(Paths.get("d://file.txt"));
                hltvList = reader1.lines().collect(Collectors.toList());

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
                            .replaceAll("valiance & co", "valiance")
                            .replaceAll("yeah!", "yeah")
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

                File file1 = new File("D:\\result.txt");
                BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
                for (Map.Entry entry : result.entrySet()) {
                    writer1.write(entry.getKey() + ", " + entry.getValue());
                    writer1.newLine();
                }
                writer1.write("--------------------------------------------");
                writer1.newLine();

                int gameCount = 0;
                List<String> listEmail = new ArrayList<>();
                for (Map.Entry entry : result.entrySet()){
                    String[] value = entry.getValue().toString().replaceAll(",", ".").split(" ");
                    if (Double.parseDouble(value[value.length - 4]) >= minOdds){
                        listEmail.add(String.valueOf(entry.getKey()));
                        gameCount++;
                        writer1.write(entry.getKey() + ", " + entry.getValue());
                        writer1.newLine();
                    }
                }
                writer1.close();

                Date date = new Date();
                if (gameCount == 0){
                    System.out.println(dateFormat.format(date) + " : NOT FOUND");
                }else {
                    sendEmail("@gmail.com", "", "@gmail.com", dateFormat.format(date), listEmail.toString());
                    System.out.println(dateFormat.format(date) + ": " + gameCount + " MATCH(ES)" + " email was send");
                }
                hltvList.clear();
                marList.clear();
                result.clear();
                matchesList.clear();
                hltvResult.clear();
                TimeUnit.MINUTES.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

    private static void sendEmail(String from, String pass, String where, String subject, String msg){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pass);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(where));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            //System.out.println("DONE");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
