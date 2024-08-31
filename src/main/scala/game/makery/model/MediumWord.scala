package game.makery.model

case class MediumWord(value: String, theme: String, hint: String) extends Word {
  def difficulty = "Normal"
}

object MediumWord {
  private val words = List(
    ("balloon", "Object", "A rubber or plastic bag that can be inflated with air or gas"),
    ("campus", "Place", "The grounds of a school, university, or college"),
    ("curtain", "Household", "A piece of cloth that hangs down from above a window"),
    ("dentist", "Profession", "A person qualified to treat disease of the teeth"),
    ("engine", "Machine", "A machine that converts energy into motion"),
    ("fossil", "Science", "The remains or impression of a prehistoric organism"),
    ("gravity", "Science", "The force that attracts a body towards the center of the earth"),
    ("history", "Subject", "The study of past events, particularly in human affairs"),
    ("jungle", "Nature", "A dense forest, typically in a tropical area"),
    ("library", "Place", "A building collections of books for reading or borrowing"),
    ("miracle", "Event", "An extraordinary event that is not explicable by natural laws"),
    ("neutron", "Science", "A subatomic particle with no electric charge"),
    ("orchestra", "Music", "A large group of musicians who play together"),
    ("satellite", "Space", "An artificial body placed in orbit around the earth"),
    ("triangle", "Shape", "A three-sided polygon"),
    ("vaccine", "Medicine", "A substance used to provide immunity against diseases"),
    ("whistle", "Object", "A small device that makes a high-pitched sound"),
    ("xylophone", "Instrument", "A musical instrument with bars")
  )

  def getRandomWord(): MediumWord = {
    val (word, theme, hint) = words(util.Random.nextInt(words.length))
    MediumWord(word, theme, hint)
  }

  def getTheme(word: String): String = {
    words.find(_._1 == word).map(_._2).getOrElse("Unknown")
  }

  def getHint(word: String): String = {
    words.find(_._1 == word).map(_._3).getOrElse("No hint available")
  }
}
