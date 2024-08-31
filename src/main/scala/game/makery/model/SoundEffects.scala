package game.makery.model

import javafx.scene.media.AudioClip

class SoundEffects() {
  private val correctSound = new AudioClip(getClass.getResource("/game.makery.view/game.makery.sounds/correctSound.mp3").toExternalForm)
  private val incorrectSound = new AudioClip(getClass.getResource("/game.makery.view/game.makery.sounds/wrongSound.mp3").toExternalForm)
  private val gameOverSound = new AudioClip(getClass.getResource("/game.makery.view/game.makery.sounds/gameOver.mp3").toExternalForm)
  private val buttonClicked = new AudioClip(getClass.getResource("/game.makery.view/game.makery.sounds/buttonClick.mp3").toExternalForm)
  private val backgroundMusic = new AudioClip(getClass.getResource("/game.makery.view/game.makery.sounds/jazzBg.mp3").toExternalForm)
  private var muted: Boolean = false
  def toggleMute(): Unit = {
    muted = !muted
    if (muted) {
      backgroundMusic.stop()

    } else {
      backgroundMusic.play()

    }
  }

  def playCorrect(): Unit =  correctSound.play()
  def playIncorrect(): Unit = incorrectSound.play()
  def playGameOver(): Unit =  gameOverSound.play()
  def clickButton(): Unit =  buttonClicked.play()

  def bgMusic(): Unit = {
    if (!muted) {
      backgroundMusic.setCycleCount(AudioClip.INDEFINITE)
      backgroundMusic.play()
    }
  }
}
