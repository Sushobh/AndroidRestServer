package com.ranrings.libs.anhtserv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_message).text = """
            Server is running on port ${MyApplication.application.port},
            run this command on 'adb forward tcp:${MyApplication.application.port} tcp:[your_computer_port]'
        """.trimIndent()
    }
}
