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

> - Possible values for the **STATUS** parameter are: **activating** and **deactivating**.


### Providers

<img src="img/media_details/providers.png" style="width:350px;" />

#### Click 
```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "select-streaming")
    putLong("item_id", 42L) // This id value will change dinamically.
}

analytics.logEvent("click", params)
```
> - In this tagging we will create a logic to get the **streaming** id clicked dynamically.

### Genres

<img src="img/media_details/genres.png" style="width:250px;" />

#### Click 
```kotlin
val params = Bundle().apply {
    putString("custom_path", "/media-details")
    putString("detail", "select-genre")
     putLong("item_id", 42L) // This id value will change dinamically.
}

analytics.logEvent("click", params)
```
> - In this tagging we will create a logic to get the **genre** id clicked dynamically.


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
    putLong("item_id", 42L) // This id value will change dinamically.
}

analytics.logEvent("click", params)
```

> - In this tagging we will create a logic to get the **media** id clicked dynamically.

[Back to index](index.md)
