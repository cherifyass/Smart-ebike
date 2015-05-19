package com.esir.si.smarte_bike.json;

import java.util.Date;

public class EBikeBattery {
    private boolean bool;
    private String niveau;
    private Date derniereRecharge;

    public EBikeBattery(boolean bool, String niveau, Date derniereRecharge) {
        this.bool = bool;
        this.niveau = niveau;
        this.derniereRecharge = derniereRecharge;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Date getDerniereRecharge() {
        return derniereRecharge;
    }

    public void setDerniereRecharge(Date derniereRecharge) {
        this.derniereRecharge = derniereRecharge;
    }

}
