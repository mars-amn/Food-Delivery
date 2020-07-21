package playground.develop.fdelivery.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @see <a href="https://www.androidtutorialonline.com/draw-curved-bottom-navigation-view">Draw Curved Bottom Navigation View</a>
 */

class CurvedBottomNavigationView : BottomNavigationView {
    private var mPath: Path? = null
    private var mPaint: Paint? = null

    /** the CURVE_CIRCLE_RADIUS represent the radius of the fab button  */
    val CURVE_CIRCLE_RADIUS = 256 / 3

    // the coordinates of the first curve
    var mFirstCurveStartPoint: Point = Point()
    var mFirstCurveEndPoint: Point = Point()
    var mFirstCurveControlPoint2: Point = Point()
    var mFirstCurveControlPoint1: Point = Point()

    //the coordinates of the second curve
    var mSecondCurveStartPoint: Point = Point()
    var mSecondCurveEndPoint: Point = Point()
    var mSecondCurveControlPoint1: Point = Point()
    var mSecondCurveControlPoint2: Point = Point()
    var mNavigationBarWidth = 0
    var mNavigationBarHeight = 0

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!,
            attrs,
            defStyleAttr) {
        init()
    }

    private fun init() {
        mPath = Path()
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.color = Color.WHITE
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = width
        mNavigationBarHeight = height
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(mNavigationBarWidth / 2 - CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS / 3,
                0)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(mNavigationBarWidth / 2,
                CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4)
        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set(mNavigationBarWidth / 2 + CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS / 3,
                0)

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(mFirstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4,
                mFirstCurveStartPoint.y)
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(mFirstCurveEndPoint.x - CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS,
                mFirstCurveEndPoint.y)
        mSecondCurveControlPoint1.set(mSecondCurveStartPoint.x + CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS,
                mSecondCurveStartPoint.y)
        mSecondCurveControlPoint2.set(mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4),
                mSecondCurveEndPoint.y)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath!!.reset()
        mPath!!.moveTo(0f, 0f)
        mPath!!.lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())
        mPath!!.cubicTo(mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat())
        mPath!!.cubicTo(mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat())
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), 0f)
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
        mPath!!.lineTo(0f, mNavigationBarHeight.toFloat())
        mPath!!.close()
        canvas.drawPath(mPath!!, mPaint!!)
    }
}