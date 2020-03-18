package com.epimetrics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.epimetrics.Fragment.GenderFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class DiseaseDetailsActivity extends AppCompatActivity
{
    Context context = DiseaseDetailsActivity.this;
    Toolbar toolbar;

    String genderString = "";

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

    void initFragment(Fragment fr)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fr);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_doc);
        setUpToolbar();

        Fragment fr = new GenderFragment();
        initFragment(fr);
    }

    public String getGenderString()
    {
        return genderString;
    }

    public void setGenderString(String genderString)
    {
        this.genderString = genderString;
        Fragment fr = new GenderFragment();
        initFragment(fr);
    }
}