package JUnit;

import Model.PriceHistory;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PriceHistoryTest {

    @Test
    void readFromFile() {
        PriceHistory a = new PriceHistory();
        try {

            a.setDate("5/3/18 07:28");
            a.addPrice("500", 3.90);
            a.addPrice("500", 3.90);


            a.addPrice("320", 3.90);
            a.addPrice("420", 3.90);
            a.writeToFile();
        }catch (Exception e){
            e.printStackTrace();
        }
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