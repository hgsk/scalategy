package gdxs.scenes.scene2d.ui

import com.badlogic.gdx.graphics.g2d.{BitmapFont, NinePatch}
import com.badlogic.gdx.scenes.scene2d.ui.{Button => GdxButton, TextButton => GdxTextButton}
import com.badlogic.gdx.scenes.scene2d.utils.{ClickListener, Drawable, NinePatchDrawable}
import com.badlogic.gdx.scenes.scene2d.{EventListener, InputEvent}

object Button {
  def text(text: String, bitmapFont: BitmapFont): TextButton.Builder = TextButton.builder(text, bitmapFont)
}

object TextButton {
  def builder(text: String, bitmapFont: BitmapFont) = Builder(text, Style.builder(bitmapFont))
  case class Builder(
    text: String,
    styleBuilder: Style.Builder,
    eventListenerBuilder: EventListenerBuilder = EventListenerBuilder()
  ) {
    def text(text: String): Builder = copy(text = text)
    def font(bitmapFont: BitmapFont): Builder = copy(styleBuilder = styleBuilder.font(bitmapFont))
    def up(drawable: Drawable): Builder = copy(styleBuilder = styleBuilder.up(drawable))
    def up(ninePatch: NinePatch): Builder = up(new NinePatchDrawable(ninePatch))
    def down(drawable: Drawable): Builder = copy(styleBuilder = styleBuilder.down(drawable))
    def down(ninePatch: NinePatch): Builder = down(new NinePatchDrawable(ninePatch))
    def over(drawable: Drawable): Builder = copy(styleBuilder = styleBuilder.over(drawable))
    def over(ninePatch: NinePatch): Builder = over(new NinePatchDrawable(ninePatch))
    def clicked(f: () => Unit): Builder = copy(eventListenerBuilder = eventListenerBuilder.clicked(f))
    def build(): GdxButton = {
      val textButton = new GdxTextButton(text, styleBuilder.build())
      eventListenerBuilder.build().foreach(textButton.addListener)
      textButton
    }
  }
  object Style {
    import GdxTextButton.{TextButtonStyle => GdxStyle}
    def builder(bitmapFont: BitmapFont) = Builder(bitmapFont)
    case class Builder(
      bitmapFont: BitmapFont,
      up: Option[Drawable] = None,
      down: Option[Drawable] = None,
      over: Option[Drawable] = None
    ) {
      def build(): GdxStyle = {
        val style = new GdxStyle(up.orNull, down.orNull, null, bitmapFont)
        style.over = over.orNull
        style
      }
      def font(bitmapFont: BitmapFont): Builder = copy(bitmapFont = bitmapFont)
      def up(drawable: Drawable): Builder = copy(up = Some(drawable))
      def down(drawable: Drawable): Builder = copy(down = Some(drawable))
      def over(drawable: Drawable): Builder = copy(over = Some(drawable))
    }
  }
}

case class EventListenerBuilder(clicked: Option[() => Unit] = None) {
  builder =>
  def build(): Option[EventListener] = if (nonEmpty) {
    val eventListener = new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = builder.clicked.foreach(f => f())
    }
    Some(eventListener)
  } else None
  def nonEmpty: Boolean = clicked.nonEmpty
  def clicked(f: () => Unit): EventListenerBuilder = copy(clicked = Some(f))
}

