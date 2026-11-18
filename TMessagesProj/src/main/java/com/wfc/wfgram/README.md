# com.wfc.wfgram

Wfgram-only code lives in this package. Upstream Telegram code stays in
`org.telegram.*` untouched (except the documented one-line init hook in
`ApplicationLoader.postInitApplication()`).

- `WfgramFeatures` — entry point, called once at app init.
- `WfgramConfig` — Wfgram's own SharedPreferences store (`wfgramConfig`).

Add new features here, gate them behind `WfgramFeatures.isEnabled()` /
a flag in `WfgramConfig`, and hook them into upstream code with the
smallest possible one-line call, the way Nekogram does.
