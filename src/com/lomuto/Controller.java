package com.lomuto;

public class Controller {
    private static final int MAX_CASH = 1000;
    private int remainCash;
    private static Controller controllerInstance;

    public static Controller getInstance() {
        if(controllerInstance == null) {
            controllerInstance = new Controller();
        }

        return controllerInstance;
    }

    public boolean setRemainCash(int amount) {
        if(amount < 0) {
            return false;
        }

        this.remainCash = amount;
        return true;
    }

    public boolean depositCash(int amount) {
        if(this.remainCash + amount > MAX_CASH) {
            return false;
        }

        this.remainCash += amount;
        return true;
    }

    public boolean withdrawCash(int amount) {
        if(this.remainCash - amount < 0) {
            return false;
        }

        this.remainCash -= amount;
        return true;
    }



    private Controller(){}
}
