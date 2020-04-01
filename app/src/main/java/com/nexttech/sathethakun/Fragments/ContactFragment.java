package com.nexttech.sathethakun.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nexttech.sathethakun.Model.ContactModel;
import com.nexttech.sathethakun.R;

import java.util.ArrayList;


public class ContactFragment extends Fragment {

    ScrollView scrollView;
    LinearLayout mainLayout;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ContactModel> seniorDeveloper = new ArrayList<>();
        ArrayList<ContactModel> juniorDeveloper = new ArrayList<>();
        ArrayList<ContactModel> seniorDesign = new ArrayList<>();
        ArrayList<ContactModel> juniorDesign = new ArrayList<>();

        seniorDeveloper.add(new ContactModel("RmFoaW0gRmFoYWQgTGVvbg==", "ZmFoaW1mYWhhZGxlb0BnbWFpbC5jb20=", "MDE5MTQ2MTY0NTM=", "aHR0cHM6Ly9naXRodWIuY29tL2ZhaGltZmFoYWRsZW8="));
        juniorDeveloper.add(new ContactModel("QWZyb3ogSG9zc2Fpbg==", "YWZyb3pob3NzYWluOTdAZ21haWwuY29t", "MDE3NjYyMjYyNjI=", "aHR0cHM6Ly9naXRodWIuY29tL2Fmcm96LW5lcm8="));
        juniorDeveloper.add(new ContactModel("TmF5YW4gQ2hha3JhYmFydHk=", "bmF5YW5kY2M1QGdtYWlsLmNvbQ==", "MDE1MjEzODA5NzQ=", "aHR0cHM6Ly9naXRodWIuY29tL25heWFuY2hha3JhYmFydHk="));
        juniorDeveloper.add(new ContactModel("UXVhemkgTWFoYWJ1YnVsIEhhc2Fu", "aHJpZG95aGFzYW4xNEBnbWFpbC5jb20=", "MDE5MTM2Mjg0MTA=", "aHR0cHM6Ly9naXRodWIuY29tL0hyaWRveUhhc2Fu"));
        juniorDeveloper.add(new ContactModel("U2hhaGFyaWFyIE5ld2F6IFRha2k=", "c2hhaGFyaWFybmF3c2hpbnRha2lAZ21haWwuY29t", "MDE2NzkxMzE0MTM=", "aHR0cHM6Ly9naXRodWIuY29tL3NoYWhhcmlhcnRha2k="));
        juniorDeveloper.add(new ContactModel("Ry5LLlNuaWdkaGE=", "Z2tzbmlnZGhhY3NlNDFAZ21haWwuY29t", "MDE3ODMwMzkyMzk=", "aHR0cHM6Ly9naXRodWIuY29tL0dLU25pZ2RoYQ=="));


        scrollView = new ScrollView(getContext());
        LinearLayout.LayoutParams scrollParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setPadding(10,10,10,10);
        scrollView.setLayoutParams(scrollParam);

        mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams mainParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainLayout.setLayoutParams(mainParam);

        mainLayout.addView(addContact("Senior Developer", seniorDeveloper));
        mainLayout.addView(addContact("Junior Developer", juniorDeveloper));
        mainLayout.addView(addContact("Senior UI Design", seniorDesign));
        mainLayout.addView(addContact("Junior UI Design", juniorDesign));

        scrollView.addView(mainLayout);

        return scrollView;
    }

    private TextView singleText(String text, int alignment, float size){
        TextView tv = new TextView(getContext());
        tv.setText(text);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvParams);
        tv.setTextAlignment(alignment);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(size);

        return tv;
    }

    private CardView addContact(String title, ArrayList<ContactModel> information){
        CardView cardView = new CardView(getContext());

        LinearLayout.LayoutParams cardViewParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParam.setMargins(20,20,20,20);

        cardView.setLayoutParams(cardViewParam);
        cardView.setPadding(20, 20, 20, 20);
        cardView.setRadius(20);
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams linearParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearParam);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(singleText(title, View.TEXT_ALIGNMENT_TEXT_START, 20));

        for (int i = 0; i<information.size(); i++){
            ContactModel contactModel = information.get(i);

            View view = new View(getContext());
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            viewParams.setMargins(0,10,0,10);
            view.setLayoutParams(viewParams);
            view.setBackgroundColor(Color.GRAY);

            LinearLayout linearLayout1 = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout1.setLayoutParams(layoutParams);
            linearLayout1.setPadding(0,10,0,0);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);

            linearLayout1.addView(singleText(contactModel.getName(), View.TEXT_ALIGNMENT_TEXT_START, 18));
            linearLayout1.addView(singleText(contactModel.getContact(), View.TEXT_ALIGNMENT_TEXT_START, 16));
            linearLayout1.addView(singleText(contactModel.getEmail(), View.TEXT_ALIGNMENT_TEXT_START, 16));
            linearLayout1.addView(singleText(contactModel.getGithub(), View.TEXT_ALIGNMENT_TEXT_START, 16));

            linearLayout.addView(view);
            linearLayout.addView(linearLayout1);
        }

        cardView.addView(linearLayout);

        return cardView;
    }
}
