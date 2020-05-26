package com.example.appbella.Model;

public class ServiceCategory {

    // Remember, all name of variable need EXACTLY name orf JSON property return from API
    // That will help Gson parse it for you correct
    private int id;
    private String name;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
