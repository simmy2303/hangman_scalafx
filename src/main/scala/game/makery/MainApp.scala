package game.makery

import game.makery.controller.GameController
import game.makery.model.{ScoreManager, SoundEffects}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.image.Image
import scalafx.stage.WindowEvent
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp {
  private val soundEffects = new SoundEffects()
  private val scoreManager = ScoreManager.getInstance

  stage = new PrimaryStage {
    title = "Hangman"
    icons.add(new Image(getClass.getResourceAsStream("/game.makery.view/game.makery.images/icon.png")))
    scene = new Scene {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString
    }
  }

  showWelcomeScreen()
  stage.onCloseRequest = (event: WindowEvent) => {
    scoreManager.saveScores()
  }
  private def loadScene(fxml: String): jfxs.layout.AnchorPane = {
    val resource = getClass.getResource(fxml)
    if (resource == null) {
      throw new RuntimeException(s"$fxml resource not found")
    }
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    loader.getRoot[jfxs.layout.AnchorPane]
  }

  def showWelcomeScreen(): Unit = {
    val welcomeScreen = loadScene("/game.makery.view/Welcome.fxml")

    stage.scene = new Scene(welcomeScreen) {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString

    }

  }

  def showDifficultyScreen(): Unit = {
    val difficultyScreen = loadScene("/game.makery.view/Difficulty.fxml")
    stage.scene = new Scene(difficultyScreen) {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString
    }
  }

  def showTutorial(): Unit = {
    val tutorialScreen = loadScene("/game.makery.view/Tutorial.fxml")
    stage.scene = new Scene(tutorialScreen) {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString
    }
  }

  def showTutorial2(): Unit = {
    val tutorial2Screen = loadScene("/game.makery.view/Tutorial2.fxml")
    stage.scene = new Scene(tutorial2Screen) {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString
    }
  }
  def exitMessage(): Unit={
    val alert = new Alert(AlertType.Confirmation) {
      initOwner(stage)
      soundEffects.clickButton()
      title = "Confirmation Dialog"
      headerText = "Exiting might cause unsaved changes to be lost."
      contentText = "Would you like to proceed?"
    }

    val result = alert.showAndWait()

    result match {
      case Some(ButtonType.OK) => System.exit(0)
      case _ => println("Cancel or closed")
    }

  }
  def handleMute(): Unit = {
    soundEffects.toggleMute()
    soundEffects.clickButton()
  }
  def loadGame(difficulty: String): Unit = {
    val resource = getClass.getResource("/game.makery.view/Game.fxml")
    if (resource == null) {
      throw new RuntimeException("Game.fxml resource not found")
    }
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val gameScreen = loader.getRoot[jfxs.layout.AnchorPane]
    val gameController = loader.getController[GameController#Controller]

    stage.scene = new Scene(gameScreen) {
      stylesheets += getClass.getResource("/game.makery.view/theme.css").toString
    }
    gameController.startGame(difficulty)
  }
  soundEffects.bgMusic()



}






