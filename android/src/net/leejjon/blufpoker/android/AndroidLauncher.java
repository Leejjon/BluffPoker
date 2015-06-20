package net.leejjon.blufpoker.android;

import net.leejjon.blufpoker.BlufPokerGame;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		int zoomfactor = 0;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_MEDIUM: 
			zoomfactor = 1;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomfactor = 2;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			zoomfactor = 3;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			zoomfactor = 4;
			break;
		case DisplayMetrics.DENSITY_TV:
			zoomfactor = 8;
		}
		
		initialize(new BlufPokerGame(zoomfactor), config);
	}
}
