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

            // Find nearest unvisited location
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[current][j] < minDist) {
                    minDist = dist[current][j];
                    nearest = j;
                }
            }

            // Move to that location
            totalDistance += minDist;
            current = nearest;
            visited[current] = true;
            route.append(" -> ").append(locations[current]);
        }

        // Return to starting point
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
        visited[0] = true; // Start from Port A

        StringBuilder path = new StringBuilder(locations[0]);
        int minCost = divideAndConquerHelper(0, visited, 0, dist, n, path);

        // Close the loop
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
    // Dynamic Programming (Bitmasking) TSP
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
    // Min-Heap Implementation
    // =====================================
    static class MinHeap {
        private ArrayList<Integer> heap;

        public MinHeap() {
            heap = new ArrayList<>();
        }

        public void insert(int val) {
            heap.add(val);
            int i = heap.size() - 1;
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) < heap.get(parent)) {
                    int temp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, temp);
                    i = parent;
                } else break;
            }
        }

        public int extractMin() {
            if (heap.size() == 0) return -1;
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (heap.size() == 0) return min;
            heap.set(0, last);
            heapify(0);
            return min;
        }

        private void heapify(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < heap.size() && heap.get(left) < heap.get(smallest)) smallest = left;
            if (right < heap.size() && heap.get(right) < heap.get(smallest)) smallest = right;

            if (smallest != i) {
                int temp = heap.get(i);
                heap.set(i, heap.get(smallest));
                heap.set(smallest, temp);
                heapify(smallest);
            }
        }
    }

    // =====================================
    // Splay Tree Implementation
    // =====================================
    static class SplayTree {
        class Node {
            int key;
            Node left, right;
            Node(int key) { this.key = key; }
        }

        private Node root;

        private Node rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        private Node splay(Node root, int key) {
            if (root == null || root.key == key) return root;

            if (key < root.key) {
                if (root.left == null) return root;
                if (key < root.left.key) {
                    root.left.left = splay(root.left.left, key);
                    root = rightRotate(root);
                } else if (key > root.left.key) {
                    root.left.right = splay(root.left.right, key);
                    if (root.left.right != null) root.left = leftRotate(root.left);
                }
                return (root.left == null) ? root : rightRotate(root);
            } else {
                if (root.right == null) return root;
                if (key < root.right.key) {
                    root.right.left = splay(root.right.left, key);
                    if (root.right.left != null) root.right = rightRotate(root.right);
                } else if (key > root.right.key) {
                    root.right.right = splay(root.right.right, key);
                    root = leftRotate(root);
                }
                return (root.right == null) ? root : leftRotate(root);
            }
        }

        public void insert(int key) {
            if (root == null) { root = new Node(key); return; }
            root = splay(root, key);
            if (root.key == key) return;
            Node newNode = new Node(key);
            if (key < root.key) {
                newNode.right = root;
                newNode.left = root.left;
                root.left = null;
            } else {
                newNode.left = root;
                newNode.right = root.right;
                root.right = null;
            }
            root = newNode;
        }

        public boolean search(int key) {
            root = splay(root, key);
            return root != null && root.key == key;
        }

        public void inorder() {
            inorderHelper(root);
            System.out.println();
        }

        private void inorderHelper(Node node) {
            if (node != null) {
                inorderHelper(node.left);
                System.out.print(node.key + " ");
                inorderHelper(node.right);
            }
        }
    }

    // ======================================================
    // MAIN METHOD (TEST EVERYTHING)
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
        System.out.print("Splay Tree Inorder Traversal: ");
        tree.inorder();
    }
}
