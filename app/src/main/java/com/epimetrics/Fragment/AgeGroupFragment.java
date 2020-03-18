package com.epimetrics.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.epimetrics.DiseaseDetailsActivity;
import com.epimetrics.R;
import com.epimetrics.helper.ToggleButtonGroupTableLayout;

public class AgeGroupFragment extends Fragment
{
    TextView heading,details;

    ToggleButtonGroupTableLayout ageRadioGroup;
    RadioButton ageGroupZeroToTwo,ageGroupTwoToTen,ageGroupTenToTwentyFive,ageGroupTwentyFiveToForty,ageGroupFortyToSixty,ageGroupSixtyPlus;

    void findView(View view)
    {
        heading = view.findViewById(R.id.heading);
        details = view.findViewById(R.id.details);

        ageRadioGroup = view.findViewById(R.id.ageRadioGroup);
        ageGroupZeroToTwo = view.findViewById(R.id.ageGroupZeroToTwo);
        ageGroupTwoToTen = view.findViewById(R.id.ageGroupTwoToTen);
        ageGroupTenToTwentyFive = view.findViewById(R.id.ageGroupTenToTwentyFive);
        ageGroupTwentyFiveToForty = view.findViewById(R.id.ageGroupTwentyFiveToForty);
        ageGroupFortyToSixty = view.findViewById(R.id.ageGroupFortyToSixty);
        ageGroupSixtyPlus = view.findViewById(R.id.ageGroupSixtyPlus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_age_group, container, false);

        findView(view);
        return view;
    }
}
