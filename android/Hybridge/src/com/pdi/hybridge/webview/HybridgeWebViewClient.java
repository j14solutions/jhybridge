/**
 * Hybridge
 * (c) Telefonica Digital, 2015 - All rights reserved
 * License: MIT (see LICENSE file)
 */

package com.pdi.hybridge.webview;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pdi.hybridge.HybridgeConst;
import com.pdi.hybridge.HybridgeConst.Event;
import com.pdi.hybridge.JsAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HybridgeWebViewClient extends WebViewClient {

    protected JSONArray mActions;
    protected JSONArray mEvents;
    protected JSONObject mCustomData;

    @SuppressLint("DefaultLocale")
    public HybridgeWebViewClient(JsAction[] actions, JSONObject customData) {
        mActions = new JSONArray();
        for (final JsAction action : actions) {
            this.mActions.put(action.toString().toLowerCase());
        }

        mEvents = new JSONArray();
        final Event[] events = HybridgeConst.Event.values();
        for (final Event event : events) {
            this.mEvents.put(event.getJsName());
        }

        try {
            mCustomData = new JSONObject(customData.toString());
        } catch (final JSONException e) {
            mCustomData = new JSONObject();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        final HybridgeBroadcaster hybridge = HybridgeBroadcaster.getInstance(view);
        if (hybridge != null) {
            hybridge.initJs(view, mActions, mEvents, mCustomData);
        }
    }
}
