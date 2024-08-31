package game.makery.model

import scala.collection.mutable
import java.io._
import scala.util.{Try, Failure, Success}

case class GameResult(score: Int, timeInSeconds: Int)

class ScoreManager {
  private val highScores = mutable.Map[String, List[GameResult]]()
  private val fileName = "highscores.dat"

  loadScores()

  def updateHighScore(difficulty: String, score: Int, timeInSeconds: Int): Boolean = {
    val newResult = GameResult(score, timeInSeconds)
    val currentHighScores = highScores.getOrElse(difficulty, List.empty)
    val updatedHighScores = (newResult :: currentHighScores)
      .sortBy(result => (result.timeInSeconds))
      .take(3)

    Try {
      highScores(difficulty) = updatedHighScores
      saveScores()
    } match {
      case Success(_) =>
        println("High score updated successfully.")
        updatedHighScores.head == newResult
      case Failure(exception) =>
        println(s"Failed to update high score: ${exception.getMessage}")
        false
    }
  }
  def deleteAllScores(): Unit = {
    highScores.clear()
    saveScores()
    println("All scores have been deleted.")
  }
  def getHighScores(difficulty: String): List[GameResult] = {
    highScores.getOrElse(difficulty, List.empty)
  }

  def getAllHighScores: Map[String, List[GameResult]] = {
    val difficulties = List("Easy", "Normal", "Hard")
    difficulties.map(diff => diff -> highScores.getOrElse(diff, List.empty)).toMap
  }
  def saveScores(): Unit = {
    Try {
      val oos = new ObjectOutputStream(new FileOutputStream(fileName))
      oos.writeObject(highScores.toMap)
      oos.close()
      println(s"Scores saved to $fileName")
    } match {
      case Success(_) => println("Scores saved successfully.")
      case Failure(exception) => println(s"Failed to save scores: ${exception.getMessage}")
    }
  }

  private def loadScores(): Unit = {
    Try {
      val ois = new ObjectInputStream(new FileInputStream(fileName))
      val loadedScores = ois.readObject().asInstanceOf[Map[String, List[GameResult]]]
      ois.close()
      highScores.clear()
      highScores ++= loadedScores
      println(s"Scores loaded from $fileName")
    } match {
      case Success(_) => println("Scores loaded successfully.")
      case Failure(exception) => println(s"Failed to load scores: ${exception.getMessage}")
    }
  }
}

object ScoreManager {
  private val instance = new ScoreManager()
  def getInstance: ScoreManager = instance
}
