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

## Features

### Wfgram settings

`SettingsActivity` gains one row (id 100, purple gradient, `msg_settings`
icon, string `WfgramSettings`) right after the Language row; its click
case presents `com.wfc.wfgram.WfgramSettingsActivity`. The page follows
the upstream `AudioRecorderSettingsActivity` pattern (BaseFragment +
RecyclerListView + HeaderCell/TextCheckCell/TextInfoPrivacyCell) and
holds one switch per Wfgram feature, stored in `WfgramConfig`. The
forward-without-attribution entries in `ChatActivity` are only added
when `WfgramConfig.isNoQuoteForwardEnabled()` is true (default on).

### Forward without attribution

Modeled on Nagram's no-quote forward entry (theirs: a static flag on
ChatActivity set from a dedicated menu action, feeding the upstream
hide-sender-name forward path).

- New state holder `com.wfc.wfgram.WfgramForward` (transient flag +
  consume), new strings `NoQuoteForward` (`values/strings_wfgram.xml`,
  plus zh-rCN / zh-rTW), icon `msg_forward_noquote` copied from Nagram
  (hdpi–xxhdpi).
- ChatActivity hooks (the only upstream file touched):
  - selection action mode: extra item `forward_noquote` (2011) next to
    the forward item, enabled/alpha mirroring it; the bottom forward
    button and the regular forward item clear the flag, the new item
    sets it, both then run the normal `openForward`.
  - single-message menu: `OPTION_FORWARD_NOQUOTE` (119) next to
    Forward, handled by the same `processSelectedOption` case as
    `OPTION_FORWARD`, setting the flag from the chosen option; also
    removed for hidden-sensitive messages like Forward.
  - `didSelectDialogs` direct-send branch: the flag is consumed once
    and passed as `forwardFromMyName` to `SendMessagesHelper.sendMessage`
    (server-side `drop_author`, no forward header).
  - `showFieldPanel` forward branch: the flag is consumed and applied
    as `messagePreviewParams.hideForwardSendersName`, so the preview
    shows the hidden-sender state and the eventual send uses it.

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
