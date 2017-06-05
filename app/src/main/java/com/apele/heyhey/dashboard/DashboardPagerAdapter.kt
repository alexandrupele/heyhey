package com.apele.heyhey.dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.apele.heyhey.groups.GroupsFragment

/**
 * Created by alexandrupele on 05/06/2017.
 */
class DashboardPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val PEOPLE_PAGE = 0
    val GROUPS_PAGE = 1

    val PAGE_NUM = 2

    override fun getItem(position: Int): Fragment = when(position) {
        PEOPLE_PAGE -> PeopleFragment()
        GROUPS_PAGE -> GroupsFragment()
        else -> throw IllegalStateException()
    }

    override fun getCount(): Int = PAGE_NUM

    override fun getPageTitle(position: Int): CharSequence = when(position) {
        PEOPLE_PAGE -> "Friends"
        GROUPS_PAGE -> "Groups"
        else -> throw IllegalStateException()
    }
}