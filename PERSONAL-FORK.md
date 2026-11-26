# Personal fork: preserve intent, minimize upstream divergence

## Intent to preserve

This fork follows upstream Next Player for playback, media operations, settings, data,
security fixes and dependencies. Its personal customization is chiefly layout:

- Keep the phone's bottom page-navigation pill hidden; retain the trailing resume/play FAB.
- Show the existing four page chips below the home toolbar, not overlaid on the toolbar.
- Hide those chips inside folders and while selecting media.
- Keep the tablet/TV navigation rail and upstream back-stack behavior.
- Keep the clean application name and stable personal signing identity. Published arm64
  APKs must update over the previous personal build with a greater version code.

Preserve these behaviors, not the historical implementation. Reuse upstream capabilities
when available; do not retain large deletions or duplicate entire upstream screens.

## Minimal integration points

1. `MainActivity.kt`: `showFabOnly = true` reuses the upstream bottom-bar implementation.
   A small `LocalTopLevelPageSwitcher` provider retains the existing chip switch behavior.
2. `MediaPickerScreen.kt`: one toolbar call changes to `PersonalMediaTopAppBar`, with the
   home/non-selection visibility condition. The upstream toolbar content remains intact.
3. `PersonalMediaTopAppBar.kt`: owns the `Column` and chip row. Its chip labels retain
   upstream navigation content descriptions, so upstream integration tests stay unchanged.
4. `PersonalPageNavigation.kt`: owns the fork-only composition-local extension.
5. `app/build.gradle.kts`: the final personal overlay supplies optional CI version/signing
   overrides. Upstream version literals and signing blocks remain untouched.
6. `personal-build.yaml`: builds with the original repository keystore, publishes only from
   `main`, reads versionName from the produced APK metadata, and uploads only arm64.
7. The debug `app_name` override remains `Next Player`.

`TopLevelNavigation.kt`, `TopLevelFab.kt`, and upstream instrumentation tests should match
upstream exactly. Do not delete upstream's unused bottom-bar implementation: it is simply
not selected in this fork.

## Sync and verification

Merge upstream into a dedicated sync branch; never reset the fork onto upstream.
Review any change to the integration points above. Run the existing Android Build workflow
(debug build, local tests, ktlint and API 35 instrumentation) through a pull request and run
Personal Build manually on the same branch. A branch build must skip rolling publication.

The fork-owned `PersonalMediaTopAppBarTest` checks chip placement below the toolbar,
secondary-page switching, and removal of chips without removal of the toolbar.
Before publishing an update, compare the new arm64 APK's certificate against the prior
personal APK and confirm that the version code increased. Do not install or replace the
user's device application as part of a repository sync.

No extra sync bot, build plugin, dependency, preference screen, or code-generation pipeline
is required for this customization.
