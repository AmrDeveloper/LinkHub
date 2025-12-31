package com.amrdeveloper.linkhub.ui.link

import android.webkit.URLUtil
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.common.TaskState
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.components.FolderItem
import com.amrdeveloper.linkhub.ui.components.PinnedSwitch
import com.amrdeveloper.linkhub.ui.components.SaveDeleteActionsRow
import com.amrdeveloper.linkhub.util.CREATED_FOLDER_NAME_KEY
import com.amrdeveloper.linkhub.util.CREATE_FOLDER_ID
import com.amrdeveloper.linkhub.util.FOLDER_NONE_ID
import com.amrdeveloper.linkhub.util.UiPreferences
import java.net.URI
import java.text.DateFormat

val artificialNoneFolder = Folder(name = "None", id = FOLDER_NONE_ID)
val artificialCreateNewFolder = Folder(name = "Create Folder", id = CREATE_FOLDER_ID)

// TODO: generateLinkTitleAndSubTitle

@Composable
fun LinkScreen(
    currentLink: Link?,
    viewModel: LinkViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    val link = currentLink ?: Link(title = "", subtitle = "", url = "")

    val taskState = viewModel.taskState

    var linkTitle by remember { mutableStateOf(value = link.title) }
    var linkTitleErrorMessage by remember { mutableStateOf(value = if (linkTitle.isEmpty()) "Title can't be empty" else "") }

    var linkUrl by remember { mutableStateOf(value = link.url) }
    var linkUrlErrorMessage by remember { mutableStateOf(value = if (linkUrl.isEmpty()) "Url can't be empty" else "") }

    var linkSubTitle by remember { mutableStateOf(value = link.subtitle) }

    val foldersState = viewModel.selectSortedFoldersState.collectAsStateWithLifecycle()
    var selectedFolder by remember { mutableStateOf(value = artificialNoneFolder) }
    var selectedFolderDry by remember { mutableStateOf(value = true) }

    val createOrUpdateLink = {
        if (currentLink == null) {
            viewModel.createNewLink(link)
        } else {
            viewModel.updateLink(link)
        }
    }

    BackHandler(enabled = true) {
        if (uiPreferences.isAutoSavingEnabled()
            && (linkTitleErrorMessage.isEmpty() || linkUrlErrorMessage.isEmpty())) {
            createOrUpdateLink()
            return@BackHandler
        }
        navController.popBackStack()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SaveDeleteActionsRow(
                onSaveActionClick = {
                    createOrUpdateLink()
                    navController.navigateUp()
                },
                onDeleteActionClick = {
                    if (currentLink != null) viewModel.deleteLink(currentLink)
                    navController.navigateUp()
                }
            )

            LinkHeaderIcon()

            LinkInputField(
                label = "Title",
                value = linkTitle,
                errorMessage = linkTitleErrorMessage,
                onValueChange = {
                    linkTitle = it
                    link.title = it

                    if (it.isEmpty()) {
                        linkTitleErrorMessage = "Title can't be empty"
                        return@LinkInputField
                    }

                    if (it.length < 3) {
                        linkTitleErrorMessage = "Folder name can't be less than 3 characters"
                        return@LinkInputField
                    }

                    linkTitleErrorMessage = ""
                }
            )

            LinkInputField(
                label = "Url",
                value = linkUrl,
                errorMessage = linkUrlErrorMessage,
                onValueChange = {
                    linkUrl = it
                    link.url = it

                    if (it.length < 3) {
                        linkUrlErrorMessage = "Url can't be empty"
                        return@LinkInputField
                    }

                    if (!isValidURI(it)) {
                        linkUrlErrorMessage = "Url is not valid"
                        return@LinkInputField
                    }

                    linkUrlErrorMessage = ""
                }
            )

            LinkInputField(
                label = "SubTitle (Optional)",
                value = linkSubTitle,
                errorMessage = "",
                onValueChange = {
                    linkSubTitle = it
                    link.subtitle = it
                }
            )

            val folders = foldersState.value.data.toMutableList()
            folders.addAll(0, listOf(artificialNoneFolder, artificialCreateNewFolder))

            if (selectedFolderDry) {
                if (uiPreferences.isDefaultFolderEnabled()) {
                    val defFolderId = uiPreferences.getDefaultFolderId()
                    selectedFolder = folders.find { it.id == defFolderId } ?: folders.find { it.id == link.folderId } ?: folders[0]
                } else {
                    selectedFolder = folders.find { it.id == link.folderId } ?: folders[0]
                }
            }

            // Check if user created a new folder for this link and suggest it as the current link folder
            val newFolderCreatedName =
                navController.currentBackStackEntry?.savedStateHandle?.get<String>(CREATED_FOLDER_NAME_KEY)
            if (newFolderCreatedName != null) {
                selectedFolder =
                    folders.find { it.name == newFolderCreatedName } ?: selectedFolder
                link.folderId = selectedFolder.id
                navController.currentBackStackEntry?.savedStateHandle?.remove<String>(CREATED_FOLDER_NAME_KEY)
            }

            FoldersDropDownSelector(selectedFolder = selectedFolder, folders = folders) { folder ->
                if (folder.id == CREATE_FOLDER_ID) {
                    navController.navigate(R.id.action_linkFragment_to_folderFragment)
                    return@FoldersDropDownSelector
                }

                link.folderId = folder.id
                selectedFolder = folder
                selectedFolderDry = false
            }

            PinnedSwitch(isChecked = link.isPinned) { isChecked ->
                link.isPinned = isChecked
            }

            // Link metadata
            if (currentLink != null) {
                val dateFormatter = DateFormat.getDateTimeInstance()
                Text(
                    text = "Created at ${dateFormatter.format(currentLink.createdTime)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )

                if (currentLink.isUpdated) {
                    Text(
                        text = "Updated at ${dateFormatter.format(currentLink.lastUpdatedTime)}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }

            when (taskState) {
                TaskState.Success -> {
                    navController.popBackStack()
                }

                is TaskState.Error -> {
                    linkTitleErrorMessage = stringResource(taskState.message)
                }

                TaskState.Idle -> {}
            }
        }
    }
}

private fun isValidURI(url: String) =
    URLUtil.isValidUrl(url) && runCatching { URI(url) }.isSuccess

@Composable
fun LinkHeaderIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_link),
        contentDescription = "Link Icon",
        tint = Color.Unspecified,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )
}

@Composable
fun LinkInputField(
    label: String,
    value: String,
    errorMessage: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.trim()) },
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_link),
                contentDescription = "Link Icon",
                tint = Color.Unspecified,
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Link Icon"
                    )
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        isError = errorMessage.isNotEmpty(),
        supportingText = {
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage)
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersDropDownSelector(
    selectedFolder: Folder,
    folders: List<Folder>,
    onFolderSelected: (Folder) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {

        OutlinedTextField(
            value = selectedFolder.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Folder (Optional") },
            leadingIcon = {
                Icon(
                    painter = painterResource(selectedFolder.folderColor.drawableId),
                    contentDescription = "Folder Icon",
                    tint = Color.Unspecified
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            folders.forEach { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { folder ->
                        expanded = false
                        onFolderSelected(folder)
                    },
                    folderItemElevation = 0.dp
                )
            }
        }
    }
}
