import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseManager {
    private static final String DB_URL = "...";
    private static final String USER = null;

    public DatabaseManager(){
        Path filePath = Paths.get("Login_Daten.txt");
        try {
            Files.createFile(filePath);
        } catch (java.nio.file.FileAlreadyExistsException e) {
            System.out.println("File already exist.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
