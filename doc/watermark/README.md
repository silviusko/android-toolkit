Water Mark
==========

How to use
----------

You can use

- **WaterMarker.markImage()** to get a bitmap with watermark string
    ```
    Bitmap waterMarkedBitmap = WaterMarker.markImage(bitmap, "Hello World\n2016/03/08 12:34");
    ```

or

- **WaterMarker.markImageFile()** to output a file with watermark string
    ```
    WaterMarker.markImageFile(srcFile, destFile, "Hello World\n2016/03/08 12:34");
    ```

Limitation
----------

- Input is split a message by '\n' character only
- Output is compressed to the PNG file only, not support other types
