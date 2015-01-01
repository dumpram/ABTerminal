package com.github.dumpram.abt;


import java.io.IOException;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.github.dumpram.abt.adapter.*;
import com.github.dumpram.abt.util.bluetooth.ConnectionHandler;
import com.github.dumpram.abt.util.bluetooth.exceptions.DeviceNotConnectedException;

/**
 * Activity displays list of connectable devices. By clicking device user connects to it. 
 * @author Ivan Pavic
 *
 */
public class ConnectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Choose device and establish connection");
		setContentView(R.layout.activity_connect);
		ListView pairedDevicesListView = (ListView) findViewById(R.id.connect_list);	
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		final BluetoothDeviceAdapter deviceAdapter = new BluetoothDeviceAdapter(getApplicationContext(), 0);
		for (BluetoothDevice i : pairedDevices) {
			deviceAdapter.add(i);
		}
		
		pairedDevicesListView.setAdapter(deviceAdapter);
		pairedDevicesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BluetoothDevice device = deviceAdapter.getItem(position);
				try {
					ConnectionHandler.getInstance().connect(device);
					Toast.makeText(getApplicationContext(), "Connected to " + device.getName(), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't connect! Make sure that device is in range!", Toast.LENGTH_LONG).show();
				} catch (DeviceNotConnectedException e) {
					Toast.makeText(getApplicationContext(), "Couldn't connect! Make sure that device is in range!", Toast.LENGTH_LONG).show();
				} 
				finish();
			} 
		});
	}
}
