package JUnit;

import Model.VFHashMapValues;
import Model.XMLReader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class XMLReaderTest {

    @Test
    void getDatabaseCredentials() {
        XMLReader xml = new XMLReader();
        XMLReader.DatabaseEntry a =xml.getDatabaseCredentials();
        assertEquals(a.getUsername(),"kef" );
        assertEquals(a.getDbURL(),"jdbc:sqlserver://DESKTOP-4K5L7KE;databaseName=Cmp006" );
    }

    @Test
    void getProducts() {
        XMLReader xml = new XMLReader();
        HashMap<String, VFHashMapValues> hash= xml.getProducts();
        assert(hash != null);
        assert (hash.get("ΠΑΤΑΤΕΣ") != null);
    }
}