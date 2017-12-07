# PDF Viewer

The PDF Viewer can be used as a presenter for slides in PDF format, e.g. made in LaTeX.

When started the presentation is shown in fullscreen on one screen only.
You can navigate through slides using the arrow key, Enter or Space, by clicking any mouse button or using the mouse wheel.
You can exit the viewer by hitting the ESC-button.


Run PDF viewer
-

Run the following command to start presenting.
If there is just one screen attached to our computer then just that screen is used.
If there are two screens or more the PDF viewer runs in multi-screen mode automatically.
In multi-screen mode just two screens are used even if there are more screens attached.

`java -jar pdf-viewer-1.0-SNAPSHOT-jar-with-dependencies.jar myPresentation.pdf`

`java -jar pdf-viewer-1.0-SNAPSHOT-jar-with-dependencies.jar -pdf myPresentation.pdf`

If you want to run your presentation in single-screen mode even with more screens attached add one of these parameters:

- `-single true`
- `-s`

There is a debug mode that runs the viewer in a window instead of fullscreen.
In that mode you can stop at breakpoints easily.

- `-debug true`
- `-d`

If you use any additional parameters you have to specify the PDF file with the `-pdf` parameter.


Build
-

To build the project you have to build the [PDFrenderer](https://github.com/katjas/PDFrenderer) dependency first.
That's a fork of the original PDFrenderer but implements some features, e.g. the PDF pattern type 2.

Just clone that repository and install it into your local maven repository.
There might be an issue when you try to build the project with Java 8. My quick fix was to update the property `java.version` from `1.6` to `1.8` inside the pom.xml.
Afterwards when the dependency is installed you can build this project.


Indeterminate roadmap
-

- Implement interactive mode where the user can select the PDF file to present.
- Implement precaching at startup so that browsing through slides might be faster in some presentations


