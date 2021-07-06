package com.task.smartdubai.ui.component.details

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.task.R
import com.task.smartdubai.ARTICLE_ITEM_KEY
import com.task.smartdubai.TestUtil.initData
import com.task.smartdubai.TestUtil.articles
import com.task.smartdubai.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DetailsActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(DetailsActivity::class.java, true, false)
    private var mIdlingResource: IdlingResource? = null


    @Before
    fun setup() {
        initData()
        val intent: Intent = Intent().apply {
            putExtra(ARTICLE_ITEM_KEY, articles.results[0])
        }
        mActivityTestRule.launchActivity(intent)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testArticlesDetails() {
        //Assert Title
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(withText(articles.results[0].title)))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}
