package com.botmote.NativeModules;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.botmote.Bluetooth;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.List;

/**
 * Created by Mike on 3/7/16.
 */
public class BluetoothModule extends ReactContextBaseJavaModule {

    Bluetooth blt;
    private static String LOG_TAG = BluetoothModule.class.toString();
    private Handler mLeHandler;
    // Create the Handler object (on the main thread by default)
    Handler handler;
    private Runnable runnableCode;

    private ReactApplicationContext mContext;
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
            BluetoothLE.sharedManager().start();
            //handler.post(runnableCode);
        }
    }

    @Override
    public String getName() {
        return "BluetoothLE";
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
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
                    switch(msg.what){
                        case BluetoothLE.MSG_CONNECTED:
                        break;
                        case BluetoothLE.MSG_DISCONNECTED:
                            break;
                        case BluetoothLE.MSG_CONNECT_FAIL:
                            Log.d(BluetoothModule.LOG_TAG,"connect fail");
                        break;
                        case BluetoothLE.MSG_SCAN_START:
                        break;
                        case BluetoothLE.MSG_SCAN_END:
                            break;
                        case BluetoothLE.MSG_FOUNDDEVICE:
                            List<String> devices = BluetoothLE.sharedManager().getDeviceList();
                            final WritableMap params = Arguments.createMap();
                            WritableArray array = Arguments.createArray();
                            for (String device: devices) {
                                array.pushString(device);
                            }
                            //params.putArray("FoundDevices", array);
                            params.putArray("deviceList", array);
                            runnableCode = new Runnable() {
                                @Override
                                public void run() {
                                    sendEvent(mContext, "foundDevices", params);
                                }
                            };
                            handler.post(runnableCode);
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
