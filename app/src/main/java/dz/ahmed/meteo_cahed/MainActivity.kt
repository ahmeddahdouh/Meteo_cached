package dz.ahmed.meteo_cahed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dz.ahmed.meteo_cahed.ui.theme.Meteo_cahedTheme
import dz.ahmed.meteo_cahed.ui.navigation.MeteoNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Meteo_cahedTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MeteoNavHost()
                }
            }
        }
    }
}