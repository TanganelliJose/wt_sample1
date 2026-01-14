package com.example.wt_sample1.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt_sample1.MainActivity;
import com.example.wt_sample1.NetworkUtils;
import com.example.wt_sample1.R;
import com.example.wt_sample1.ui.login.LoginViewModel;
import com.example.wt_sample1.ui.login.LoginViewModelFactory;
import com.example.wt_sample1.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    public static ArrayList<String> serviceList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceList = new ArrayList<>();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        usernameEditText.setText("adm");
        passwordEditText.setText("12345678");
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((usernameEditText.getText().toString().equals("adm") || usernameEditText.getText().toString().equals("user01")) && passwordEditText.getText().toString().equals("12345678")) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    // CHANGE 'this' TO 'LoginActivity.this' BELOW
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Also good practice to use LoginActivity.this here if 'this' causes issues in Toast
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceList.clear();
        loadServices();
    }

    private void loadServices(){
        try {
            getServicesData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getServicesData() throws IOException {
        URL url= NetworkUtils.buildUrl();
        new DataTask().execute(url);
    }
    public class DataTask extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String items= null;
            try {
                items = NetworkUtils.getDatafromHttpUrl(url,"0");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPostExecute(String s) {
            //textView.setText(s);
            setServiceData(s);
        }
        public void setServiceData(String items){
            //textView.setText(items);
            JSONObject myObject=null;
            try {
                myObject = new JSONObject(items);
                JSONArray serviceJsonArray = myObject.getJSONArray("items");
                if (serviceJsonArray.length()>1) { // para limpar e carregar novamente se tiver internet
                    for (int i = 0; i < serviceJsonArray.length(); i++) {
                        JSONObject serviceJsonObject = serviceJsonArray.getJSONObject(i);

                        //questionarioListCod.add(pesquisaNameJsonObject.get("cod_pesquisa").toString());
                        serviceList.add(serviceJsonObject.get("service_id").toString()+"\n"+
                                        serviceJsonObject.get("asset_id").toString());
                        //serviceList.add("serviceJsonObject.get.");
                    }
                }
                //tvResult.setText( String.valueOf(usersList.size()) );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}