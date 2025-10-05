import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    //define the warning threshold //TODO: Noch keine Warnung gemacht
    private static final int WARNING_DAYS_THRESHOLD = 3;
    private static final String FILE_NAME = "fridge_data.txt";
    private static final List<FoodItem> fridge = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //Create file in case it does not exist yet
        Path filePath = Paths.get(FILE_NAME);
        try {
            Files.createFile(filePath);
        } catch (java.nio.file.FileAlreadyExistsException e) {
            System.out.println("File already exist.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        updateList();

        System.out.println("Current items: ");
        ListFridgeItems();

        boolean tmp = true;
        while (tmp){
            System.out.println("Press 1 if you want to add an item");
            System.out.println("Press 2 if you want to remove an item");
            System.out.println("Press 3 if you want to put an item into the freezer");
            System.out.println("Press X if you want to close the program");

            switch (scanner.nextLine()){
                case "1":
                    addItem();
                    break;
                case "2":
                    System.out.println("The item name: ");
                    removeItemByName(scanner.nextLine());
                    break;
                case "3":
                    System.out.println("The item name: ");
                    PutIntoFreezer(scanner.nextLine());
                    break;
                case "X", "x":
                    tmp = false;
                    break;
                default:
                    System.out.println(scanner.nextLine() + " is not an option please type in a valid option");
            }
            System.out.println("List:");
            ListFridgeItems();
        }
    }

    private static void addItem(){
        System.out.println("Enter the product name:");
        String name = scanner.nextLine();

        //Check if user uses the right format (YYYY-MM-DD)
        LocalDate expirationDate = null;
        boolean correctFormat = false;
        do {
            System.out.println("Enter the expiration date (YYYY-MM-DD):");
            try {
                expirationDate = LocalDate.parse(scanner.nextLine());
                correctFormat = true;
            } catch (java.time.format.DateTimeParseException e){
                System.out.println("Error: The date format was incorrect. Please ensure you used YYYY-MM-DD.");
            }
        } while (!correctFormat);

        System.out.println("In Freezer? (y/n)");
        boolean inFreezer = Objects.equals(scanner.nextLine(), "y");

        //add to list
        FoodItem food = new FoodItem(name, expirationDate, inFreezer);
        fridge.add(food);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){ //Without the true it will overwrite the file
            writer.write(food.toString());
            writer.newLine();
        } catch (IOException e){
            // Handle file access errors
            System.err.println("❌ An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static void updateList(){
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;

            //Reading all the line and add them to the List fridge
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");

                fridge.add(new FoodItem(parts[0], LocalDate.parse(parts[1]), Objects.equals(parts[2], "true")));
            }
        } catch (IOException e) {
            System.err.println("Error while reading: " + e.getMessage());
        }
    }

    private static void removeItemByName(String nameToRemove){
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            int n = 0; //Zählt welche Zeile das gesuchte Objekt ist. Mit n wird dann auch gelöscht

            //Reading all the line and add them to the List fridge
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(Objects.equals(parts[0], nameToRemove)){
                    //Ich lösche das erste Objekt, was eigentlich auch das älteste seien sollte aber das muss nicht immer der Fall sein. (mit tmp[] könnte man es lösen)

                    fridge.remove(n);
                    break; //Import cause otherwise it deletes the wrong object
                }
                n++;
            }
        } catch (IOException e) {
            System.err.println("Error while reading: " + e.getMessage());
        }

        saveItems(fridge); //Doing it like this will not give u an error message if u try to delet in object that does not exist.
    }

    private static void saveItems(List<FoodItem> items){
        boolean tmp = false;
        for(FoodItem item : items){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, tmp))){
                writer.write(item.toString());
                writer.newLine();
            } catch (IOException e){
                System.err.println("❌ An error occurred while writing to the file: " + e.getMessage());
            }
            tmp = true;
        }
    }

    private static void ListFridgeItems(){
        for(FoodItem foodItem : fridge){
            System.out.println(foodItem);
        }
    }

    private static void PutIntoFreezer(String name){
        for(FoodItem item : fridge){
            if(Objects.equals(item.getName(), name)){
                item.setInFreezer(true);
            }
        }
        saveItems(fridge);
    }
}