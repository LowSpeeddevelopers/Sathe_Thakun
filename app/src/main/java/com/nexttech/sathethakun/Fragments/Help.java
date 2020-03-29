package com.nexttech.sathethakun.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.nexttech.sathethakun.Adapter.ExpandableListViewAdapter;
import com.nexttech.sathethakun.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
// * A simple {@link Fragment} subclass.
// // * Use the {@link Help#//newInstance} factory method to
// * create an instance of this fragment.
// */
public class Help extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;

    public Help() {
        // Required empty public constructor
    }*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Help.
     */
    // TODO: Rename and change types and number of parameters
    /*public static Help newInstance(String param1, String param2) {
        Help fragment = new Help();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        expandableListView = view.findViewById(R.id.expandableListView);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();
        expandableListViewAdapter = new ExpandableListViewAdapter(context, listDataGroup, listDataChild);
        expandableListView.setAdapter(expandableListViewAdapter);

        // Adding group data
        listDataGroup.add(getString(R.string.text_on_off));
        listDataGroup.add(getString(R.string.text_add_remainder));
        listDataGroup.add(getString(R.string.text_priority));
        listDataGroup.add(getString(R.string.text_update_delete));
        listDataGroup.add(getString(R.string.text_transfer));
        String[] array;
        array = getResources().getStringArray(R.array.string_array_on_off);
        List<String> onList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_remainder);
        List<String> updateList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_priority);
        List<String> priorityList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_update_delete);
        List<String> remainderList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_transfer);
        List<String> transferList = new ArrayList<>(Arrays.asList(array));
        List<String> questionList = new ArrayList<>(Arrays.asList(array));
        listDataChild.put(listDataGroup.get(0), onList);
        listDataChild.put(listDataGroup.get(1), updateList);
        listDataChild.put(listDataGroup.get(2), priorityList);
        listDataChild.put(listDataGroup.get(3), remainderList);
        listDataChild.put(listDataGroup.get(4), transferList);
        expandableListViewAdapter.notifyDataSetChanged();


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initViews();
//        initListeners();
//        initObjects();
//        initListData();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }



    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            //NavUtils.PARENT_ACTIVITY.charAt(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
