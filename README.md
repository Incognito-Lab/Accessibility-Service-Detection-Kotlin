# Accessiblity-Service-Detection-Kotlin
## What does this demo app do?
This demo application is an Android application built with Kotlin, this application can detect the device's enabled Accessibility Services. This is useful for preventing malicious applications from abusing the Accessibility Service in order to attack your application.

<p align="center"><img src="https://user-images.githubusercontent.com/100112988/231369670-68847655-5d16-4b99-bcac-1231cad56bda.png" width="280"></p>

## What these sections on the screen means?
### Detected Backlisted Service
Displays the detected blacklisted Accessibility Services that were enabled and blacklisted in the "Blacklisted Accessibility Services" section.

### Accessiblity Setting Status
Displays whether the device has any Accessibility Services enabled.
The method `isEnabled` of the class `AccessibilityManager` was used (Ref: https://developer.android.com/reference/kotlin/android/view/accessibility/AccessibilityManager#isenabled).

### Official Accessibility Services
Displays the enabled Accessibility Services that were installed from the official stores.
The method `getInitiatingPackageName()` of the class `InstallSourceInfo` was used to retrieve the name of the package that requested to install these Accessibility Services, and the output name can be used to determine whether or not it is the package name of the official stores (Ref: https://developer.android.com/reference/android/content/pm/InstallSourceInfo#getInitiatingPackageName()).

### Side Loading Accessibility Services
Displays the enabled Accessibility Services that were installed using the side-loading method.
Using the same method as described in the "Official Accessibility Services" section, if the initializing package name of the Accessibility Services does not match the official stores' package name, it will be determined that the Accessibility Service was installed on the device using the side-loading method.

### Blacklisted Accessibility Services
This section enables the user to add the package name of the accessibility service they wish to blacklist, as well as display all blacklisted package names. If the package name in this list is discovered to be enabled on the device's Accessibility Services, it will be displayed in the "Detected Backlisted Service" section.
