package com.example.appbella.Interface;

import com.example.appbella.Model.Addon;

import java.util.List;

public interface IAddonLoadListener {
    void onAddonLoadSuccess(List<Addon> addonList);
    void onAddonLoadFailed(String message);
}
