package com.reatchall.charan.reatchallVendor.Fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.GridNearByAdapter;
import com.reatchall.charan.reatchallVendor.Adapters.ListSearchAdapter;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 08/02/18.
 */

public class SearchFragment extends Fragment {


    ImageView grid,list;

    RecyclerView recyclerViewGrid,recyclerViewList;

    ArrayList<String> arrayList= new ArrayList<>();

    GridNearByAdapter gridNearByAdapter;
    ListSearchAdapter listSearchAdapter;
    boolean checkGrid=true,checkList=true;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        grid=(ImageView)view.findViewById(R.id.list_grid);
        list=(ImageView)view.findViewById(R.id.list_icon);
        recyclerViewGrid=(RecyclerView)view.findViewById(R.id.near_by_rcv_grid);
        recyclerViewList=(RecyclerView)view.findViewById(R.id.near_by_rcv_list);

        recyclerViewGrid.setHasFixedSize(true);
        recyclerViewList.setHasFixedSize(true);





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



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewGrid.setLayoutManager(mLayoutManager);
        recyclerViewGrid.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        gridNearByAdapter=new GridNearByAdapter(arrayList,getActivity(),"");
        recyclerViewGrid.setAdapter(gridNearByAdapter);


        RecyclerView.LayoutManager mLayoutManagerList = new GridLayoutManager(getActivity(), 1);
        recyclerViewList.setLayoutManager(mLayoutManagerList);
        recyclerViewList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        listSearchAdapter=new ListSearchAdapter(arrayList,getActivity());
        recyclerViewList.setAdapter(listSearchAdapter);

        /*grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    recyclerViewGrid.setVisibility(View.VISIBLE);
                    recyclerViewList.setVisibility(View.GONE);



                grid.setImageResource(R.drawable.ic_grid_white);
                list.setImageResource(R.drawable.ic_list_grey);
            }
        });*/


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerViewGrid.setVisibility(View.GONE);
                recyclerViewList.setVisibility(View.VISIBLE);
                list.setImageResource(R.drawable.ic_list_white);
                grid.setImageResource(R.drawable.ic_grid_grey);


            }
        });


        list.performClick();

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
