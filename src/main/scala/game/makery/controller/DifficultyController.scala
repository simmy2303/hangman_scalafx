package game.makery.controller

import game.makery.MainApp
import game.makery.MainApp.{exitMessage, handleMute}
import game.makery.model.{GameResult, ScoreManager, SoundEffects}
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, ButtonType, Tooltip}
import scalafxml.core.macros.sfxml

  @sfxml
  class DifficultyController (
                               private val easyButton: Button,
                               private val normalButton: Button,
                               private val hardButton: Button,
                               private val highScoresButton: Button
                             ) {
    private val soundEffects = new SoundEffects()
    private val scoreManager = ScoreManager.getInstance

    easyButton.tooltip = new Tooltip("Easy: 5 minutes, 8 lives, 3 hints.")
    normalButton.tooltip = new Tooltip("Normal: 3 minutes, 8 lives, 2 hints.")
    hardButton.tooltip = new Tooltip("Hard: 1 minute, 8 lives, 1 hint.")

    def handleShowHighScores(): Unit = {
      soundEffects.clickButton()


      val allHighScores = scoreManager.getAllHighScores
      val scoresText = formatAllHighScores(allHighScores)

      val deleteButtonType = new ButtonType("Delete All")

      val alert = new Alert(AlertType.Information) {
        initOwner(game.makery.MainApp.stage)
        title = "High Scores"
        headerText = "Current High Scores"
        contentText = scoresText
        buttonTypes = Seq(ButtonType.OK, deleteButtonType)
      }
      val result = alert.showAndWait()

      result match {
        case Some(buttonType) if buttonType == deleteButtonType =>
          println("Delete All button pressed")
          handleDeleteScores()
        case Some(ButtonType.OK) =>
          println("OK button pressed")
        case _ =>
          println("Dialog closed or other action")
      }
    }


    private def formatAllHighScores(allHighScores: Map[String, List[GameResult]]): String = {
      val difficulties = List("Easy", "Normal", "Hard")
      difficulties.map { difficulty =>
        val scores = allHighScores.getOrElse(difficulty, List.empty)
        s"$difficulty:\n${formatHighScores(scores)}"
      }.mkString("\n\n")
    }

    private def formatHighScores(scores: List[GameResult]): String = {
      if (scores.isEmpty) {
        "-"
      } else {
        scores.zipWithIndex.map { case (result, index) =>
          f"${index + 1}. Time: ${result.timeInSeconds}s, Score: ${result.score}"
        }.mkString("\n")
      }
    }
    private def handleDeleteScores(): Unit = {
      scoreManager.deleteAllScores()
    }

    def handleBack(): Unit = {
      MainApp.showWelcomeScreen()
      soundEffects.clickButton()

    }

    def handleEasy(action: ActionEvent): Unit = {
      startGame("Easy")
      soundEffects.clickButton()

    }

    def handleNormal(action: ActionEvent): Unit = {
      startGame("Normal")
      soundEffects.clickButton()

    }

    def handleHard(action: ActionEvent): Unit = {
      startGame("Hard")
      soundEffects.clickButton()

    }

    def startGame(difficulty: String): Unit = {
      MainApp.loadGame(difficulty: String)
      soundEffects.clickButton()


    }

    def handleExit(action: ActionEvent): Unit = {
      exitMessage()


    }

    def handleMuteButton(): Unit = {
      handleMute()
    }

  }