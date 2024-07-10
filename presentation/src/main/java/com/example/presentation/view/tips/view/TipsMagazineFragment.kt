package com.example.presentation.view.tips.view

import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.model.tips.MagazineResponse
import com.example.domain.model.tips.TipsMagazineItem
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentTipsMagazineBinding
import com.example.presentation.view.tips.adapter.TipsMagazineRvAdapter
import com.example.presentation.view.tips.viewModel.MagazineViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TipsMagazineFragment : BaseFragment<FragmentTipsMagazineBinding>(R.layout.fragment_tips_magazine) {
    @Inject
    @MainNavController
    lateinit var mainNavigationHandler: MainNavigationHandler

    private val magazineViewModel : MagazineViewModel by activityViewModels()

    private lateinit var magazineRvAdapter: TipsMagazineRvAdapter

    override fun init() {
        binding.lifecycleOwner = this
        setRvAdapter()
        setTabBtn()
    }

    private fun setRvAdapter(){
        magazineViewModel.magazineList.observe(this){
            magazineRvAdapter = TipsMagazineRvAdapter(requireContext(), it)
            binding.rvMagazine.adapter = magazineRvAdapter
            binding.rvMagazine.layoutManager = LinearLayoutManager(this.context)

            magazineRvAdapter.setOnItemClickListener(object :
                TipsMagazineRvAdapter.OnItemClickListener {
                override fun onItemClick(magazineId:Int) {
                    mainNavigationHandler.navigateToTipsMagazineDetail()
                    magazineViewModel.setSelectedMagazineId(magazineId)
                }

                override fun changeBookmark(
                    toggleButton: CompoundButton,
                    isBookmarked: Boolean,
                    data: TipsMagazineItem
                ) {
                    when(isBookmarked){
                        true -> {
                            magazineViewModel.postBookmark(data.id, "MAGAZINE")
                        }
                        false -> {
                            magazineViewModel.deleteBookmark(data.id, "MAGAZINE")
                        }
                    }
                }
            })
        }
    }
    private fun setTabBtn(){
        binding.ibFab.setOnClickListener {
            binding.rvMagazine.smoothScrollToPosition(0)
        }
    }
}