package uz.polat.broadcast

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import uz.polat.broadcast.repository.LocalStorage
import uz.polat.broadcast.ui.theme.BroadcastTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        setContent {
            BroadcastTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {

    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    SideEffect {
        systemUiController.setStatusBarColor(
            color = color, darkIcons = color == Color.White
        )
        systemUiController.setNavigationBarColor(color = color)
    }

    fun stopService() {
        context.stopService(
            Intent(
                context,
                ReceiverRegisterService::class.java
            )
        )
    }

    val localStorage = LocalStorage.getInstance(LocalContext.current)

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                val appWorking = remember { mutableStateOf(localStorage.appWorking) }
                localStorage.appWorking = appWorking.value

                Text(
                    text = if (appWorking.value) "Maxmadona yoniq" else "Maxmadona o'chiq",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Switch(
                    checked = appWorking.value,
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onCheckedChange = {
                        appWorking.value = it
                        if (it) {
                            val intent = Intent(context, ReceiverRegisterService::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(intent)
                            } else {
                                context.startService(intent)
                            }
                        } else {
                            stopService()
                        }
                    },
                    modifier = Modifier.padding(start = 16.dp),
                )

            }
        }

    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 16.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
        ) {

            val planeMode = remember { mutableStateOf(localStorage.airPlaneMode) }
            localStorage.airPlaneMode = planeMode.value

            ItemModes(
                image = R.drawable.travel_24px, text = "Parvoz rejimi", state = planeMode
            )

            val screenMode = remember { mutableStateOf(localStorage.screenMode) }
            localStorage.screenMode = screenMode.value

            ItemModes(
                image = R.drawable.smartphone_24px, text = "Ekran o'chishi/yonishi", state = screenMode
            )

            val chargeMode = remember { mutableStateOf(localStorage.powerMode) }
            localStorage.powerMode = chargeMode.value

            ItemModes(
                image = R.drawable.charger_24px, text = "Quvvatlanish", state = chargeMode
            )

            val headsetMode = remember { mutableStateOf(localStorage.headSetMode) }
            localStorage.headSetMode = headsetMode.value

            ItemModes(
                image = R.drawable.headphones_24px, text = "Quloqchin", state = headsetMode
            )

            val lowBatteryMode = remember { mutableStateOf(localStorage.lowBatteryMode) }
            localStorage.lowBatteryMode = lowBatteryMode.value

            ItemModes(
                image = R.drawable.battery_charging_full_24px, text = "Past quvvat", state = lowBatteryMode
            )

            val timeTickMode = remember { mutableStateOf(localStorage.timeMode) }
            localStorage.timeMode = timeTickMode.value

            ItemModes(
                image = R.drawable.schedule_24px, text = "Har daqiqa", state = timeTickMode
            )

            val blueTooth = remember { mutableStateOf(localStorage.blueToothMode) }
            localStorage.blueToothMode = blueTooth.value

            ItemModes(
                image = R.drawable.bluetooth_24px, text = "BlueTooth", state = blueTooth
            )

            val network = remember { mutableStateOf(localStorage.networkMode) }
            localStorage.networkMode = network.value

            Text(
                text = "Faqat ilova ishlayotganda:",
                style = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp)
            )
            ItemModes(
                image = R.drawable.language_24px, text = "Internet", state = network
            )

        }
    }
}

@Preview()
@Composable
fun MainContentPreview() {
    BroadcastTheme {
        MainContent()
    }
}

@Composable
fun ItemModes(
    image: Int, text: String, state: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(56.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            painter = painterResource(id = image), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = state.value,
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.onPrimary
            ),
            onCheckedChange = {
                state.value = it
            },
        )
    }
}