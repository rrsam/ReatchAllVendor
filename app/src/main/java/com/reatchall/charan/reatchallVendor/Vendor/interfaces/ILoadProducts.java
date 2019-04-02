package com.reatchall.charan.reatchallVendor.Vendor.interfaces;

import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;

/**
 * Created by NaNi on 07/03/18.
 */

public interface ILoadProducts {

    void saveAndLoad(String productName, NewProduct allProducts);
    void deleteAndLoad(int i);
}
