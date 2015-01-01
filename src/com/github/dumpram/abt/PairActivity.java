package com.github.dumpram.abt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.github.dumpram.abt.adapter.BluetoothDeviceAdapter;

/**
 * Activity displays list of pairable devices.
 * @author Ivan Pavic
 *
 */
public class PairActivity extends Activity {

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Pair device");
		setContentView(R.layout.activity_pair);
		final BluetoothDeviceAdapter mArrayAdapter = new BluetoothDeviceAdapter(getApplicationContext(), 0);
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					mArrayAdapter.add(device);
					mArrayAdapter.notifyDataSetChanged();
				}
			}
		};
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		adapter.startDiscovery();
		ListView discoveredDevices = (ListView) findViewById(R.id.pair_list);
		discoveredDevices.setAdapter(mArrayAdapter);
		discoveredDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!mArrayAdapter.getItem(position).createBond()) {
					Toast.makeText(getApplicationContext(), "Devices already bonded!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Devices paired successfully!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
