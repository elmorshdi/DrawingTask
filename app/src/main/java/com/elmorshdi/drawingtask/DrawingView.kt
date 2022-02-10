package com.elmorshdi.drawingtask

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


open class DrawingView : View {
    var mCurrentShape = 0
    var color = "#FF3700B3"
    private lateinit var mPath: Path
    private lateinit var mPaint: Paint
    private lateinit var mPaintFinal: Paint
    private  lateinit  var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas



    private var isDrawing = false



    private var mStartX = 0f
    private var mStartY = 0f
    private var mx = 0f
    private var my = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var startPoint = PointF()
    private var endPoint = PointF()
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int,color: String) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.color=color
        init()
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap, 0F, 0F, this.mPaint)
        if (isDrawing) {
            when (mCurrentShape) {
                LINE -> onDrawLine(canvas)
                RECTANGLE -> onDrawRectangle(canvas)
                ARROW -> onDrawArrow(canvas)
                CIRCLE -> onDrawCircle(canvas)
             }
        }
    }

    private fun init() {
        this.mPath = Path()
        this.mPaint = Paint(Paint.DITHER_FLAG)
        this.mPaint.isAntiAlias = true
        this.mPaint.isDither = true
        this.mPaint.color = Color.parseColor(color)
        this.mPaint.style = Paint.Style.STROKE
        this.mPaint.strokeJoin = Paint.Join.ROUND
        this.mPaint.strokeCap = Paint.Cap.ROUND
        this.mPaint.strokeWidth = TOUCH_STROKE_WIDTH
        mPaintFinal = Paint(Paint.DITHER_FLAG)
        mPaintFinal.isAntiAlias = true
        mPaintFinal.isDither = true
        mPaintFinal.color = Color.parseColor(color)
        mPaintFinal.style = Paint.Style.STROKE
        mPaintFinal.strokeJoin = Paint.Join.ROUND
        mPaintFinal.strokeCap = Paint.Cap.ROUND
        mPaintFinal.strokeWidth = TOUCH_STROKE_WIDTH
    }

    fun reset() {
        this.mPath = Path()
        this.mPaint.color = Color.parseColor(color)
        mPaintFinal.color = Color.parseColor(color)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mx = event.x
        my = event.y
        when (mCurrentShape) {
             LINE -> onTouchEventSmoothLine(event)
            RECTANGLE -> onTouchEventRectangle(event)
            ARROW -> onTouchEventArrow(event)
            CIRCLE -> onTouchEventCircle(event)
         }
        return true
    }


    // Line

    private fun onDrawLine(canvas: Canvas) {
        val dx = Math.abs(mx - mStartX)
        val dy = Math.abs(my - mStartY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(mStartX, mStartY, mx, my, this.mPaint)
        }
    }

//    private fun onTouchEventLine(event: MotionEvent) {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                isDrawing = true
//                mStartX = mx
//                mStartY = my
//                invalidate()
//            }
//            MotionEvent.ACTION_MOVE -> invalidate()
//            MotionEvent.ACTION_UP -> {
//                isDrawing = false
//                mCanvas.drawLine(mStartX, mStartY, mx, my, mPaintFinal)
//                invalidate()
//            }
//        }
//    }
    //------------------------------------------------------------------
    // Smooth Line
    //------------------------------------------------------------------
    private fun onTouchEventSmoothLine(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = mx
                mStartY = my
                this.mPath.reset()
                this.mPath.moveTo(mx, my)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(mx - mStartX)
                val dy = Math.abs(my - mStartY)
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    this.mPath.quadTo(mStartX, mStartY, (mx + mStartX) / 2, (my + mStartY) / 2)
                    mStartX = mx
                    mStartY = my
                }
                mCanvas.drawPath(this.mPath, this.mPaint)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                isDrawing = false
                this.mPath.lineTo(mStartX, mStartY)
                mCanvas.drawPath(this.mPath, mPaintFinal)
                this.mPath.reset()
                invalidate()
            }
        }
    }

    //------------------------------------------------------------------
    // Triangle
    //------------------------------------------------------------------
