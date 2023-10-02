package com.example.kangai.Objects;

public class Plants {
    Integer slotNumber;
    String name, status, value, lastWatered;

    public Plants(Integer slotNumber, String name, String status, String value, String lastWatered) {
        this.slotNumber = slotNumber;
        this.name = name;
        this.status = status;
        this.value = value;
        this.lastWatered = lastWatered;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(String lastWatered) {
        this.lastWatered = lastWatered;
    }
}
