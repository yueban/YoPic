YoPic
=====
A wallpaper gallery application on Android platform, powered by Unsplash API. Display photos from Unsplash, Providing download, set as wallpaper and share features.

YoPic use `Kotlin` & `Jetpack` to develop.

How to build
------------
create new file `unsplash_keys.json` in `main/assets`, fill in your `unsplash app keys`:

```json
{
  "access_key": "your Unsplash app access_key",
  "secret_key": "your Unsplash app secret_key",
  "app_name": "your Unsplash app name"
}
``` 

> you can register as unsplash developers and create a new app at this [link](https://unsplash.com/oauth/applications/new) to get your own keys.

Dependencies
------------
YoPic is based on [Jetpack](https://developer.android.com/jetpack):

* DataBinding
* Lifecycle
* LiveData
* Navigation
* Room
* ViewModel
* WorkManager

And some other Android Open Source libraries:

* [dagger2](https://github.com/google/dagger)
* [RxJava2](https://github.com/ReactiveX/RxJava)
* [RxBinding](https://github.com/JakeWharton/RxBinding)
* [RxPermissions](https://github.com/tbruyelle/RxPermissions)
* [RxLifecycle](https://github.com/trello/RxLifecycle)
* [okhttp3](https://github.com/square/okhttp)
* [retrofit2](https://github.com/square/retrofit)
* [glide4](https://github.com/bumptech/glide)
* [glide-transformations](https://github.com/wasabeef/glide-transformations)
* [moshi](https://github.com/square/moshi)
* [kotshi](https://github.com/ansman/kotshi)
* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
* [BottomSheetMenu](https://github.com/Kennyc1012/BottomSheetMenu)
* [Android-SpinKit](https://github.com/ybq/Android-SpinKit)
* [tray](https://github.com/grandcentrix/tray)
* [MultiType](https://github.com/drakeet/MultiType)
* [about-page](https://github.com/drakeet/about-page)
* [xlog](https://github.com/elvishew/xLog)

Screenshots
-----------

<a href="https://images2.imgbox.com/62/e1/aJU1QJGM_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/62/e1/aJU1QJGM_o.png" alt="image host"/></a>
<a href="https://images2.imgbox.com/1c/50/oALrDYa1_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/1c/50/oALrDYa1_o.png" alt="image host"/></a>
<a href="https://images2.imgbox.com/8c/be/7oRH1hZJ_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/8c/be/7oRH1hZJ_o.png" alt="image host"/></a>
<a href="https://images2.imgbox.com/c8/fb/sIGHMi2q_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/c8/fb/sIGHMi2q_o.png" alt="image host"/></a>
<a href="https://images2.imgbox.com/51/62/U7sNqyIc_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/51/62/U7sNqyIc_o.png" alt="image host"/></a>
<a href="https://images2.imgbox.com/93/1f/MlSCm4b6_o.png" target="_blank"><img width="240" src="https://images2.imgbox.com/93/1f/MlSCm4b6_o.png" alt="image host"/></a>

License
-------

    Copyright 2019 yueban
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.