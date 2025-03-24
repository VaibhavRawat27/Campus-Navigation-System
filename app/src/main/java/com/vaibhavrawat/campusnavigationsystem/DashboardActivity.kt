package com.vaibhavrawat.campusnavigationsystem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class DashboardActivity : AppCompatActivity() {
    private lateinit var sourceSpinner: Spinner
    private lateinit var destinationSpinner: Spinner
    private lateinit var algorithmSpinner: Spinner
    private lateinit var findPathButton: Button
    private lateinit var resultText: TextView
    private lateinit var graphView: CustomGraphView
    lateinit var btnReset: Button
    lateinit var loadingText: TextView
    lateinit var textToggle: TextView
    lateinit var aboutText: TextView

    private lateinit var graph: Graph


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        btnReset = findViewById(R.id.buttonReset)
        // Initialize UI
        sourceSpinner = findViewById(R.id.spinnerSource)
        destinationSpinner = findViewById(R.id.spinnerDestination)
        algorithmSpinner = findViewById(R.id.spinnerAlgorithm)
        findPathButton = findViewById(R.id.buttonFindPath)
        resultText = findViewById(R.id.textResult)
        graphView = findViewById(R.id.graphView)

        loadingText = findViewById(R.id.textLoading)

        textToggle = findViewById(R.id.textToggleAbout)
        aboutText = findViewById(R.id.textAboutGraph)
        textToggle.setOnClickListener { v: View? ->
            if (aboutText.visibility == View.GONE) {
                aboutText.visibility = View.VISIBLE
                textToggle.text = "▼ About this graph"
            } else {
                aboutText.visibility = View.GONE
                textToggle.text = "▶ About this graph"
            }
        }

        val infoButton: ImageView = findViewById(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }
        // Initialize graph
        graph = Graph()
        graph.addNode("A", 500f, 100f)
        graph.addNode("B", 900f, 100f)
        graph.addNode("C", 300f, 300f)
        graph.addNode("D", 500f, 300f)
        graph.addNode("E", 700f, 300f)
        graph.addNode("F", 700f, 500f)
        graph.addNode("G", 1000f, 500f)
        graph.addNode("H", 900f, 600f)
        graph.addNode("I", 700f, 700f)
        graph.addNode("J", 500f, 700f)

        graph.addEdge("A", "C", 5)
        graph.addEdge("A", "D", 4)
        graph.addEdge("D", "J", 12)
        graph.addEdge("B", "E", 4)
        graph.addEdge("D", "E", 3)
        graph.addEdge("E", "F", 2)
        graph.addEdge("F", "G", 3)
        graph.addEdge("F", "H", 4)
        graph.addEdge("G", "H", 3)
        graph.addEdge("J", "I", 2)
        graph.addEdge("I", "H", 2)
        graph.addEdge("J", "F", 7)

        graphView.setGraph(graph)

        val nodeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            graph.nodeNames
        )
        sourceSpinner.adapter = nodeAdapter
        destinationSpinner.adapter = nodeAdapter

        val algoAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Dijkstra", "Bellman-Ford", "Johnson")
        )
        algorithmSpinner.adapter = algoAdapter

        findPathButton.setOnClickListener {
            val src = sourceSpinner.selectedItem.toString()
            val dest = destinationSpinner.selectedItem.toString()
            val algo = algorithmSpinner.selectedItem.toString()

            resultText.text = "Finding path"
            var dots = 0

            val loadingHandler = Handler(Looper.getMainLooper())
            val loadingRunnable = object : Runnable {
                override fun run() {
                    resultText.text = "Finding path" + ".".repeat(dots % 4)
                    dots++
                    loadingHandler.postDelayed(this, 500)
                }
            }

            loadingHandler.post(loadingRunnable)

            Handler(Looper.getMainLooper()).postDelayed({
                val result = when (algo) {
                    "Dijkstra" -> ShortestPathAlgorithms.dijkstra(graph, src, dest)
                    "Bellman-Ford" -> ShortestPathAlgorithms.bellmanFord(graph, src, dest)
                    "Johnson" -> ShortestPathAlgorithms.johnson(graph, src, dest)
                    else -> null
                }

                loadingHandler.removeCallbacksAndMessages(null)

                result?.let {
                    resultText.text = "Path Found: ${it.path.joinToString(" -> ")}\nTotal Weight: ${it.totalWeight}\n\n${it.algorithmInfo}"

                    // Animate path drawing
                    Thread {
                        val drawnPath = mutableListOf<String>()
                        for (node in it.path) {
                            drawnPath.add(node)
                            runOnUiThread {
                                graphView.setPath(drawnPath)
                            }
                            Thread.sleep(200)
                        }
                    }.start()
                } ?: run {
                    resultText.text = "No path found."
                }

            }, 2000) // Simulate 2 seconds loading
        }

        btnReset.setOnClickListener {
            sourceSpinner.setSelection(0)
            destinationSpinner.setSelection(0)
            algorithmSpinner.setSelection(0)
            resultText.text = "Path will appear here..."
            graphView.clearPath()
        }


    }
}
