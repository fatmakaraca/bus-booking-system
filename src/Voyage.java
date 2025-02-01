import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Voyage {
    private String voyageType;
    private int voyageID;
    private String from;
    private String to;
    private int numberOfRows;
    private double price;
    private double revenue;
    private boolean isThereAnInputError = false;
    private String[] line;
    private ArrayList<String[]> seatPlan; //The seat plan represents the layout of bus seats, consisting of arrays of strings.

    public Voyage(String[] line, FileWriter writer) throws IOException {
        this.line = line; //"line" represents a line in the input file.
        this.voyageType = line[1];
        try {
            this.voyageID = Integer.parseInt(line[2]);
        } catch (
                NumberFormatException e) { //If the voyage ID cannot be converted to an integer value, it prints an error message.
            writer.write("ERROR: " + line[2] + " is not a positive integer, ID of a voyage must be a positive integer!\n");
            this.isThereAnInputError = true;
        }
        this.from = line[3];
        this.to = line[4];
        if (isThereAnInputError == false) {
            try {
                this.numberOfRows = Integer.parseInt(line[5]);
            } catch (
                    NumberFormatException e) { //If the numberOfRows cannot be converted to an integer value, it prints an error message.
                writer.write("ERROR: " + line[5] + " is not a positive integer, number of seat rows of a voyage must be a positive integer!\n");
                this.isThereAnInputError = true;
            }
        }
        if (isThereAnInputError == false) {
            try {
                this.price = Double.parseDouble(line[6]);
            } catch (
                    NumberFormatException e) { //If the price cannot be converted to an integer value, it prints an error message.
                writer.write("ERROR: " + line[6] + " is not a positive number, price must be a positive number!\n");
                this.isThereAnInputError = true;
            }
        }

    }

    public int getVoyageID() {
        return voyageID;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public double getPrice() {
        return price;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public ArrayList<String[]> getSeatPlan() {
        return seatPlan;
    }

    public void setSeatPlan(ArrayList<String[]> seatPlan) {
        this.seatPlan = seatPlan;
    }

    public void initializeSeatPlan() {
        this.seatPlan = seatPlan;
    }

    public String getVoyageType() {
        return voyageType;
    }

    public boolean isThereAnInputError() {
        return isThereAnInputError;
    }
}
