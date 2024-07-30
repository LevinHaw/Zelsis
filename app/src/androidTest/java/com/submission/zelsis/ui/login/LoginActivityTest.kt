package com.submission.zelsis.ui.login


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.submission.zelsis.util.EspressoIdlingResource
import com.submission.zelsis.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    private val email = "testuser123@gmail.com"
    private val password = "010201aa"

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    fun tearDown(){
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_and_logout_success() {
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etEmail)).perform(
            ViewActions.typeText(email),
            closeSoftKeyboard()
        )

        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).perform(
            ViewActions.typeText(password),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).perform(click())

        onView(withId(R.id.rvStory)).check(matches(isDisplayed()))

        onView(withId(R.id.menuProfile)).check(matches(isDisplayed()))
        onView(withId(R.id.menuProfile)).perform(click())

        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogout)).perform(click())

    }
}