package com.nexttech.sathethakun.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.nexttech.sathethakun.Adapter.HelpExpandableListAdapter;
import com.nexttech.sathethakun.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private Context context;
    private ExpandableListView list_view;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = this.getActivity();
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_view = view.findViewById(R.id.list_view);
        createListData();

        list_view.setOnGroupClickListener((parent, v, groupPosition, id) -> false);


        list_view.setOnGroupExpandListener(groupPosition -> {

        });


        list_view.setOnGroupCollapseListener(groupPosition -> {

        });


        list_view.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            return false;
        });
    }

    private void createListData() {
        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding group data
        listDataGroup.add("Help 1");
        listDataGroup.add("Help 2");
        listDataGroup.add("Help 3");

        // Adding child data List one
        List<String> help1 = new ArrayList<>();
        help1.add("1");
        help1.add("2");
        help1.add("3");

        // Adding child data List two
        List<String> help2  = new ArrayList<>();
        help2.add("1");
        help2.add("2");
        help2.add("3");

        // Adding child data List three
        List<String> help3 = new ArrayList<>();
        help3.add("1");
        help3.add("2");
        help3.add("3");
        help3.add("4");

        listDataChild.put(listDataGroup.get(0), help1); // Header, Child data
        listDataChild.put(listDataGroup.get(1), help2); // Header, Child data
        listDataChild.put(listDataGroup.get(2), help3); // Header, Child data

        HelpExpandableListAdapter listAdapter = new HelpExpandableListAdapter(context, listDataGroup, listDataChild);
        list_view.setAdapter(listAdapter);
    }
}
