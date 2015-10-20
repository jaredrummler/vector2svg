# vector2svg
Convert [Android VectorDrawable](https://developer.android.com/reference/android/graphics/drawable/VectorDrawable.html) XML resource file to SVG

___

Download the [JAR](https://github.com/jaredrummler/vector2svg/blob/master/release/vector2svg-1.1.jar?raw=true)

Usage: java -jar [JAR] [FILES]

Each argument should be a path to a vector drawable. A new file will be created in the same directory as the vector drawable with a "svg" extension. This is useful because you cannot view a VectorDrawable with Adobe Illustrator or other similar software.

To convert an SVG to an Android VectorDrawable, use [this tool](http://inloop.github.io/svg2android/).

___

Example:
---

Original:

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="48dp"
        android:height="48dp"
        android:viewportWidth="48.0"
        android:viewportHeight="48.0">
    <path
        android:pathData="M34.9,13.2c-0.8,-0.8,-4.2,-2.4,-10.9,-2.4s-10.1,1.6,-10.9,2.4c-0.8,0.8,-2.4,4.2,-2.4,10.9s1.6,10.1,2.4,10.9    c0.8,0.8,4.2,2.4,10.9,2.4s10.1,-1.6,10.9,-2.4c0.8,-0.8,2.4,-4.2,2.4,-10.9S35.6,14,34.9,13.2z"
        android:fillColor="#FFFFFF"/>
    <path
        android:pathData="M34.7,13.7c0,0.8,-1.2,1.5,-3.1,2.1c-1.9,0.5,-4.6,0.8,-7.6,0.8s-5.6,-0.3,-7.6,-0.8    c-1.9,-0.5,-3.1,-1.2,-3.1,-2.1s1.2,-1.5,3.1,-2.1c1.9,-0.5,4.6,-0.8,7.6,-0.8s5.6,0.3,7.6,0.8C33.5,12.1,34.7,12.9,34.7,13.7z"
        android:fillColor="#EBEBEB"/>
    <path
        android:pathData="M30,13c-0.1,0,-0.1,0,-0.2,0c-0.4,-0.1,-0.7,-0.6,-0.6,-1l1.3,-5.5c0.1,-0.4,0.6,-0.7,1,-0.6c0.4,0.1,0.7,0.6,0.6,1    l-1.3,5.5C30.7,12.7,30.4,13,30,13z"
        android:fillColor="#FFFFFF"/>
    <path
        android:pathData="M18,13c-0.4,0,-0.7,-0.3,-0.8,-0.6l-1.3,-5.5c-0.1,-0.4,0.2,-0.9,0.6,-1c0.4,-0.1,0.9,0.2,1,0.6l1.3,5.5    c0.1,0.4,-0.2,0.9,-0.6,1C18.1,13,18.1,13,18,13z"
        android:fillColor="#FFFFFF"/>
</vector>
```

Output:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg viewBox="0 0 48.0 48.0">
  <path d="M34.9,13.2c-0.8,-0.8,-4.2,-2.4,-10.9,-2.4s-10.1,1.6,-10.9,2.4c-0.8,0.8,-2.4,4.2,-2.4,10.9s1.6,10.1,2.4,10.9    c0.8,0.8,4.2,2.4,10.9,2.4s10.1,-1.6,10.9,-2.4c0.8,-0.8,2.4,-4.2,2.4,-10.9S35.6,14,34.9,13.2z" fill="#FFFFFF"/>
  <path d="M34.7,13.7c0,0.8,-1.2,1.5,-3.1,2.1c-1.9,0.5,-4.6,0.8,-7.6,0.8s-5.6,-0.3,-7.6,-0.8    c-1.9,-0.5,-3.1,-1.2,-3.1,-2.1s1.2,-1.5,3.1,-2.1c1.9,-0.5,4.6,-0.8,7.6,-0.8s5.6,0.3,7.6,0.8C33.5,12.1,34.7,12.9,34.7,13.7z" fill="#EBEBEB"/>
  <path d="M30,13c-0.1,0,-0.1,0,-0.2,0c-0.4,-0.1,-0.7,-0.6,-0.6,-1l1.3,-5.5c0.1,-0.4,0.6,-0.7,1,-0.6c0.4,0.1,0.7,0.6,0.6,1    l-1.3,5.5C30.7,12.7,30.4,13,30,13z" fill="#FFFFFF"/>
  <path d="M18,13c-0.4,0,-0.7,-0.3,-0.8,-0.6l-1.3,-5.5c-0.1,-0.4,0.2,-0.9,0.6,-1c0.4,-0.1,0.9,0.2,1,0.6l1.3,5.5    c0.1,0.4,-0.2,0.9,-0.6,1C18.1,13,18.1,13,18,13z" fill="#FFFFFF"/>
</svg>
```

Now you can open the SVG in Adobe Illustrator or a similar program.

