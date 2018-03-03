package com.yaamani.satellitesimulation;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yaamani.satellitesimulation.SatelliteSimulationGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SatelliteSimulationGame(), config);

		config.hideStatusBar = true;
		config.useImmersiveMode = true;
	}
}
