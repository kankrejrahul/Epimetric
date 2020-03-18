package com.epimetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epimetrics.helper.OtherUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener
{
    Context context = LoginActivity.this;
    Toolbar toolbar;

    TextView login,resetPassword,signup;
    EditText password,email;
    String emailString,passwordString;
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
        login = findViewById(R.id.login);
        resetPassword = findViewById(R.id.resetPassword);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signup = findViewById(R.id.signup);

        progressLL = findViewById(R.id.progressLL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        setUpToolbar();

        login.setOnClickListener(this);
        resetPassword.setOnClickListener(this);
        signup.setOnClickListener(this);

        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login:
                                validateAndLogin();
                                break;
        case R.id.resetPassword:

                                break;
            case R.id.signup:
                                startActivity(new Intent(context,RegistrationActivity.class));
                                break;

        }
    }

    void validateAndLogin()
    {
        email.setError(null);
        password.setError(null);

        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(emailString))
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
        if (TextUtils.isEmpty(passwordString))
        {
            password.setError(getResources().getString(R.string.field_required));
            focusView = password;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            OtherUtils.hideKeyboard(LoginActivity.this);
            if (OtherUtils.isNetworkAvailable(context))
            {
                progressLL.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
//                APIHandler.login(context,emailString,passwordString);
            }
            else
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.internet_not_found), Toast.LENGTH_SHORT).show();
        }

    }


    public void onLogin(int result)
    {
        progressLL.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);

        switch (result)
        {
            case 1:
                OtherUtils.showMessage(context,getString(R.string.text_login_success));
                Intent intent = new Intent(context,DiseaseDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
            case R.id.email:
                email.setError(null);
                break;
            case R.id.password:
                password.setError(null);
                break;
        }
    }
}
