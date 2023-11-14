package com.example.kangai.Objects;

import android.util.Log;

public class Plants {
    Integer slotNumber;
    String name, status;

    public Plants(Integer slotNumber, String name, String status) {
        this.slotNumber = slotNumber;
        this.name = name;
        this.status = status;
    }

    public Boolean exists(){
        return slotNumber!=null;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
