# Botmote

#Requirements
1. JDK 7: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
2. Android Studio: http://developer.android.com/sdk/index.html
3. Node.js v4.2.6: https://nodejs.org/en/
4. React Native: In a terminal run `npm install -g react-native-cli`

#Running
1. Clone this repository
    a. In shell: `git clone https://github.com/UCITShareIT/Botmote.git`
    b. Github Desktop: https://desktop.github.com/
2. Start an Android Emulator in Android Device Manager
3. run `react-native run-android`

#Development
The android root file is index.android.js.
The iOS root file is index.ios.js.

You can make changes in index.android.js while running, and press
F2 to reload without recompiling.

# Windows Environment Variables
In order to run on Windows, it is most likely necessary to modify your environment variables.

1. ANDROID_HOME = C:\Users\{user_name}\AppData\Local\Android\sdk
2. JAVA_HOME = C:\Program Files\Java\jdk1.7.0_79 (check your java version!)
3. Append the following to your PATH: ANDROID_HOME\tools;ANDROID_HOME\platform-tools


# Troubleshooting

### Missing tools.jar?

If you run `react-native run-android` and receive an error that tools.jar
cannot be found, set your JAVA_HOME environment variable

### Can't run Android Emulator?
Ensure your BIOS settings allow for hardware virtualization

### Logging
To view console.log statements in Android, run: `adb logcat *:S ReactNative:V ReactNativeJS:V`
