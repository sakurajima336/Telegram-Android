package com.wfc.wfgram;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

import java.util.Locale;

/**
 * Wfgram UI strings that follow the in-app language.
 *
 * Telegram resolves its own strings through language packs, while the
 * bundled localization only keys off the raw locale. When the in-app
 * language is a Chinese pack, the locale collapses to plain "zh" and
 * the bundled zh-CN/zh-TW overlays never match, so Wfgram strings
 * would fall back to English while the rest of the UI is Chinese.
 * This helper reads the selected language from the current
 * {@link LocaleController.LocaleInfo} instead and picks the matching
 * Wfgram translation directly.
 */
public final class WfgramStrings {

    private static final int LANG_EN = 0;
    private static final int LANG_CN = 1;
    private static final int LANG_TW = 2;

    private WfgramStrings() {
    }

    private static int language() {
        try {
            LocaleController.LocaleInfo info = LocaleController.getInstance().getCurrentLocaleInfo();
            StringBuilder code = new StringBuilder();
            if (info != null) {
                if (info.shortName != null) {
                    code.append(info.shortName).append(' ');
                }
                if (info.baseLangCode != null) {
                    code.append(info.baseLangCode).append(' ');
                }
                if (info.pluralLangCode != null) {
                    code.append(info.pluralLangCode);
                }
            }
            String value = code.toString().toLowerCase(Locale.ROOT);
            if (value.contains("zh")) {
                if (value.contains("hant") || value.contains("zhtw") || value.contains("zh-tw")
                        || value.contains("zh_tw") || value.contains("_tw") || value.contains("-tw")
                        || value.contains("hk") || value.contains("mo")) {
                    return LANG_TW;
                }
                return LANG_CN;
            }
        } catch (Exception ignored) {
        }
        return LANG_EN;
    }

    private static String pick(int resId, String en, String cn, String tw) {
        int lang = language();
        if (lang == LANG_CN) {
            return cn;
        }
        if (lang == LANG_TW) {
            return tw;
        }
        String localized = LocaleController.getString(resId);
        if (localized != null && !localized.startsWith("LOC_ERR")) {
            return localized;
        }
        return en;
    }

    public static String settingsTitle() {
        return pick(R.string.WfgramSettings, "Wfgram Settings", "Wfgram 设置", "Wfgram 設定");
    }

    public static String featuresHeader() {
        return pick(R.string.WfgramFeaturesHeader, "Features", "功能", "功能");
    }

    public static String noQuoteForward() {
        return pick(R.string.NoQuoteForward, "Forward without attribution", "无引用转发", "無引用轉發");
    }

    public static String noQuoteForwardInfo() {
        return pick(R.string.WfgramNoQuoteForwardInfo,
                "When on, a \u201cForward without attribution\u201d entry appears next to Forward in message menus. Messages sent with it carry no forward source.",
                "开启后，消息菜单的「转发」旁会出现「无引用转发」，用它转发的消息不带转发来源。",
                "開啟後，訊息選單的「轉發」旁會出現「無引用轉發」，用它轉發的訊息不帶轉發來源。");
    }

    public static String aboutHeader() {
        return pick(R.string.WfgramAboutHeader, "About", "关于", "關於");
    }

    public static String aboutInfo() {
        return pick(R.string.WfgramAboutInfo,
                "Wfgram is a personal-use client based on the official Telegram source code.",
                "Wfgram 是基于官方 Telegram 源码的自用客户端。",
                "Wfgram 是基於官方 Telegram 原始碼的自用客戶端。");
    }
}
