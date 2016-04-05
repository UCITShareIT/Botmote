# Botmote


## Today's Goals

1. Finish installing all of the tools we need to write apps using React Native.
2. Start writing the Botmote app to move the robots

## Required tools
1. Visual Studio Code*: https://code.visualstudio.com
2. Android Studio*: http://developer.android.com/sdk/index.html
3. Node.js*: https://nodejs.org/en/
4. Github Desktop and Github shell: https://desktop.github.com 

** Should already be installed

## Check Dependencies

1. Open Git Shell. Its icon looks like this:
![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.38.32%20PM.png)
2. Enter `echo $Env:JAVA_HOME` to verify that Java is installed. 
![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.40.49%20PM.png)
	1. If you don't see something like `C:\Program Files\Java\jdk1.7.0_79`, open System Properties, go to Advanced, and open Environment Variables. 
	2. Click New and add JAVA_HOME ![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.46.39%20PM.png)
3. Back in the shell, enter `echo $Env:ANDROID_HOME`. 
![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.48.58%20PM.png)
You should see the path to Android logged. If not repeat the steps above for adding an environment variable, this time for ANDROID_HOME
4. Install react native by running `npm install -g react-native-cli`
 ![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.53.58%20PM.png)
5. Create the Botmote project. Run `react-native init Botmote`
![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.55.22%20PM.png)
6. Open Visual Studio Code
![title](Screenshots/Screen%20Shot%202016-04-05%20at%201.56.58%20PM.png)
7. Install React Native Tools for Visual Studio Code
	1. Within VS Code, press `F1` to open command prompt and type `install extension`
![title](Screenshots/Screen%20Shot%202016-04-05%20at%202.29.38%20PM.png)
	2. Type react native tools and select it to install.
![title](Screenshots/Screen%20Shot%202016-04-05%20at%202.30.53%20PM.png)
8. Open the Botmote project in Visual Studio Code. File > Open Folder and find the project you created in step 5. All of the code we'll write will be in index.android.js
	![title](Screenshots/Screen%20Shot%202016-04-05%20at%202.09.28%20PM.png)
	
## Running the default app
Now let's run the default React Native project. 

1. Open a simulator. To do so, open Android Studio and open the Android Device Manager. ![title](Screenshots/Screen%20Shot%202016-04-05%20at%202.26.18%20PM.png)
2. Launch an emulator. You may have to create a new one.
 ![title](Screenshots/Screen%20Shot%202016-04-05%20at%202.28.56%20PM.png)
 3. Back in the shell, run `npm start`.
2. Open another shell window and run`react-native run-android`. 
	
## Learning React Native

React Native is a tool developed by Facebook that allows you to write apps for iOS and Android using JavaScript. Today we'll get started by getting the default React Native project to run and then we'll add the four buttons to the screen to control the robots. 


## Intro to JavaScript

Javascript is a programming language primarily used for making websites interactive. When you tweet on Twitter or search google, JavaScript is the language being used.

## Variables

A variable is used to store information. We can store String, Numbers, Arrays, and Objects.  We define a variable by using either `let` or `const`, giving it a name, and assigning a value. 

```
/* This is a comment. When you run your code, it will be ignored.
Comments are good for leaving documentation about what 
your code does */

let x = 5; //a number
//Since I used let, I can change the value of x
x = 10;
const aVariableThatIsTrue = true; //a boolean
const aVariableThatIsFalse = false; //a boolean

const aString = "Hello, World"; // a string variable

const anArray = ["One", "Two", "Three"]; //an array of strings

const anObject = {key1: "myValue" , "my other key" : 4 }; //an object with two properties. An object can store multiple values

```