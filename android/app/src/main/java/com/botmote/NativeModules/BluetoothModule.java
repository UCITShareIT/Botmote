package com.botmote.NativeModules;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Mike on 3/7/16.
 */
public class BluetoothModule extends ReactContextBaseJavaModule {

    Bluetooth blt;
    public static final int WRITEMODULE = 2;
    private static String LOG_TAG = BluetoothModule.class.toString();
    private Handler mLeHandler;
    // Create the Handler object (on the main thread by default)
    Handler handler;
    private Runnable runnableCode;
    public static final int DEV_JOYSTICK = 5;
    public static final int MSG_VALUECHANGED = 0x10;
    private int motorSpeed = 180;

    private ReactApplicationContext mContext;
    private Handler mStopHandler;
    private Runnable mStopRunnable=new Runnable() {
        @Override
        public void run() {
            byte[] wr = buildJoystickWrite(DEV_JOYSTICK, 0, 0);
            mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
        }
    };
    public BluetoothModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        mContext = reactApplicationContext;
    }

    @ReactMethod
    public void start() {
        Log.i(LOG_TAG, "Start scanning");
        prepareBluetoothStack();
        if (blt == null) {
            handler = new Handler(Looper.getMainLooper());
            mStopHandler = new Handler();
            BluetoothLE.sharedManager().start();
            //handler.post(runnableCode);
        }
    }

    @ReactMethod
    public void connectDevice(int position) {
        Log.i(LOG_TAG, "Asked to connect to device at position: " + position);
        try{ // the bluetooth list may vary
            if(blt!=null){
                BluetoothDevice dev = blt.btDevices.get(position);
                if(blt.connDev!=null && blt.connDev.equals(dev)){
                    // disconnect device
                    blt.bluetoothDisconnect(blt.connDev);
                    return;
                }
                blt.bluetoothConnect(dev);
            }else{
                if(BluetoothLE.sharedManager().isConnected()){
                    BluetoothLE.sharedManager().close();
                }else{
                    final WritableMap params = Arguments.createMap();
                    params.putBoolean("status", BluetoothLE.sharedManager().selectDevice(position));
                    sendEvent(mContext, "deviceConnection", params);
                }
            }
        }catch(Exception e){
            Log.d(LOG_TAG, e.toString());
        }
    }

    @ReactMethod
    public void moveUp() {
        byte[] wr = buildJoystickWrite(DEV_JOYSTICK, motorSpeed, motorSpeed);
        mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
    }

    @ReactMethod
    public void moveDown() {
        byte[] wr = buildJoystickWrite(DEV_JOYSTICK, -motorSpeed, -motorSpeed);
        mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
    }

    @ReactMethod
    public void moveLeft() {
        byte[] wr = buildJoystickWrite(DEV_JOYSTICK, -motorSpeed, motorSpeed);
        mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
    }

    @ReactMethod
    public void moveRight() {
        byte[] wr = buildJoystickWrite(DEV_JOYSTICK, motorSpeed, -motorSpeed);
        mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
    }

    @ReactMethod
    public void stopMoving() {
        byte[] wr = buildJoystickWrite(DEV_JOYSTICK, 0, 0);
        mLeHandler.obtainMessage(MSG_VALUECHANGED,wr).sendToTarget();
        mStopHandler.postDelayed(mStopRunnable, 150);
    }

    static public byte[] buildJoystickWrite(int type,int leftSpeed,int rightSpeed){
        byte[] cmd = new byte[13];
        cmd[0]=(byte) 0xff;
        cmd[1]=(byte) 0x55;
        cmd[2]=(byte) 8;
        cmd[3]=(byte) 0;
        cmd[4]=(byte) WRITEMODULE;
        cmd[5]=(byte) type;
        final ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putShort((short)leftSpeed);
        buf.putShort((short)rightSpeed);
        buf.position(0);
        // Read back bytes
        final byte b1 = buf.get();
        final byte b2 = buf.get();
        final byte b3 = buf.get();
        final byte b4 = buf.get();
        cmd[6] = b2;
        cmd[7] = b1;
        cmd[8] = b4;
        cmd[9] = b3;
        cmd[10]=(byte) '\n';
        return cmd;
    }

    @Override
    public String getName() {
        return "BluetoothLE";
    }

    private void sendEvent(final ReactContext reactContext, final String eventName, @Nullable final WritableMap params) {
        runnableCode = new Runnable() {
            @Override
            public void run() {
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
            }
        };
        handler.post(runnableCode);
    }

    @SuppressWarnings("HandlerLeak")
    private void prepareBluetoothStack() {
        if (!getReactApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            blt = Bluetooth.sharedManager();
//            blt.mHandler = mHandler;
//            devAdapter = new DeviceListAdapter(this,blt.getBtDevList(),R
// .layout.device_list_item);
        } else {
            BluetoothLE.sharedManager().setup(getCurrentActivity());
            mLeHandler = new Handler() {
                @Override
                public void handleMessage(Message msg){
                    switch(msg.what) {
                        case BluetoothLE.MSG_CONNECTED:
                            Log.d(LOG_TAG, "Connected to device");
                            break;
                        case BluetoothLE.MSG_DISCONNECTED:
                            Log.d(LOG_TAG, "Disconnected to device");
                            break;
                        case BluetoothLE.MSG_CONNECT_FAIL:
                            Log.d(BluetoothModule.LOG_TAG, "connect fail");
                            break;
                        case BluetoothLE.MSG_SCAN_START:
                            break;
                        case BluetoothLE.MSG_SCAN_END:
                            break;
                        case BluetoothLE.MSG_CONNECTING:
                            Log.d(LOG_TAG, "Connecting to device");
                        case BluetoothLE.MSG_FOUNDDEVICE:
                            List<String> devices = BluetoothLE.sharedManager().getDeviceList();
                            final WritableMap params = Arguments.createMap();
                            WritableArray array = Arguments.createArray();
                            for (String device : devices) {
                                array.pushString(device);
                            }
                            params.putArray("deviceList", array);
                            sendEvent(mContext, "foundDevices", params);
                            break;
                        case MSG_VALUECHANGED:
                            byte[] cmd = (byte[]) msg.obj;
                            BluetoothLE.sharedManager().writeBuffer(cmd);
                            break;
                    }
                }
            };
            BluetoothLE.sharedManager().leHandler = mLeHandler;
//            devAdapter = new DeviceListAdapter(this,BluetoothLE.sharedManager().getDeviceList(),R.layout.device_list_item);
//            MeTimer.startWrite();
        }
    }
}