/*
    var countTouch = 0
    var basexTriangle = 0f
    var baseyTriangle = 0f
    private fun onDrawTriangle(canvas: Canvas) {
        if (countTouch < 3) {
            canvas.drawLine(mStartX, mStartY, mx, my, mPaint)
        } else if (countTouch == 3) {
            canvas.drawLine(mx, my, mStartX, mStartY, mPaint)
            canvas.drawLine(mx, my, basexTriangle, baseyTriangle, mPaint)
        }
    }

    private fun onTouchEventTriangle(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                countTouch++
                if (countTouch == 1) {
                    isDrawing = true
                    mStartX = mx
                    mStartY = my
                } else if (countTouch == 3) {
                    isDrawing = true
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                countTouch++
                isDrawing = false
                if (countTouch < 3) {
                    basexTriangle = mx
                    baseyTriangle = my
                    mCanvas.drawLine(mStartX, mStartY, mx, my, mPaintFinal)
                } else if (countTouch >= 3) {
                    mCanvas.drawLine(mx, my, mStartX, mStartY, mPaintFinal)
                    mCanvas.drawLine(mx, my, basexTriangle, baseyTriangle, mPaintFinal)
                    countTouch = 0
                }
                invalidate()
            }
        }
    }
*/

    //------------------------------------------------------------------
    // Circle
    //------------------------------------------------------------------
    private fun onDrawCircle(canvas: Canvas) {
        canvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX, mStartY, mx, my),
            this.mPaint
        )
    }

    private fun onTouchEventCircle(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = mx
                mStartY = my
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                isDrawing = false
                mCanvas.drawCircle(
                    mStartX,
                    mStartY,
                    calculateRadius(mStartX, mStartY, mx, my),
                    mPaintFinal
                )
                invalidate()
            }
        }
    }

    /**
     *
     * @return
     */
    private fun calculateRadius(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(
            Math.pow((x1 - x2).toDouble(), 2.0) +
                    Math.pow((y1 - y2).toDouble(), 2.0)
        ).toFloat()
    }

    //------------------------------------------------------------------
    // Rectangle
    //------------------------------------------------------------------
    private fun onDrawRectangle(canvas: Canvas) {
        drawRectangle(canvas, this.mPaint)
    }

    private fun onTouchEventRectangle(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = mx
                mStartY = my
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                isDrawing = false
                drawRectangle(mCanvas, mPaintFinal)
                invalidate()
            }
        }
    }

    private fun drawRectangle(canvas: Canvas, paint: Paint) {
        val right = if (mStartX > mx) mStartX else mx
        val left = if (mStartX > mx) mx else mStartX
        val bottom = if (mStartY > my) mStartY else my
        val top = if (mStartY > my) my else mStartY
        canvas.drawRect(left, top, right, bottom, paint)
    }

    //------------------------------------------------------------------
    // Square
    //------------------------------------------------------------------
    private fun onDrawArrow(canvas: Canvas) {
        canvas.drawPath(this.mPath, this.mPaint)
    }

    private fun onTouchEventArrow(event: MotionEvent) {
        var eventX = event.x
        var eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                this.mPath.moveTo(eventX, eventY)
                lastTouchX = eventX
                lastTouchY = eventY
                startPoint = PointF(event.x, event.y)
                endPoint = PointF()
                invalidate()

            }
            MotionEvent.ACTION_MOVE -> {
                val dx: Float = abs(x - eventX)
                println("action move")
                val dy: Float = abs(y - eventY)
                eventX = x
                eventY = y
                endPoint.x = event.x
                endPoint.y = event.y
                invalidate()

            }
            MotionEvent.ACTION_UP -> {
                this.mPath.lineTo(eventX, eventY)

                val deltaX = endPoint.x - startPoint.x
                val deltaY = endPoint.y - startPoint.y
                val frac = 0.1.toFloat()
                val point_x_1 = startPoint.x + ((1 - frac) * deltaX + frac * deltaY)
                val point_y_1 = startPoint.y + ((1 - frac) * deltaY - frac * deltaX)
                val point_x_2 = endPoint.x
                val point_y_2 = endPoint.y
                val point_x_3 = startPoint.x + ((1 - frac) * deltaX - frac * deltaY)
                val point_y_3 = startPoint.y + ((1 - frac) * deltaY + frac * deltaX)
                this.mPath.moveTo(point_x_1, point_y_1)
                this.mPath.lineTo(point_x_2, point_y_2)
                this.mPath.lineTo(point_x_3, point_y_3)
                mCanvas.drawPath(this.mPath, this.mPaint)
                endPoint.x = event.x
                endPoint.y = event.y
                invalidate()
            }
            else -> {}
        }
    }

    companion object {
        const val RECTANGLE = 3
        const val ARROW = 4
        const val CIRCLE = 5
        const val LINE = 2
        const val TOUCH_TOLERANCE = 4f
        const val TOUCH_STROKE_WIDTH = 5f
    }
}