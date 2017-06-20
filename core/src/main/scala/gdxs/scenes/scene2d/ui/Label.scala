package gdxs.scenes.scene2d.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Skin, Label => GdxLabel}

object Label {
  def apply(text: CharSequence, labelStyle: LabelStyle): GdxLabel = new GdxLabel(text, labelStyle)
  def apply(text: CharSequence, skin: Skin): GdxLabel = new GdxLabel(text, skin)
  def apply(text: CharSequence, skin: Skin, styleName: String): GdxLabel = new GdxLabel(text, skin, styleName)
  def apply(text: CharSequence, skin: Skin, fontName: String, color: Color): GdxLabel = new GdxLabel(text, skin, fontName, color)
  def apply(text: CharSequence, skin: Skin, fontName: String, colorName: String): GdxLabel = new GdxLabel(text, skin, fontName, colorName)
}
