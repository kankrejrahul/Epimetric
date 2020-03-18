package com.epimetrics.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epimetrics.DiseaseDetailsActivity;
import com.epimetrics.R;

public class GenderFragment extends Fragment implements View.OnClickListener
{
    String genderString = "";
    TextView male,feMale;

    void findView(View view)
    {
        male = view.findViewById(R.id.male);
        feMale = view.findViewById(R.id.feMale);
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
