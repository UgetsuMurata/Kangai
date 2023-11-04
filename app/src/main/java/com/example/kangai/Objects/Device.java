package com.example.kangai.Objects;

import androidx.annotation.NonNull;

import java.util.List;

public class Device {
    String id, manager, name;
    List<Plants> plantSlots;
    Long reservoir_water_level, lastUpdate;

    public Device(String id, String manager, String name, List<Plants> plantSlots, Long reservoir_water_level, Long lastUpdate) {
        this.id = id;
        this.manager = manager;
        this.name = name;
        this.plantSlots = plantSlots;
        this.reservoir_water_level = reservoir_water_level==null?0:reservoir_water_level;
        this.lastUpdate = lastUpdate==null?0:lastUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Plants> getPlantSlots() {
        return plantSlots;
    }

    public void setPlantSlots(List<Plants> plantSlots) {
        this.plantSlots = plantSlots;
    }

    public Long getReservoir_water_level() {
        return reservoir_water_level;
    }

    public void setReservoir_water_level(Long reservoir_water_level) {
        this.reservoir_water_level = reservoir_water_level;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
