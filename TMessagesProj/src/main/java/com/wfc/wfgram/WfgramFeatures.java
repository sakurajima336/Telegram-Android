package com.wfc.wfgram;

import android.content.Context;
import android.util.Log;

/**
 * Entry point for all Wfgram-only features.
 *
 * The official Telegram sources (org.telegram.*) are kept as close to
 * upstream as possible. The single hook is one call from
 * ApplicationLoader.postInitApplication(); everything Wfgram adds
 * lives under com.wfc.wfgram and is initialized from here, so future
 * upstream syncs only ever conflict at that one line.
 */
public final class WfgramFeatures {

    private static final String TAG = "Wfgram";

    private static boolean inited;

    private WfgramFeatures() {
    }

    public static synchronized void init(Context context) {
        if (inited || context == null) {
            return;
        }
        inited = true;
        try {
            WfgramConfig.load(context);
            Log.d(TAG, "Wfgram features init, enabled=" + WfgramConfig.enabled);
            // New Wfgram features are registered here.
        } catch (Throwable t) {
            // A Wfgram feature must never take the whole client down.
            Log.e(TAG, "Wfgram features init failed", t);
        }
    }

    public static boolean isEnabled() {
        return inited && WfgramConfig.enabled;
    }
}
