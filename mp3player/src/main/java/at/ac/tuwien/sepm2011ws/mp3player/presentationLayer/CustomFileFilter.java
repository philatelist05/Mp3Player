package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter {
	String[] extensions;
	String description;

	public CustomFileFilter(String ext) {
		this(new String[] { ext }, null);
	}

	public CustomFileFilter(String[] exts, String descr) throws NullPointerException {
		String tempext = null;
		// Clone and lowercase the extensions
		if (exts == null)
			exts = new String[] {"wav"};
		extensions = new String[exts.length];
		for (int i = 0; i < exts.length; i++) {
			extensions[i] = "." + exts[i].toLowerCase();
			if (tempext != null)
				tempext = tempext + ", " + "*" + extensions[i];
			else
				tempext = "*" + extensions[i];
		}

		// Make sure we have a valid (if simplistic) description
		if (descr == null) {
			description = "(" + tempext + ") files";
		} else
			description = "" + descr + " (" + tempext + ")";
	}

	public boolean accept(File f) throws NullPointerException {
		// We always allow directories, regardless of their extension
		if (f.isDirectory()) {
			return true;
		}

		// Ok, it's a regular file, so check the extension
		String name = f.getName().toLowerCase();
		for (int i = extensions.length - 1; i >= 0; i--) {
			if (name.endsWith(extensions[i])) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}
}