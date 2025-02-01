import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class BookingSystem {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments," +
                    " the first one is the path to the input file whereas the second one is the path to the output file." +
                    " Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        // Check if input file exists and readable
        File input = new File(inputFileName);
        if (!input.exists() || !input.isFile() || !input.canRead()) {
            System.out.println("ERROR: This program cannot read from the \"" + inputFileName + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            return;
        }

        // Check if output file can be written

        File outputDir = new File(outputFileName).getParentFile();
        if (!outputDir.exists() || !outputDir.isDirectory() || !outputDir.canWrite()) {
            System.out.println("ERROR: This program cannot write to the \"" + outputFileName + "\", please check the permissions to write that directory. Program is going to terminate!");
            return;
        }

        try {
            FileWriter writer = new FileWriter(outputFileName);
            String[] inputContent = FileInput.readFile(inputFileName, false, false);
            FileOutput.writeToFile(outputFileName, "", false, false);

            Operations(inputContent, writer);
            writer.close();
            removeTrailingNewLine(outputFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an unnecessary newline character at the end of the specified file.
     *
     * @param filePath The path of the file from which the newline character will be removed.
     * @throws IOException If an I/O error occurs while performing file operations.
     */
    public static void removeTrailingNewLine(String filePath) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        long length = file.length();
        if (length == 0) {
            return;
        }
        file.setLength(length - 1);
        file.close();
    }

    /**
     * Performs operations based on the input content and writes the results to a FileWriter.
     *
     * @param inputContent An array of strings representing the input content to be processed.
     * @param writer       The FileWriter object used for writing output.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public static void Operations(String[] inputContent, FileWriter writer) throws IOException {

        List<Voyage> voyages = new ArrayList<>();
        boolean hasZReport = false;

        for (int i = 0; i < inputContent.length; i++) {
            String inputLine = inputContent[i].trim();
            if (inputLine.isEmpty()) {
                continue; // Skip the empty line.
            }
            String[] line = inputLine.split("\t");
            if (line.length == 0) {
                continue; // Skip the invalid line.
            }

            String stringLine = null;
            for (int j = 0; j < line.length; j++) {
                stringLine += line[j];
            }

            writer.write("COMMAND: " + inputLine + "\n");
            VoyageManagement voyageManagement = new VoyageManagement(line, voyages);
            voyageManagement.voyageManagementOperation(writer);

            if (!line[0].equals("Z_REPORT")) {
                hasZReport = false;
            } else {
                hasZReport = true;
            }
        }

        if (!hasZReport) {
            String[] zReportLine = {"Z_REPORT"}; //If the last command in the input file is not Z_REPORT, it prints the last Z_REPORT command.

            VoyageManagement zReportManagement = new VoyageManagement(zReportLine, voyages);
            zReportManagement.voyageManagementOperation(writer);
        }
    }
}