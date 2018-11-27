package com.github.kaikis.dmg.resourceloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一的资源加载类，思想是预先定义了一些资源的加载方法，遍历去查询，找到后返回<br>
 * 预先定义了两种查找方法<br>
 * <ul>
 * <li>通过Classpath</li>
 * <li>通过File</li>
 * </ul>
 */
public class ResourceLoader {
    /**
     * The list of locations to be searched
     */
    private static List<ResourceLocation> locations = new ArrayList<>();

    static {
        locations.add(new ClasspathLocation());
        locations.add(new FileSystemLocation(new File(".")));
    }

    public static InputStream getResourceAsStream(String ref) {
        InputStream in = null;
        for (ResourceLocation location1 : locations) {
            in = location1.getResourceAsStream(ref);
            if (in != null) {
                break;
            }
        }
        if (in == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }

        return new BufferedInputStream(in);
    }


    public static boolean resourceExists(String ref) {
        URL url;
        for (ResourceLocation location1 : locations) {
            url = location1.getResource(ref);
            if (url != null) {
                return true;
            }
        }

        return false;
    }

    public static URL getResource(String ref) {
        URL url = null;
        for (ResourceLocation location1 : locations) {
            url = location1.getResource(ref);
            if (url != null) {
                break;
            }
        }
        if (url == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }

        return url;
    }
}
