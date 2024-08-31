package game.makery.model

case class HardWord(value: String, theme: String, hint: String) extends Word {
  def difficulty: String = "Hard"
}

object HardWord {
  private val words = List(

    ("cacophony", "Sound", "A harsh, discordant mixture of sounds"),
    ("continent", "Geography", "One of the main large landmasses on Earth"),
    ("liverpool", "Football", "An English football club"),
    ("labyrinth", "Maze", "A complicated, irregular network of passages"),
    ("mysterious", "Unknown", "Difficult or impossible to understand or explain"),
    ("serendipity", "Luck", "The occurrence of events by chance in a happy way"),
    ("vortex", "Swirl", "A mass of whirling fluid or air"),
    ("ambiguous", "Meaning", "Open to more than one interpretation"),
    ("benevolent", "Character", "Well-meaning and kindly"),
    ("galaxy", "Astronomy", "A system of millions or billions of stars"),
    ("enthusiasm", "Emotion", "Intense and eager enjoyment or interest"),
    ("mechanical", "Technology", "Relating to machinery or tools"),
    ("celebration", "Event", "The action of marking a significant event"),
    ("laboratory", "Science", "A room or building for scientific experiments"),
    ("harmony", "Music", "The combination of musical notes to produce chords"),
    ("nomad", "Person", "A member of a people having no permanent abode"),
    ("whimsical", "Mood", "Playfully quaint or fanciful"),
    ("parachute", "Equipment", "A device used to slow the descent of a person or object"),
    ("jewellery", "Decor", "Something to put on when going out for an event"),
    ("intrigue", "Interest", "Arouse the curiosity or interest of; fascinate")

  )

  def getRandomWord(): HardWord = {
    val (word, theme, hint) = words(util.Random.nextInt(words.length))
    HardWord(word, theme, hint)
  }

  def getTheme(word: String): String = {
    words.find(_._1 == word).map(_._2).getOrElse("Unknown")
  }

  def getHint(word: String): String = {
    words.find(_._1 == word).map(_._3).getOrElse("No hint available")
  }
}
