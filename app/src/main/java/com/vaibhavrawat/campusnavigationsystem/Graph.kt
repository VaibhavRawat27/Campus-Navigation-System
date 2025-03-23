package com.vaibhavrawat.campusnavigationsystem

class Graph {

    class Node(var name: String, var x: Float, var y: Float)

    class Edge(var from: String, var to: String, var weight: Int)

    private val nodes: MutableMap<String, Node> = HashMap()
    private val edges: MutableList<Edge> = ArrayList()
    private val adjacencyList: MutableMap<String, MutableList<Edge>> = HashMap()

    fun addNode(name: String, x: Float, y: Float) {
        val node = Node(name, x, y)
        nodes[name] = node
        adjacencyList[name] = ArrayList()
    }

    fun addEdge(from: String, to: String, weight: Int) {
        // Add edge in both directions to make the graph undirected
        val edge = Edge(from, to, weight)
        val reverseEdge = Edge(to, from, weight)
        edges.add(edge)
        edges.add(reverseEdge)

        adjacencyList[from]?.add(edge)
        adjacencyList[to]?.add(reverseEdge)
    }

    fun getNode(name: String): Node? {
        return nodes[name]
    }

    fun getNodes(): List<Node> {
        return ArrayList(nodes.values)
    }

    fun getEdges(): List<Edge> {
        return edges
    }

    fun getAdjEdges(nodeName: String): List<Edge> {
        return adjacencyList.getOrDefault(nodeName, ArrayList())
    }

    val nodeNames: List<String>
        get() = ArrayList(nodes.keys)

    fun getAdjacencyList(): Map<String, MutableList<Edge>> {
        return adjacencyList
    }
}
