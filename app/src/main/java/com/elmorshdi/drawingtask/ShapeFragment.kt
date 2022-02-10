package com.elmorshdi.drawingtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


open class ShapeFragment : Fragment() {
    private lateinit var mDrawingView: DrawingView
    private lateinit var colorPalette: LinearLayout
    private lateinit var colorBlack: AppCompatButton
    private lateinit var colorBlue: AppCompatButton
    private lateinit var colorGreen: AppCompatButton
    private lateinit var colorRed: AppCompatButton

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemSelected()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)
        mDrawingView = rootView.findViewById(R.id.drawing_view)
        mDrawingView.mCurrentShape = DrawingView.LINE
        mDrawingView.reset()
        colorRed = activity?.findViewById(R.id.btRed) as AppCompatButton
        colorBlue = activity?.findViewById(R.id.btBlue) as AppCompatButton
        colorBlack = activity?.findViewById(R.id.btBlack) as AppCompatButton
        colorGreen = activity?.findViewById(R.id.btGreen) as AppCompatButton

        colorPalette = activity?.findViewById(R.id.color_palette) as LinearLayout
        bottomNavigationView =
            activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView
        return rootView
    }

    private fun itemSelected() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_line -> {
                    colorPalette.visibility = View.INVISIBLE

                    mDrawingView.mCurrentShape = DrawingView.LINE
                    mDrawingView.reset()
                }
                R.id.action_rectangle -> {
                    colorPalette.visibility = View.INVISIBLE

                    mDrawingView.mCurrentShape = DrawingView.RECTANGLE
                    mDrawingView.reset()
                }
                R.id.action_arrow -> {
                    colorPalette.visibility = View.INVISIBLE

                    mDrawingView.mCurrentShape = DrawingView.ARROW
                    mDrawingView.reset()
                }
                R.id.action_circle -> {
                    colorPalette.visibility = View.INVISIBLE

                    mDrawingView.mCurrentShape = DrawingView.CIRCLE
                    mDrawingView.reset()
                }
                R.id.palette -> {
                    showPopup()
                    mDrawingView.mCurrentShape = DrawingView.LINE
                    mDrawingView.reset()

                }
            }
            true

        }
    }

    private fun showPopup() {
        colorPalette.visibility = View.VISIBLE

        colorBlack.setOnClickListener {
            mDrawingView.mCurrentShape = DrawingView.LINE
            mDrawingView.color = "#FF000000"
            mDrawingView.reset()
            colorPalette.visibility = View.INVISIBLE
        }
        colorBlue.setOnClickListener  {
            mDrawingView.mCurrentShape = DrawingView.LINE
            mDrawingView.color = "#0078DE"
            mDrawingView.reset()
            colorPalette.visibility = View.INVISIBLE
        }
        colorGreen.setOnClickListener  {
            mDrawingView.mCurrentShape = DrawingView.LINE
            mDrawingView.color = "#007F00"
            mDrawingView.reset()
            colorPalette.visibility = View.INVISIBLE
        }
        colorRed.setOnClickListener  {
            mDrawingView.mCurrentShape = DrawingView.LINE
            mDrawingView.color = "#FB0008"
            mDrawingView.reset()
            colorPalette.visibility = View.INVISIBLE
        }


    }
}