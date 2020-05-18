public class Instrument {

    private String name;
    private int qty;
    private int price;
    private int ID;

   public Instrument(String name, int qty, int price, int ID) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }

    public int getQty() {
        return qty;
    }

    public int getPrice() {
        return price;
    }

    public int getID() {
        return ID;
    }

    public void buy(){
        qty--;
    }

    public void sell(){
        qty++;
    }

    //market selling to broker
    public String buyqty(String x)
    {
        int value = Integer.parseInt(x);

        if(value > qty)
        {
           return "Order Rejected.Qty not available";
        }
        else if (value == 0)
        {
            return "Please enter a qty number";
        }

        this.qty = qty-value;

        return  "Order successful! Remaining Quantity: "+this.qty;
    }

    //market buying from broker

    public String sellqty(String x)
    {
        int value = Integer.parseInt(x);

        if (value <= 0)
        {
            return "Invalid Qty...Available Qty:" + this.qty;
        }
        this.qty = qty+value;

        return  "Order successful, new qty: " + this.qty;
    }


}
