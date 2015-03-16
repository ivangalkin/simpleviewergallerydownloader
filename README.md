# Introduction #

After you start downloader you have to fill two mandatory fields:

  1. URL of Gallery's XML description (don't forget leading 'http://'). If the entered URL is wrong, it will be highlighted with red color. You can find the reason pointing on the text field. The black text color is a sign of a (more or less) proper URL.
  1. Path to your local directory, where downloaded pictures will be stored in

Since version 0.2 you can download images protected by [basic access authentication](http://en.wikipedia.org/wiki/Basic_access_authentication). Just check the flag 'Protected gallery' and enter your credentials.

After you entered all required parameters you can click on download button.

# How to find XML address #

Usually you will find an URL to desired XML description opening the HTML source code of gallery's web page. Sooner or later anywhere you will see something like

```
	<div id="flashcontent">SimpleViewer requires Adobe Flash. <a href="http://www.macromedia.com/go/getflashplayer/">Get Adobe Flash.</a> If you have Flash installed, <a href="index.html?detectflash=false">click to view gallery</a>.</div>	
	<script type="text/javascript">
		var fo = new SWFObject("viewer.swf", "viewer", "100%", "100%", "7", "#333333");	
		fo.addVariable("preloaderColor", "0xffffff");
		fo.addVariable("xmlDataPath", "gallery.xml");	
		fo.addVariable( "langOpenImage", "Bild in neuem Fenster öffnen" );
		fo.addVariable("langAbout", "Info");	
		fo.write("flashcontent");	
	</script>	
```

This snippet can be also placed in some HTML frame or included JavaScript. According to its URL and path of **xmlDataPath** variable you will build your address.

## Example ##

Assume you found an interesting gallery under http://www.topgallery.eu/heide/. Viewing the source code of its page you see the above mentioned script block. That means desired XML description of a gallery is stored under http://www.topgallery.eu/heide/gallery.xml.

# Known bugs and open features #

Sadly there is no official XML schema (XSD or DTD) published for SimpleViewer/AutoViewer, also there are several versions of formats, used for gallery's description. From this reason not all versions of SimpleViewer/AutoViewer are supported yet. Please try out to download the gallery and create an issue if there were some errors.

If there will be interested parties (and some free time) I will make the downloader more universal.
