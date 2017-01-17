package com.mmx.fsk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mmx.fsk.DecodeLive.DecodeLiveActivity;
import com.mmx.fsk.PCM.PCM;
import com.mmx.fsk.live8bit.live8bit;
import com.mmx.fsk.terminal.terminal;
import com.mmx.fsk.wav.wav;
import com.mmx.fsk.EncodeLiveMono.encodeLiveMono;
import com.mmx.fsk.EncodeLiveStereo.encodeLiveStereo;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bDecodeLive, bLive8Bit, bPCM, bTerminal, bWAV, bEncodeLiveMono, bEncodeLiveStereo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        listener();
    }
    private void initialize() {
        bDecodeLive = (Button) findViewById(R.id.bDecodeLive);
        bLive8Bit = (Button) findViewById(R.id.bLive8Bit);
        bPCM = (Button) findViewById(R.id.bPCM);
        bTerminal = (Button) findViewById(R.id.bTerminal);
        bWAV = (Button) findViewById(R.id.bWAV);
        bEncodeLiveMono = (Button) findViewById(R.id.bEncodeLiveMono);
        bEncodeLiveStereo = (Button) findViewById(R.id.bEncodeLiveStereo);
    }

    private void listener() {
        bDecodeLive.setOnClickListener(this);
        bLive8Bit.setOnClickListener(this);
        bPCM.setOnClickListener(this);
        bTerminal.setOnClickListener(this);
        bWAV.setOnClickListener(this);
        bEncodeLiveMono.setOnClickListener(this);
        bEncodeLiveStereo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.bDecodeLive){
            Intent intent = new Intent(this, DecodeLiveActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bLive8Bit){
            Intent intent = new Intent(this, live8bit.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bPCM){
            Intent intent = new Intent(this, PCM.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bTerminal){
            Intent intent = new Intent(this, terminal.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bWAV){
            Intent intent = new Intent(this, wav.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bEncodeLiveMono){
            Intent intent = new Intent(this, encodeLiveMono.class);
            startActivity(intent);
        }else if (v.getId() == R.id.bEncodeLiveStereo){
            Intent intent = new Intent(this, encodeLiveStereo.class);
            startActivity(intent);
        }
    }
}
