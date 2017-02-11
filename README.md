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

If you use any additional parameters you have to specify the PDF file with the `-pdf` parameter.


Indeterminate roadmap
-
- Implement interactive mode where the user can select the PDF he wants to present.


Known issues
-
- From time to time the screens are freezing during start when starting presentations in multi-screen mode.
- From time to time some slides appear completely black painted.
