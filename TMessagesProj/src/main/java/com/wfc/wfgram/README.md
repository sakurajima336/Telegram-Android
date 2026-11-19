# com.wfc.wfgram

Wfgram-only code lives in this package. Upstream Telegram code stays in
`org.telegram.*` untouched (except the documented one-line init hook in
`ApplicationLoader.postInitApplication()`).

- `WfgramFeatures` — entry point, called once at app init.
- `WfgramConfig` — Wfgram's own SharedPreferences store (`wfgramConfig`).
- `WfgramForward` — forward-without-attribution entry: ChatActivity
  hooks set its flag from the dedicated action, and the forward flow
  sends with the sender name hidden.
- `WfgramSettingsActivity` — Wfgram's own settings page, opened from a
  dedicated row (id 100) in the main Telegram settings list. Feature
  switches live here, backed by `WfgramConfig`.
- `WfgramStrings` — Wfgram UI strings resolved against the in-app
  language (the current LocaleInfo), because the bundled localization
  cannot follow Chinese language packs (their locale collapses to a
  plain "zh" that matches no bundled overlay).

Add new features here, gate them behind `WfgramFeatures.isEnabled()` /
a flag in `WfgramConfig`, and hook them into upstream code with the
smallest possible one-line call, the way Nekogram does.
