🧭 Campus Navigation System - CGC Jhanjeri
An Android-based campus navigation tool that helps students, faculty, and visitors find the shortest path between blocks within the campus (Map used: CGC Jhanjeri). The app uses graph algorithms like Dijkstra, Bellman-Ford, and Johnson’s to simulate and visualize the most efficient routes.

📍 Built with: Android Studio (Kotlin) and Custom Graph Algorithms

🗺️ How It Works
Each building (A, B, C...) is a node, and each path is an edge with weights representing real distances (in meters). Users can choose source, destination, and algorithm to find the optimal path.

📌 Features
Visualized shortest paths on campus
Selectable algorithms
Animated graph traversal
Displays total path distance
Clean, interactive UI

📚 Algorithms Used
Dijkstra:	Fastest for non-negative weights O((V + E) log V)
Bellman-Ford:	Handles negative weights, simple O(V × E)
Johnson’s:	All-pairs shortest paths simulation	O(VE + V log V)

📷 Visuals
🏫 College Map Layout
<img src="assets/college_map.png" width="600"/>
🔗 Graph Representation
<img src="assets/campus_graph.png" width="600"/>
📊 Algorithm Flow
<img src="assets/algorithm_used.png" width="600"/>

ℹ️ Additional Info
Coordinates placed to replicate actual campus layout
User-friendly UI with dropdown selection
Reset and re-calculate any time
Built as part of Experiential Learning Project (2025)
