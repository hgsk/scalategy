package gdxs.scenes.scene2d

import com.badlogic.gdx.scenes.scene2d.{Actor, Group => GdxGroup}

class Group extends GdxGroup {
  def add(actor: Actor): Group = {
    super.addActor(actor)
    this
  }
  def +(actor: Actor): Group = add(actor)
}
object Group {
  def apply(): Group = new Group()
}

