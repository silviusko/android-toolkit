Android Toolkit
===============

[![API](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/silviusko/android-toolkit)
[![Circle CI](https://circleci.com/gh/silviusko/android-toolkit/tree/master.svg?style=shield)](https://circleci.com/gh/silviusko/android-toolkit/tree/master)

Android Toolkit provides common utilities to build a application easily.

How to use
----------

1. Open a **App** project in Android Studio
2. New > New Module... > Import .JAR/.AAR Package
3. Select release of the downloaded aar file and rename the sub-project name to **toolkit**, then finish it
4. Copy debug aar into root folder of android-toolkit project
5. Open the **build.gradle** in **toolkit** project and paste as below:
```
configurations.create("debug")
artifacts.add("debug", file('toolkit-debug.aar'))
configurations.create("release")
artifacts.add("release", file('toolkit-release.aar'))
```
    
6. Open the **build.gradle** in **App** project and paste codes as below to include android-toolkit library:
```
debugCompile project(path: ':toolkit', configuration: 'debug')
releaseCompile project(path: ':toolkit', configuration: 'release')
```

7. Add a dummy task in the gradle of the toolkit. This task can avoid some errors when executing a shell command.
```
task testClasses {
    doLast {
        println 'This is a dummy testClasses task'
    }
}
```
  
How to build aar files
----------------------

* Execute shell command:
```
$ ./gradlew clean :toolkit:build
```
