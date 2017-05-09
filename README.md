# Utils
Useful, reusable functionality for common tasks in Java.

# Usage

To use code from this repository in your gradle-enabled project, just add this as a jitpack dependency to your build.gradle:

```gradle
...

dependencies {
    compile 'com.github.medavox:utils:v0.4'
}

...
```

you'll also need to enable the jitpack repository:

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

For more info on Jitpack, see <http://jitpack.io/> .
