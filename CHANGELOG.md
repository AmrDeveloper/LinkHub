Change Log
==========

Version 2.0.3 *(2026-01-10)*
-----------------------------

- Fix auto saving constraints.
- Fix error message when saving link with same title.
- Fix the flow of saving link when it shared from outside the app.

Version 2.0.2 *(2026-01-06)*
-----------------------------

- Fix navigation when creating link/folder inside folder.

Version 2.0.1 *(2026-01-05)*
-----------------------------

- Fix navigation after saving shared link.
- Support edge-to-edge.

Version 2.0.0 *(2026-01-04)*
-----------------------------

- Migrate to Jetpack Compose.
- Improve the design.
- Support List and Grid view for Folders.
- Improve Error messages.
- Fix can't see "All Folders" options.
- Implement multi type search.
- Support filter by click count, pinned, is inside folder.
- Support nested folders.

Version 1.6.7 *(2025-12-29)*
-----------------------------

- Fix edge-to-edge problem.
- Add linkhub prefix for exported file name.

Version 1.6.6 *(2025-10-28)*
-----------------------------

- Revert: Fix proper system bar handling (#85)

Version 1.6.5 *(2025-10-24)*
-----------------------------

- Fix proper system bar handling (#85)

Version 1.6.4 *(2025-08-10)*
-----------------------------

- Fix exporting json data in case of name modified by R8.

Version 1.6.3 *(2025-08-10)*
-----------------------------

- Quick fix for import/export json data.

Version 1.6.2 *(2025-07-06)*
-----------------------------

- Update Min and Target SDK.

Version 1.6.1 *(2024-09-20)*
-----------------------------

- Fix: Class not found exception on release mode.
- Issue #65: Handle url encoding.
- Update SDK version to 34
- Migrate from Groovy to Kotlin DSL
- Migrate to Gradle version catalog
- Russian translation of the app
- Created remember the last used folder option
- fixed string resources for default folder option
- Added import/export to html functionality
- Add Vietnamese to the list of supported languages
- Migrate to Material 3
- Add Chinese to the supported languages
- Optimize onChildDraw calculating icon top position

Version 1.6.0 *(2023-05-11)*
-----------------------------

* Fixing Empty folder field entries #45 by @AmrDeveloper
* Change cursor color in light and dark theme to be clear #44 by @AmrDeveloper

Version 1.5.1 *(2023-06-06)*
-----------------------------

* Fixing Empty folder field entries #45 by @AmrDeveloper
* Change cursor color in light and dark theme to be clear #44 by @AmrDeveloper

Version 1.5.0 *(2023-05-25)*
-----------------------------

* Russian translation.
* Russian Vietnamese.
* Russian Chinese.
* Added import/export to html functionality.
* Update Kotlin and libraries versions
* Optimize onChildDraw calculating icon top position

Version 1.4.0 *(2023-04-19)*
-----------------------------

* Add support for password layer
* Make setting screen scrollable
* Add padding for switch in link fragment

Version 1.3.0 *(2023-04-09)*
-----------------------------

* Add Folder Name to bottom sheet dialog Issue #41 by @AmrDeveloper
* Implement setting option for saving links and folders automatically Issue #24 by @AmrDeveloper

Version 1.2.0 *(2023-02-21)*
-----------------------------

* Fix show link created time not last updated time PR #30 by @SIRSteiner
* Update Data Formatter to use the current locale PR #30 by @SIRSteiner
* Fix pinned links duplications on widget Issue #26 by @AmrDeveloper
* Add support for german language PR #27 by @SIRSteiner
* Fix Android 12 crash when creating new widget by @AmrDeveloper
* Add support for updating link click count from widget Issue #28 by @AmrDeveloper
* Add support for reading non media file on Android 13 by @AmrDeveloper
* Improve pinned links widget design PR #35 by @SIRSteiner
* Fix status of option button after add/edit link/folder PR #34 by @SIRSteiner
* Fix update option button when click on option button link/folder PR #34 by @SIRSteiner
* Add button for "all/next folders" instead of the inconspicuous arrow PR #38 by @SIRSteiner

Version 1.1.6 *(2023-02-09)*
-----------------------------

* Improve save and delete icon from Design suggestions #17
* Import/Export show click count flag
* Import/Export user Theme Issue #19
* Fix Smooth scrolling on home page Issue #22
* Update Link list header to show folder color and name #17

Version 1.1.5 *(2022-12-03)*
-----------------------------

* Change export path for android 9 or less to be download directory

Version 1.1.4 *(2022-10-14)*
-----------------------------

* Make link info generator more secure and validate that string is url
* Replace Deprecated adapter with adapterPosition

Version 1.1.3 *(2022-08-17)*
-----------------------------

* Add new Setting option to show and hide the link counter

Version 1.1.2 *(2022-06-16)*
-----------------------------

* Solved Issue #5: #5
* Fastlane files added by @IzzySoft #4
* Fix the Creation date crash issue
* Show update date

Version 1.1.1 *(2022-04-25)*
-----------------------------

* Show total number of URLs (Feature request from Google play reviewer)

Version 1.1.0 *(2022-04-04)*
-----------------------------

* Fix Widget issue

Version 1.0.9 *(2021-11-01)*
-----------------------------

* Add Lottie Animation when the list is empty
* Fix shortcuts issue

Version 1.0.7 *(2021-09-10)*
-----------------------------

* Improve Import-export implementation on New Android Versions

Version 1.0.6 *(2021-08-30)*
-----------------------------

* Implement Import and Export feature as a JSON file
* Add Support for Android 10, 11 for file management

Version 1.0.3 *(2021-08-02)*
-----------------------------

* Auto-generated smart Title and subtitle
* Fix copy issue on Android Oreo devices

Version 1.0.3 *(2021-07-30)*
-----------------------------

* Init release