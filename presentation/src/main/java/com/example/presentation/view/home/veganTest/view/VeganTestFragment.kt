package com.example.presentation.view.home.veganTest.view

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentVeganTestBinding
import com.example.presentation.databinding.IncludeIllusVeganLevelBinding
import com.example.presentation.view.home.veganTest.viewModel.VeganTestViewModel
import com.example.presentation.view.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VeganTestFragment : BaseFragment<FragmentVeganTestBinding>(R.layout.fragment_vegan_test) {
    private val PATCH_TYPE = "TEST"

    private var latestRadioButton:View? = null
    private var latestIncludedView: IncludeIllusVeganLevelBinding? =null

    @Inject
    @MainNavController
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val viewModel: VeganTestViewModel by activityViewModels()

    private var selectedVeganTypeNum = 0
    private lateinit var veganTestDescriptions:Array<String>
    private lateinit var veganTypes:Array<String>

    override fun init() {
        veganTestDescriptions = resources.getStringArray(R.array.vegan_test_descriptions)
        veganTypes = resources.getStringArray(R.array.vegan_types_eng)

        setDescription(binding.includedVegan,binding.acrbLacto,0 )
        controlRadioButton()

        goBackUp()
        goResult()
    }
    private fun controlRadioButton(){
        binding.rgVeganTest.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.acrb_vegan -> setDescription(binding.includedVegan,binding.acrbLacto, 0)
                R.id.acrb_lacto -> setDescription(binding.includedLacto,binding.acrbOvo, 1)
                R.id.acrb_ovo -> setDescription(binding.includedOvo,binding.acrbLactoOvo,2)
                R.id.acrb_lacto_ovo -> setDescription(binding.includedLactoOvo,binding.acrbPesco,3)
                R.id.acrb_pesco -> setDescription(binding.includedPesco, binding.acrbPollo,4)
                R.id.acrb_pollo -> setDescription(binding.includedPollo, binding.acrbFlexitarian,5)
                R.id.acrb_flexitarian -> setDescription(binding.includedFlexitarian,null,6)
            }
        }
    }

    //라디오버튼 클릭 시 반영
    private fun setDescription(selectedView: IncludeIllusVeganLevelBinding, radioView: View?, veganTypeNum:Int){
        resetMargin(latestRadioButton,94)
        latestIncludedView?.tvDescription?.isVisible = false

        selectedView.description = veganTestDescriptions[veganTypeNum]
        selectedView.tvDescription.isVisible = true
        resetMargin(radioView, 160)

        latestIncludedView = selectedView
        latestRadioButton = radioView ?: binding.acrbFlexitarian

        selectedVeganTypeNum = veganTypeNum
    }
    private fun resetMargin(radioView:View?, marginTop:Int){
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, marginTop, 0, 0)
        radioView?.layoutParams = params
    }

    //이동
    private fun goBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
            mainNavigationHandler.popBackStack()
        }
    }
    private fun goResult(){
        binding.tvGoResult.setOnClickListener {
            viewModel.setUserVeganTypeNum(selectedVeganTypeNum)
            patchVeganType(selectedVeganTypeNum)
//            viewModel.patchVeganTypeState.observe(this){
//                when(it){
//                    true ->{
//                        Timber.d("PatchVeganType Successful")
//                        mainNavigationHandler.navigateToBeganTestResult()
//                    }
//                    false -> Timber.e("PatchVeganType Failure")
//                }
//            }
            mainNavigationHandler.navigateToBeganTestResult()
        }
    }

    //retrofit
    private fun patchVeganType(typeNum: Int){
        viewModel.patchVeganType(PATCH_TYPE,veganTypes[typeNum+1])
    }
}