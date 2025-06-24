# Person Details Screen

[Back to index](index.md)

<img src="img/person_details/screen.png" style="width:600px;" />

### Screen View

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/person-details")
    putString("status", "<STATUS>") 
}

analytics.logEvent("screen_view", params)
```
> - Possible values for the **STATUS** parameter are: **loading**, **success**, or **error**.

<img src="img/media_item.png" style="width:450px;" />

### Click – Back

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/person-details")
    putString("detail", "back")
}

analytics.logEvent("click", params)
```

### Media Item


#### Click

```kotlin
val params = Bundle().apply {
    putString("custom_path", "/person-details")
    putString("detail", "media-item")
    putLong("item_id", 42L) // This id value will change dinamically.
}

analytics.logEvent("click", params)
```

[Back to index](index.md)