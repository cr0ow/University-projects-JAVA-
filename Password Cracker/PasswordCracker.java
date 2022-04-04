import java.io.*;
import java.net.*;
import java.util.*;

public class PasswordCracker implements PasswordCrackerInterface {
    Socket socket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;
    String encoded = null;
    String decoded = "";
    String previous = "";
    Map<Character, Integer> iterators = new HashMap<>();

    public PasswordCracker() {
        iterators.put('l', -1);
        iterators.put('u', -1);
        iterators.put('n', -1);
        iterators.put('s', -1);
    }

    public String getPassword(String host, int port) {
        String password = "";
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            System.out.println(reader.readLine());
            System.out.println("Program");
            writer.println("Program");
            System.out.println(reader.readLine());
            encoded = reader.readLine();
            System.out.println(encoded);
            System.out.println(reader.readLine());
        }
        catch (UnknownHostException ex) {
            System.out.println("Invalid host: " + host);
        }
        catch (ConnectException ex) {
            System.out.println("Connection error with host: " + host);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        for(int i=9; i<encoded.length(); i++)
            password += encoded.charAt(i);

        encoded = password;
        String toSend = generateAnything();
        int valid = validate(toSend);
        int prevValid = valid;
        int letter = 0;

        while(valid != -1) {
            if(prevValid == valid) { //niezgadnieta litera
                toSend = decode(letter);
            }
            else if(prevValid < valid) { // zgadnieta litera
                iterators.replace(encoded.charAt(letter), -1);
                letter++;
                toSend = decode(letter);
            }
            else { // poprzednia wersja byla poprawna
                decoded = previous;
                toSend = decoded;
            }
            prevValid = valid;
            valid = validate(toSend);
        }
        return toSend;
    }

    private String generateAnything() {
        String result = "";
        for(int i=0; i<encoded.length(); i++) {
            char code = encoded.charAt(i);
            result += PasswordComponents.passwordComponents.get(code).get(0);
        }
        decoded = result;
        return result;
    }

    private int validate(String toSend) {
        try {
            writer.println(toSend);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        int valid = 0;
        String temp;
        try {
            temp = reader.readLine();
            if(temp.equals("+OK")) {
                System.out.println(temp);
                return -1;
            }
            String[] words = temp.split("\\s");
            temp = words[4];
            valid = Integer.parseInt(temp);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return valid;
    }


    private String decode(int letter) {
        String result = "";
        for(int i=0; i<letter; i++)
            result += decoded.charAt(i);
        int incremented = iterators.get(encoded.charAt(letter)) + 1;
        iterators.replace(encoded.charAt(letter), incremented);
        result += PasswordComponents.passwordComponents.get(encoded.charAt(letter))
                .get(iterators.get(encoded.charAt(letter)));
        for(int i=letter+1; i<encoded.length(); i++)
            result += decoded.charAt(i);
        previous = decoded;
        decoded = result;
        return result;
    }

    public static void main(String[] args) {
        final String host = "172.30.24.15";
        final int port = 8080;
        PasswordCracker cracker = new PasswordCracker();
        System.out.println("haslo : " + cracker.getPassword(host, port));
    }
}
