package model;

import java.util.Date;
import java.util.Objects;

public class Transaction {
    private String firstName;
    private String lastName;
    private String customerID;
    private String creditCardNumber;
    private String itemPurchased;
    private int quantity;
    private double price;
    private Date purchaseDate;
    private String zipCode;

    private String department;
    public Transaction(){

    }
    public Transaction(Builder builder){
        this.firstName = builder.firstName;
        this.lastName=  builder.lastName;
        this.customerID = builder.customerID;
        this.creditCardNumber = builder.creditCardNumber;
        this.itemPurchased = builder.itemPurchased;
        this.department = builder.department;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.purchaseDate = builder.purchasedDate;
        this.zipCode = builder.zipCode;
    }
    public static Builder newBuilder(){
        return new Builder();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getItemPurchased() {
        return itemPurchased;
    }

    public void setItemPurchased(String itemPurchased) {
        this.itemPurchased = itemPurchased;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static Builder newBuilder(Transaction transaction){
        Builder builder = new Builder();
        builder.firstName = transaction.firstName;
        builder.lastName = transaction.lastName;
        builder.customerID = transaction.customerID;
        builder.creditCardNumber = transaction.creditCardNumber;
        builder.itemPurchased = transaction.itemPurchased;
        builder.department = transaction.department;
        builder.quantity = transaction.quantity;
        builder.price = transaction.price;
        builder.purchasedDate = transaction.purchaseDate;
        builder.zipCode = transaction.zipCode;
        return builder;
    }
    public static class Builder{
        private String firstName;
        private String lastName;
        private String customerID;
        private String creditCardNumber;
        private String itemPurchased;
        private int quantity;
        private double price;
        private Date purchasedDate;
        private String zipCode;
        private String department;
        private static final String MASKING_CREDIT_CARD = "xxxx-xxxx-xxxx-";
        public Builder(){

        }

        public Builder(Transaction transaction) {
        }

        public Builder maskCreditCard(){
            Objects.requireNonNull(this.creditCardNumber,"Credit card number cannot be null");
            String[] parts = this.creditCardNumber.split("-");
            if(parts.length < 4){
                this.creditCardNumber = "xxxx";
            }
            else{
                String lastFourDigits = parts[3];
                this.creditCardNumber = MASKING_CREDIT_CARD+lastFourDigits;
            }
            return this;
        }
        public Builder firstName(String val){
            this.firstName = val;
            return this;
        }
        public Builder lastName(String val){
            this.lastName = val;
            return this;
        }
        public Builder customerID(String val){
            this.customerID = val;
            return this;
        }
        public Builder creditCardNumber(String val) {
            this.creditCardNumber = val;
            return this;
        }
        public Builder itemPurchased(String val){
            this.itemPurchased = val;
            return this;
        }
        public Builder quantity(int val){
            this.quantity = val;
            return this;
        }
        public Builder price(double val){
            this.price = val;
            return this;
        }
        public Builder purchasedDate(Date val){
            this.purchasedDate = val;
            return this;
        }
        public Builder zipCode(String val){
            this.zipCode = val;
            return this;
        }
        public Builder department(String val){
            this.department = val;
            return this;
        }
        public Transaction build(){

            return new Transaction(this);
        }
    }
}
