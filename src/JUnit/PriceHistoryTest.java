package JUnit;

import Model.PriceHistory;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PriceHistoryTest {

    @Test
    void readFromFile() {
        PriceHistory a = new PriceHistory();
        a.addPrice("500",3.90);
        a.addPrice("500",3.90);

        a.addPrice("320",3.90);
        a.addPrice("420",3.90);
        a.writeToFile();
    }

    @Test
    void addPrice() {
    }

    @Test
    void getPrices() {
    }

    @Test
    void writeToFile() {

    }

    @Test
    void getVector(){
        //PriceHistory a = new PriceHistory();
        //System.out.println(a.getVector());

    }
}