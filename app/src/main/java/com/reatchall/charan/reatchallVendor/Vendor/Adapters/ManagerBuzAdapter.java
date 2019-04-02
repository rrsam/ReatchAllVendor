package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.VendorDashBoardActivity;
import com.reatchall.charan.reatchallVendor.Vendor.VendorDashBoardNewActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoginManager;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ManagerBuzAdapter extends RecyclerView.Adapter<ManagerBuzAdapter.ManagerBuzViewHolder> {
    Context context;
    ArrayList<BusinessDetails> businessDetailsArrayList;
    ILoginManager iLoginManager;

    public ManagerBuzAdapter(Context context, ArrayList<BusinessDetails> businessDetailsArrayList, ILoginManager iLoginManager) {
        this.context = context;
        this.businessDetailsArrayList = businessDetailsArrayList;
        this.iLoginManager = iLoginManager;
    }

    @NonNull
    @Override
    public ManagerBuzViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_business_list,parent,false);
        return new ManagerBuzViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerBuzViewHolder holder, int position) {
            holder.businessName.setText(businessDetailsArrayList.get(position).getBusinessName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(businessDetailsArrayList.size()>1){
                        iLoginManager.loginManager(position);
                    }else{
                        if(businessDetailsArrayList.get(position).isService()){
                            Intent intent = new Intent(context,VendorDashBoardNewActivity.class);
                            intent.putExtra("businessDetails",businessDetailsArrayList.get(position));
                            context.startActivity(intent);
                        }else{
                            Intent intent = new Intent(context,VendorDashBoardActivity.class);
                            intent.putExtra("businessDetails",businessDetailsArrayList.get(position));
                            context.startActivity(intent);
                        }
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return businessDetailsArrayList.size();
    }

    public class ManagerBuzViewHolder extends RecyclerView.ViewHolder {
        FontTextView businessName;
        public ManagerBuzViewHolder(View itemView) {
            super(itemView);
            businessName = (FontTextView)itemView.findViewById(R.id.businessName);
        }
    }
}
