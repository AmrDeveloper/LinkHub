package com.amrdeveloper.linkhub

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amrdeveloper.linkhub.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShareLinkTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private fun runCaseFor(sharedLink: String, title: String, subTitle: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, sharedLink)
        }
        intentsTestRule.launchActivity(intent)

        onView(withId(R.id.link_url_edit))
            .check(matches(withText(sharedLink)))
        onView(withId(R.id.link_title_edit))
            .check(matches(withText(title)))
        onView(withId(R.id.link_subtitle_edit))
            .check(matches(withText(subTitle)))
    }

    @Test
    fun generalLink() {
        runCaseFor("https://amr.com", "amr Link", "Link from amr website")
    }

    @Test
    fun stackoverflowQuestion() {
        runCaseFor("https://stackoverflow.com/questions/android", "Programming question", "Question from stackoverflow.com")
    }

    @Test
    fun linkContainsSpace() {
        // not a valid URI, skip generation
        runCaseFor("https://www.example.com/hello world", "", "")
    }
}