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
        listDataGroup.add("How can I Registration New Account");
        listDataGroup.add("How can I Sing in");
        listDataGroup.add("How can I Search People");
        listDataGroup.add("How can I Tracking People");

        // Adding child data List one
        List<String> help1 = new ArrayList<>();
        help1.add("Click on Sign Up Button ");
        help1.add("Put Your Personal  information");
        help1.add("then Click on Sign Up Button, which is available on bottom");

        // Adding child data List two
        List<String> help2  = new ArrayList<>();
        help2.add("put your email address ");
        help2.add("put your password");
        help2.add("then Click on Sign In Button");

        // Adding child data List three
        List<String> help3 = new ArrayList<>();
        help3.add("you would find those people which are connected with this application");
        help3.add("Click Add People");
        help3.add("put people phone number");
        help3.add("then click on search button ");
        help3.add("if you find right person you can send request them");
        help3.add("if he/she accept your request, you can see them on connected people");

        List<String> help4 = new ArrayList<>();
        help4.add("click on connected people ");
        help4.add("Click on Location Button ");
        help4.add("then maps will open and see realtime location which is connected with you");


        listDataChild.put(listDataGroup.get(0), help1); // Header, Child data
        listDataChild.put(listDataGroup.get(1), help2); // Header, Child data
        listDataChild.put(listDataGroup.get(2), help3); // Header, Child data
        listDataChild.put(listDataGroup.get(3), help4); // Header, Child data

        HelpExpandableListAdapter listAdapter = new HelpExpandableListAdapter(context, listDataGroup, listDataChild);
        list_view.setAdapter(listAdapter);
    }
}
