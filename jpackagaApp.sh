rm -rf ~/Documents/Polyphonies/Orchestration/actionPdfToJpeg.app
jpackage \
  --type app-image \
  --input target  \
  --main-jar PdfToJpeg-1.0-jar-with-dependencies.jar \
  --name actionPdfToJpeg \
  --main-class control.Control \
  --dest /Users/heynerrichard/Documents/Polyphonies/Orchestration \
  --arguments "/Users/heynerrichard/Documents/Polyphonies/Orchestration" \
  --verbose
