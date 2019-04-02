package com.reatchall.charan.reatchallVendor.Fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Adapters.CartBusinessAdapter;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 20/02/18.
 */

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";

    RecyclerView recyclerView;

    CartBusinessAdapter cartBusinessAdapter;
    ArrayList<String> arrayList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView)view.findViewById(R.id.cart_rcv_business);
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        arrayList.add("ddsd");
        cartBusinessAdapter = new CartBusinessAdapter(getActivity(),arrayList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManagerList = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManagerList);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setAdapter(cartBusinessAdapter);
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
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
