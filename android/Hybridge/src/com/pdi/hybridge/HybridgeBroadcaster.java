/**
 * Hybridge
 * (c) Telefonica Digital, 2013 - All rights reserved
 * License: GNU Affero V3 (see LICENSE file)
 */

package com.pdi.hybridge;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;

import com.pdi.hybridge.HybridgeConst.Event;

public class HybridgeBroadcaster extends Observable {

    private static HybridgeBroadcaster instance;

    private boolean isInitialized = false;

    private final String TAG = "HybridgeBroadcaster";

    private StringBuffer jsBuffer;
    
    private HashMap<Integer, AsyncTask<JSONObject, Void, JSONObject>> currents;

    @SuppressLint("UseSparseArrays")
    public HybridgeBroadcaster() {
        currents = new HashMap<Integer, AsyncTask<JSONObject,Void,JSONObject>>();
        jsBuffer = new StringBuffer("");
    }

    public static HybridgeBroadcaster getInstance() {
        if (instance == null) {
            instance = new HybridgeBroadcaster();
        }
        return instance;
    }

    public void initJs(WebView view, JSONArray actions, JSONArray events) {
        runJsInWebView(view, "window.HybridgeGlobal || function () {" +
                "window.HybridgeGlobal = {" +
                "  isReady : true" +
                ", version : " + HybridgeConst.VERSION +
                ", actions : " + actions.toString() +
                ", events : " + events.toString() +
                "};" +
                "window.$ && $('#hybridgeTrigger').toggleClass('switch');" +
                "}()"
                );
        isInitialized = true;
    }

    public void firePause(WebView view) {
        HybridgeConst.Event event = HybridgeConst.Event.PAUSE;
        notifyObservers(event);
        fireJavascriptEvent(view, event, null);
    }

    public void fireResume(WebView view) {
        HybridgeConst.Event event = HybridgeConst.Event.RESUME;
        notifyObservers(event);
        fireJavascriptEvent(view, event, null);
    }

    public void fireMessage (WebView view, JSONObject data) {
        HybridgeConst.Event event = HybridgeConst.Event.MESSAGE;
        notifyObservers(event);
        fireJavascriptEvent(view, event, data);
    }

    public void fireReady(WebView view, JSONObject data) {
        HybridgeConst.Event event = HybridgeConst.Event.READY;
        notifyObservers(event);
        fireJavascriptEvent(view, event, data);
    }

    public void fireJavascriptEvent(WebView view, Event event, JSONObject data) {
        if (isInitialized) {
            WebView.HitTestResult hr = ((WebView)view).getHitTestResult();
            String prejs = "";
            String json = data != null ? data.toString() : "{}";
            StringBuffer js = new StringBuffer("HybridgeGlobal.fireEvent(\"");
            js.append(event.getJsName()).append("\",").append(json).append(");");

            if (hr == null || hr.getType() != HitTestResult.EDIT_TEXT_TYPE) {
                if(jsBuffer.length() != 0) {
                    prejs = jsBuffer.append(js.toString()).toString();
                    runJsInWebView(view, prejs);
                    jsBuffer = new StringBuffer();
                } else {
                    runJsInWebView(view, js.toString());
                }
            } else {
                Log.d(TAG, "Defer javascript message, user is entering text");
                jsBuffer.append(js.toString());
            }
        }
    }

    public void runJsInWebView(final WebView view, final String js) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        view.loadUrl("javascript:(function(){" + js + "})()");
                    }
                });
    }

    public void updateState(JSONObject data) {
        this.setChanged();
        this.notifyObservers(data);
        Log.d(TAG, data.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        Class<?> clazz = observer.getClass().getSuperclass();
        if (clazz != null && clazz == android.os.AsyncTask.class) {
            int hashCode = observer.hashCode();
            AsyncTask<JSONObject, Void, JSONObject> current = currents.get(hashCode); 
            if (current != null) {
                if (current.cancel(true)) {
                    currents.remove(hashCode);
                }
            }
            currents.put(hashCode, (AsyncTask<JSONObject, Void, JSONObject>) observer);
        }
    }

    @Override
    public void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
        Class<?> clazz = observer.getClass().getSuperclass();
        if (clazz != null && clazz == android.os.AsyncTask.class) {
            int hashCode = observer.hashCode();
            currents.remove(hashCode);
        }	    
    }
}
