package Model;

public class VFHashMapValues {
    double profit;
    String kef5Code;
    public VFHashMapValues(double profit,String kef5Code){
        this.profit=profit;
        this.kef5Code=kef5Code;
    }
    public VFHashMapValues(){}
    public double getProfit(){
        return profit;
    }
    public String getKef5Code(){
        return  kef5Code;
    }

    @Override
    public String toString() {
        return profit+" "+kef5Code;
    }
}
