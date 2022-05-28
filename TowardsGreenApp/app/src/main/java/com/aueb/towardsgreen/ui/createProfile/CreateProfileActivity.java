package com.aueb.towardsgreen.ui.createProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.domain.Profile;

public class CreateProfileActivity extends AppCompatActivity implements CreateProfileView{

    EditText firstName,lastName,email,passwordInput,passwordConf;
    Button btnSubmit, btnCancel;

    private CreateProfilePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        presenter = new CreateProfilePresenter(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        passwordConf = findViewById(R.id.confirmPas);

        btnCancel = findViewById(R.id.cancel);
        btnSubmit = findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile createdProfile = new Profile();
                createdProfile.setFullName(firstName.getText().toString()+lastName.getText().toString());
                createdProfile.generateUserId();

                //TODO: save to dao and continue to main screen
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: return to login screen
            }
        });
    }

    //=====GETTERS SETTERS================
    public EditText getFirstName() {
        return firstName;
    }

    public void setFirstName(EditText firstName) {
        this.firstName = firstName;
    }

    public EditText getLastName() {
        return lastName;
    }

    public void setLastName(EditText lastName) {
        this.lastName = lastName;
    }

    public EditText getEmail() {
        return email;
    }

    public void setEmail(EditText email) {
        this.email = email;
    }

    public EditText getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(EditText passwordInput) {
        this.passwordInput = passwordInput;
    }

    public EditText getPasswordConf() {
        return passwordConf;
    }

    public void setPasswordConf(EditText passwordConf) {
        this.passwordConf = passwordConf;
    }
    //====================================


}