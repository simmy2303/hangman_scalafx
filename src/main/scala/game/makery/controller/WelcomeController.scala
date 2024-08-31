package game.makery.controller

import game.makery.MainApp
import game.makery.MainApp.exitMessage
import game.makery.MainApp.handleMute

import game.makery.model.SoundEffects
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class WelcomeController() {
  private val soundEffects = new SoundEffects()

  def getPlay(): Unit = {
    MainApp.showDifficultyScreen()
    soundEffects.clickButton()

    print("going to play")

  }
  def getTutorial(): Unit = {
    MainApp.showTutorial()
    print("going to tutorial")
    soundEffects.clickButton()



  }
  def handleExit(action: ActionEvent): Unit = {
    exitMessage()

  }
  def handleMuteButton(): Unit = {
    handleMute()
  }
}
