package net.leejjon.bluffpoker.android

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Main : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("libgdx.png") }

    override fun render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch.begin()
        batch.draw(image, 140f, 210f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
    }
}