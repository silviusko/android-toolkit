Android Toolkit
===============

[![API](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/silviusko/android-toolkit)
[![Circle CI](https://circleci.com/gh/silviusko/android-toolkit/tree/master.svg?style=shield)](https://circleci.com/gh/silviusko/android-toolkit/tree/master)

Android Toolkit provides various functions to build a application easily.

Features
--------

+ **BaseSQLiteOpenHelper :** Provide all sql actions with a transaction mechanism.
+ **[PermissionManager](./doc/permission/README.md)**
+ **BasePreferences :** Use simple annotations to access preferences.
+ **CryptoUtil :** Provide encrypt, decrypt and md5 methods.
+ **[BaseVollyClient](./doc/volley/README.md)**
+ **[WaterMark](./doc/watermark/README.md)**
+ **Widgets**
  - **[TaggingLayout](./doc/widget/TaggingLayout/README.md)**
+ **Other Utilities**

How to use
----------
1. Build aar files
```
  $ ./gradlew clean :toolkit:build
```
2. Copy aar files from output folder to libs folder of your project
```
  $ cp ./toolkit/build/outputs/aar/toolkit-release.aar \
  ./yourproject/app/libs
```
3. Declare a repository as a flatDir for importing aar files
```
  repositories {
    flatDir {
        dirs 'libs'
    }
  }
```
4. Declare aar dependencies
```
  dependencies {
    compile 'com.ktt.toolkit:toolkit-release@aar'
  }
```

License
-------
Copyright 2016 Silvius Kao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
