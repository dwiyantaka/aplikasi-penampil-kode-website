package com.example.wildanifqie.aplikasipenampilkode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    TextView txtView;
    EditText edtText;
    Spinner spinn;
    ArrayAdapter<CharSequence> list_sp;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = findViewById(R.id.ViewText);
        edtText = findViewById(R.id.txtUrl);
        spinn = findViewById(R.id.spinner);

        list_sp = ArrayAdapter.createFromResource(this, R.array.list_spin,android.R.layout.simple_spinner_item);
        list_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinn.setAdapter(list_sp);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void getSource(View view){
        String getSpin, getUrl, link;
        getSpin = spinn.getSelectedItem().toString(); //Mengambil spin untuk menjadi string
        getUrl = edtText.getText().toString(); //Mengambil edittext untuk menjadi string

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


        if(!getUrl.isEmpty()){ //Kondisi EditText tidak kosong
            if(getUrl.contains(".") && !(getUrl.contains(" "))){ //cek input url mengandung . dan tidak mengandung spasi
                if(checkConnection()){ //cek koneksi internet
                    txtView.setText("Loading....");
                    txtView.setTextSize(15);

                    link = getSpin + getUrl;

                    Bundle bundle = new Bundle();
                    bundle.putString("link_pros", link);
                    getSupportLoaderManager().restartLoader(0, bundle, this);
                }
                else{
                    Toast.makeText(this,"Check Your Internet Connection and Try Again",Toast.LENGTH_SHORT).show();
                    txtView.setText("Check Your Internet Connection and Try Again");
                    txtView.setTextSize(25);
                }
            }
            else{
                Toast.makeText(this,"Invalid Input URL",Toast.LENGTH_SHORT).show();
                txtView.setText("Invalid Input URL");
                txtView.setTextSize(25);
            }
        }
        else{
            Toast.makeText(this,"Please Input URL",Toast.LENGTH_SHORT).show();
            txtView.setText("URL is empty, please input URL");
            txtView.setTextSize(25);
        }
    }


    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ConnectInternetTask(this, args.getString("link_pros"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) { txtView.setText(data);}

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan Sekali Lagi", Toast.LENGTH_SHORT).show();
    }

}
