package com.github.dumpram.abt.util.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.abt.util.bluetooth.exceptions.DeviceNotConnectedException;

import android.bluetooth.BluetoothDevice;

public class ConnectionHandler {
	
	private BluetoothConnection connection;
	
	private List<ConnectionStateChangedListener> listeners = new ArrayList<ConnectionStateChangedListener>();
	
	private List<BluetoothConnectionListener> bListeners = new ArrayList<BluetoothConnectionListener>();
	
	private static ConnectionHandler instance = new ConnectionHandler();
	
	private ConnectionHandler() {
	
	}
	
	public static ConnectionHandler getInstance() {
		return instance;
	}
	
	public void disconnect() throws IOException {
		if (connection != null) {
			instance.connection.setStopped(true);
			instance.connection.getSocket().close();
			instance.connection = null;
			fireConnectionStateChanged();
		}
	}
	
	private void fireConnectionStateChanged() {
		for (ConnectionStateChangedListener i : listeners) {
			i.stateChanged();
		}
	}
	
	public void connect(BluetoothDevice remoteDevice) throws IOException, DeviceNotConnectedException {
		connection = new BluetoothConnection(remoteDevice, bListeners);
		if (!connection.getSocket().isConnected()) {
			connection = null;
			throw new DeviceNotConnectedException();
		}
		connection.start();
		fireConnectionStateChanged();
	}

	public void sendBytes(byte[] bytes) throws IOException {
		connection.write(bytes);
	}

	public boolean isConnected() {
		return (connection != null)? true : false;
	}

	public void addConnectionStateChangedListener(
			ConnectionStateChangedListener connectionStateChangedListener) {
		listeners.add(connectionStateChangedListener);		
	}

	public void addBluetoothConnectionListener(
			BluetoothConnectionListener bluetoothConnectionListener) {
		bListeners.add(bluetoothConnectionListener);
	}
	

}
