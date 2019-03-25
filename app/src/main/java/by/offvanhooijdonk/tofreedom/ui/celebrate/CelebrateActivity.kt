package by.offvanhooijdonk.tofreedom.ui.celebrate

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper
import by.offvanhooijdonk.tofreedom.helper.NotificationHelper
import by.offvanhooijdonk.tofreedom.helper.PrefHelper
import by.offvanhooijdonk.tofreedom.helper.celebrate.AchievementsHelper
import by.offvanhooijdonk.tofreedom.helper.celebrate.IconsAnimHelper
import by.offvanhooijdonk.tofreedom.helper.celebrate.MusicHelper
import by.offvanhooijdonk.tofreedom.helper.celebrate.ParticlesHelper
import kotlinx.android.synthetic.main.activity_celebrate.*

class CelebrateActivity : AppCompatActivity() {
    private var fireworksHelper: ParticlesHelper.Fireworks? = null
    private var confettiHelper: ParticlesHelper.Confetti? = null
    private var musicHelper: MusicHelper? = null
    private var achievementsHelper: AchievementsHelper? = null
    private var iconsHelper: IconsAnimHelper? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celebrate)

        initViews()

        NotificationHelper.removeNotification(this)
    }

    override fun onStart() {
        super.onStart()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        runCelebrations()
    }

    override fun onStop() {
        super.onStop()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        releaseAll()
    }

    private fun initViews() {
        txtTimeElapsed!!.text = getString(R.string.achiev_elapsed, DateFormatHelper.formatElapsed(
                PrefHelper.getCountdownStartDate(this), PrefHelper.getFreedomTime(this)
        ))

        fabStopReplay!!.setOnClickListener {
            if (achievementsHelper != null) achievementsHelper!!.dropToInitial()
            if (iconsHelper != null) iconsHelper!!.dropToInitial()

            fabStopReplay!!.hide()
            runCelebrations()
        }
    }

    private fun runCelebrations() {
        musicHelper = MusicHelper(applicationContext)
        playMusic()
        val particleDelay = musicHelper!!.punchTime

        fireworksHelper = ParticlesHelper.Fireworks()
        confettiHelper = ParticlesHelper.Confetti()
        prepareAchievements()
        prepareIcons()

        Handler().postDelayed({ this.startParticles() }, particleDelay.toLong())
        //new Handler().postDelayed(this::runAchievements, particleDelay); // different than the particles delay
        Handler().postDelayed({ iconsHelper!!.runIcons() }, particleDelay.toLong()) // different than the particles delay
    }

    private fun prepareAchievements() {
        achievementsHelper = AchievementsHelper.Builder()
                .moveView(glGreeting)
                .addAchievement(blockTimeElapsed)
                .addAchievement(txtStarredNumber)
                .addAchievement(txtSorrowTimes)
                .addAchievement(txtHappyTimes)
                .durationDefault()
                .delayBetweenDefault()
                .build()
    }

    private fun prepareIcons() {
        iconsHelper = IconsAnimHelper.Builder(this, root)
                .minMaxIcons(5, 7)
                .seriesNumber(4) // TODO ability to set alpha
                .timeBetweenSeriesStart(3000)
                .withRotation(true)
                .rotation(-15, 15)
                .build()
    }

    private fun startParticles() {
        fireworksHelper!!.initialize(applicationContext)
        confettiHelper!!.initialize(applicationContext)

        fireworksHelper!!.setupMaxDimens(root)
        fireworksHelper!!.runParticles(this, viewAnchor)

        val confDelay = fireworksHelper!!.lastDelay
        confettiHelper!!.runConfetti(this, viewStartCorner, viewEndCorner, confDelay)
    }

    private fun playMusic() {
        musicHelper!!.play({ animation ->
            val value = animation.animatedValue as Float
            txtGreeting!!.alpha = value
        }, { animation ->
            if (java.lang.Float.compare(animation.animatedFraction, 1.0f) == 0) {
                fabStopReplay.show()
            }
        })
    }

    private fun releaseAll() {
        if (musicHelper != null) {
            musicHelper!!.releasePlayer()
        }

        if (fireworksHelper != null) {
            fireworksHelper!!.stop()
        }

        if (confettiHelper != null) {
            confettiHelper!!.stop()
        }
    }

}
