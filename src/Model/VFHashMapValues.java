package Model;

public class VFHashMapValues {
    double profit;
    String kef5Code;
    public double getProfit(){
        return profit;
    }
    public String getKef5Code(){
        return  kef5Code;
    }

    @Override
    public String toString() {
        return "("+profit+","+kef5Code+")";
    }
}
