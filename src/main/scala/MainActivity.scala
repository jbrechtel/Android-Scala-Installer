package com.mobilemagic.scalainstaller

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity extends Activity {
  def installClickListener: View.OnClickListener {def onClick(view: View): Unit} = {
    new View.OnClickListener {
      def onClick(view: View): Unit = {
        progress = ProgressDialog.show(MainActivity.this, "Installing...", "")
        val thread: Thread = new Thread(new Runnable {
          def run: Unit = {
            val installer: RoboInstaller = new RoboInstaller(getApplicationContext)
            installer.installScalaLibs()
            runOnUiThread(new Runnable {
              def run {
                installButton.setEnabled(false)
                unInstallButton.setEnabled(false)
                text.setTextSize(20)
                text.setText("Finished! Please restart your phone.")
                progress.dismiss()
              }
            })
          }
        })
        thread.start()
      }
    }
  }

  def uninstallClickListener: View.OnClickListener {def onClick(view: View): Unit} = {
    new View.OnClickListener {
      def onClick(view: View): Unit = {
        progress = ProgressDialog.show(MainActivity.this, "Uninstalling...", "")
        val thread: Thread = new Thread(new Runnable {
          def run: Unit = {
            val installer: RoboInstaller = new RoboInstaller(getApplicationContext)
            installer.uninstallScalaLibs()
            runOnUiThread(new Runnable {
              def run {
                installButton.setEnabled(false)
                unInstallButton.setEnabled(false)
                text.setTextSize(20)
                text.setText("Finished! Please restart your phone.")
                progress.dismiss()
              }
            })
          }

        })
        thread.start()
      }
    }
  }

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)

    installButton.setOnClickListener(installClickListener)
    unInstallButton.setOnClickListener(uninstallClickListener)
  }


  var progress: ProgressDialog = null
  lazy val text = findViewById(R.id.text).asInstanceOf[TextView]
  lazy val installButton = findViewById(R.id.install_button).asInstanceOf[Button]
  lazy val unInstallButton = findViewById(R.id.uninstall_button).asInstanceOf[Button]

}

