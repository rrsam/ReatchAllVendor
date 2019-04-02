package com.reatchall.charan.reatchallVendor.Vendor.interfaces;

import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;

/**
 * Created by NaNi on 07/03/18.
 */

public interface ILoadProductsList {

    void saveAndLoad(String productName, ListDetailsModel listDetailsModel);
    void deleteAndLoad(int i);
}
