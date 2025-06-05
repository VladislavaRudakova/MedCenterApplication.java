package com.medCenter.medCenter.model.entity;

public enum Days {
    ONE_DAY(1);
    private final int daysQuantity;

    Days (int daysQuantity){
       this.daysQuantity=daysQuantity;
    }

   public int getDaysQuantity(){
        return daysQuantity;
   }

}
