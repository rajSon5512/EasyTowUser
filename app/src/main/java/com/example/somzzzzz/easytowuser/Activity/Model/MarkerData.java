package com.example.somzzzzz.easytowuser.Activity.Model;

import java.io.Serializable;

public class MarkerData implements Serializable {

    private double lag;
    private double log;
    private String nameofplace;


    public MarkerData(double lag, double log, String nameofplace){

        this.lag=lag;
        this.log=log;
        this.nameofplace=nameofplace;

    }

    public double getLag() {
        return lag;
    }

    public void setLag(double lag) {
        this.lag = lag;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }


    public String getNameofplace() {
        return nameofplace;
    }

    public void setNameofplace(String nameofplace) {
        this.nameofplace = nameofplace;
    }

}
