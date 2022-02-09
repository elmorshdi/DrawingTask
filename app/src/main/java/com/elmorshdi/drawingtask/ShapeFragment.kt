package com.elmorshdi.drawingtask

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView





open class ShapeFragment : Fragment() {
    private lateinit var mDrawingView: DrawingView
    private lateinit var bottomNavigationView:BottomNavigationView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemSelected()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)
        mDrawingView = rootView.findViewById(R.id.drawingview)
        mDrawingView.mCurrentShape = DrawingView.LINE
        mDrawingView.reset()
         bottomNavigationView = activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView
        return rootView
    }

     private fun itemSelected() {
         bottomNavigationView.setOnNavigationItemSelectedListener { item ->
             when (item.itemId) {
            R.id.action_smoothline -> {
                mDrawingView.mCurrentShape = DrawingView.LINE
                mDrawingView.reset()
            }
            R.id.action_rectangle -> {
                mDrawingView.mCurrentShape = DrawingView.RECTANGLE
                mDrawingView.reset()
            }
            R.id.action_arrow -> {
                mDrawingView.mCurrentShape = DrawingView.ARROW
                mDrawingView.reset()
            }
            R.id.action_circle -> {
                mDrawingView.mCurrentShape = DrawingView.CIRCLE
                mDrawingView.reset()
            }
            R.id.palette -> {
                showPopup(requireActivity().findViewById(R.id.palette))


            }
        }
             true

    }
     }
    private fun showPopup(view: View) {
    val popup = PopupMenu(requireContext(), view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.color_menu, popup.menu)
        popup.show()
    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

        when (item!!.itemId) {
            R.id.black -> {
                mDrawingView.mCurrentShape = DrawingView.LINE
                mDrawingView.color="#FF018786"
                mDrawingView.reset()
            }
            R.id.blue -> {
                mDrawingView.mCurrentShape = DrawingView.LINE
                mDrawingView.color="#0078DE"
                mDrawingView.reset()            }
            R.id.green -> {
                mDrawingView.mCurrentShape = DrawingView.LINE
                mDrawingView.color="#007F00"
                mDrawingView.reset()            }
            R.id.red -> {
                mDrawingView.mCurrentShape = DrawingView.LINE
                mDrawingView.color="#FB0008"
                mDrawingView.reset()            }
        }

        true
    })

    popup.show()
}
}