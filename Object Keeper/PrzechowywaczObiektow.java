import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.sql.*;
import java.util.Optional;

public class PrzechowywaczObiektow implements PrzechowywaczI {
    Connection connection;
    String dbPath = "jdbc:mysql://localhost:3306/zad12";

    public void setConnection(Connection connection) {
        // koncowa wersja:
        // this.connection = connection

        try {
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            this.connection = DriverManager.getConnection(dbPath);
            System.out.println("\nPolaczenie z baza danych = " + this.connection);
        }
        catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int save(int path, Object obiektDoZapisu) throws IllegalArgumentException {
        String sql1 = "SELECT * FROM Katalogi WHERE idKatalogu == " + path + ";";
        String sql2 = "SELECT * FROM Pliki;";
        int i=1;
        try {
            ResultSet _path = connection.createStatement().executeQuery(sql1);
            if(!_path.first())
                throw new IllegalArgumentException("Podano niepoprawny indeks");
            ResultSet index = connection.createStatement().executeQuery(sql2);
            while(index.next())
                i++;
            String name = "file" + i + ".xml";
            String sql3 = "INSERT INTO Pliki VALUES (" + i + ", " + _path.getInt(path) + ", " + name + ";";
            connection.createStatement().executeUpdate(sql3);
            File file = new File(_path.getString("katalog") + "/" + name);
            file.createNewFile();
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file.getName()));
            encoder.writeObject(obiektDoZapisu);
            encoder.close();

        }
        catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public Optional<Object> read(int obiektDoOdczytu) {
        String sql1 = "SELECT * FROM Pliki WHERE idPliku == " + obiektDoOdczytu + ";";
        try {
            ResultSet file = connection.createStatement().executeQuery(sql1);
            if(!file.first())
                return Optional.empty();
            String sql2 = "SELECT * FROM Katalogi WHERE idKatalogu == " + file.getString("idKatalogu");
            String path = connection.createStatement().executeQuery(sql2).getString("katalog");
            File xml = new File(path + "/" + file.getString("plik"));
            XMLDecoder decoder = new XMLDecoder(new FileInputStream(xml));
            Object result = decoder.readObject();
            decoder.close();
            return Optional.of(result);
        }
        catch(SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}


