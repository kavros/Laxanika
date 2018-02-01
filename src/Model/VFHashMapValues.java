package Model;

public class VFHashMapValues {
    private double profit;
    private String kef5Code;

    VFHashMapValues(double profit,String kef5Code){
        this.profit=profit;
        this.kef5Code=kef5Code;
    }
    VFHashMapValues(){}
    protected double getProfit(){
        return profit;
    }
    protected String getKef5Code(){
        return  kef5Code;
    }

    @Override
    public String toString() {
        return profit+" "+kef5Code;
    }

    public void setProfit(double profit){
        this.profit=profit;
    }

    public void setKef5Code(String kef5Code){
        this.kef5Code=kef5Code;
    }

}
