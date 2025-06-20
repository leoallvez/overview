# Media Details Screen

[Back to index](index.md)

<img src="img/media_details/screen.png" style="width:600px;" />

### Screen View

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("status", "<STATUS>") 
}

analytics.logEvent("screen_view", params)
```
> - Possible values for the **STATUS** parameter are: **loading**, **success**, or **error**.


### Click – Back

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "back")
}

analytics.logEvent("click", params)
```

### Click – Like

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "like:<STATUS>")
}

analytics.logEvent("click", params)
```
> - Possible values for the **STATUS** parameter are: **activing** and **deactivating**.


### Providers

<img src="img/media_details/providers.png" style="width:350px;" />

#### Click 
```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "<STREAMING>")
}

analytics.logEvent("click", params)
```
> - The placeholder **STREAMING** will be replaced with the name of the selected streaming service.

### Genres

<img src="img/media_details/genres.png" style="width:250px;" />

#### Click 
```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "<GENRE>")
}

analytics.logEvent("click", params)
```
> - The placeholder **GENRE** will be replaced with the name of the clicked genre.


### Videos

<img src="img/media_details/videos.png" style="width:500px;" />

#### Click 
```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "video")
}

analytics.logEvent("click", params)
```


### Media Item

<img src="img/media_item.png" style="width:450px;" />

#### Click

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "media-item")
}

analytics.logEvent("click", params)
```

[Back to index](index.md)
