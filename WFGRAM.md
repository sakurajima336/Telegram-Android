# Wfgram — changes vs upstream Telegram-Android

Upstream: https://github.com/TelegramMessenger/Telegram-Android.git
Base at fork time: 13.2.2 (7303), commit 21f7d10 (shallow clone)

Wfgram is a personal-use fork. Rule: **do not rename the official
source package** (`org.telegram.*` stays). Wfgram identity is applied
only at the app level, and Wfgram features live in their own package,
the way Nekogram does it.

## Identity changes (app level only)

- `settings.gradle` — `rootProject.name = 'Wfgram'`
- `gradle.properties` — `APP_PACKAGE=com.wfc.wfgram`
  (this is the applicationId for every app module; debug builds get
  the upstream `.beta` suffix, standalone gets `.web`)
- `strings.xml` — `AppName` = Wfgram, `AppNameBeta` = Wfgram Beta
- `BuildVars.java`
  - `APP_ID` / `APP_HASH` = our own Telegram API credentials
    (canonical copy lives outside the repo, see workspace README;
    never publish this repo publicly with these filled in)
  - `PLAYSTORE_APP_URL` points at com.wfc.wfgram
  - `CHECK_UPDATES = false` (official updates cannot install over a
    fork's package/signature)
  - `SUPPORTS_PASSKEYS = false` (upstream note: official app ids only)

## FCM

- `google-services.json` in `TMessagesProj` and `TMessagesProj_App`
  replaced with our own Firebase project
  (package `com.wfc.wfgram`). Originals backed up outside the repo.
- FCM code itself is upstream's (`GcmPushListenerService`,
  `PushListenerController`) and needs no changes: it picks the project
  up from google-services.json.
- Registered packages: `com.wfc.wfgram` (release) and
  `com.wfc.wfgram.beta` (debug).

## Wfgram feature layer

- New package `com.wfc.wfgram` under
  `TMessagesProj/src/main/java/com/wfc/wfgram/`
  (`WfgramFeatures` entry point, `WfgramConfig` settings store).
- Exactly one upstream hook: `ApplicationLoader.postInitApplication()`
  calls `WfgramFeatures.init(...)` — keep it that way so upstream
  syncs stay near conflict-free.

## Removed app modules

Wfgram ships only the main app. Deleted modules (and their
`settings.gradle` includes): `TMessagesProj_AppHuawei`,
`TMessagesProj_AppHockeyApp`, `TMessagesProj_AppStandalone`,
`TMessagesProj_AppTests`. Kept: `TMessagesProj` (core library),
`TMessagesProj_App` (the app), `jlatexmath` and `TMessagesProj_Modules`
(libraries the app depends on).
Two dormant upstream references remain on purpose, to keep upstream
merges simple: `checkVisibility` in `TMessagesProj/build.gradle` only
probes an AppHockeyApp file path when an HA_public task runs, and
buildSrc's TestGeneratorPlugin is never applied without AppTests.
Upstream syncs will try to resurrect the deleted modules; drop them
again after each sync.

Note: the upstream Dockerfile still names the deleted modules in its
build command; build with Gradle directly instead.
