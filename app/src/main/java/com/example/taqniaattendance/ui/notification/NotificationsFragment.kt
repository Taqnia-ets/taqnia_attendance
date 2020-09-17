package com.example.taqniaattendance.ui.notification

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.taqniaattendance.AppViewModelFactory
import com.example.taqniaattendance.R
import com.example.taqniaattendance.ServiceLocator
import com.example.taqniaattendance.databinding.FragmentNotificationsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationsFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private lateinit var viewDataBinding: FragmentNotificationsBinding
    private val viewModel by viewModels<NotificationsViewModel> {
        AppViewModelFactory(ServiceLocator.provideRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NotificatoinsBottomSheetDialogTheme);

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        viewDataBinding = FragmentNotificationsBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@NotificationsFragment
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = (it as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog;
    }
    //To setup the height of the dialog to match parent.
    override fun onStart() {
        super.onStart()
        dialog?.also {
            val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
            val behavior = bottomSheet?.let { it1 -> BottomSheetBehavior.from<View>(it1) }
            behavior?.peekHeight = resources.displayMetrics.heightPixels //replace to whatever you want
            behavior?.skipCollapsed
            (bottomSheet?.layoutParams as CoordinatorLayout.LayoutParams).setMargins(0, 70, 0, 0)
            view?.requestLayout()
        }
    }





}