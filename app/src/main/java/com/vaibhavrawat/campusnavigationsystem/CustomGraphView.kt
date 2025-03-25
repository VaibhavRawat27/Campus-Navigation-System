package com.vaibhavrawat.campusnavigationsystem

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class CustomGraphView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var nodePaint: Paint? = null
    private var edgePaint: Paint? = null
    private var textPaint: Paint? = null
    private var pathPaint: Paint? = null

    private var graph: Graph? = null
    private var path: List<String> = ArrayList()

    // Tooltip map
    private val nodeTooltips = mapOf(
        "A" to "Main Gate",
        "B" to "Second Gate",
        "C" to "Himalaya Boys Hostel",
        "D" to "Block 5 (CPC)",
        "E" to "Block 4 (CLC)",
        "F" to "Block 3 (CCE)",
        "G" to "Sports Arena",
        "H" to "Gangotri Girls Hostel",
        "I" to "Block 2 (CSB)",
        "J" to "Block 1 (CEC)"
    )

    init {
        init()
    }

    private fun init() {
        nodePaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        edgePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 6f
            isAntiAlias = true
        }

        textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
            isAntiAlias = true
        }

        pathPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 8f
            isAntiAlias = true
        }
    }

    fun setGraph(graph: Graph?) {
        this.graph = graph
        invalidate()
    }

    fun setPath(path: List<String>) {
        this.path = path
        invalidate()
    }

    fun clearPath() {
        path = emptyList()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (graph == null) return

        val nodes = graph!!.getNodes()
        if (nodes.isEmpty()) return

        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        for (node in nodes) {
            if (node.x < minX) minX = node.x
            if (node.y < minY) minY = node.y
            if (node.x > maxX) maxX = node.x
            if (node.y > maxY) maxY = node.y
        }

        val graphWidth = maxX - minX
        val graphHeight = maxY - minY

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        val scale = 0.85f * minOf(viewWidth / graphWidth, viewHeight / graphHeight)
        val offsetX = (viewWidth - graphWidth * scale) / 2 - minX * scale
        val offsetY = (viewHeight - graphHeight * scale) / 2 - minY * scale

        canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(scale, scale)

        // Draw edges
        for (edge in graph!!.getEdges()) {
            val from = graph!!.getNode(edge.from)
            val to = graph!!.getNode(edge.to)
            if (from != null && to != null) {
                canvas.drawLine(from.x, from.y, to.x, to.y, edgePaint!!)
                val midX = (from.x + to.x) / 2f
                val midY = (from.y + to.y) / 2f
                canvas.drawText(edge.weight.toString(), midX, midY, textPaint!!)
            }
        }

        // Draw path
        for (i in 0 until path.size - 1) {
            val from = graph!!.getNode(path[i])
            val to = graph!!.getNode(path[i + 1])
            if (from != null && to != null) {
                canvas.drawLine(from.x, from.y, to.x, to.y, pathPaint!!)
            }
        }

        // Draw nodes
        for (node in nodes) {
            canvas.drawCircle(node.x, node.y, 20f, nodePaint!!)
            canvas.drawText(node.name, node.x + 25, node.y, textPaint!!)
        }

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN && graph != null) {
            val nodes = graph!!.getNodes()

            val minX = nodes.minOf { it.x }
            val minY = nodes.minOf { it.y }
            val maxX = nodes.maxOf { it.x }
            val maxY = nodes.maxOf { it.y }

            val graphWidth = maxX - minX
            val graphHeight = maxY - minY
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val scale = 0.85f * minOf(viewWidth / graphWidth, viewHeight / graphHeight)
            val offsetX = (viewWidth - graphWidth * scale) / 2 - minX * scale
            val offsetY = (viewHeight - graphHeight * scale) / 2 - minY * scale

            val touchX = (event.x - offsetX) / scale
            val touchY = (event.y - offsetY) / scale

            for (node in nodes) {
                val dx = touchX - node.x
                val dy = touchY - node.y
                if (dx * dx + dy * dy <= 400) { // radius 20px
                    val tooltip = nodeTooltips[node.name]
                    if (tooltip != null) {
                        Toast.makeText(context, "${node.name} : $tooltip", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, node.name, Toast.LENGTH_SHORT).show()
                    }
                    break
                }
            }
        }
        return true
    }
}
