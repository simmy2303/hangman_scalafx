package game.makery.model

import scala.util.Random

class GameState(word: Word) {
  private var guessedLetters: Set[Char] = Set.empty
  private var remainingAttempts: Int = 8
  val startTime: Long = System.currentTimeMillis()

  private val gameTimeLimit: Long = word.difficulty match {
    case "Easy" => 5 * 60 * 1000
    case "Normal" => 3 * 60 * 1000
    case "Hard" => 1 * 60 * 1000
  }
  private var hintsRemaining: Int = word.difficulty match {
    case "Easy" => 3
    case "Normal" => 2
    case "Hard" => 1
  }

  def guessLetter(letter: Char): Boolean = {
    if (!guessedLetters.contains(letter)) {
      guessedLetters += letter
      if (!word.value.contains(letter)) {
        remainingAttempts -= 1
        false
      } else {
        true
      }
    } else {
      false
    }
  }

  def isGameWon: Boolean = word.value.forall(guessedLetters.contains)
  def isGameLost: Boolean = remainingAttempts <= 0 || isTimeUp
  def getDisplayWord: String = word.value.map(c => if (guessedLetters.contains(c)) c else '_').mkString(" ")
  def getRemainingAttempts: Int = remainingAttempts
  def getWord: String = word.value
  def getHangmanStage: String = HangmanStages.stages(8 - remainingAttempts)

  def getRemainingTime: Long = {
    val elapsedTime = System.currentTimeMillis() - startTime
    Math.max(0, gameTimeLimit - elapsedTime)
  }

  def isTimeUp: Boolean = getRemainingTime <= 0

  def getHint: String = {
    if (hintsRemaining > 0) {
      hintsRemaining -= 1

      val hintLimit = word.difficulty match {
        case "Easy" => 2
        case "Normal" => 1
        case "Hard" => 0
      }

      val providedHint = if (hintsRemaining == hintLimit) {
        word match {
          case hw: HardWord => hw.hint
          case mw: MediumWord => mw.hint
          case ew: EasyWord => ew.hint
          case _ => "No hint available for this word."
        }
      } else {
        val unguessedLetters = word.value.filter(!guessedLetters.contains(_))
        if (unguessedLetters.nonEmpty) {
          val hintLetter = unguessedLetters(Random.nextInt(unguessedLetters.length))
          s"The word contains the letter '${hintLetter.toUpper}'."
        } else {
          "No more letters to hint."
        }
      }


      s"Hint: $providedHint"
    } else {
      "No more hints available."
    }
  }


  def getRemainingHints: Int = hintsRemaining
}

object HangmanStages {
  val stages = Vector(
    """
      |
      |
      |
      |
      |
      |
    """.stripMargin,
    """
      |  +
      |  |
      |  |
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |
      |  |
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |   |
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |  /|
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |  /|\
      |  |
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |  /|\
      |  |  /
      |  |
      |==
    """.stripMargin,
    """
      |  +---+
      |  |   O
      |  |  /|\
      |  |  / \
      |  |
      |==
    """.stripMargin
  )
}
