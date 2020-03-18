package com.epimetrics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.epimetrics.Fragment.AgeGroupFragment;
import com.epimetrics.Fragment.GenderFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class DiseaseDetailsActivity extends AppCompatActivity
{
    Context context = DiseaseDetailsActivity.this;
    Toolbar toolbar;

    String genderString = "",ageGroupString = "";

    void setUpToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Disease Details");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorToolBar));
        toolbar.setBackgroundColor(ContextCompat.getColor(context,R.color.colorToolBar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void initFragment(Fragment fr, String tag)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fr,tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_doc);
        setUpToolbar();

        Fragment fr = new GenderFragment();
        initFragment(fr,"genderFragment");
    }

    public String getGenderString()
    {
        return genderString;
    }

    public void setGenderString(String genderString)
    {
        Log.d("genderString","genderString: "+genderString);
        this.genderString = genderString;
        Fragment fr = new AgeGroupFragment();
        initFragment(fr,"ageFragment");
    }

    public void setSelcetedId(int id)
    {
        Log.d("id","clicked id: "+id);
        switch (id)
        {
            case R.id.ageGroupZeroToTwo:
                ageGroupString = getString(R.string.string_age_group_one);
                break;
            case R.id.ageGroupTwoToTen:
                ageGroupString = getString(R.string.string_age_group_two);
                break;
            case R.id.ageGroupTenToTwentyFive:
                ageGroupString = getString(R.string.string_age_group_two);
                break;
            case R.id.ageGroupTwentyFiveToForty:
                ageGroupString = getString(R.string.string_age_group_two);
                break;
            case R.id.ageGroupFortyToSixty:
                ageGroupString = getString(R.string.string_age_group_two);
                break;
            case R.id.ageGroupSixtyPlus:
                ageGroupString = getString(R.string.string_age_group_two);
                break;
        }
/*
        Fragment fr = new GenderFragment();
        initFragment(fr);
*/
    }
}