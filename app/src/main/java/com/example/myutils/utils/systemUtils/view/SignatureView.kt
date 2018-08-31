package com.example.myutils.utils.systemUtils.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.*

/**
 * 创 建 人: 艺仔加油
 * 创建时间: 2018/8/5 9:18
 * 本类描述: 签名画板
 */

class SignatureView : View {

    private var mContext: Context? = null

    /**
     * 笔画X坐标起点
     */
    private var mX: Float = 0.toFloat()

    /**
     * 笔画Y坐标起点
     */
    private var mY: Float = 0.toFloat()

    /**
     * 手写画笔
     */
    private val mGesturePaint = Paint()

    /**
     * 路径
     */
    private val mPath = Path()

    /**
     * 签名画笔
     */
    private var cacheCanvas: Canvas? = null

    /**
     * 签名画布
     */
    private var cacheBitmap: Bitmap? = null

    /**
     * 是否有签名
     */
    private var isTouched = false

    /**
     * 画笔宽度 px
     */
    private var mPaintWidth = 10

    /**
     * 前景色
     */
    private var mPenColor = Color.BLACK

    /**
     * 背景色（指最终签名结果文件的背景颜色，默认为透明色）
     * 备注：保存文件的背景色（默认是Color.TRANSPARENT透明色，实则看到的是黑背景）
     */
    private var mBackColor = Color.WHITE

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        this.mContext = context
        //设置抗锯齿
        mGesturePaint.isAntiAlias = true
        //设置签名笔画样式
        mGesturePaint.style = Paint.Style.STROKE
        //设置画笔宽度
        mGesturePaint.strokeWidth = mPaintWidth.toFloat()
        //设置签名颜色
        mGesturePaint.color = mPenColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //创建跟View一样大的bitmap，用来保存签名
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap)
        cacheCanvas!!.drawColor(mBackColor)
        isTouched = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchDown(event)
            MotionEvent.ACTION_MOVE -> {
                isTouched = true
                touchMove(event)
            }
            MotionEvent.ACTION_UP -> {
                //将路径画到bitmap中，即以此画笔完成采取更新bitmap，二手势轨迹是实时显示在画板上的
                cacheCanvas!!.drawPath(mPath, mGesturePaint)
                mPath.reset()
            }
        }
        //更新绘制
        invalidate()
        return true
    }

    fun touchDown(event: MotionEvent) {
        //重置绘制路线
        mPath.reset()
        val x = event.x
        val y = event.y
        mX = x
        mY = y
        mPath.moveTo(x, y)
    }

    fun touchMove(event: MotionEvent) {
        val x = event.x
        val y = event.y
        val previousX = mX
        val previousY = mY
        val dx = Math.abs(x - previousX)
        val dy = Math.abs(y - previousY)
        //两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            //设置贝塞尔曲线的操作点为起点和终点的一半
            val cX = (x + previousX) / 2
            val cY = (y + previousY) / 2
            //二次贝塞尔，实现平滑曲线；previousX,previousY为操作点，cX,cY为终点
            mPath.quadTo(previousX, previousY, cX, cY)
            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x
            mY = y
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画此次画笔之前的签名
        canvas.drawBitmap(cacheBitmap, 0.toFloat(), 0.toFloat(), mGesturePaint)
        //通过画布绘制多点形成的图形
        canvas.drawPath(mPath, mGesturePaint)
    }

    /**
     * 清除画板
     */
    fun clear() {
        isTouched = false
        //更新画板信息
        mGesturePaint.color = mPenColor
        cacheCanvas!!.drawColor(mBackColor, PorterDuff.Mode.CLEAR)
        mGesturePaint.color = mPenColor
        invalidate()
    }

    /**
     * 保存画板
     */
    @Throws(IOException::class)
    fun save(path: String) {
        var bitmap = cacheBitmap
        val bos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val buffer = bos.toByteArray()
        if (buffer != null) {
            val file = File(path)
            if (file.exists()) file.delete()
            val os = FileOutputStream(file)
            os.write(buffer)
            os.close()
        }
    }

    /**
     * 设置画笔宽度，默认10px
     */
    fun setPaintWidth(paintWidth: Int) {
        var with = paintWidth
        with = if (paintWidth > 0) paintWidth else 10
        this.mPaintWidth = with
        mGesturePaint.strokeWidth = mPaintWidth.toFloat()
    }

    /**
     * 设置画笔颜色
     */
    fun setPenColor(penColor: Int) {
        this.mPenColor = penColor
        mGesturePaint.color = mPenColor
    }

    /**
     * 是否有签名
     */
    fun getTouched() = isTouched
}