package com.mobilemagic.scalainstaller

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity extends Activity with Runnable {
  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    button.setOnClickListener(new View.OnClickListener {
      def onClick(view: View): Unit = {
        progress = ProgressDialog.show(MainActivity.this, "Installing...", "")
        val thread: Thread = new Thread(MainActivity.this)
        thread.start
      }
    })
  }

  def run: Unit = {
    val installer: RoboInstaller = new RoboInstaller(getApplicationContext)
    installer.installScalaLibs()
    runOnUiThread(new Runnable {
      def run {
        button.setEnabled(false)
        text.setTextSize(20)
        text.setText("Finished! Please restart your phone.")
        progress.dismiss()
      }
    })
  }

  var progress: ProgressDialog = null
  lazy val text = findViewById(R.id.text).asInstanceOf[TextView]
  lazy val button = findViewById(R.id.button).asInstanceOf[Button]
}

