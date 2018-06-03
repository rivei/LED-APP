package com.led.led;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnOn, btnOff, btnDis;
    SeekBar brightnessR;
    SeekBar brightnessG;
    SeekBar brightnessB;
    String strR;
    String strG;
    String strB;
    int afa;
    int hexR;
    int hexG;
    int hexB;
    int intRGB;
    String strRGB;
    TextView lumnR;
    TextView lumnG;
    TextView lumnB;
    TextView colorLED;
    String address = null;
    Integer tmp;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
        btnOn = (Button)findViewById(R.id.btnON);
        btnOff = (Button)findViewById(R.id.btnOFF);
        btnDis = (Button)findViewById(R.id.button4);
        brightnessR = (SeekBar)findViewById(R.id.seekBarR);
        brightnessG = (SeekBar)findViewById(R.id.seekBarG);
        brightnessB = (SeekBar)findViewById(R.id.seekBarB);
        lumnR = (TextView)findViewById(R.id.lumnR);
        lumnG = (TextView)findViewById(R.id.lumnG);
        lumnB = (TextView)findViewById(R.id.lumnB);
        colorLED = (TextView)findViewById(R.id.colorLED);
        afa = 50;
        hexR = 50;
        hexG = 50;
        hexB = 50;

        strR = "050";
        strG = "050";
        strB = "050";
        strRGB = strR + strG + strB;

        final ImageView changeColor = (ImageView)findViewById(R.id.myLED);
        intRGB = Color.argb(255,0,0,0);
        changeColor.setColorFilter(intRGB);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed();      //method to turn on
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed();   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        brightnessR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    lumnR.setText(String.valueOf(progress));
                    if(progress < 100){
                        if (progress <10 ){
                            strR = "00" + Integer.toString(progress);
                        }
                        else {
                            strR = "0" + Integer.toString(progress);
                        }
                    }
                    else {
                        strR = Integer.toString(progress);
                    }
                    strRGB = strR + strG + strB;
                    hexR = progress;
                    afa = (int)((hexR+hexG+hexB)/3);
                    if (afa == 0) afa = 255;
                    intRGB = Color.argb(afa, hexR, hexG, hexB);
                    colorLED.setText("RGB: #" + Integer.toHexString(intRGB).substring(2));
                    try
                    {
                        btSocket.getOutputStream().write(strRGB.getBytes());
                        changeColor.setColorFilter(intRGB);
                    }
                    catch (IOException e)
                    {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightnessG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    lumnG.setText(String.valueOf(progress));
                    if(progress < 100){
                        if(progress < 10){
                            strG = "00" + Integer.toString(progress);
                        }
                        else {
                            strG = "0" + Integer.toString(progress);
                        }
                    }
                    else {
                        strG = Integer.toString(progress);
                    }
                    strRGB = strR + strG + strB;
                    hexG = progress;
                    afa = (int)((hexR+hexG+hexB)/3);
                    if (afa == 0) afa = 255;
                    intRGB = Color.argb(afa, hexR, hexG, hexB);
                    colorLED.setText("RGB: #" + Integer.toHexString(intRGB).substring(2));
                    try
                    {
                        btSocket.getOutputStream().write(strRGB.getBytes());
                        changeColor.setColorFilter(intRGB);
                    }
                    catch (IOException e)
                    {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightnessB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    lumnB.setText(String.valueOf(progress));
                    if(progress < 100){
                        if (progress <10 ){
                            strB = "00" + Integer.toString(progress);
                        }
                        else {
                            strB = "0" + Integer.toString(progress);
                        }
                    }
                    else {
                        strB = Integer.toString(progress);
                    }
                    strRGB = strR + strG + strB;
                    hexB = progress;
                    afa = (int)((hexR+hexG+hexB)/3);
                    if (afa == 0) afa = 255;
                    intRGB = Color.argb(afa, hexR, hexG, hexB);
                    colorLED.setText("RGB: #" + Integer.toHexString(intRGB).substring(2));
                    try
                    {
                        btSocket.getOutputStream().write(strRGB.getBytes());
                        changeColor.setColorFilter(intRGB);
                    }
                    catch (IOException e)
                    {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                final ImageView changeColor = (ImageView)findViewById(R.id.myLED);
                intRGB = Color.parseColor("#FF000000");
                changeColor.setColorFilter(intRGB);
                btSocket.getOutputStream().write("TF".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TO".toString().getBytes());
                //btSocket.getOutputStream().write(";".toString().getBytes());
                //btSocket.getOutputStream().write(strRGB.getBytes());
                intRGB = Color.argb(afa, hexR, hexG, hexB);
                ImageView changeColor = (ImageView)findViewById(R.id.myLED);
                changeColor.setColorFilter(intRGB);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
