package model;

import java.util.Objects;

public class TransactionReward {
    private String customerId;
    private double purchaseTotal;
    private int rewardPoints;
    public TransactionReward(Builder builder){

    }
    private TransactionReward(String customerId, double purchaseTotal, int rewardPoints){
        this.customerId = customerId;
        this.purchaseTotal = purchaseTotal;
        this.rewardPoints= rewardPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(double purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    public static Transaction.Builder builder(Transaction transaction){
        return new Transaction.Builder(transaction);
    }
    public static Builder builder(TransactionReward transactionReward ){
        return new Builder(transactionReward);
    }

//    public static Builder builder(TransactionReward transactionReward){
//        Builder builder = new Builder();
//        builder.customerId = transactionReward.customerId;
//        builder.purchaseTotal = transactionReward.purchaseTotal;
//        builder.rewardPoints = transactionReward.rewardPoints;
//        return builder;
//    }

    public boolean equals(Object o){
        if(this == o) return true;
        if( o == null || getClass() != o.getClass()) return false;
        TransactionReward that = (TransactionReward) o;
        return Objects.equals(getCustomerId(),that.getCustomerId()) && Objects.equals(getPurchaseTotal(),that.getPurchaseTotal());

    }
    public static class Builder {
        private String customerId;
        private double purchaseTotal;
        private int rewardPoints;
        private Builder(){

        }

        public Builder(TransactionReward transactionReward) {

        }

        public Builder customerId(String val){
            this.customerId = val;
            return this;
        }
        public Builder purchaseTotal(double val){
            this.purchaseTotal = val;
            return this;
        }
        public Builder rewardPoints(int val){
            this.rewardPoints = val;
            return this;
        }
        public TransactionReward build(){
            return new TransactionReward(this);
        }



    }



}

