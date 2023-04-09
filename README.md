# Accessiblity-Service-Detection-Kotlin
## What does this sample app do?
This demo application is an Android application built with Kotlin that can detect the device's enabled Accessibility Services. This is useful for preventing malicious applications from abusing the Accessibility Service in order to attack your application.

![image](https://user-images.githubusercontent.com/100112988/230752684-81404288-7598-4062-858d-d1bda6d9d311.png)

## What these sections on the screen means?
### Detected Backlisted Service
Displays the detected blacklisted Accessibility Services that were blacklisted in the section "Blacklisted Accessibility Services".

### Accessiblity Setting Status
Displays whether the device has any Accessibility Services enabled. The method `isEnabled` of the class `AccessibilityManager` was used (Ref: https://developer.android.com/reference/kotlin/android/view/accessibility/AccessibilityManager#isenabled).

### Official Accessibility Services
Displays the enabled Accessibility Services that were installed from the official store. The method `getInitiatingPackageName()` of the class `InstallSourceInfo` was used to retrieve the name of the package that requested to install these accessibility services, and the output name can be used to determine whether or not it is the package name of the official store (Ref: https://developer.android.com/reference/android/content/pm/InstallSourceInfo#getInitiatingPackageName()).

### Side Loading Accessibility Services
Displays the enabled Accessibility Services that were installed using the side-loading method. Using the same method as described in the "Official Accessibility Services" section, if the initializing package name of the Accessibility Services does not match the official stores' package name, it will be determined that the application was installed on the device using the side-loading method.

### Blacklisted Accessibility Services
This section enables the user to add the package name of the accessibility service they wish to blacklist, as well as display all blacklisted package names. If the package name in this list is discovered to be enabled on the device's Accessibility Service, it will be displayed in the "Detected Backlisted Service" section.
