package model;

import java.util.Date;

public class TransactionPattern {

    private String zipCode;
    private String item;
    private Date date;
    private double amount;

    private TransactionPattern(Builder builder){
        this.zipCode = builder.zipCode;
        this.item = builder.item;
        this.date= builder.date;
        this.amount = builder.amount;
    }

    public static Builder newBuilder(){
        return new Builder();
    }
    public static Builder builder(Transaction transactionPattern ){
        return new Builder(transactionPattern);
    }

    public static class Builder {
        private String zipCode;
        private String item;
        private Date date;
        private double amount;

        private Builder(){

        }

        public Builder(Transaction transactionPattern) {
        }


        public Builder zipCode(String val){
            this.zipCode = val;
            return this;
        }
        public Builder item (String val){
            this.item = val ;
            return this;
        }
        public Builder date(Date val){
            this.date = val;
            return this;

        }
        public Builder amount (Double val){
            this.amount = val;
            return this;

        }
        public TransactionPattern build(){

            return new TransactionPattern(this);
        }

    }





}





