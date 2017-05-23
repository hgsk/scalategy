package gdxs.scenes.scene2d.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Skin, Label => GdxLabel}

class Label(text: CharSequence, labelStyle: LabelStyle) extends GdxLabel(text, labelStyle) {
  def this(text: CharSequence, skin: Skin) = this(text, skin.get(classOf[LabelStyle]))
  def this(text: CharSequence, skin: Skin, styleName: String) = this(text, skin.get(styleName, classOf[LabelStyle]))
  def this(text: CharSequence, skin: Skin, fontName: String, color: Color) = this(text, new LabelStyle(skin.getFont(fontName), color))
  def this(text: CharSequence, skin: Skin, fontName: String, colorName: String) = this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)))
}
object Label {
  def apply(text: CharSequence, labelStyle: LabelStyle): Label = new Label(text, labelStyle)
  def apply(text: CharSequence, skin: Skin): Label = new Label(text, skin)
  def apply(text: CharSequence, skin: Skin, styleName: String): Label = new Label(text, skin, styleName)
  def apply(text: CharSequence, skin: Skin, fontName: String, color: Color): Label = new Label(text, skin, fontName, color)
  def apply(text: CharSequence, skin: Skin, fontName: String, colorName: String): Label = new Label(text, skin, fontName, colorName)
}
