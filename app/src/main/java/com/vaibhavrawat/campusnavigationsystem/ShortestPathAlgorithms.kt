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
        val dist = mutableMapOf<String, Int>()
        val prev = mutableMapOf<String, String>()
        val explanation = mutableListOf<String>()
        val queue = PriorityQueue(compareBy<String> { dist[it] ?: Int.MAX_VALUE })

        graph.nodeNames.forEach { dist[it] = Int.MAX_VALUE }
        dist[start] = 0
        queue.add(start)
        explanation.add("Starting from node: $start")

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            explanation.add("Visiting node: $current with current distance ${dist[current]}")

            if (current == end) break

            for (edge in graph.getAdjEdges(current)) {
                val newDist = dist[current]!! + edge.weight
                if (newDist < dist[edge.to]!!) {
                    dist[edge.to] = newDist
                    prev[edge.to] = current
                    queue.add(edge.to)
                    explanation.add("  ↳ Updating distance to ${edge.to} = $newDist via $current")
                }
            }
        }

        val path = buildPath(prev, start, end)
        return PathResult(
            path,
            dist[end] ?: Int.MAX_VALUE,
            explanation,
            "Dijkstra’s Algorithm\nTime Complexity: O((V + E) log V)"
        )
    }

    fun bellmanFord(graph: Graph, start: String, end: String): PathResult {
        val dist = mutableMapOf<String, Int>()
        val prev = mutableMapOf<String, String>()
        val explanation = mutableListOf<String>()

        graph.nodeNames.forEach { dist[it] = Int.MAX_VALUE }
        dist[start] = 0
        explanation.add("Starting from node: $start")

        for (i in 1 until graph.nodeNames.size) {
            explanation.add("Iteration $i:")
            for (edge in graph.getEdges()) {
                val u = edge.from
                val v = edge.to
                val w = edge.weight

                if (dist[u] != Int.MAX_VALUE && dist[u]!! + w < dist[v]!!) {
                    dist[v] = dist[u]!! + w
                    prev[v] = u
                    explanation.add("  ↳ Relaxed edge $u → $v, new distance: ${dist[v]}")
                }
            }
        }

        val path = buildPath(prev, start, end)
        return PathResult(
            path,
            dist[end] ?: Int.MAX_VALUE,
            explanation,
            "Bellman-Ford Algorithm\nTime Complexity: O(V * E)\nHandles negative weights."
        )
    }

    fun johnson(graph: Graph, start: String, end: String): PathResult {
        val dijkstraResult = dijkstra(graph, start, end)
        return dijkstraResult.copy(
            algorithmInfo = "Johnson’s Algorithm (Simplified)\nUsed for all-pairs shortest path\nTime Complexity: O(VE + V log V)"
        )
    }

    private fun buildPath(prev: Map<String, String>, start: String, end: String): List<String> {
        val path = LinkedList<String>()
        var curr = end
        while (curr != start) {
            path.addFirst(curr)
            curr = prev[curr] ?: return emptyList()
        }
        path.addFirst(start)
        return path
    }
}
