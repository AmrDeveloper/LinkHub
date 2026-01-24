package com.amrdeveloper.linkhub.ui.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.BuildConfig
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.openLinkIntent
import com.amrdeveloper.linkhub.util.packageName

private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=$packageName"
private const val REPOSITORY_URL = "https://github.com/AmrDeveloper/LinkHub"
private const val REPOSITORY_ISSUES_URL = "$REPOSITORY_URL/issues"
private const val REPOSITORY_CONTRIBUTORS_URL = "$REPOSITORY_URL/graphs/contributors"
private const val REPOSITORY_SPONSORSHIP_URL = "https://github.com/sponsors/AmrDeveloper"

@Composable
fun SettingsScreen(
    uiPreferences: UiPreferences,
    navController: NavController
) {
    var selectedUrlOptionToOpen by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(selectedUrlOptionToOpen) {
        try {
            openLinkIntent(context = context, link = selectedUrlOptionToOpen)
        } catch (_: Exception) {
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        TextMenuOption(
            text = "Version ${BuildConfig.VERSION_NAME}",
            icon = R.drawable.ic_version,
            onClick = {}
        )

        TextSwitchMenuOption(
            text = "Dark mode",
            icon = R.drawable.ic_dark_mode,
            isChecked = uiPreferences.getThemeType() == Theme.DARK,
            onCheckedChange = { isEnabled ->
                val themeMode = if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(themeMode)

                val theme = if (isEnabled) Theme.DARK else Theme.WHITE
                uiPreferences.setThemeType(theme)
            }
        )

        TextSwitchMenuOption(
            text = "Show Counter",
            icon = R.drawable.ic_click,
            isChecked = uiPreferences.isClickCounterEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableClickCounter(isEnabled)
            }
        )

        TextSwitchMenuOption(
            text = "Auto saving",
            icon = R.drawable.ic_save,
            isChecked = uiPreferences.isAutoSavingEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableClickCounter(isEnabled)
            }
        )

        TextSwitchMenuOption(
            text = "Remember last folder",
            icon = R.drawable.ic_folders,
            isChecked = uiPreferences.isDefaultFolderEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableDefaultFolderEnabled(isEnabled)
            }
        )

        TextMenuOption(
            text = "Password",
            icon = R.drawable.ic_password,
            onClick = { navController.navigate(R.id.configPasswordFragment) }
        )

        TextMenuOption(
            text = "Import/Export",
            icon = R.drawable.ic_import_export,
            onClick = { navController.navigate(R.id.importExportFragment) }
        )

        // Open source
        TextMenuOption(
            text = "Source code",
            icon = R.drawable.ic_code,
            onClick = { selectedUrlOptionToOpen = REPOSITORY_URL }
        )

        TextMenuOption(
            text = "Sponsor",
            icon = R.drawable.ic_github_sponsor,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_SPONSORSHIP_URL
            }
        )

        TextMenuOption(
            text = "Contributors",
            icon = R.drawable.ic_team,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_CONTRIBUTORS_URL
            }
        )

        TextMenuOption(
            text = "Issues",
            icon = R.drawable.ic_problems,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_ISSUES_URL
            }
        )

        TextMenuOption(
            text = "Share",
            icon = R.drawable.ic_share,
            onClick = { selectedUrlOptionToOpen = PLAY_STORE_URL }
        )
    }
}

@Composable
private fun TextMenuOption(text: String, icon: Int, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Info Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun TextSwitchMenuOption(
    text: String,
    icon: Int,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    var enabled by remember { mutableStateOf(isChecked) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Info Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = !enabled
                    onCheckedChange(enabled)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.light_blue_900),
                    checkedTrackColor = colorResource(R.color.light_blue_200),
                )
            )
        }
    }
}
