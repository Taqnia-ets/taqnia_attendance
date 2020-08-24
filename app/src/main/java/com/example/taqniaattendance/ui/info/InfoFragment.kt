package com.example.taqniaattendance.ui.info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.taqniaattendance.AppViewModelFactory
import com.example.taqniaattendance.R
import com.example.taqniaattendance.ServiceLocator
import com.example.taqniaattendance.databinding.InfoFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.info_fragment.*


class InfoFragment : BottomSheetDialogFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = InfoFragment()
    }

    private var googleMap: GoogleMap? = null
    private lateinit var viewDataBinding: InfoFragmentBinding
    private val viewModel by viewModels<InfoViewModel> {
        AppViewModelFactory(ServiceLocator.provideRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.InfoBottomSheetDialogTheme);

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_fragment, container, false)
        viewDataBinding = InfoFragmentBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@InfoFragment
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
//        mapFragment?.view?.setOnTouchListener(OnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_MOVE -> rlParent.requestDisallowInterceptTouchEvent(true)
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN, MotionEvent.ACTION_CANCEL -> rlParent.requestDisallowInterceptTouchEvent(
//                    false
//                )
//            }
//            v.onTouchEvent(event)
//        })
        viewModel.savedUser.observe(this@InfoFragment, Observer {
            // Add polygons to indicate areas on the map.
            viewModel.savedUser.value?.let {user ->
                val departmentPolygons = user.departmentLocation?.geometry?.calculatePolygonsAsLatLng()
                if (departmentPolygons.isNullOrEmpty())
                    return@let

                googleMap?.addPolygon(
                    PolygonOptions().addAll(departmentPolygons)
                )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(user.departmentLocation.geometry.calculateCenterPoint(), 17F))
            }
        })
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

    override fun onMapReady(p0: GoogleMap) {
        p0.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap = p0
        // Add polygons to indicate areas on the map.
        viewModel.savedUser.value?.let {user ->
            val departmentPolygons = user.departmentLocation?.geometry?.calculatePolygonsAsLatLng()
            if (departmentPolygons.isNullOrEmpty())
                return

            p0.addPolygon(
                PolygonOptions().addAll(departmentPolygons)
            )
            p0.moveCamera(CameraUpdateFactory.newLatLngZoom(user.departmentLocation.geometry.calculateCenterPoint(),17F))
        }
    }




}