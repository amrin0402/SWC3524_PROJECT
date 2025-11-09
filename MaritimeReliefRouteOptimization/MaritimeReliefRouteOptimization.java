import java.util.*;

public class MaritimeReliefRouteOptimization {

    // Distance Matrix (Adjacency Matrix)
    static int[][] distanceMatrix = {
        {0, 15, 25, 35},
        {15, 0, 30, 28},
        {25, 30, 0, 20},
        {35, 28, 20, 0}
    };

    // Location names
    static String[] locations = {"Port A", "Port B", "Relief Center C", "Relief Center D"};

    // ======================================================
    // =============== GREEDY TSP ALGORITHM =================
    // ======================================================
    public static String greedyTSP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        int totalDistance = 0;
        int current = 0; // Start at Port A
        visited[current] = true;

        StringBuilder route = new StringBuilder(locations[current]);

        for (int step = 1; step < n; step++) {
            int nearest = -1;
            int minDist = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[current][j] < minDist) {
                    minDist = dist[current][j];
                    nearest = j;
                }
            }

            totalDistance += minDist;
            current = nearest;
            visited[current] = true;
            route.append(" -> ").append(locations[current]);
        }

        totalDistance += dist[current][0];
        route.append(" -> ").append(locations[0]);

        return "Greedy TSP Route: " + route.toString() + " | Total Distance: " + totalDistance + " nm";
    }

    // -------------------------------------------------
    // Divide and Conquer TSP
    // -------------------------------------------------
    public static String divideAndConquerTSP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;

        StringBuilder path = new StringBuilder(locations[0]);
        int minCost = divideAndConquerHelper(0, visited, 0, dist, n, path);

        path.append(" -> ").append(locations[0]);
        return "Divide & Conquer TSP Route: " + path + " | Total Distance: " + minCost + " nm";
    }

    private static int divideAndConquerHelper(int pos, boolean[] visited, int currentCost,
                                              int[][] dist, int n, StringBuilder path) {
        if (allVisited(visited)) return currentCost + dist[pos][0];

        int minCost = Integer.MAX_VALUE;
        String bestNext = "";

        for (int city = 0; city < n; city++) {
            if (!visited[city]) {
                visited[city] = true;
                StringBuilder tempPath = new StringBuilder(path);
                tempPath.append(" -> ").append(locations[city]);

                int newCost = divideAndConquerHelper(city, visited, currentCost + dist[pos][city], dist, n, tempPath);

                if (newCost < minCost) {
                    minCost = newCost;
                    bestNext = tempPath.toString();
                }

                visited[city] = false;
            }
        }

        path.setLength(0);
        path.append(bestNext);
        return minCost;
    }

    private static boolean allVisited(boolean[] visited) {
        for (boolean v : visited) if (!v) return false;
        return true;
    }

    // -------------------------------
    // Dynamic Programming TSP
    // -------------------------------
    public static String dynamicProgrammingTSP(int[][] dist) {
        int n = dist.length;
        int VISITED_ALL = (1 << n) - 1;

        int[][] memo = new int[n][1 << n];
        String[][] paths = new String[n][1 << n];

        for (int[] row : memo) Arrays.fill(row, -1);

        int minCost = dynamicProgrammingTSPHelper(0, 1, dist, memo, VISITED_ALL, paths);
        String bestPath = paths[0][1];

        return "Dynamic Programming TSP Route: " + bestPath + " | Total Distance: " + minCost + " nm";
    }

    private static int dynamicProgrammingTSPHelper(int pos, int mask, int[][] dist,
                                                   int[][] memo, int VISITED_ALL, String[][] paths) {
        int n = dist.length;
        if (mask == VISITED_ALL) {
            paths[pos][mask] = locations[pos] + " -> " + locations[0];
            return dist[pos][0];
        }

        if (memo[pos][mask] != -1) return memo[pos][mask];

        int ans = Integer.MAX_VALUE;
        String bestPath = "";

        for (int city = 0; city < n; city++) {
            if ((mask & (1 << city)) == 0) {
                int newMask = mask | (1 << city);
                int newCost = dist[pos][city] +
                        dynamicProgrammingTSPHelper(city, newMask, dist, memo, VISITED_ALL, paths);

                if (newCost < ans) {
                    ans = newCost;
                    bestPath = locations[pos] + " -> " + paths[city][newMask];
                }
            }
        }

        memo[pos][mask] = ans;
        paths[pos][mask] = bestPath;
        return ans;
    }

    // ------------------------------------
    // Backtracking TSP
    // ------------------------------------
    public static String backtrackingTSP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;

        StringBuilder path = new StringBuilder(locations[0]);
        int minCost = tspBacktracking(0, dist, visited, n, 1, 0, path);

        path.append(" -> ").append(locations[0]);
        return "Backtracking TSP Route: " + path + " | Total Distance: " + minCost + " nm";
    }

    private static int tspBacktracking(int pos, int[][] dist, boolean[] visited,
                                       int n, int count, int cost, StringBuilder path) {
        if (count == n) return cost + dist[pos][0];

        int minCost = Integer.MAX_VALUE;
        String bestPath = "";

        for (int city = 0; city < n; city++) {
            if (!visited[city]) {
                visited[city] = true;
                StringBuilder tempPath = new StringBuilder(path);
                tempPath.append(" -> ").append(locations[city]);

                int newCost = tspBacktracking(city, dist, visited, n, count + 1, cost + dist[pos][city], tempPath);

                if (newCost < minCost) {
                    minCost = newCost;
                    bestPath = tempPath.toString();
                }

                visited[city] = false;
            }
        }

        path.setLength(0);
        path.append(bestPath);
        return minCost;
    }

    // ------------------------------------
    // Insertion Sort
    // ------------------------------------
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // ------------------------------------
    // Binary Search
    // ------------------------------------
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // =====================================
    // Min-Heap
    // =====================================
    static class MinHeap {
        private PriorityQueue<Integer> heap = new PriorityQueue<>();

        public void insert(int val) {
            heap.add(val); // maintains min-heap automatically
        }

        public int extractMin() {
            if (heap.isEmpty()) return -1;
            return heap.poll();
        }
    }

    // =====================================
    // Splay Tree placeholder
    // =====================================
    static class SplayTree {
        // TO BE IMPLEMENTED: Splay Tree logic
        public void insert(int key) {
            System.out.println("Insert " + key + " into Splay Tree (not implemented)");
        }

        public boolean search(int key) {
            System.out.println("Search " + key + " in Splay Tree (not implemented)");
            return true;
        }
    }

    // ======================================================
    // MAIN METHOD
    // ======================================================
    public static void main(String[] args) {
        System.out.println("=== INTERNATIONAL MARITIME RELIEF MISSION ===\n");

        System.out.println(greedyTSP(distanceMatrix));
        System.out.println(dynamicProgrammingTSP(distanceMatrix));
        System.out.println(backtrackingTSP(distanceMatrix));
        System.out.println(divideAndConquerTSP(distanceMatrix));

        int[] arr = {8, 3, 5, 1, 9, 2};
        insertionSort(arr);
        System.out.println("Sorted Array: " + Arrays.toString(arr));
        System.out.println("Binary Search (5 found at index): " + binarySearch(arr, 5));

        // Min-Heap Test
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(3);
        heap.insert(15);
        System.out.println("Min-Heap Extract Min: " + heap.extractMin());

        // Splay Tree Test
        SplayTree tree = new SplayTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        System.out.println("Splay Tree Search (10 found): " + tree.search(10));
    }
}
