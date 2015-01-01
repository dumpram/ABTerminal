package com.github.dumpram.abt;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity displays help, how to use instructions.
 * @author Ivan Pavic
 *
 */
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setTitle("Help");
	}
}
