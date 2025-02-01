import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface IVoyageManegement {
    void initVoyage(List<Voyage> voyages, FileWriter writer) throws IOException;

    void ZReport(List<Voyage> voyages, FileWriter writer) throws IOException;

    void cancelVoyage(List<Voyage> voyages, FileWriter writer) throws IOException;

    void sellTicket(List<Voyage> voyages, FileWriter writer) throws IOException;

    void refundTicket(List<Voyage> voyages, FileWriter writer) throws IOException;

    void printVoyage(List<Voyage> voyages, FileWriter writer) throws IOException;
}
