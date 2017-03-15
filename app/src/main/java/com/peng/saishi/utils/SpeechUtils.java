package com.peng.saishi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;

import android.content.Context;

public class SpeechUtils {
	public static byte[] readFileFromSDcard(Context context, String filepath) {
		byte[] buffer = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(filepath);
			buffer = new byte[in.available()];
			in.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}

	public static ArrayList<byte[]> splitBuffer(byte[] buffer, int length, int spsize) {
		ArrayList<byte[]> array = new ArrayList<byte[]>();
		if (spsize <= 0 || length <= 0 || buffer == null || buffer.length < length)
			return array;
		int size = 0;
		while (size < length) {
			int left = length - size;
			if (spsize < left) {
				byte[] sdata = new byte[spsize];
				System.arraycopy(buffer, size, sdata, 0, spsize);
				array.add(sdata);
				size += spsize;
			} else {
				byte[] sdata = new byte[left];
				System.arraycopy(buffer, size, sdata, 0, left);
				array.add(sdata);
				size += left;
			}
		}
		return array;
	}

	public static void writeaudio(final ArrayList<byte[]> buffers, final SpeechRecognizer mSpeech,
			final RecognizerListener listner) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mSpeech.setParameter(SpeechConstant.DOMAIN, "iat");
				mSpeech.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
				mSpeech.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
				// 设置多个候选结果
				mSpeech.setParameter(SpeechConstant.ASR_NBEST, "3");
				mSpeech.setParameter(SpeechConstant.ASR_WBEST, "3");
				mSpeech.startListening(listner);
				for (int i = 0; i < buffers.size(); i++) {
					try {
						mSpeech.writeAudio(buffers.get(i), 0, buffers.get(i).length);
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mSpeech.stopListening();
			}

		}).start();
	}

}
