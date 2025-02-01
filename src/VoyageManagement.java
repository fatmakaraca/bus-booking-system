import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VoyageManagement implements IVoyageManegement {
    private String[] line;
    List<Voyage> voyages;

    /**
     * Constructs a VoyageManagement object with the specified input line and list of voyages.
     *
     * @param line    An array of strings representing the input line.
     * @param voyages A List of Voyage objects to manage.
     */
    public VoyageManagement(String[] line, List<Voyage> voyages) {
        this.line = line;
        this.voyages = voyages;
    }

    /**
     * Formats the given double value to a string with two decimal places.
     *
     * @param value The double value to be formatted.
     * @return A string representing the formatted value with two decimal places.
     */
    public String formatValue(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedValue = df.format(value).replace(',', '.');
        return formattedValue;
    }

    /**
     * Initializes a new voyage based on the provided command line and adds it to the list of voyages.
     *
     * @param voyages A List of Voyage objects representing existing voyages.
     * @param writer  The FileWriter object used for writing output.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    @Override
    public void initVoyage(List<Voyage> voyages, FileWriter writer) throws IOException {
        Voyage voyage = null; //The object to be created appropriately in the subclass will be held as an object of type Voyage.
        boolean isThereAnError = false;

        if (line[1].equals("Standard")) {
            if (line.length != 8) { //It checks whether the line has the appropriate number of elements.
                isThereAnError = true;
                writer.write("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!\n");
            } else {
                voyage = new StandartVoyage(line, writer);
            }

        } else if (line[1].equals("Premium")) {
            if (line.length != 9) { //It checks whether the line has the appropriate number of elements.
                isThereAnError = true;
                writer.write("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!\n");
            } else {
                voyage = new PremiumVoyage(line, writer);
            }

        } else if (line[1].equals("Minibus")) {
            if (line.length != 7) { //It checks whether the line has the appropriate number of elements.
                isThereAnError = true;
                writer.write("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!\n");
            } else {
                voyage = new MinibusVoyage(line, writer);
            }

        } else {
            isThereAnError = true; //If the voyage type is not standard, premium, or minibus, it prints an error message
            writer.write("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!\n");

        }

        if (voyage != null) {
            isThereAnError = voyage.isThereAnInputError();

            for (int i = 0; i < voyages.size(); i++) {
                if (voyage.getVoyageID() == voyages.get(i).getVoyageID()) {
                    isThereAnError = true;
                    writer.write("ERROR: There is already a voyage with ID of " + voyage.getVoyageID() + "!\n");
                }
            }

            if (isThereAnError == false) {
                voyage.initializeSeatPlan();
                int index = 0;
                //According to the index number, a new voyage is added to the voyages array list at the appropriate position.
                while (index < voyages.size() && voyages.get(index).getVoyageID() < voyage.getVoyageID()) {
                    index++;
                }
                voyages.add(index, voyage);
                if (voyage.getVoyageType().equals("Standard")) {
                    writer.write("Voyage " + voyage.getVoyageID() + " was initialized as a standard (2+2)" +
                            " voyage from " + voyage.getFrom() + " to " + voyage.getTo() + " with " + formatValue(voyage.getPrice()) + " TL " +
                            "priced " + 4 * voyage.getNumberOfRows() + " regular seats. Note that refunds will" +
                            " be " + line[7] + "% less than the paid amount.\n");

                } else if (voyage.getVoyageType().equals("Premium")) {
                    int premiumFee = Integer.parseInt(line[8]);
                    double premiumPrice = voyage.getPrice() * ((double) (100 + premiumFee) / 100);
                    writer.write(
                            "Voyage " + voyage.getVoyageID() + " was initialized as a premium (1+2) voyage from " +
                                    voyage.getFrom() + " to " + voyage.getTo() + " with " + formatValue(voyage.getPrice()) + " TL priced "
                                    + voyage.getNumberOfRows() * 2 + " regular seats" +
                                    " and " + formatValue(premiumPrice) + " TL priced " + voyage.getNumberOfRows() + " premium seats. Note that refunds will be " +
                                    line[7] + "% less than the paid amount.\n");

                } else if (voyage.getVoyageType().equals("Minibus")) {
                    writer.write("Voyage " + voyage.getVoyageID() + " was initialized as a minibus (2) voyage" +
                            " from " + voyage.getFrom() + " to " + voyage.getTo() + " with " + formatValue(voyage.getPrice()) + " TL priced "
                            + voyage.getNumberOfRows() * 2 + " regular seats." +
                            " Note that minibus tickets are not refundable.\n");
                }
            }
        }
    }

    /**
     * Cancels a voyage based on the provided voyage ID and updates the revenue accordingly.
     *
     * @param voyages A List of Voyage objects representing existing voyages.
     * @param writer  The FileWriter object used for writing output.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    @Override
    public void cancelVoyage(List<Voyage> voyages, FileWriter writer) throws IOException {
        boolean isThereAnError = false;
        Voyage voyage = null;

        if (line.length != 2) {
            isThereAnError = true;
            writer.write("ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!\n");
        }
        if (isThereAnError == false) {
            try {
                int voyageID = Integer.parseInt(line[1]);
            } catch (NumberFormatException e) {
                writer.write("ERROR: " + line[1] + " is not a positive integer, ID of a voyage must be a positive integer!\n");
                isThereAnError = true;
            }

            if (isThereAnError == false) {
                int voyageID = Integer.parseInt(line[1]);
                if (voyageID < 0) {
                    isThereAnError = true;
                    writer.write("ERROR: " + voyageID + " is not a positive integer, ID of a voyage must be a positive integer!\n");
                }

                boolean isThereVoyage = false;

                for (int i = 0; i < voyages.size(); i++) {
                    if (voyages.get(i).getVoyageID() == voyageID) {
                        voyage = voyages.get(i);
                        isThereVoyage = true; //If there is a voyage with the same ID, it returns true; otherwise, it returns false.
                    }
                }

                if (isThereVoyage == false && isThereAnError == false) { //If there is no voyage with the same ID, it prints an error message.
                    isThereAnError = true;
                    writer.write("ERROR: There is no voyage with ID of " + voyageID + "!\n");
                }
            }
        }
        if (isThereAnError == false) {
            double revenue = voyage.getRevenue();
            double feeForOccupiedSeats = 0;
            List<String[]> seatPlan;

            //If the voyage is to be canceled,
            // the money for the occupied seats in that voyage should be refunded,
            // and the remaining amount (deducted from the refunded tickets) should be the revenue.

            //RX represents occupied regular seats, while PX represents occupied premium seats.
            if (voyage instanceof StandartVoyage) {
                StandartVoyage standartVoyage = (StandartVoyage) voyage;
                seatPlan = standartVoyage.getSeatPlan();
                for (String[] seatRow : seatPlan) {
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            feeForOccupiedSeats += standartVoyage.getPrice();
                        }
                    }
                }

            } else if (voyage instanceof PremiumVoyage) {
                PremiumVoyage premiumVoyage = (PremiumVoyage) voyage;
                seatPlan = premiumVoyage.getSeatPlan();
                for (String[] seatRow : seatPlan) {
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            feeForOccupiedSeats += premiumVoyage.getPrice();
                        } else if (Objects.equals(seat, "PX")) {
                            feeForOccupiedSeats += premiumVoyage.getPremiumSeatPrice();
                        }
                    }
                }

            } else if (voyage instanceof MinibusVoyage) {
                MinibusVoyage minibusVoyage = (MinibusVoyage) voyage;
                seatPlan = minibusVoyage.getSeatPlan();
                for (String[] seatRow : seatPlan) {
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            feeForOccupiedSeats += minibusVoyage.getPrice();
                        }
                    }
                }
            }

            double currentRevenue = revenue - feeForOccupiedSeats;
            voyage.setRevenue(currentRevenue);
            writer.write("Voyage " + voyage.getVoyageID() + " was successfully cancelled!\n");
            writer.write("Voyage details can be found below:\n");
            printVoyage(voyages, writer);
            voyages.remove(voyage);
        }
    }

    /**
     * Sells tickets for a specific voyage based on the provided voyage ID and seat numbers,
     * updates revenue accordingly, and checks for errors during the process.
     *
     * @param voyages A List of Voyage objects representing existing voyages.
     * @param writer  The FileWriter object used for writing output.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    @Override
    public void sellTicket(List<Voyage> voyages, FileWriter writer) throws IOException {
        boolean isThereAnError = false;
        double moneyEarned = 0; //Money earned from the sold ticket.
        Voyage currentVoyage = null;

        List<Integer> seatsNumbers = new ArrayList<>();

        if (line.length != 3) {
            isThereAnError = true;
            writer.write("ERROR: Erroneous usage of \"SELL_TICKET\" command!\n");
        }

        if (isThereAnError == false) {
            try {
                int voyageID = Integer.parseInt(line[1]);
            } catch (NumberFormatException e) {
                writer.write("ERROR: There is no voyage with ID of " + line[1] + "!\n");
                isThereAnError = true;
            }
        }

        if (isThereAnError == false) {
            boolean isThereVoyage = false;
            int voyageID = Integer.parseInt(line[1]);
            for (Voyage voyage : voyages) {
                if (voyage.getVoyageID() == voyageID) {
                    isThereVoyage = true;
                    currentVoyage = voyage;
                    break;
                }
            }
            if (isThereVoyage == false) { //If there is no voyage with the same ID, it prints an error.
                isThereAnError = true;
                writer.write("ERROR: There is no voyage with ID of " + voyageID + "!\n");
            }
        }

        if (isThereAnError == false) {
            String[] seats = line[2].split("_");

            for (String seat : seats) {
                try {
                    int seatNumber = Integer.parseInt(seat);
                } catch (NumberFormatException e) {
                    isThereAnError = true;
                    writer.write("ERROR: " + seat + " is not a positive integer, seat number must be a positive integer!");
                }
                if (isThereAnError == false) {
                    int seatNumber = Integer.parseInt(seat);
                    seatsNumbers.add(seatNumber);
                }
            }
        }

        if (isThereAnError == false) {
            int voyageID = Integer.parseInt(line[1]);
            int numberOfSeats = 0;

            for (int i = 0; i < seatsNumbers.size(); i++) {
                int count = 0;
                for (int j = 0; j < seatsNumbers.size(); j++) {
                    if (Objects.equals(seatsNumbers.get(i), seatsNumbers.get(j))) {
                        count++;
                    }
                }
                if (count > 1) {
                    writer.write("ERROR: Seat " + seatsNumbers.get(i) + " cannot be sold more than once.\n");
                    isThereAnError = true;
                    break;
                }
            }

            if (isThereAnError == false) {

                for (int seatNumber : seatsNumbers) {
                    int row = 0;
                    int column = 0;

                    //RX represents occupied regular seats, while PX represents occupied premium seats.

                    if (currentVoyage instanceof StandartVoyage) {
                        StandartVoyage standartVoyage = (StandartVoyage) currentVoyage;
                        numberOfSeats = standartVoyage.getNumberOfSeats();
                        row = standartVoyage.getRow(seatNumber);
                        column = standartVoyage.getColumn(seatNumber);

                        if (seatNumber < 1) {
                            isThereAnError = true;
                            writer.write("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!\n");
                            break;

                        } else if (seatNumber > numberOfSeats) {
                            isThereAnError = true;
                            writer.write("ERROR: There is no such a seat!\n");
                            break;

                        } else if (Objects.equals(standartVoyage.getSeatPlan().get(row - 1)[column - 1], "RX")) {
                            isThereAnError = true;
                            writer.write("ERROR: One or more seats already sold!\n");
                            break;
                        }

                    } else if (currentVoyage instanceof PremiumVoyage) {
                        PremiumVoyage premiumVoyage = (PremiumVoyage) currentVoyage;
                        numberOfSeats = premiumVoyage.getNumberOfSeats();
                        row = premiumVoyage.getRow(seatNumber);
                        column = premiumVoyage.getColumn(seatNumber);

                        if (seatNumber < 1) {
                            isThereAnError = true;
                            writer.write("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!\n");
                            break;
                        } else if (seatNumber > numberOfSeats) {
                            isThereAnError = true;
                            writer.write("ERROR: There is no such a seat!\n");
                            break;

                        } else if (Objects.equals(premiumVoyage.getSeatPlan().get(row - 1)[column - 1], "RX")) {
                            isThereAnError = true;
                            writer.write("ERROR: One or more seats already sold!\n");
                            break;
                        } else if (Objects.equals(premiumVoyage.getSeatPlan().get(row - 1)[column - 1], "PX")) {
                            isThereAnError = true;
                            writer.write("ERROR: One or more seats already sold!\n");
                            break;
                        }

                    } else if (currentVoyage instanceof MinibusVoyage) {
                        MinibusVoyage minibusVoyage = (MinibusVoyage) currentVoyage;
                        numberOfSeats = minibusVoyage.getNumberOfSeats();
                        row = minibusVoyage.getRow(seatNumber);
                        column = minibusVoyage.getColumn(seatNumber);

                        if (seatNumber < 1) {
                            isThereAnError = true;
                            writer.write("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!\n");
                            break;
                        } else if (seatNumber > numberOfSeats) {
                            isThereAnError = true;
                            writer.write("ERROR: There is no such a seat!\n");
                            break;

                        } else if (Objects.equals(minibusVoyage.getSeatPlan().get(row - 1)[column - 1], "RX")) {
                            isThereAnError = true;
                            writer.write("ERROR: One or more seats already sold!\n");
                            break;
                        }
                    }
                }
            }

            if (isThereAnError == false) {
                ArrayList<String[]> seatPlan;

                for (int seatNumber : seatsNumbers) {
                    int row = 0;
                    int column = 0;
                    double regularSeatPrice;
                    double premiumSeatPrice = 0;

                    if (currentVoyage instanceof StandartVoyage) {
                        StandartVoyage standartVoyage = (StandartVoyage) currentVoyage;
                        numberOfSeats = standartVoyage.getNumberOfSeats();
                        row = standartVoyage.getRow(seatNumber);
                        column = standartVoyage.getColumn(seatNumber);
                        regularSeatPrice = standartVoyage.getPrice();

                        seatPlan = standartVoyage.getSeatPlan();

                        if (Objects.equals(seatPlan.get(row - 1)[column - 1], "R")) {
                            moneyEarned += regularSeatPrice;
                        }

                        seatPlan.get(row - 1)[column - 1] = "RX";
                        standartVoyage.setSeatPlan(seatPlan); //The seat plan of the voyage is updated with the sold seat that was previously occupied.

                    } else if (currentVoyage instanceof PremiumVoyage) {
                        PremiumVoyage premiumVoyage = (PremiumVoyage) currentVoyage;
                        numberOfSeats = premiumVoyage.getNumberOfSeats();
                        row = premiumVoyage.getRow(seatNumber);
                        column = premiumVoyage.getColumn(seatNumber);
                        regularSeatPrice = premiumVoyage.getPrice();
                        premiumSeatPrice = premiumVoyage.getPremiumSeatPrice();

                        seatPlan = premiumVoyage.getSeatPlan();

                        if (Objects.equals(seatPlan.get(row - 1)[column - 1], "P")) {
                            moneyEarned += premiumSeatPrice;
                            seatPlan.get(row - 1)[column - 1] = "PX";

                        } else if (Objects.equals(seatPlan.get(row - 1)[column - 1], "R")) {
                            moneyEarned += regularSeatPrice;
                            seatPlan.get(row - 1)[column - 1] = "RX";
                        }

                        premiumVoyage.setSeatPlan(seatPlan); //The seat plan of the voyage is updated with the sold seat that was previously occupied.

                    } else if (currentVoyage instanceof MinibusVoyage) {
                        MinibusVoyage minibusVoyage = (MinibusVoyage) currentVoyage;
                        numberOfSeats = minibusVoyage.getNumberOfSeats();
                        row = minibusVoyage.getRow(seatNumber);
                        column = minibusVoyage.getColumn(seatNumber);
                        regularSeatPrice = minibusVoyage.getPrice();

                        seatPlan = minibusVoyage.getSeatPlan();

                        if (Objects.equals(seatPlan.get(row - 1)[column - 1], "R")) {
                            moneyEarned += regularSeatPrice;
                        }
                        seatPlan.get(row - 1)[column - 1] = "RX";
                        minibusVoyage.setSeatPlan(seatPlan); //The seat plan of the voyage is updated with the sold seat that was previously occupied.
                    }
                }

                double previousRevenue = currentVoyage.getRevenue();
                double totalRevenue = previousRevenue + moneyEarned;
                currentVoyage.setRevenue(totalRevenue); //The revenue of that voyage is updated with the money earned from the sold tickets.

                writer.write("Seat " + line[2].replace("_", "-") + " of the Voyage " +
                        currentVoyage.getVoyageID() + " from " + currentVoyage.getFrom() + " to " + currentVoyage.getTo() +
                        " was successfully sold for " + formatValue(moneyEarned) + " TL.\n");
            }
        }
    }

    ///**
    // * Refunds the ticket(s) for the specified voyage and seat number(s).
    // *
    // * @param voyages The list of voyages.
    // * @param writer  The FileWriter object to write output.
    // * @throws IOException If an I/O error occurs.
    // */
    @Override
    public void refundTicket(List<Voyage> voyages, FileWriter writer) throws IOException {
        boolean isThereAnError = false;
        double totalMoneyRefunded = 0;
        Voyage currentVoyage = null;
        List<Integer> seatsNumbers = new ArrayList<>();

        if (line.length != 3) {
            isThereAnError = true;
            writer.write("ERROR: Erroneous usage of \"REFUND_TICKET\" command!\n");
        }

        if (isThereAnError == false) {
            boolean isThereVoyage = false;
            try {
                int voyageID = Integer.parseInt(line[1]);
            } catch (NumberFormatException e) {
                isThereAnError = true;
                writer.write("ERROR: There is no voyage with ID of " + line[1] + "!\n");
            }

            if (isThereAnError == false) {
                int voyageID = Integer.parseInt(line[1]);
                for (Voyage voyage : voyages) {
                    if (voyage.getVoyageID() == voyageID) {
                        isThereVoyage = true;
                        currentVoyage = voyage;
                        break;
                    }
                }
            }

            if (isThereVoyage == false) { //If there is no voyage with the specified ID, it prints an error
                isThereAnError = true;
                writer.write("ERROR: There is no voyage with ID of " + line[1] + "!\n");
            }
        }

        if (isThereAnError == false) {
            String[] seats = line[2].split("_");

            for (String seat : seats) {
                try {
                    int seatNumber = Integer.parseInt(seat);
                } catch (NumberFormatException e) {
                    isThereAnError = true;
                    writer.write("ERROR: " + seat + " is not a positive integer, seat number must be a positive integer!");
                }
                if (isThereAnError == false) {
                    int seatNumber = Integer.parseInt(seat);
                    seatsNumbers.add(seatNumber);
                }
            }
        }


        if (isThereAnError == false) {
            int voyageID = Integer.parseInt(line[1]);
            int numberOfSeats = 0;

            for (int i = 0; i < seatsNumbers.size(); i++) {
                int count = 0;
                for (int j = 0; j < seatsNumbers.size(); j++) {
                    if (Objects.equals(seatsNumbers.get(i), seatsNumbers.get(j))) {
                        count++;
                    }
                }
                if (count > 1) {
                    writer.write("ERROR: Seat " + seatsNumbers.get(i) + " cannot be refund more than once.\n");
                    isThereAnError = true;
                    break;
                }
            }

            if (isThereAnError == false) {

                for (int seatNumber : seatsNumbers) {
                    int row = 0;
                    int column = 0;

                    if (isThereAnError == false) {

                        if (currentVoyage instanceof StandartVoyage) {
                            StandartVoyage standartVoyage = (StandartVoyage) currentVoyage;
                            numberOfSeats = standartVoyage.getNumberOfSeats();
                            row = standartVoyage.getRow(seatNumber);
                            column = standartVoyage.getColumn(seatNumber);

                            if (seatNumber < 1) {
                                isThereAnError = true;
                                writer.write("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!\n");
                                break;

                            } else if (seatNumber > numberOfSeats) {
                                isThereAnError = true;
                                writer.write("ERROR: There is no such a seat!\n");
                                break;

                            } else if (!Objects.equals(standartVoyage.getSeatPlan().get(row - 1)[column - 1], "RX")) {
                                isThereAnError = true;
                                writer.write("ERROR: One or more seats are already empty!\n");
                                break;
                            }

                        } else if (currentVoyage instanceof PremiumVoyage) {
                            PremiumVoyage premiumVoyage = (PremiumVoyage) currentVoyage;
                            numberOfSeats = premiumVoyage.getNumberOfSeats();
                            row = premiumVoyage.getRow(seatNumber);
                            column = premiumVoyage.getColumn(seatNumber);

                            if (seatNumber < 1) {
                                isThereAnError = true;
                                writer.write("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!\n");
                                break;
                            } else if (seatNumber > numberOfSeats) {
                                isThereAnError = true;
                                writer.write("ERROR: There is no such a seat!\n");
                                break;

                            } else if (!Objects.equals(premiumVoyage.getSeatPlan().get(row - 1)[column - 1], "RX") && !Objects.equals(premiumVoyage.getSeatPlan().get(row - 1)[column - 1], "PX")) {
                                isThereAnError = true;
                                writer.write("ERROR: One or more seats are already empty!\n");
                                break;
                            }

                        } else if (currentVoyage instanceof MinibusVoyage) {
                            isThereAnError = true;
                            writer.write("ERROR: Minibus tickets are not refundable!\n");
                            break;
                        }
                    }
                }
            }

            if (isThereAnError == false) {
                ArrayList<String[]> seatPlan;

                for (int seatNumber : seatsNumbers) {
                    int row = 0;
                    int column = 0;
                    double regularSeatPrice;
                    double premiumSeatPrice = 0;
                    double refundedMoney = 0;
                    int refundCut = 0;

                    if (currentVoyage instanceof StandartVoyage) {
                        StandartVoyage standartVoyage = (StandartVoyage) currentVoyage;
                        numberOfSeats = standartVoyage.getNumberOfSeats();
                        row = standartVoyage.getRow(seatNumber);
                        column = standartVoyage.getColumn(seatNumber);
                        regularSeatPrice = standartVoyage.getPrice();
                        refundCut = standartVoyage.getRefundCut();
                        refundedMoney = regularSeatPrice * (100 - refundCut) / 100; //The refunded money is the ticket price minus the refund cut percentage

                        seatPlan = standartVoyage.getSeatPlan();

                        if (Objects.equals(seatPlan.get(row - 1)[column - 1], "RX")) {
                            totalMoneyRefunded += refundedMoney;
                        }

                        seatPlan.get(row - 1)[column - 1] = "R"; //When a ticket refund is made, that seat becomes empty again.
                        standartVoyage.setSeatPlan(seatPlan);

                    } else if (currentVoyage instanceof PremiumVoyage) {
                        PremiumVoyage premiumVoyage = (PremiumVoyage) currentVoyage;
                        numberOfSeats = premiumVoyage.getNumberOfSeats();
                        row = premiumVoyage.getRow(seatNumber);
                        column = premiumVoyage.getColumn(seatNumber);
                        regularSeatPrice = premiumVoyage.getPrice();
                        premiumSeatPrice = premiumVoyage.getPremiumSeatPrice();
                        refundCut = premiumVoyage.getRefundCut();

                        seatPlan = premiumVoyage.getSeatPlan();

                        if (Objects.equals(seatPlan.get(row - 1)[column - 1], "PX")) {
                            refundedMoney = premiumSeatPrice * (100 - refundCut) / 100;
                            totalMoneyRefunded += refundedMoney;
                            seatPlan.get(row - 1)[column - 1] = "P"; //When a ticket refund is made, that seat becomes empty again.

                        } else if (Objects.equals(seatPlan.get(row - 1)[column - 1], "RX")) {
                            refundedMoney = regularSeatPrice * (100 - refundCut) / 100;
                            totalMoneyRefunded += refundedMoney;
                            seatPlan.get(row - 1)[column - 1] = "R"; //When a ticket refund is made, that seat becomes empty again.
                        }
                        premiumVoyage.setSeatPlan(seatPlan);
                    }
                }

                double previousRevenue = currentVoyage.getRevenue();
                double totalRevenue = previousRevenue - totalMoneyRefunded;
                currentVoyage.setRevenue(totalRevenue);

                writer.write("Seat " + line[2].replace("_", "-") + " of the Voyage " +
                        currentVoyage.getVoyageID() + " from " + currentVoyage.getFrom() + " to " + currentVoyage.getTo() +
                        " was successfully refunded for " + formatValue(totalMoneyRefunded) + " TL.\n");
            }
        }
    }

    /**
     * Prints the details of a voyage, including its ID, route, seat plan, and revenue.
     *
     * @param voyages List of Voyage objects containing all the voyages.
     * @param writer  FileWriter object to write the output.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void printVoyage(List<Voyage> voyages, FileWriter writer) throws IOException {
        boolean isThereAnError = false;
        Voyage voyage = null;

        if (line.length != 2) {
            isThereAnError = true;
            writer.write("ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!\n");
        }

        if (isThereAnError == false) {
            try {
                int voyageID = Integer.parseInt(line[1]);
                if (!(voyageID > 0)) { //The ID must be greater than 0.
                    isThereAnError = true;
                    writer.write("ERROR: " + voyageID + " is not a positive integer, ID of a voyage must be a positive integer!\n");
                } else {
                    boolean isThereVoyage = false;

                    for (int i = 0; i < voyages.size(); i++) {
                        if (voyages.get(i).getVoyageID() == voyageID) {
                            voyage = voyages.get(i);
                            isThereVoyage = true;
                        }
                    }

                    if (isThereVoyage == false) { //If there is no voyage with that ID, it will display an error message.
                        isThereAnError = true;
                        writer.write("ERROR: There is no voyage with ID of " + voyageID + "!\n");
                    }
                }
            } catch (NumberFormatException e) {
                isThereAnError = true;
                writer.write("ERROR: " + line[1] + " is not a positive integer, ID of a voyage must be a positive integer!\n");
            }
        }

        if (isThereAnError == false) {
            writer.write(
                    "Voyage " + voyage.getVoyageID() + "\n" +
                            voyage.getFrom() + "-" + voyage.getTo() + "\n");

            if (voyage instanceof StandartVoyage) {
                StandartVoyage currentVoyage = (StandartVoyage) voyage;
                List<String[]> seatPlan = currentVoyage.getSeatPlan();
                for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                    int j = 0;
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            if (j == 3) {
                                writer.write("X");
                            } else {
                                writer.write("X" + " ");
                            }
                        } else {
                            if (j == 3) {
                                writer.write("*");
                            } else {
                                writer.write("*" + " ");
                            }
                        }
                        j++;
                        if (j == 2) {
                            writer.write("| ");
                        }
                    }
                    writer.write("\n");
                }
            } else if (voyage instanceof PremiumVoyage) {
                PremiumVoyage currentVoyage = (PremiumVoyage) voyage;
                List<String[]> seatPlan = currentVoyage.getSeatPlan();

                for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                    int j = 0;
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            if (j == 2) {
                                writer.write("X");
                            } else {
                                writer.write("X" + " ");
                            }
                        } else if (Objects.equals(seat, "PX")) {
                            if (j == 2) {
                                writer.write("X");
                            } else {
                                writer.write("X" + " ");
                            }
                        } else {
                            if (j == 2) {
                                writer.write("*");
                            } else {
                                writer.write("*" + " ");
                            }
                        }
                        j++;
                        if (j == 1) {
                            writer.write("| ");
                        }
                    }
                    writer.write("\n");
                }

            } else if (voyage instanceof MinibusVoyage) {
                MinibusVoyage currentVoyage = (MinibusVoyage) voyage;
                List<String[]> seatPlan = currentVoyage.getSeatPlan();
                for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                    int j = 0;
                    for (String seat : seatRow) {
                        if (Objects.equals(seat, "RX")) {
                            if (j == 1) {
                                writer.write("X");
                            } else {
                                writer.write("X" + " ");
                            }
                        } else {
                            if (j == 1) {
                                writer.write("*");
                            } else {
                                writer.write("*" + " ");
                            }
                        }

                        j++;
                    }
                    writer.write("\n");
                }
            }
            writer.write("Revenue: " + formatValue(voyage.getRevenue()) + "\n");
        }
    }

    /**
     * Generates a Z report containing details of all voyages, including their IDs, routes, seat plans, and revenues.
     *
     * @param voyages List of Voyage objects containing all the voyages.
     * @param writer  FileWriter object to write the output.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void ZReport(List<Voyage> voyages, FileWriter writer) throws IOException {
        if (line.length != 1) {
            writer.write("ERROR: Erroneous usage of \"Z_REPORT\" command!\n");
        } else {
            if (voyages.size() == 0) {
                writer.write("Z Report:\n" +
                        "----------------\n" +
                        "No Voyages Available!\n" +
                        "----------------\n");
            } else {
                writer.write("Z Report:\n" +
                        "----------------\n");
                for (int i = 0; i < voyages.size(); i++) {
                    writer.write(
                            "Voyage " + voyages.get(i).getVoyageID() + "\n" +
                                    voyages.get(i).getFrom() + "-" + voyages.get(i).getTo() + "\n");

                    if (voyages.get(i) instanceof StandartVoyage) {
                        StandartVoyage voyage = (StandartVoyage) voyages.get(i);
                        List<String[]> seatPlan = voyage.getSeatPlan();
                        for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                            int j = 0;
                            for (String seat : seatRow) {
                                if (Objects.equals(seat, "RX")) {
                                    if (j == 3) {
                                        writer.write("X");
                                    } else {
                                        writer.write("X" + " ");
                                    }
                                } else {
                                    if (j == 3) {
                                        writer.write("*");
                                    } else {
                                        writer.write("*" + " ");
                                    }
                                }
                                j++;
                                if (j == 2) {
                                    writer.write("| ");
                                }
                            }
                            writer.write("\n");
                        }

                    } else if (voyages.get(i) instanceof PremiumVoyage) {
                        PremiumVoyage voyage = (PremiumVoyage) voyages.get(i);
                        List<String[]> seatPlan = voyage.getSeatPlan();

                        for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                            int j = 0;
                            for (String seat : seatRow) {
                                if (Objects.equals(seat, "RX")) {
                                    if (j == 2) {
                                        writer.write("X");
                                    } else {
                                        writer.write("X" + " ");
                                    }
                                } else if (Objects.equals(seat, "PX")) {
                                    if (j == 2) {
                                        writer.write("X");
                                    } else {
                                        writer.write("X" + " ");
                                    }
                                } else {
                                    if (j == 2) {
                                        writer.write("*");
                                    } else {
                                        writer.write("*" + " ");
                                    }
                                }
                                j++;
                                if (j == 1) {
                                    writer.write("| ");
                                }
                            }
                            writer.write("\n");
                        }

                    } else if (voyages.get(i) instanceof MinibusVoyage) {
                        MinibusVoyage voyage = (MinibusVoyage) voyages.get(i);
                        List<String[]> seatPlan = voyage.getSeatPlan();
                        for (String[] seatRow : seatPlan) { //Adjustments have been made for the seat plan to be printed properly.
                            int j = 0;
                            for (String seat : seatRow) {
                                if (Objects.equals(seat, "RX")) {
                                    if (j == 1) {
                                        writer.write("X");
                                    } else {
                                        writer.write("X" + " ");
                                    }
                                } else {
                                    if (j == 1) {
                                        writer.write("*");
                                    } else {
                                        writer.write("*" + " ");
                                    }
                                }
                                j++;
                            }
                            writer.write("\n");
                        }
                    }
                    writer.write("Revenue: " + formatValue(voyages.get(i).getRevenue()) +
                            "\n----------------\n");

                }
            }
        }
    }

    /**
     * Executes a voyage management operation based on the provided command.
     *
     * @param writer FileWriter object to write the output.
     * @throws IOException If an I/O error occurs.
     */
    public void voyageManagementOperation(FileWriter writer) throws IOException {
        if (line[0].equals("INIT_VOYAGE")) {
            this.initVoyage(voyages, writer);

        } else if (line[0].equals("Z_REPORT")) {
            this.ZReport(voyages, writer);

        } else if (line[0].equals("CANCEL_VOYAGE")) {
            this.cancelVoyage(voyages, writer);

        } else if (line[0].equals("PRINT_VOYAGE")) {
            this.printVoyage(voyages, writer);

        } else if (line[0].equals("SELL_TICKET")) {
            this.sellTicket(voyages, writer);

        } else if (line[0].equals("REFUND_TICKET")) {
            this.refundTicket(voyages, writer);

        } else {
            writer.write("ERROR: There is no command namely " + line[0] + "!\n");
        }
    }
}
