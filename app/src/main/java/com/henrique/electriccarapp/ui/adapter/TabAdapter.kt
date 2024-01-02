package com.henrique.electriccarapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.henrique.electriccarapp.ui.CarFragment
import com.henrique.electriccarapp.ui.FavFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CarFragment()
            1 -> FavFragment()
            else -> CarFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

}