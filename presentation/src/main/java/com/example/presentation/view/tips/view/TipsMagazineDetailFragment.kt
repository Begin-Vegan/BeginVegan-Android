package com.example.presentation.view.tips.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.domain.model.tips.MagazineContent
import com.example.domain.model.tips.TipsMagazineDetail
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentTipsMagazineDetailBinding
import com.example.presentation.view.tips.viewModel.MagazineViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class TipsMagazineDetailFragment : BaseFragment<FragmentTipsMagazineDetailBinding>(R.layout.fragment_tips_magazine_detail) {
    @Inject
    @MainNavController
    lateinit var mainNavigationHandler: MainNavigationHandler

    private val magazineViewModel:MagazineViewModel by activityViewModels()

    override fun init() {
        binding.lifecycleOwner = this
        magazineViewModel.getMagazineDetail()
        magazineViewModel.magazineDetail.observe(this){
            Timber.d("Observed")
            setView(it)
        }
        goBackUp()
        createContentTextView(MagazineContent("TEST", 1))
    }

    private fun goBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
            mainNavigationHandler.popBackStack()
        }
    }

    private fun transferDate(date:String):String{
        val stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val newDate = LocalDateTime.parse(date, stringToDate)

        val dateToString = DateTimeFormatter.ofPattern("yyyy. MM. dd")
        return newDate.format(dateToString)
    }

    private fun setView(it: TipsMagazineDetail){
        Timber.d("setView: ${it.title}")
        binding.tvMagazineTitle.text = it.title
        binding.tvWriter.text = it.editor
        binding.tvDate.text = transferDate(it.createdDate)
        binding.tbInterest.isChecked = it.isBookmarked

        Glide.with(this)
            .load(it.thumbnail)
            .into(binding.ivThumbnail)

        for(content in it.magazineContents){
            createContentTextView(content)
        }
    }

    private fun createContentTextView(content: MagazineContent){
        Timber.d("createContentTextView")
        val textView = TextView(context).apply {
            text = content.content
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.color_text_01))
            gravity = Gravity.CENTER
        }

        val parentLayout = binding.llMagazineContents
        parentLayout.addView(textView)
    }

    //Control Back Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressedCallback()
    }
    private fun setupOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d("detail onBackPressed")
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}