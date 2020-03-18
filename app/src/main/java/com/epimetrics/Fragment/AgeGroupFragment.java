package com.epimetrics.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epimetrics.DiseaseDetailsActivity;
import com.epimetrics.R;

public class AgeGroupFragment extends Fragment implements View.OnClickListener
{


    void findView(View view)
    {
        heading = view.findViewById(R.id.heading);
        details = view.findViewById(R.id.details);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_gender, container, false);

        findView(view);


        male.setOnClickListener(this);
        feMale.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.male:
                genderString = "male";
                break;
            case R.id.feMale:
                genderString = "feMale";
                break;
        }
        ((DiseaseDetailsActivity)getActivity()).setGenderString(genderString);
    }
}
