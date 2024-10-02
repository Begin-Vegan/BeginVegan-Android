package com.example.presentation.view.tips.view

import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.databinding.FragmentMainTipsBinding
import com.example.presentation.util.DrawerController
import com.example.presentation.view.main.viewModel.MainViewModel
import com.example.presentation.view.tips.adapter.TipsVpAdapter
import com.example.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TipsFragment : BaseFragment<FragmentMainTipsBinding>(R.layout.fragment_main_tips) {
    @Inject
    lateinit var drawerController: DrawerController
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)
    override fun init() {
        binding.lifecycleOwner = this

        setTipsTab()
        //fcm 테스트
        //mainViewModel.postFcmPush()
    }

    private fun setTipsTab() {
        binding.vpViewpagerArea.adapter = TipsVpAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlTab, binding.vpViewpagerArea) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.magazine)
                1 -> tab.text = getString(R.string.recipe)
            }
        }.attach()

        checkMainViewModel()
        setOpenDrawer()
    }

    //이동
    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun checkMainViewModel() {
        //나를 위한 레시피
        if (mainViewModel.fromTest.value!!) {
            binding.vpViewpagerArea.post {
                binding.vpViewpagerArea.currentItem = 1
                recipeViewModel.setIsFromTest(true)
            }
        } else if (mainViewModel.tipsMoveToRecipe.value!!) {
            binding.vpViewpagerArea.post {
                binding.vpViewpagerArea.currentItem = 1
            }
        }
        mainViewModel.setFromTest(false)
        mainViewModel.setTipsMoveToRecipe(false)
    }
}