package com.github.kaikis.dmg.resourceloader;

import java.io.InputStream;
import java.net.URL;

public class ClasspathLocation implements ResourceLocation {

    /**
     * 返回所查找资源的URL，第一个字符不能是<b>/</b>
     * @param ref
     * @return
     */
    @Override
    public URL getResource(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResource(cpRef);
    }

    /**
     * 与{@link #getResource(String)}作用相同，只是以流的形式返回
     * @param ref
     * @return
     */
    @Override
    public InputStream getResourceAsStream(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResourceAsStream(cpRef);
    }
}
