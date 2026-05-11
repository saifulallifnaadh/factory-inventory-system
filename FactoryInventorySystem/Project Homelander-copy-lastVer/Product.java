public class Product {

    // --- Attributes 
    private String productID;
    private String productName;
    private int productQuantity;
    private double price;
    private String category;

    // --- Constructor ---
    public Product(String productID, String productName, int productQuantity, double price, String category) {
        this.productID = productID;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.price = price;
        this.category = category;
    }

    // --- Setters (Mutators) ---
    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // --- Getters (Accessors) ---
    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    // Returns a string representation of the object
    @Override
    public String toString() {
        return "Product [" +
               "ID='" + productID + '\'' +
               ", Name='" + productName + '\'' +
               ", Qty=" + productQuantity +
               ", Price=" + price +
               ", Category='" + category + '\'' +
               ']';
    }
}