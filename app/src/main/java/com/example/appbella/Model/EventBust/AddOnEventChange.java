package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Addon;

public class AddOnEventChange {
    private boolean isAdd;
    private Addon addon;

    public AddOnEventChange(boolean isAdd, Addon addon) {
        this.isAdd = isAdd;
        this.addon = addon;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }
}
