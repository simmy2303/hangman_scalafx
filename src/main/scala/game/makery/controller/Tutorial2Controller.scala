package game.makery.controller

import game.makery.MainApp
import game.makery.MainApp.exitMessage
import game.makery.model.SoundEffects
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class Tutorial2Controller {
  private val soundEffects = new SoundEffects()

  def handleBack(): Unit = {
    // Call the method to show the welcome screen
    MainApp.showWelcomeScreen()
    soundEffects.clickButton()

  }
  def gettutoral1(): Unit = {
    // Call the method to show the welcome screen
    MainApp.showTutorial()
    soundEffects.clickButton()

  }
  def getPlay(): Unit={
    MainApp.showDifficultyScreen()
    soundEffects.clickButton()



  }
  def handleExit(action: ActionEvent): Unit = {
    exitMessage()

  }

}
