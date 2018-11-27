package com.github.kaikis.dmg.resourceloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileSystemLocation implements ResourceLocation {
    private File root;

    public FileSystemLocation(File root) {
        this.root = root;
    }

    /**
     * 通过{@link File}定位资源文件，默认会定位到当前模块下<br>
     * 若打成jar包，会定位到jar包所在路径
     *
     * @param ref
     * @return
     */
    @Override
    public URL getResource(String ref) {
        try {
            File file = new File(root, ref);
            if (!file.exists()) {
                file = new File(ref);
            }
            if (!file.exists()) {
                return null;
            }

            return file.toURI().toURL();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(String ref) {
        try {
            File file = new File(root, ref);
            if (!file.exists()) {
                file = new File(ref);
            }
            return new FileInputStream(file);
        } catch (IOException e) {
            return null;
        }
    }
}
