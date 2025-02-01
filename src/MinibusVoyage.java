import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinibusVoyage extends Voyage {
    private boolean isThereAnInputError = false;
    private ArrayList<String[]> seatPlan;

    public MinibusVoyage(String[] line, FileWriter writer) throws IOException {
        super(line, writer);
        if (super.isThereAnInputError() == false) { //When creating a voyage, if there is no error related to the creation of the superclass Voyage, then other errors are checked.
            if (getVoyageID() <= 0) { //The voyage ID must be greater than 0.
                isThereAnInputError = true;
                writer.write("ERROR: " + getVoyageID() + " is not a positive integer, ID of a voyage must be a positive integer!\n");
            } else if (getNumberOfRows() <= 0) { //The numberOfRows must be greater than 0.
                isThereAnInputError = true;
                writer.write("ERROR: " + getNumberOfRows() + " is not a positive integer, number of seat rows of a voyage must be a positive integer!\n");
            } else if (getPrice() <= 0) { //The price must be greater than 0.
                isThereAnInputError = true;
                writer.write("ERROR: " + (int) (getPrice()) + " is not a positive number, price must be a positive number!\n");
            }
        }
    }

    @Override
    public ArrayList<String[]> getSeatPlan() {
        return seatPlan;
    }

    @Override
    public void initializeSeatPlan() {
        seatPlan = new ArrayList<>();

        for (int i = 0; i < getNumberOfRows(); i++) { //The seating arrangement represented by "seatLine" is initialized by adding as many rows as specified by "numberOfRows" to create the seat plan.
            String[] seatLine = {"R", "R"}; //The seating arrangement for a minibus voyage consists of two regular seats per row.
            seatPlan.add(seatLine);
        }
    }

    public int getNumberOfSeats() {
        int numberOfSeats = getNumberOfRows() * 2; //The total number of seats for a standard voyage is 2 times the number of rows.
        return numberOfSeats;
    }

    public int getRow(int seatNumber) {
        int row = (int) Math.ceil((double) seatNumber / 2);
        return row;
    }

    public int getColumn(int seatNumber) {
        int column;
        if (seatNumber % 2 == 0) { //Seats with a multiple of 2 actually belong to the second column, but since they are perfectly divisible by 2, appropriate adjustments have been made.
            column = 2;
        } else {
            column = seatNumber % 2;
        }
        return column;
    }

    public boolean isThereAnInputError() { //If there is any error in both the superclass Voyage and the subclass, it returns true; otherwise, it returns false.
        boolean totalError = false;
        if (isThereAnInputError == true) {
            totalError = true;
        } else if (super.isThereAnInputError()) {
            totalError = true;
        }
        return totalError;
    }
}
