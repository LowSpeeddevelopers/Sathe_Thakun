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
        String[] menuitems ={"Personal information\n" +
                "We use offline database to store one’s current settings and we use google’s firebase database to track your location and store your personal information. This project is provided by the govt so one should not concern about their information leackage. ",
                "We also collect personally identifiable information that you provide to us, such as your name, phone number, photo, email address and geographical location. With your permission, we may also access other personal information on your device, such as your phone book, calendar or messages, in order to provide services to you.",
                "We store all these personal data under the Digital Security Act 2018, Bangladesh.",
                "Ads are served to you only from Google Admob.",
                "We will post any privacy policy changes on this page and, if the changes are significant, we will provide a more prominent notice.",
                "If you have any question about this Privacy Policy, please contact us!"


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
