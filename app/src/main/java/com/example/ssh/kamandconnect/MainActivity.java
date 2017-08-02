package com.example.ssh.kamandconnect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by hitman on 02/08/17.
 */

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;
    private Spinner mHostelSpinner;
    private Spinner mStreamSpinner;
    private EditText mRoomNoEditText;
    private LinearLayout mHostelRoomNoLinearLayout;
    private RadioGroup mSelectionBasisRadioGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean loggedIn = sharedPreferences.getBoolean(getString(R.string.logged_in), false);
        if(loggedIn)
            Log.d("log", "true");
        else
            Log.d("log", "false");

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        mHostelSpinner = (Spinner) findViewById(R.id.hostel_spinner);
        mRoomNoEditText = (EditText) findViewById(R.id.room_no_edit_text);
        mHostelRoomNoLinearLayout = (LinearLayout) findViewById(R.id.hostel_room_layout);
        mSelectionBasisRadioGroup = (RadioGroup) findViewById(R.id.radio_selection_basis);
        mStreamSpinner = (Spinner) findViewById(R.id.stream_spinner);

        mSelectionBasisRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton rb = (RadioButton) findViewById(i);
                String searchBasis = rb.getText().toString();
                Log.d("basis", searchBasis);
                switch (searchBasis) {
                    case "Hostel and Room no":
                        mHostelRoomNoLinearLayout.setVisibility(View.VISIBLE);
                        mSearchEditText.setVisibility(View.GONE);
                        mStreamSpinner.setVisibility(View.GONE);
                        break;
                    case "Stream":
                        mStreamSpinner.setVisibility(View.VISIBLE);
                        mHostelRoomNoLinearLayout.setVisibility(View.GONE);
                        mSearchEditText.setVisibility(View.GONE);
                        break;
                    default:
                        mSearchEditText.setVisibility(View.VISIBLE);
                        mSearchEditText.setHint("Enter " + searchBasis);
                        mStreamSpinner.setVisibility(View.GONE);
                        mHostelRoomNoLinearLayout.setVisibility(View.GONE);
                }
            }
        });

    }
}
