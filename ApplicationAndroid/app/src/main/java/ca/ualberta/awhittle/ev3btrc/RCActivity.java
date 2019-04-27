package ca.ualberta.awhittle.ev3btrc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RCActivity extends AppCompatActivity {

    BT_Comm btComm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc);
        btComm = new BT_Comm();

        // To notify user for permission to enable bt, if needed
        AlertDialog.Builder builder = new AlertDialog.Builder(RCActivity.this);

        builder.setMessage(R.string.bt_permission_request);
        builder.setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                btComm.setBtPermission(true);
                btComm.reply();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                btComm.setBtPermission(false);
                btComm.reply();
            }
        });

        // Create the AlertDialog
        AlertDialog btPermissionAlert = builder.create();

        Context context = getApplicationContext();
        //CharSequence text1 = getString(R.string.bt_enabled);
        CharSequence text1 = getString(R.string.bt_disabled);
        CharSequence text2 = getString(R.string.bt_failed);


        Toast btDisabledToast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
        Toast btFailedToast = Toast.makeText(context, text2, Toast.LENGTH_LONG);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);

        //if(btComm.enableBT(btPermissionAlert, btEnabledToast)){
        //}
        if(!btComm.initBT()){
            // User did not enable Bluetooth
            btDisabledToast.show();
            Intent intent = new Intent(RCActivity.this, ConnectActivity.class);
            startActivity(intent);
        }

        if(!btComm.connectToEV3(sharedpreferences.getString(getString(R.string.EV3KEY), ""))){
            //Cannot connect to given mac address, return to connect activity
            btFailedToast.show();
            Intent intent = new Intent(RCActivity.this, ConnectActivity.class);
            startActivity(intent);
        }


        final Button buttonOuverturePartielle = (Button) findViewById(R.id.buttonOuverturePartielle);
        buttonOuverturePartielle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    btComm.writeMessage((byte) 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button buttonOuvertureTotale = (Button) findViewById(R.id.buttonOuvertureTotale);
        buttonOuvertureTotale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    btComm.writeMessage((byte) 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    btComm.writeMessage((byte) 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
