package com.amrdeveloper.linkhub.ui.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
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
import com.amrdeveloper.linkhub.ui.theme.supportedFontFamilies
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
    val scrollState = rememberScrollState()

    var selectedUrlOptionToOpen by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(selectedUrlOptionToOpen) {
        try {
            openLinkIntent(context = context, link = selectedUrlOptionToOpen)
        } catch (_: Exception) {
        }
    }

    Column(modifier = Modifier.padding(8.dp).verticalScroll(scrollState)) {
        SettingSectionDivider(text = "App Info")

        SimpleSettingOption(
            text = "Version ${BuildConfig.VERSION_NAME}",
            icon = R.drawable.ic_version,
            onClick = {}
        )

        SettingSectionDivider(text = "UI Settings")

        SwitchSettingOption(
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

        DropdownSettingOption(
            text = "Font family",
            icon = R.drawable.ic_font,
            defaultSelectedIndex = supportedFontFamilies.keys.indexOf(uiPreferences.getFontFamilyName())
                .coerceAtLeast(0),
            options = supportedFontFamilies.keys.toList(),
            onOptionSelected = { fondFamily ->
                uiPreferences.setFontFamilyName(fondFamily)
            }
        )

        SwitchSettingOption(
            text = "Show Counter",
            icon = R.drawable.ic_click,
            isChecked = uiPreferences.isClickCounterEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableClickCounter(isEnabled)
            }
        )

        SwitchSettingOption(
            text = "Auto saving",
            icon = R.drawable.ic_save,
            isChecked = uiPreferences.isAutoSavingEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableClickCounter(isEnabled)
            }
        )

        SettingSectionDivider(text = "Options")

        SwitchSettingOption(
            text = "Remember last folder",
            icon = R.drawable.ic_folders,
            isChecked = uiPreferences.isDefaultFolderEnabled(),
            onCheckedChange = { isEnabled ->
                uiPreferences.setEnableDefaultFolderEnabled(isEnabled)
            }
        )

        SimpleSettingOption(
            text = "Password",
            icon = R.drawable.ic_password,
            onClick = { navController.navigate(R.id.configPasswordFragment) }
        )

        SimpleSettingOption(
            text = "Import/Export",
            icon = R.drawable.ic_import_export,
            onClick = { navController.navigate(R.id.importExportFragment) }
        )

        SettingSectionDivider(text = "Open source")

        SimpleSettingOption(
            text = "Source code",
            icon = R.drawable.ic_code,
            onClick = { selectedUrlOptionToOpen = REPOSITORY_URL }
        )

        SimpleSettingOption(
            text = "Sponsor",
            icon = R.drawable.ic_github_sponsor,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_SPONSORSHIP_URL
            }
        )

        SimpleSettingOption(
            text = "Contributors",
            icon = R.drawable.ic_team,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_CONTRIBUTORS_URL
            }
        )

        SimpleSettingOption(
            text = "Issues",
            icon = R.drawable.ic_problems,
            onClick = {
                selectedUrlOptionToOpen = REPOSITORY_ISSUES_URL
            }
        )

        SimpleSettingOption(
            text = "Share",
            icon = R.drawable.ic_share,
            onClick = { selectedUrlOptionToOpen = PLAY_STORE_URL }
        )
    }
}

@Composable
private fun SettingSectionDivider(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, color = colorResource(R.color.light_blue_600))
        HorizontalDivider(modifier = Modifier
            .weight(1f).padding(2.dp)
            .background(color = colorResource(R.color.light_blue_600)))
    }
}

@Composable
private fun SimpleSettingOption(text: String, icon: Int, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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
private fun SwitchSettingOption(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSettingOption(
    text: String,
    icon: Int,
    options: List<String>,
    defaultSelectedIndex: Int = 0,
    onOptionSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(defaultSelectedIndex) }

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
                contentDescription = "Font Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {

                Row(
                    modifier = Modifier.clickable {
                        expanded = true
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        contentDescription = "Arrow down",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(options[selectedIndex])
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .widthIn(min = 100.dp)
                ) {
                    options.forEachIndexed { index, selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                expanded = false
                                selectedIndex = index
                                onOptionSelected(selectionOption)
                            },
                        )
                    }
                }
            }
        }
    }

}
