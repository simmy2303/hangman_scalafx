package game.makery.model

abstract class Word {
  val value: String
  val theme: String
  val hint: String

  def difficulty: String
}