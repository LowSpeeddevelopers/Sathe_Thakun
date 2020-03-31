package com.nexttech.sathethakun.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nexttech.sathethakun.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        String[] menuitems ={
            "Sathe Thakun uses an offline database to store one’s current settings and use google’s firebase database to track user location and store user information.",
            "This application is built under a government funded project so one should not be concerned about their information leakage.",
            "Sathe Thakun also collects personally identifiable information that users provide to us, such as name, phone number, photo, email address and geographical location.",
            "By taking user permission, Sathe Thakun may also access other personal information on the user's device, such as phone book, calendar in order to provide better services.",
            "Sathe Thakun stores all personal data under the Digital Security Act 2018, Bangladesh.",
            "Ads are served to you only from Google Admob.",
            "Team Sathe Thakun will post any privacy policy changes on this page and, if the changes are significant, we will provide a more prominent notice.",
            "If you have any questions about this Privacy Policy, please contact us."
        };
        ListView listView = (ListView) view.findViewById(R.id.privacy_policy);
        ArrayAdapter<String> listViewAdaptar = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                menuitems
        );
        listView.setAdapter(listViewAdaptar);
        return view;
    }
}
