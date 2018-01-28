package Test;

import Model.VFHashMap;
import Model.XMLModifier;

public class XMLModifierTest {
    public static  void main(String[] args){
        XMLModifier xml = new XMLModifier();
        xml.deleteXMLNode("ΦΥΡΙΚΙΑ1");

        VFHashMap hashmap =  new VFHashMap();

        System.out.println(hashmap.get("DELETED"));

        xml.modifyXMLNode("ΣΥΚΑ","ΣΥΚΑΑΑ");
        xml.modifyXMLNode("ΣΥΚΑΑΑ","ΣΥΚΑ");
        xml.addXMLNode("SELINO-PATATA","0.30","445566");

    }
}
