package master;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class api {

    // URL der Zielseite festlegen
    public String apiURL = "https://blackjackbefunction.azurewebsites.net/api/HttpTrigger1?code=dCnZGnJXbGqelOwxL07Zzwz88grTKbWBC14JUM2wNUAoAzFuc-YvlQ==";
    public String ObjectId = "";

    public int apiget() throws IOException {

        // Eine URL-Verbindung öffnen
        HttpURLConnection connection = (HttpURLConnection) new URL(apiURL).openConnection();

        // HTTP GET-Methode festlegen
        connection.setRequestMethod("GET");

        // Antwort überprüfen
        int responseCode = connection.getResponseCode();
        //System.out.println("Response Code: " + responseCode);

        if(responseCode != 200){
            return -1;
        }

        // Eingabe lesen
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = reader.readLine()) != null){
            response.append(inputLine);
        }
        reader.close();

        // Antwort ausgeben
        //System.out.println("Response:\n" + response.toString());

        // Balance extrahieren
        int splitIndex = response.indexOf("balance");
        String substring2 = response.substring(splitIndex, response.length());
        String substring1 = response.substring(0, splitIndex);

        StringBuilder extractBalance = new StringBuilder();
        for(int i = 0; i < substring2.length(); i++){
            char c = substring2.charAt(i);
            if(Character.isDigit(c)){
                extractBalance.append(c);
            }
        }
        String Stringbalance = extractBalance.toString();
        int balance = Integer.parseInt(Stringbalance);

        int x = substring1.indexOf("_id") + 3;
        int y = substring1.indexOf("messageBody");
        StringBuilder extractId = new StringBuilder();
        for(int i = x; i < y; i++){
            char c = substring1.charAt(i);
            if(Character.isDigit(c) || Character.isLetter(c)){
                extractId.append(c);
            }
        }
        ObjectId = extractId.toString();

        // Verbindung schließen
        connection.disconnect();

        return balance;
    }

    public void apipost(int balance) throws IOException {

        // Eine URL-Verbindung öffnen
        HttpURLConnection connection = (HttpURLConnection) new URL(apiURL).openConnection();

        // HTTP POST-Methode festlegen
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Daten, die sie senden möchten
        String postData = "{\"balance\":\"" + balance + "\"}";

        // Daten in den Request schreiben
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())){
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            wr.write(postDataBytes);
            wr.flush();
        }

        // Antwortcode überprüfen
        int responseCode = connection.getResponseCode();
        //System.out.println("Response Code: " + responseCode);

        // Eingabe lesen
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null){
            response.append(inputLine);
        }
        reader.close();

        // Antwort ausgeben
        //System.out.println("Response:\n" + response.toString());

        // Verbindung schließen
        connection.disconnect();
    }

    public void apidelete() throws IOException {

        String baseURL = apiURL;
        String id = ObjectId;
        String deleteURL = baseURL + "&id=" + id;

        // Eine URL-Verbindung öffnen
        HttpURLConnection connection = (HttpURLConnection) new URL(deleteURL).openConnection();

        // HTTP DELETE-Methode festlegen
        connection.setRequestMethod("DELETE");

        // Antwortcode überprüfen
        int responseCode = connection.getResponseCode();
        //System.out.println("Response Code: " + responseCode);

        // Verbindung schließen
        connection.disconnect();
    }

}
