package com.github.dumpram.abt;
import java.io.IOException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dumpram.abt.util.bluetooth.BluetoothConnectionListener;
import com.github.dumpram.abt.util.bluetooth.ConnectionHandler;
import com.github.dumpram.abt.util.bluetooth.ConnectionStateChangedListener;
import com.github.dumpram.abt.util.listeners.AlternateFunctionListener;


/**
 * Main Activity in application. Contains terminal, action buttons
 * which change Activity.
 *
 * @author Ivan Paviæ
 */
public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startup);       
        
        setTitle("ABTerminal v 1.0");
        initGUI();
    }
    
    /**
     * Method prompts user to enable bluetooth adapter if available.
     */
    private void initBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    	if (adapter == null) {
    		Toast.makeText(getApplicationContext(), "No bluetooth adapter available!", Toast.LENGTH_LONG).show();
    		return;
    	}
        while (!adapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}
		
	}
    /**
     * Method initializes GUI.
     */
	private void initGUI() {
		final Button leftButton = (Button) findViewById(R.id.left_button);
		final Button centerButton = (Button) findViewById(R.id.center_button);
		final Button helpButton = (Button) findViewById(R.id.help_button);
		final TextView terminalRx = (TextView) findViewById(R.id.terminal_rx);
		final EditText terminalTx = (EditText) findViewById(R.id.terminal_tx);
		
		
		terminalRx.setMovementMethod(new ScrollingMovementMethod());
		ConnectionHandler.getInstance().addBluetoothConnectionListener(new BluetoothConnectionListener() {
			
			@Override
			public void dataReceived(final byte[] data) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						terminalRx.append(new String(data));
						
					}
				});
			}
		});
		
		ConnectionHandler.getInstance().addConnectionStateChangedListener(new ConnectionStateChangedListener() {
			
			@Override 
			public void stateChanged() {
				boolean isConnected = ConnectionHandler.getInstance().isConnected();
				leftButton.setText((isConnected)? "Send" : "Pair device");
				centerButton.setText((isConnected)? "Disconnect" : "Connect device");
				helpButton.setText((isConnected)? "Clear" : "Help");
				terminalRx.setText("");
			}
		});
		
		leftButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				try {
					ConnectionHandler.getInstance().sendBytes(terminalTx.getText().toString().getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				terminalTx.setText("");
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, PairActivity.class));
			}
		});
		
		centerButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				try {
					ConnectionHandler.getInstance().disconnect();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "This shouldn't happen!", Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, ConnectActivity.class));
			}
		});
		
		helpButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				terminalRx.setText("");
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, HelpActivity.class));
			}
		});
		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		initBluetooth();
	}
}
