package com.myfirstapp.firebase_java_test;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myfirstapp.firebase_java_test.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String CelestialBody;
    FirebaseDatabase db;
    DatabaseReference reference;

    Switch switchPlanetsOnly;

    // Assuming your button has an ID named "buttonSwitchPage"
//    Button buttonSwitchPage = findViewById(R.id.login);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        switchPlanetsOnly = findViewById(R.id.planets_only);

        switchPlanetsOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateAutoCompleteList(isChecked);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("USERNAME");
            if (username != null) {
                // Assuming you have a TextView with the id greetingTextView
                TextView greetingTextView = findViewById(R.id.greetingTextView);
                greetingTextView.setText("Hello, " + username);
            }
        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CelestialBody = binding.autoCompleteTextView.getText().toString();

                if(!CelestialBody.isEmpty()){
                    Celestial celestial = new Celestial(CelestialBody);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Planet");
                    reference.setValue(CelestialBody).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            binding.autoCompleteTextView.setText("");
                            Toast.makeText(MainActivity.this, "Succesfully updated", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Handle onResume functionality here if needed
        String[] celestial = getResources().getStringArray(R.array.celestial);
        String[] planets_only = getResources().getStringArray(R.array.planets_only);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item,celestial);
        binding.autoCompleteTextView.setAdapter(arrayAdapter);
    }

    private void updateAutoCompleteList(boolean planetsOnly) {
        String[] celestialArray;
        if (planetsOnly) {
            celestialArray = getResources().getStringArray(R.array.planets_only);
        } else {
            celestialArray = getResources().getStringArray(R.array.celestial);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, celestialArray);
        binding.autoCompleteTextView.setAdapter(arrayAdapter);
    }
}
