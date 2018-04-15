package net.leejjon.bluffpoker.enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * When running the tests from Gradle the file location is different from runtime. This class is in the same package as TextureKey to provide a custom Map of Textures for the tests.
 */
public class TextureTestLoader {
    public static ObjectMap<TextureKey, Texture> getAllTexturesForInUnitTest(String relativePath) {
        ObjectMap<TextureKey, Texture> textureMap = new ObjectMap<>();
        for (TextureKey t : TextureKey.values()) {
            Texture texture = t.get(relativePath);
            if (texture != null) {
                textureMap.put(t, texture);
            }
        }
        return textureMap;
    }
}
