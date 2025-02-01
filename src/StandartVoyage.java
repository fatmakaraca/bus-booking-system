import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StandartVoyage extends Voyage {
    private int refundCut;
    private boolean isThereAnInputError = false;
    private ArrayList<String[]> seatPlan;

    public StandartVoyage(String[] line, FileWriter writer) throws IOException {
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

            try {
                this.refundCut = Integer.parseInt(line[7]);
            } catch (NumberFormatException e) { //If the refund cut cannot be converted to an integer value, it prints an error message.
                writer.write("ERROR: " + line[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!\n");
                isThereAnInputError = true;
            }
            if (isThereAnInputError == false) {
                if (getRefundCut() < 0 || getRefundCut() > 100) { //The refund cut value should be between 0 and 100.
                    isThereAnInputError = true;
                    writer.write("ERROR: " + line[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!\n");
                }
            }
        }
    }

    public int getRefundCut() {
        return refundCut;
    }

    @Override
    public ArrayList<String[]> getSeatPlan() {
        return seatPlan;
    }

    @Override
    public void initializeSeatPlan() {
        seatPlan = new ArrayList<>();

        for (int i = 0; i < getNumberOfRows(); i++) { //The seating arrangement represented by "seatLine" is initialized by adding as many rows as specified by "numberOfRows" to create the seat plan.
            String[] seatLine = {"R", "R", "R", "R"}; //The seating arrangement for a standard voyage consists of four regular seats per row.
            seatPlan.add(seatLine);
        }
    }

    public int getNumberOfSeats() {
        int numberOfSeats = getNumberOfRows() * 4; //The total number of seats for a standard voyage is four times the number of rows.
        return numberOfSeats;
    }

    public int getRow(int seatNumber) {
        int row = (int) Math.ceil((double) seatNumber / 4);
        return row;
    }

    public int getColumn(int seatNumber) {
        int column;
        if (seatNumber % 4 == 0) { //Seats with a multiple of 4 actually belong to the 4th column, but since they are perfectly divisible by 4, appropriate adjustments have been made.
            column = 4;
        } else {
            column = seatNumber % 4;
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
