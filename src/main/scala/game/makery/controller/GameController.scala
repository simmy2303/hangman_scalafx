package game.makery.controller

import game.makery.MainApp
import game.makery.MainApp.{exitMessage, handleMute, stage}
import game.makery.model._
import javafx.event.{ActionEvent => JfxActionEvent}
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.text.Text
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

@sfxml
class GameController(
                      private val wordLabel: Label,
                      private val guessTextField: TextField,
                      private val guessButton: Button,
                      private val messageLabel: Label,
                      private val attemptsLabel: Text,
                      private val hangmanDrawing: TextArea,
                      private val timerLabel: Label,
                      private val hintLabel: Text,
                      private val hintButton: Button,
                      private val scoreLabel:Label,
                      private val themeLabel:Text,
                      private val difficultyText:Text

                    ) {
  private val soundEffects = new SoundEffects()

  var currentGame: Option[GameState] = None
  var timeline: Option[Timeline] = None
  var currentDifficulty: String = _
  var currentScore: Int = 0
  private val scoreManager = ScoreManager.getInstance


  def startGame(difficulty: String): Unit = {
    currentDifficulty = difficulty
    currentScore = 0

    val word: Word = difficulty match {
      case "Easy" => EasyWord.getRandomWord()
      case "Normal" => MediumWord.getRandomWord()
      case "Hard" => HardWord.getRandomWord()
      case _ => EasyWord.getRandomWord()
    }
    themeLabel.text = s"Theme: ${word.theme}"

    currentGame = Some(new GameState(word))
    enableGameControls()
    updateUI()
    messageLabel.style = ""
    messageLabel.text = s"Game started with $difficulty difficulty. Good luck!"
    startTimer()
  }

  def handleGuess(): Unit = {
    currentGame.foreach { game =>
      val guess = guessTextField.text.value.trim.toLowerCase
      if (guess.length == 1) {
        val guessedLetter = guess.head
        if (game.guessLetter(guessedLetter)) {
            messageLabel.text = "Correct guess!"
            currentScore += 1
            soundEffects.playCorrect()
            messageLabel.style = "-fx-text-fill: green; -fx-font-weight: Bold;"

        } else {
            messageLabel.text = "Incorrect guess!"
            messageLabel.style = "-fx-text-fill: red; -fx-font-weight: Bold;"
            soundEffects.playIncorrect()

        }
        updateUI()
        checkGameEnd()
      } else {
        messageLabel.style = ""
        messageLabel.text = "Please enter a single letter"
        val alert = new Alert(AlertType.Warning) {
          initOwner(stage)
          title = "Warning Dialogue"
          headerText = "You can't guess an empty field/ multiple letters!"
          contentText = "Please enter a single letter."
        }

        val result = alert.showAndWait()
      }
      guessTextField.text = ""
    }
  }

  def updateUI(): Unit = {
    currentGame.foreach { game =>
      difficultyText.text = currentDifficulty
      wordLabel.text = game.getDisplayWord
      attemptsLabel.text = s"Lives: ${game.getRemainingAttempts}"
      hangmanDrawing.text = game.getHangmanStage
      scoreLabel.text = s"Score: $currentScore"
      updateTimerLabel(game.getRemainingTime)
      updateHintButton(game.getRemainingHints)
    }
  }

  def updateHintButton(remainingHints: Int): Unit = {
    hintLabel.text = s"Hint ($remainingHints)"
    hintButton.disable = remainingHints == 0
  }

  def handleHint(): Unit = {
    currentGame.foreach { game =>
      messageLabel.style = ""
      messageLabel.text = game.getHint
      updateHintButton(game.getRemainingHints)
      soundEffects.clickButton()
    }
  }

  def checkGameEnd(): Unit = {
    currentGame.foreach { game =>
      if (game.isGameWon || game.isGameLost || game.isTimeUp) {

        timeline.foreach(_.stop())
        endGame()

        if (game.isGameWon) {
          val timeInSeconds = ((System.currentTimeMillis() - game.startTime) / 1000).toInt
          messageLabel.text = "Congratulations! You won!"
          messageLabel.style = "-fx-text-fill: green; -fx-font-weight: Bold;"

          val isNewHighScore = scoreManager.updateHighScore(currentDifficulty, currentScore, timeInSeconds)
          val highScores = scoreManager.getHighScores(currentDifficulty)
          val highScoreMessage = if (isNewHighScore) "New High Score!" else s"High Scores for ${currentDifficulty}:"

          val alert = new Alert(AlertType.Information) {
            initOwner(stage)
            title = "You Won!"
            headerText = s"Congratulations, you won!\nTime: ${timeInSeconds}s, Score: ${currentScore}"
            contentText = s"The word was: ${game.getWord}\n\n$highScoreMessage\n\n${formatHighScores(highScores)}\n\nDo you want to restart?"
          }

          val result = alert.showAndWait()

          result match {
            case Some(ButtonType.OK) => startGame(currentDifficulty)
            case _ => MainApp.showDifficultyScreen()
          }
        } else {
          messageLabel.text = s"You Lost! The word was: ${game.getWord}"
          messageLabel.style = "-fx-text-fill: red; -fx-font-weight: Bold;"
          soundEffects.playGameOver()
          println("Playing game over sound")

          val alert = new Alert(AlertType.Information) {
            initOwner(stage)
            title = "You Lost."
            headerText = s"The correct word was: ${game.getWord}"
            contentText = "Do you want to restart?"
          }

          val result = alert.showAndWait()

          result match {
            case Some(ButtonType.OK) => handleRedo()
            case _ => MainApp.showDifficultyScreen()
          }
        }

        scoreManager.saveScores()
      }
    }
  }
  def formatHighScores(scores: List[GameResult]): String = {
    scores.zipWithIndex.map { case (result, index) =>
      f"${index + 1}. Time: ${result.timeInSeconds}s, Score: ${result.score}"
    }.mkString("\n")
  }
  def startTimer(): Unit = {

    timeline.foreach(_.stop())

    timeline = Some(new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(Duration(1000), onFinished = (_: JfxActionEvent) => {
          currentGame.foreach { game =>
            if (game.isTimeUp) {
              soundEffects.playGameOver()
              stop()

              messageLabel.style = "-fx-text-fill: red; -fx-font-weight: bold;"
              messageLabel.text = s"Time's up! The word was: ${game.getWord}"

              endGame()
              checkGameEnd()
            } else {
              updateTimerLabel(game.getRemainingTime)
            }
          }
        })
      )
    })
    timeline.foreach(_.play())
  }

  def updateTimerLabel(remainingTime: Long): Unit = {
    val minutes = remainingTime / 60000
    val seconds = (remainingTime % 60000) / 1000
    timerLabel.text = f"Time: $minutes%d:$seconds%02d"
  }

  def endGame(): Unit = {
    disableGameControls()
    timeline.foreach(_.stop())
  }

  def enableGameControls(): Unit = {
    guessTextField.disable = false
    guessButton.disable = false
    hintButton.disable = false
  }

  def disableGameControls(): Unit = {
    guessTextField.disable = true
    guessButton.disable = true
    hintButton.disable = true
  }

  def handleBack(): Unit = {
    endGame()
    soundEffects.clickButton()
    MainApp.showDifficultyScreen()
  }
  def handleMuteButton(): Unit = {
    handleMute()
  }
  def handleExit(): Unit = {
    scoreManager.saveScores()
      exitMessage()

  }

  def handleRedo(): Unit = {
    soundEffects.clickButton()
    startGame(currentDifficulty)
  }





}
