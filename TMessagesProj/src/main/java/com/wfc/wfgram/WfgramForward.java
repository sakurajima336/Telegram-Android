package com.wfc.wfgram;

/**
 * Shared state for Wfgram's forward-without-attribution entry.
 *
 * The entry points in ChatActivity set {@link #noQuote} when the user
 * picks the dedicated action; the forward flow then sends with the
 * sender name hidden (the upstream "forward from my name" path, which
 * drops the forward header) instead of a regular quoted forward.
 */
public final class WfgramForward {

    public static boolean noQuote;

    private WfgramForward() {
    }

    public static boolean consumeNoQuote() {
        boolean value = noQuote;
        noQuote = false;
        return value;
    }
}
