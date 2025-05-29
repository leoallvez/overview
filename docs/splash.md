# Splash Screen
[Back to index](index.md)


<img src="img/splash_screen.png" style="width:200px;" />

### Screen View

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/splash")
}

analytics.logEvent("screen_view", params)
```
> There are no **status** parameters in this tag.




