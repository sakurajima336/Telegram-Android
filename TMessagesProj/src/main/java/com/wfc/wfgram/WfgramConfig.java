package com.wfc.wfgram;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Wfgram's own settings store.
 *
 * All Wfgram-only feature flags live here, backed by a dedicated
 * SharedPreferences file ("wfgramConfig") so upstream Telegram code and
 * upstream merges stay untouched. Modeled after the way Nekogram keeps
 * its settings separate from the official client.
 */
public final class WfgramConfig {

    private static final String PREFS_NAME = "wfgramConfig";

    private static final String KEY_NO_QUOTE_FORWARD = "no_quote_forward";

    private static SharedPreferences prefs;

    /** Master switch for every Wfgram feature. */
    public static boolean enabled = true;

    private WfgramConfig() {
    }

    static synchronized void load(Context context) {
        if (prefs != null) {
            return;
        }
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        enabled = prefs.getBoolean("enabled", true);
    }

    public static synchronized boolean getBoolean(String key, boolean defValue) {
        return prefs == null ? defValue : prefs.getBoolean(key, defValue);
    }

    public static synchronized void setBoolean(String key, boolean value) {
        if (prefs != null) {
            prefs.edit().putBoolean(key, value).apply();
        }
    }

    /** The stored preference for the "forward without attribution" entry. */
    public static boolean noQuoteForward() {
        return getBoolean(KEY_NO_QUOTE_FORWARD, true);
    }

    /** Whether the "forward without attribution" entry is shown in chat menus. */
    public static boolean isNoQuoteForwardEnabled() {
        return enabled && noQuoteForward();
    }

    public static void setNoQuoteForwardEnabled(boolean value) {
        setBoolean(KEY_NO_QUOTE_FORWARD, value);
    }

    public static synchronized String getString(String key, String defValue) {
        return prefs == null ? defValue : prefs.getString(key, defValue);
    }

    public static synchronized void setString(String key, String value) {
        if (prefs != null) {
            prefs.edit().putString(key, value).apply();
        }
    }
}
