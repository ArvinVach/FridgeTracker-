import java.time.LocalDate;

public class FoodItem {
    private String name;
    private LocalDate expirationDate;
    private boolean inFreezer;

    public FoodItem(String name, LocalDate expirationDate, boolean inFreezer){
        this.name = name;
        this.expirationDate = expirationDate;
        this.inFreezer = inFreezer;
    }

    public String getName(){
        return name;
    }
    public LocalDate getExpirationDate(){
        return expirationDate;
    }
    public boolean getInFreezer(){
        return inFreezer;
    }
    public void setInFreezer(boolean inFreezer){
        this.inFreezer = inFreezer;
    }

    //if u try to print the Object it will print the right data like food name and expiration date
    @Override
    public String toString(){
        return name + "," + expirationDate + "," + inFreezer;
    }
}
