/**
 *   This file is part of the FSKModem java/android library for
 *   processing FSK audio signals.
 *
 *   The FSKModem library is developed by Ivan Ganev, CEO at
 *   Cytec BG Ltd.
 *
 *   Copyright (C) 2014  Cytec BG Ltd. office@cytec.bg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mmx.fsk.wav;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.mmx.fsk.R;
import com.mmx.fsk.fskmodem.FSKConfig;
import com.mmx.fsk.fskmodem.FSKDecoder;
import com.mmx.fsk.fskmodem.FSKDecoder.FSKDecoderCallback;
import com.mmx.fsk.wav.WavToPCM;
import com.mmx.fsk.wav.WavToPCM.WavInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;

public class wav extends AppCompatActivity {

	protected FSKConfig mConfig;
	protected FSKDecoder mDecoder;

	protected Runnable mPCMFeeder = new Runnable() {

		@Override
		public void run() {
			try {
				//open input stream to the WAV file
				InputStream input = getResources().getAssets().open("fsk.wav");

				//get information about the WAV file
				WavInfo info = WavToPCM.readHeader(input);

				//get the raw PCM data
				ByteBuffer pcm = ByteBuffer.wrap(WavToPCM.readWavPcm(info, input));

				//the decoder has 1 second buffer (equals to sample rate),
				//so we have to fragment the entire file,
				//to prevent buffer overflow or rejection
				byte[] buffer = new byte[1024];

				//feed signal little by little... another way to do that is to
				//check the returning value of appendSignal(), it returns the
				//remaining space in the decoder signal buffer
				while (pcm.hasRemaining()) {

					if (pcm.remaining() > 1024) {
						pcm.get(buffer);
					}
					else {
						buffer = new byte[pcm.remaining()];

						pcm.get(buffer);
					}

					mDecoder.appendSignal(buffer);

					Thread.sleep(100);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_wav);

		/// INIT FSK CONFIG

		try {
			mConfig = new FSKConfig(FSKConfig.SAMPLE_RATE_29400, FSKConfig.PCM_8BIT, FSKConfig.CHANNELS_MONO, FSKConfig.SOFT_MODEM_MODE_4, FSKConfig.THRESHOLD_20P);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/// INIT FSK DECODER

		mDecoder = new FSKDecoder(mConfig, new FSKDecoderCallback() {

			@Override
			public void decoded(byte[] newData) {

				final String text = new String(newData);

				runOnUiThread(new Runnable() {
					public void run() {

						TextView view = ((TextView) findViewById(R.id.result));

						view.setText(view.getText()+text);
					}
				});
			}
		});

		///

		new Thread(mPCMFeeder).start();
	}

	@Override
	protected void onDestroy() {

		mDecoder.stop();

		super.onDestroy();
	}

}
