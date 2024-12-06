package com.example.waypointjournalosm
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in, navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Finish SplashActivity so it doesn't remain in the back stack
        finish()
    }
}
