package game.makery.model

case class EasyWord(value: String, theme: String, hint: String) extends Word {
  def difficulty = "Easy"
}

object EasyWord {
  private val words = List(
    ("book", "Object", "A set of written or printed pages bound together"),
    ("fish", "Animal", "A limbless cold-blooded vertebrate animal with gills"),
    ("moon", "Astronomy", "The natural satellite of Earth"),
    ("shoe", "Clothing", "A covering for the foot, typically made of leather"),
    ("chair", "Furniture", "A piece of furniture for sitting on"),
    ("orange", "Fruit", "A citrus fruit with a tough bright reddish-yellow rind"),
    ("river", "Nature", "A large natural stream of water flowing to the sea"),
    ("snow", "Weather", "Precipitation in the form of ice crystals"),
    ("python", "Coding", "A high-level programming language known for its readability"),
    ("bird", "Animal", "A warm-blooded egg-laying vertebrate distinguished by feathers"),
    ("peach", "Fruit", "A soft round fruit with a fuzzy yellowish-red skin"),
    ("bridge", "Structure", "A structure carrying a road or path across a river"),

    ("pencil", "Stationery", "A writing instrument with a graphite core"),
    ("bread", "Food", "A staple food made from flour and water")
  )

  def getRandomWord(): EasyWord = {
    val (word, theme, hint) = words(util.Random.nextInt(words.length))
    EasyWord(word, theme, hint)
  }

  def getTheme(word: String): String = {
    words.find(_._1 == word).map(_._2).getOrElse("Unknown")
  }

  def getHint(word: String): String = {
    words.find(_._1 == word).map(_._3).getOrElse("No hint available")
  }
}
