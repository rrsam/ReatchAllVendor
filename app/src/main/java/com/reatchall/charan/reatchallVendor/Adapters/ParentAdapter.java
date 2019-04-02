package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewBusinessProductsViewHolder> {
    Context context;

    ArrayList<String> arrayList;
    ArrayList<String> productsList;

    GridNearByAdapter gridNearByAdapter;

    public ParentAdapter(Context context, ArrayList<String> arrayList, ArrayList<String> productsList) {
        this.context = context;
        this.arrayList = arrayList;
        this.productsList = productsList;
    }

    @Override
    public ParentAdapter.ViewBusinessProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parent_recycler_view, parent, false);

        return new ViewBusinessProductsViewHolder(itemView);
    }
    int i=0;
    @Override
    public void onBindViewHolder(final ParentAdapter.ViewBusinessProductsViewHolder holder, int position) {


        holder.title.setText(arrayList.get(position));

        holder.recyclerView.setHasFixedSize(true);


        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(3), true));

        gridNearByAdapter=new GridNearByAdapter(productsList,context,"");

        holder.recyclerView.setAdapter(gridNearByAdapter);




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewBusinessProductsViewHolder extends RecyclerView.ViewHolder {

        FontTextView title;
        RecyclerView recyclerView;

        public ViewBusinessProductsViewHolder(View itemView) {
            super(itemView);

            title=(FontTextView)itemView.findViewById(R.id.headerTitle);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.parent_rcv_item);


        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
