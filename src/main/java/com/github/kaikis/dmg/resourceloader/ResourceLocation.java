package com.github.kaikis.dmg.resourceloader;

import java.io.InputStream;
import java.net.URL;

public interface ResourceLocation {

    URL getResource(String ref);

    InputStream getResourceAsStream(String ref);
}
