package game.makery.controller

import game.makery.MainApp
import game.makery.MainApp.exitMessage
import game.makery.model.SoundEffects
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml


@sfxml
class TutorialController {
  private val soundEffects = new SoundEffects()

  def handleBack(): Unit = {
    // Call the method to show the welcome screen
    MainApp.showWelcomeScreen()
    soundEffects.clickButton()

  }
  def handleExit(action: ActionEvent): Unit = {
    exitMessage()
  }
  def getTutorial2(action: ActionEvent): Unit = {
    MainApp.showTutorial2()
    soundEffects.clickButton()

  }
}