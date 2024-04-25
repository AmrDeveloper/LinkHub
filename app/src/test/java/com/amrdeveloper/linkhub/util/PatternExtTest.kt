package com.amrdeveloper.linkhub.util

import java.util.regex.Pattern
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the PatternExt.kt file.
 * This class contains two tests that verify the functionality of the isPatternMatches function.
 */
class PatternExtTest {

    /**
     * Test to verify that the isPatternMatches function returns true when the pattern matches the string.
     * In this test, we create a pattern that matches any string that contains one or more alphanumeric characters.
     * We then verify that the isPatternMatches function returns true when called with this pattern and string "test123".
     */
    @Test
    fun isPatternMatches_returnsTrue_whenPatternMatchesString() {
        val pattern = Pattern.compile("[a-zA-Z0-9]+")
        assertTrue(pattern.isPatternMatches("test123"))
    }

    /**
     * Test to verify that the isPatternMatches function returns false when the pattern does not match the string.
     * In this test, we create a pattern that matches any string that contains one or more alphanumeric characters.
     * We then verify that the isPatternMatches function returns false when called with this pattern and a string "!@#",
     * which does not contain any alphanumeric characters.
     */
    @Test
    fun isPatternMatches_returnsFalse_whenPatternDoesNotMatchString() {
        val pattern = Pattern.compile("[a-zA-Z0-9]+")
        assertFalse(pattern.isPatternMatches("!@#"))
    }
}