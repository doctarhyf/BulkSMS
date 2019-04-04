package com.example.bulksms;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BSMS";
    EditText etSMS;
    TextView tvCharCount;
    int charTot = 140;
    String smsCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSMS = findViewById(R.id.etSMSCont);
        tvCharCount = findViewById(R.id.tvCharCount);

        tvCharCount.setText("0/" + charTot);

        etSMS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                smsCont = etSMS.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = etSMS.length();
                tvCharCount.setText(count + "/" + charTot);

                if(count > charTot) etSMS.setText(smsCont);
            }
        });
    }

    public void onClearSMSClicked(View view) {
        clearSMS();

    }

    public void onSendSMSClicked(View view) {

        sendSMS();
    }

    private void clearSMS(){

        etSMS.setText("");

    }

    private void sendSMS(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to send your SMS?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                sendMessageToServer();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

    private void sendMessageToServer() {

        Log.e(TAG, "sendMessageToServer: -> " + etSMS.getText().toString() );


        String url = "http://localhost/bulksms/index.php?msg=" + etSMS.getText().toString();

        //Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            onSendSMSError(s);
                        }else{
                            onSendSuccess(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        onSendSMSError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void onSendSuccess(String s) {
        Log.e(TAG, "onSendSuccess: -> " + s );
    }

    private void onSendSMSError(String s) {
        Log.e(TAG, "onSendSMSError: -> " + s );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}

