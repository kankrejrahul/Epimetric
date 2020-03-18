package com.epimetrics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.epimetrics.helper.OtherUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener
{
    Context context = RegistrationActivity.this;
    Toolbar toolbar;

    EditText name,registrationId,age,email,password,confPassword;

    String nameString,registrationIdString,ageString,emailString,passwordString,confPasswordString;

    RadioGroup radioSex;
    RadioButton radioMale,radioFemale;
    CheckBox termsAndConditionCheckBox;

    TextView register;
    LinearLayout progressLL;

    void setUpToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

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

    void findView()
    {
        name = findViewById(R.id.name);
        registrationId = findViewById(R.id.registrationId);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confPassword);



        radioSex = findViewById(R.id.radioSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        termsAndConditionCheckBox = findViewById(R.id.termsAndConditionCheckBox);;

        register = findViewById(R.id.register);

        progressLL = findViewById(R.id.progressLL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findView();
        setUpToolbar();

        register.setOnClickListener(this);

        name.setOnFocusChangeListener(this);
        registrationId.setOnFocusChangeListener(this);
        age.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        confPassword.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login:
                                validateAndLogin();
                                break;
        }
    }

    void validateAndLogin()
    {
        name.setError(null);
        registrationId.setError(null);
        age.setError(null);
        email.setError(null);
        password.setError(null);
        confPassword.setError(null);

        nameString = name.getText().toString().trim();
        registrationIdString = registrationId.getText().toString().trim();
        ageString = age.getText().toString().trim();
        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        confPasswordString = confPassword.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nameString))
        {
            name.setError(getResources().getString(R.string.field_required));
            focusView = name;
            cancel = true;
        }

        else if (TextUtils.isEmpty(registrationIdString))
        {
            registrationId.setError(getResources().getString(R.string.field_required));
            focusView = registrationId;
            cancel = true;
        }

        else if (TextUtils.isEmpty(ageString))
        {
            age.setError(getResources().getString(R.string.field_required));
            focusView = age;
            cancel = true;
        }

        else if (TextUtils.isEmpty(emailString))
        {
            email.setError(getResources().getString(R.string.field_required));
            focusView = email;
            cancel = true;
        }
        else if (!OtherUtils.isEmailValid(emailString))
        {
            email.setError(getResources().getString(R.string.incorrect_email));
            focusView = email;
            cancel = true;
        }
        else if (TextUtils.isEmpty(passwordString))
        {
            password.setError(getResources().getString(R.string.field_required));
            focusView = password;
            cancel = true;
        }
        else if (TextUtils.isEmpty(confPasswordString))
        {
            confPassword.setError(getResources().getString(R.string.field_required));
            focusView = confPassword;
            cancel = true;
        }
        else if (!confPasswordString.equals(passwordString))
        {
            confPassword.setError(getResources().getString(R.string.password_not_match));
            focusView = confPassword;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            OtherUtils.hideKeyboard(RegistrationActivity.this);
            if (OtherUtils.isNetworkAvailable(context))
            {
                register.requestFocus();

                progressLL.setVisibility(View.VISIBLE);
                register.setVisibility(View.INVISIBLE);
            }
            else
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.internet_not_found), Toast.LENGTH_SHORT).show();
        }

    }


    public void onLogin(int result)
    {
        progressLL.setVisibility(View.INVISIBLE);
        register.setVisibility(View.VISIBLE);

        switch (result)
        {
            case 1:
                OtherUtils.showMessage(context,"Registration completed successfully.");
/*                Intent intent = new Intent(context,ActivityDiseaseDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                break;
            case 2:
                OtherUtils.showMessage(context,"Incorrect email and password.");
                break;
            case 3:
            case 4:
                OtherUtils.showMessage(context,"Possibly network issue.Please try again.");
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        switch (view.getId())
        {
            case R.id.name:
                                name.setError(null);
                                break;
    case R.id.registrationId:
                                registrationId.setError(null);
                                break;
                case R.id.age:
                                age.setError(null);
                                break;
        case R.id.email:
                                email.setError(null);
                                break;
        case R.id.password:
                                password.setError(null);
                                break;
        case R.id.confPassword:
                                confPassword.setError(null);
                                break;
        }
    }
}
