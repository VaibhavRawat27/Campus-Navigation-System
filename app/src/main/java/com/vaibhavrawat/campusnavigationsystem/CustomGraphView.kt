package com.vaibhavrawat.campusnavigationsystem

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomGraphView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var nodePaint: Paint? = null
    private var edgePaint: Paint? = null
    private var textPaint: Paint? = null
    private var pathPaint: Paint? = null

    private var graph: Graph? = null
    private var path: List<String> = ArrayList()

    init {
        init()
    }

    private fun init() {
        nodePaint = Paint()
        nodePaint!!.color = Color.BLUE
        nodePaint!!.style = Paint.Style.FILL
        nodePaint!!.isAntiAlias = true

        edgePaint = Paint()
        edgePaint!!.color = Color.GRAY
        edgePaint!!.strokeWidth = 6f
        edgePaint!!.isAntiAlias = true

        textPaint = Paint()
        textPaint!!.color = Color.BLACK
        textPaint!!.textSize = 40f
        textPaint!!.isAntiAlias = true

        pathPaint = Paint()
        pathPaint!!.color = Color.RED
        pathPaint!!.strokeWidth = 8f
        pathPaint!!.isAntiAlias = true
    }

    fun setGraph(graph: Graph?) {
        this.graph = graph
        invalidate()
    }

    fun setPath(path: List<String>) {
        this.path = path
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

        // Calculate available space
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        // Scaling factor to fit graph inside view
        val scale = 0.85f * minOf(viewWidth / graphWidth, viewHeight / graphHeight)

        // Centering offsets after scaling
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

    fun clearPath() {
        path = emptyList()
        invalidate()
    }




}