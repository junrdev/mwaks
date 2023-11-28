package ke.ac.mwaks.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import ke.ac.mwaks.R

class PathDrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    val paint = Paint().apply {
        color = resources.getColor(R.color.rich_black)
        strokeWidth = 5f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path.moveTo(100f, 100f)
        path.lineTo(200f, 200f)
        path.quadTo(300f, 100f, 400f, 200f)

        canvas.drawPath(path, paint)
    }
}