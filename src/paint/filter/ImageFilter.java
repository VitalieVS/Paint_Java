/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImageFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals(Utils.jpg)
                    || extension.equals(Utils.bmp)
                    || extension.equals(Utils.png)
                    || extension.equals(Utils.jpeg);
        }

        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
