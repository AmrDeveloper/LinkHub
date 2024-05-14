package com.amrdeveloper.linkhub.util

import com.amrdeveloper.linkhub.data.LinkInfo
import java.net.URI
import java.util.regex.Pattern

private val TWITTER_USER_PATTERN = Pattern.compile("https://twitter\\.com/[_a-zA-Z0-9]{3,15}$")

private val GITHUB_USER_PATTERN = Pattern.compile("https://github\\.com/[_a-zA-Z0-9]{3,}$")
private val GITHUB_REPO_PATTERN =
    Pattern.compile("https://github\\.com/[_a-zA-Z0-9]{3,}/[_a-zA-Z0-9]{3,}$")

private val STACKOVERFLOW_QUESTION_PATTERN =
    Pattern.compile("https://stackoverflow\\.com/questions/.+")

private val FACEBOOK_PROFILE_PATTERN =
    Pattern.compile("https://www\\.facebook\\.com/[_a-zA-Z0-9]{3,}$")
private val FACEBOOK_GROUP_PATTERN = Pattern.compile("https://www\\.facebook\\.com/groups/.+")

private val INSTAGRAM_PROFILE_PATTERN =
    Pattern.compile("https://www\\.instagram\\.com/[_a-zA-Z0-9]{3,}$")

private val REDDIT_USER_PATTERN =
    Pattern.compile("https://www\\.reddit\\.com/(user|u)/[_a-zA-Z0-9]{3,15}$")
private val REDDIT_SUBREDDIT_PATTERN =
    Pattern.compile("https://www\\.reddit\\.com/r/[_a-zA-Z0-9]{3,15}$")

private val LINKEDIN_USER_PATTERN =
    Pattern.compile("https://www\\.linkedin\\.com/in/[_a-zA-Z0-9]{3,}$")
private val LINKEDIN_JOB_PATTERN = Pattern.compile("https://www\\.linkedin\\.com/jobs/.*")
private val LINKEDIN_COMPANY_PATTERN = Pattern.compile("https://www\\.linkedin\\.com/company/.*")

private val YOUTUBE_CHANNEL_PATTERN = Pattern.compile("https://(youtube\\.com|youtu\\.be)/c/.*")

fun generateLinkInfo(linkUrl: String): LinkInfo {
    var url = linkUrl
    var domain = URI(url).host

    if (url.endsWith("/")) url = url.dropLast(1)

    val questionMarkIndex = url.lastIndexOf("?")
    if (questionMarkIndex > -1) url = url.substring(0, questionMarkIndex)

    if (domain.startsWith("www.")) domain = domain.drop(4)
    if (domain.endsWith(".com")) domain = domain.dropLast(4)

    return when (domain) {
        "github" -> parseGithubUrl(url)
        "twitter" -> parseTwitterUrl(url)
        "facebook" -> parseFacebookUrl(url)
        "instagram" -> parseInstagramUrl(url)
        "reddit" -> parseRedditUrl(url)
        "linkedin" -> parseLinkedinUrl(url)
        "youtube", "youtu.be" -> parseYoutubeUrl(url)
        "stackoverflow" -> parseStackOverflowUrl(url)
        else -> return LinkInfo("$domain Link", "Link from $domain website")
    }
}

private fun getLastIdentifierFromLink(link: String): String {
    val index = link.lastIndexOf("/")
    return link.substring(index + 1)
}

private fun parseGithubUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        GITHUB_USER_PATTERN.isPatternMatches(url) -> {
            val username = getLastIdentifierFromLink(url)
            linkTitle = "$username profile"
            linkSubtitle = "$username Github profile"
        }

        GITHUB_REPO_PATTERN.isPatternMatches(url) -> {
            val repoName = getLastIdentifierFromLink(url)
            linkTitle = "$repoName Repository"
            linkSubtitle = "$repoName Github Repository"
        }

        else -> {
            linkTitle = "Github Link"
            linkSubtitle = "Link from Github.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseStackOverflowUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        STACKOVERFLOW_QUESTION_PATTERN.isPatternMatches(url) -> {
            linkTitle = "Programming question"
            linkSubtitle = "Question from stackoverflow.com"
        }

        else -> {
            linkTitle = "stackoverflow Link"
            linkSubtitle = "Link from stackoverflow.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseTwitterUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        TWITTER_USER_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler profile"
            linkSubtitle = "$handler twitter profile"
        }

        else -> {
            linkTitle = "Tweet"
            linkSubtitle = "Tweet from Twitter.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseFacebookUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        FACEBOOK_PROFILE_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler profile"
            linkSubtitle = "$handler Facebook profile"
        }

        FACEBOOK_GROUP_PATTERN.isPatternMatches(url) -> {
            linkTitle = "Facebook Group"
            linkSubtitle = "Group from Facebook.com"
        }

        else -> {
            linkTitle = "Facebook post"
            linkSubtitle = "Post from Facebook.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseInstagramUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        INSTAGRAM_PROFILE_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler profile"
            linkSubtitle = "$handler Instagram profile"
        }

        else -> {
            linkTitle = "Instagram post"
            linkSubtitle = "Post from Instagram.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseRedditUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        REDDIT_USER_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler profile"
            linkSubtitle = "$handler Reddit profile"
        }

        REDDIT_SUBREDDIT_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler Subreddit"
            linkSubtitle = "$handler Subreddit page"
        }

        else -> {
            linkTitle = "Reddit Post"
            linkSubtitle = "Post from Reddit.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseLinkedinUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String
    when {
        LINKEDIN_USER_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler Profile"
            linkSubtitle = "$handler Linkedin Profile"
        }

        LINKEDIN_JOB_PATTERN.isPatternMatches(url) -> {
            linkTitle = "linkedin Job"
            linkSubtitle = "Job from linkedin.com"
        }

        LINKEDIN_COMPANY_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler Company"
            linkSubtitle = "$handler Company page on Linkedin.com"
        }

        else -> {
            linkTitle = "linkedin Post"
            linkSubtitle = "Post from linkedin.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}

private fun parseYoutubeUrl(url: String): LinkInfo {
    val linkTitle: String
    val linkSubtitle: String

    when {
        YOUTUBE_CHANNEL_PATTERN.isPatternMatches(url) -> {
            val handler = getLastIdentifierFromLink(url)
            linkTitle = "$handler Channel"
            linkSubtitle = "$handler Youtube Channel"
        }

        else -> {
            linkTitle = "Youtube video"
            linkSubtitle = "video from Youtube.com"
        }
    }
    return LinkInfo(linkTitle, linkSubtitle)
}