package com.example.wt_sample1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt_sample1.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wt_sample1.databinding.ActivityMainBinding;
import com.example.wt_sample1.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public ListView listView;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = (ListView) findViewById(R.id.ListView);
        textView = (TextView) findViewById(R.id.TextView);


        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, LoginActivity.serviceList);
        listView.setAdapter(arrayAdapter);
        //textView.setText("This application is a prototype. All information presented is fictional.");
        // Add the listener here
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedServiceId = (String) parent.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, "Service Selected: " + selectedServiceId, Toast.LENGTH_SHORT).show();
                //textView.setText(selectedServiceId);

                // Next step: Call your update dialog logic using 'selectedServiceId'
                // Call the dialog logic
                showUpdateDialog(selectedServiceId);

            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
    }

    private void showUpdateDialog(final String serviceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Service Notes");
        builder.setMessage("Enter new notes for Service ID: " + serviceId);

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter notes here...");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newNotes = input.getText().toString();
            if (!newNotes.isEmpty()) {
                String selectedServiceId = serviceId.substring(0, 3);

                updateServiceNotesTask(selectedServiceId, newNotes);
            } else {
                Toast.makeText(MainActivity.this, "Notes cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateServiceNotesTask(String id, String notes) {
        // Build the URL with parameters as requested
        // Base: https://oracleapex.com/ords/tanganelli/servicerecords/serviceput?id=...&servicenotes=...
        String urlString = "https://oracleapex.com/ords/tanganelli/servicerecords/serviceput?id=" + id +
                "&servicenotes=" + android.net.Uri.encode(notes);
        new PutDataTask().execute(urlString);
    }

    // AsyncTask to handle the PUT/GET request for updating
    private class PutDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                // Reusing your NetworkUtils pattern (assuming it handles connection.connect())
                return NetworkUtils.putDatatoHttpUrl(url, "0");
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, "Update Request Sent", Toast.LENGTH_SHORT).show();
            // Optional: Refresh data by returning to LoginActivity or calling loadServices again
        }
    }


}