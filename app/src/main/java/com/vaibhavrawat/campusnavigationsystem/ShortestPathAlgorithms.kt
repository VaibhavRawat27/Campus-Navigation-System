import com.vaibhavrawat.campusnavigationsystem.Graph
import java.util.LinkedList
import java.util.PriorityQueue

data class PathResult(
    val path: List<String>,
    val totalWeight: Int,
    val stepsExplanation: List<String>,
    val algorithmInfo: String
)

object ShortestPathAlgorithms {

    fun dijkstra(graph: Graph, start: String, end: String): PathResult {
        val distance = mutableMapOf<String, Int>()
        val previous = mutableMapOf<String, String>()
        val explanation = mutableListOf<String>()
        val queue = PriorityQueue(compareBy<String> { distance[it] ?: Int.MAX_VALUE })

        for (node in graph.nodeNames) distance[node] = Int.MAX_VALUE
        distance[start] = 0
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current == end) break

            for (edge in graph.getAdjEdges(current)) {
                val newDist = distance[current]!! + edge.weight
                if (newDist < distance[edge.to]!!) {
                    explanation.add("Updated distance to ${edge.to}: $newDist via ${edge.from}")
                    distance[edge.to] = newDist
                    previous[edge.to] = current
                    queue.add(edge.to)
                }
            }
        }

        val path = buildPath(previous, start, end)
        return PathResult(
            path,
            distance[end] ?: Int.MAX_VALUE,
            explanation,
            "Dijkstra Algorithm:\nTime Complexity: O((V + E) log V)\nGreedy algorithm using priority queue (min-heap)."
        )
    }

    fun bellmanFord(graph: Graph, start: String, end: String): PathResult {
        val distance = mutableMapOf<String, Int>()
        val previous = mutableMapOf<String, String>()
        val explanation = mutableListOf<String>()

        for (node in graph.nodeNames) distance[node] = Int.MAX_VALUE
        distance[start] = 0

        for (i in 0 until graph.nodeNames.size - 1) {
            for (edge in graph.getEdges()) {
                val fromDist = distance[edge.from] ?: Int.MAX_VALUE
                if (fromDist != Int.MAX_VALUE && fromDist + edge.weight < distance[edge.to]!!) {
                    distance[edge.to] = fromDist + edge.weight
                    previous[edge.to] = edge.from
                    explanation.add("Relaxed edge ${edge.from} -> ${edge.to}, new dist: ${distance[edge.to]}")
                }
            }
        }

        val path = buildPath(previous, start, end)
        return PathResult(
            path,
            distance[end] ?: Int.MAX_VALUE,
            explanation,
            "Bellman-Ford Algorithm:\nTime Complexity: O(V * E)\nCan handle negative weights."
        )
    }

    fun johnson(graph: Graph, start: String, end: String): PathResult {
        return dijkstra(graph, start, end).copy(
            algorithmInfo = "Johnson's Algorithm (Simplified for one source):\nTime Complexity: O(VE + V log V)\nUsed for all-pairs shortest paths, here simulated using Dijkstra."
        )
    }

    private fun buildPath(prev: Map<String, String>, start: String, end: String): List<String> {
        val path = LinkedList<String>()
        var current: String? = end
        while (current != null && current != start) {
            path.addFirst(current)
            current = prev[current]
        }
        if (current != null) path.addFirst(start)
        return path
    }
}
