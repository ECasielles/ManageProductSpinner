package com.example.usuario.manageproductsrecycler.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.usuario.manageproductsrecycler.R;
import com.example.usuario.manageproductsrecycler.interfaces.IValidateUser;
import com.example.usuario.manageproductsrecycler.legacy.ProductsActivityRecycler;
import com.example.usuario.manageproductsrecycler.presenter.SignupPresenter;

public class SignupActivity extends AppCompatActivity implements IValidateUser.View {

    private Spinner spnCounty;
    private Spinner spnCity;
    private Button btnSignup;
    private RadioGroup typeClient;
    private TextInputLayout tilNameCompany;
    private EditText edtUser;
    private EditText edtPwd;
    private EditText edtEmail;
    private ViewGroup parentLayout;

    private AdapterView.OnItemSelectedListener spinnerListener; //Atributo delegado

    private SignupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        parentLayout = (RelativeLayout) findViewById(R.id.activity_signup);

        spnCounty = (Spinner) findViewById(R.id.spnProvincia);
        spnCity = (Spinner) findViewById(R.id.spnLocalidad);
        tilNameCompany = (TextInputLayout) findViewById(R.id.tilCompany);
        typeClient = (RadioGroup) findViewById(R.id.rgpIsCompany);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        edtUser = (EditText) findViewById(R.id.edtUsername);
        edtPwd = (EditText) findViewById(R.id.edtUserPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        presenter = new SignupPresenter(this);

        initRadioClient();
        loadSpinnerCounty();

        Toast.makeText(this, "kk", Toast.LENGTH_LONG).show();
    }

    public void signup(View view) {
        // Recoge los datos de la vista (EN CASA)
        // Llama al método del presentador
        presenter.validateCredentials(
                edtUser.getText().toString(),
                edtPwd.getText().toString(),
                edtEmail.getText().toString()
        );
    }

    private void showCompany(boolean b) {
        tilNameCompany.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void initRadioClient() {
        typeClient.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtClient:
                        showCompany(false);
                        break;
                    case R.id.rbtCompany:
                        showCompany(true);
                        break;
                }
            }
        });
    }

    // Loads both spinners, starting with the counties/provinces drop down list
    private void loadSpinnerCounty() {
        // Le pasamos CharSequence para poder manejar StringBuilder, etc.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignupActivity.this,
                R.array.array_provincia_a_localidades, android.R.layout.simple_spinner_item);
        spnCounty.setAdapter(adapter);

        spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (adapterView.getId()) {
                    case R.id.spnProvincia:
                        loadSpinnerCity(position);
                        break;
                    case R.id.spnLocalidad:
                        showSelectedCity();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spnCounty.setOnItemSelectedListener(spinnerListener);
        spnCity.setOnItemSelectedListener(spinnerListener);
    }

    private void loadSpinnerCity(int position) {
        // Inicializa el Spinner Localidades
        TypedArray typedCity = getResources().obtainTypedArray(position);
        CharSequence[] arrayCities = typedCity.getTextArray(position);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,arrayCities);

        spnCounty.setAdapter(adapter);
    }

    /**
     * Shows selected city
     */
    public void showSelectedCity() {
        // Mensaje concatenado con la provincia y la localidad elegidas

        Toast.makeText(
                getApplicationContext(),
                getString(
                        R.string.messageCityCounty,
                        spnCounty.getSelectedItem().toString(),
                        spnCity.getSelectedItem().toString()
                ),
                Toast.LENGTH_LONG
        ).show();
    }

    /**
     * Method that shows a custom message in a {@link Snackbar} component
     * depending on error occurred during data validation.
     * @param nameResource Error message String name showed
     * Use {@link android.content.res.Resources#getIdentifier(String, String, String)} to get class id in R class.
     * @param viewId view id in which error will show
     */
    @Override
    public void setMessageError(String nameResource, int viewId) {
        //Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show();
        // We have to pick the resource whose name is that given as a parameter
        String errorMessage = getResources().getString(getResources().
                getIdentifier(nameResource, "string", getPackageName()));

        switch (viewId){
            case R.id.tilUsername:
                //tilUser.setError(errorMessage);
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tilUserPassword:
                //tilPassword.setError(errorMessage);
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tilEmail:
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public boolean validate(){
        //Cambia las preferencias
        // El método validar guarda también las preferencias
        // User
        // Password
        // AccountPreferences

        boolean isValid = true;

        EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        EditText edtUsername = (EditText) findViewById(R.id.edtUsername);
        EditText edtPwd = (EditText) findViewById(R.id.edtUserPassword);

        String email = edtEmail.getText().toString();
        String username = edtUsername.getText().toString();
        String pwd = edtPwd.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        //Switch con Snackbar

        return isValid;
    }

    public void startActivity() {
        Intent intent = new Intent(SignupActivity.this, ProductsActivityRecycler.class);
        startActivity(intent);
        // Closes this activity after validation
        finish();
    }

}
