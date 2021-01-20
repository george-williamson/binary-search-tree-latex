public class Tree {

    private int nodeCounter = 0;
    private int depthCounter = 0;

    private Item root = null; // the root of the tree

    private class Item {  // Item for binary search tree
        private int value;
        private Item left, right, parent;
        private int xcoord, ycoord;

        // create a new item with parent p
        private Item(int x, Item p) {
            value = x;
            parent = p;
        }

        // a string representation of the item
        public String toString() {
            String s = value + ", left: ";
            if (left == null) {
                s += "null";
            } else {
                s += left.value;
            }
            s += ", right: ";
            if (right == null) {
                s += "null";
            } else {
                s += right.value;
            }
            return s;
        }
    }

    //finds the value x in the tree if it exists
    private Item find(int x) {
        Item p = root;
        while (p != null && p.value != x) {
            if (x < p.value) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return p;
    }

    // inserts x in the tree unless present;
    // returns pointer to item with value x
    private Item insert(int x) {
        if (root == null) {  // if the tree is empty
            root = new Item(x, null);
            return root;
        }
        Item p = root, q = null; // q stores the parent of p
        // find x, or the place where it should be inserted
        while (p != null) {
            q = p;
            if (x == p.value) { // value found
                return p;
            } else if (x < p.value) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        // insert x as child of q
        p = new Item(x, q);
        if (x < q.value) {
            q.left = p;  // q.left was null before
        } else {
            q.right = p; // q.right was null before
        }
        return p;
    }

    // finds an item with value x and deletes it
    private void delete(int x) {
        Item p = find(x);
        if (p == null) {  // item was not found, end
            return;
        } else if (p.right == null) { // no right child, easy to delete
            deleteWithoutRight(p);
        } else if (p.left == null) {  // no left child, easy to delete
            deleteWithoutLeft(p);
        } else {
            // otherwise find a smallest item q in right subtree of p
            // replace p by q and delete q in the right subtree
            Item q = p.right;
            while (q.left != null)
                q = q.left;
            p.value = q.value;
            deleteWithoutLeft(q); // the smallest element in a subtree has
            // no left child
        }
    }

    // deletes an item p which has no left child
    private void deleteWithoutLeft(Item p) {
        Item q = p.parent;
        if (q == null) {          // if p is the root
            root = p.right;          // delete p
            if (root != null) {      // tree did not get empty
                root.parent = null;
            }
        } else if (q.left == p) { // p is left child of q
            q.left = p.right;        // delete p
            if (q.left != null) {    // update left subtree of q
                q.left.parent = q;
            }
        } else {                  // p is right child of q
            q.right = p.right;       // delete p
            if (q.right != null) {   // update right subtree of q
                q.right.parent = q;
            }
        }
    }

    // deletes an item p which has no right child
    private void deleteWithoutRight(Item p) {
        Item q = p.parent;
        if (q == null) {          // if p is the root
            root = p.left;          // delete p
            if (root != null) {     // tree did not get empty
                root.parent = null;
            }
        } else if (q.left == p) { // p is left child of q
            q.left = p.left;         // delete p
            if (q.left != null) {    // update left subtree of q
                q.left.parent = q;
            }
        } else {                  // p is right child of q
            q.right = p.left;        // delete p
            if (q.right != null) {   // update right subtree of q
                q.right.parent = q;
            }
        }
    }

    //finds the number of children a given node has
    private int width(Item subtreeRoot) {

        if (subtreeRoot == null) {
            return 0;
        }

        return 1 + width(subtreeRoot.left) + width(subtreeRoot.right);
    }

    //sets coordinates for tree nodes
    private void coordSetter(Item p, Graph g){

        if(p == null){
            return;
        }

        if(p.left != null) {
            //set coordinates
            p.left.xcoord = -1 + p.xcoord - width(p.left.right);
            p.left.ycoord = p.ycoord - 2;
            //add them to internal graph data structure ready for output
            g.addNode(p.left.xcoord, p.left.ycoord, p.left.value);
            //move to next node
            coordSetter(p.left, g);

        }
        if(p.right != null) {
            p.right.xcoord = 1 + p.xcoord + width(p.right.left);
            p.right.ycoord =  p.ycoord - 2;

            g.addNode(p.right.xcoord, p.right.ycoord, p.right.value);

            coordSetter(p.right, g);
        }
    }

    //adds edges to internal Graph data structure ready for output
    private void addTreeEdge(Item p, Graph g){

        if(p.left != null) {
            g.addEdge(p.xcoord, p.ycoord, p.left.xcoord, p.left.ycoord);
            addTreeEdge(p.left, g);
        }
        if(p.right != null) {
            g.addEdge(p.xcoord, p.ycoord, p.right.xcoord, p.right.ycoord);
            addTreeEdge(p.right, g);
        }
    }

    //add nodes to ready to graph
    private void addGraph(Graph g) {

        //set and add root node
        root.xcoord = width(root);
        root.ycoord = 0;
        g.addNode(root.xcoord, root.ycoord, root.value);

        //add coordinates to internal data structure ready for graphing
        coordSetter(root, g);

        //add edges ""
        addTreeEdge(root, g);

        //print the depth and number of nodes
        System.out.printf("average depth: %.3f,", avgdepth(root));
        System.out.println("\t size: " + nodeCounter);

        depthCounter = 0;
        nodeCounter = 0;

        g.outgraph();
    }

    //used by avgdepth & traverse methods to add to the depthCounter variable
    private void addDepth(Item p) {
        if (p.parent != null) {
            depthCounter++;
            addDepth(p.parent);
        }
    }

    //used by the avgdepth method to InOrder traverse the tree
    private void traverse(Item p) {

        if (p == null) {
            return;
        }

        traverse(p.left);
        this.nodeCounter++;
        addDepth(p);
        traverse(p.right);
    }

    //calculates the average depth of a binary search tree
    private double avgdepth(Item p) {

        if(p == null){
            return 0;
        }

        traverse(p);

        return (double) depthCounter / (double) nodeCounter;
    }

    //prints the BST in sorted ascending order
    private void printsorted(Item p) {

        if (p == null) {
            return;
        }

        printsorted(p.left);
        System.out.print(p.value + "  ");
        printsorted(p.right);
    }

    /*Why is the function correct?
    So firstly we create an array of size n. Ready to fill with elements [1,...,n]
    The first for loop sets a[i-1] the number i starting from i=1 to i=n. Giving us the array [1, 2, ... , n]
    We then generate a random index of at least i on every loop and less than n.
    Doing it this way will allow us to get indexes of the whole range 1...n and we DO NOT try to swap the
    same element with itself. Also only calls Math.random() n times.
    Then simply store array[random index] in a temp variable and now
    copy the value of arr[i] to arr[random index]. So essentially what we are doing is generating a random index
    each time and then swapping the value of the array at that index with the value of the array at index i
    (i being the current iteration of the loop).
    */

    //returns a random permutation of an array [1...n]
    private int[] randomperm(int n){

        //creates array [1...n]
        int[] arr = new int[n];

        for(int i=1; i<=n; i++) {
            arr[i - 1] = i;
        }

        for(int i=0; i < n; i++){
            //generate random index in range
            int randIndex = i + (int)(Math.random() * (n - i));

            //stores random value in the array
            int temp = arr[randIndex];
            //then swaps it with the current array index value
            arr[randIndex] = arr[i];
            arr[i] = temp;
        }

        return arr;
    }

    //prints out 20 random permutations of an array [1...n],
    // only called if program is given 1 negative command line argument
    private void randomtest(int n){

        System.out.println("20 random permutations: \n");

        for(int i = 0; i < 20; i++) {
            int[] arr = randomperm(n);
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[j] + "  ");
            }
            System.out.println();
        }
    }

    //alternates insertions and deletions and compares the old and new depths in tabular form
    private void depthComparisons(int n, int d, int r, Tree t){

         double[] oldDepthsArray = new double[r];
         double[] newDepthsArray = new double[r];

         for(int i = 0; i < r; i++) {
             int[] a = t.randomperm(n + d);

             //insert the first n numbers into the tree and print them
             for (int j = 0; j < n; j++) {
                 t.insert(a[j]);
             }

             oldDepthsArray[i] = avgdepth(t.root);

             //generate random perm q[1...n]
             int[] q = t.randomperm(n);

             //alternate deletions and insertions
             for (int k = 0; k < d; k++) {
                 t.delete(a[q[k]]);
                 t.insert(a[n + k]);
             }

             newDepthsArray[i] = avgdepth(t.root);
         }

         //print the table
         System.out.print("\\begin{tabular}{|l|");
         for(int i = 0; i < r; i++) {
             System.out.print("c|");
         }
         System.out.print("}\n\\hline\nOld depth:");
         for(int i = 0; i < r; i++){
             System.out.printf("& %.2f", oldDepthsArray[i]);
         }
         System.out.println("\\\\ \n\\hline\nNew depth:");
         for(int i = 0; i < r; i++){
             System.out.printf("& %.2f", newDepthsArray[i]);
        }
        System.out.println("\\\\ \n\\hline\n\\end{tabular}");

    }

    public static void main(String[] args) {

        //make sure command line arguments are strictly numerical
        for(int i = 0; i < args.length; i++) {
            try {
                int isInt = Integer.parseInt(args[i]);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter only numerical values as program arguments.");
                return;
            }
        }

        Graph graph1 = new Graph();
        Tree tree1 = new Tree();
        Graph graph2 = new Graph();


        //*** 0 COMMAND LINE ARGUMENTS ***
        if(args.length == 0){
            //print header
            graph1.outheader();

            //insert the nodes into the tree and print them
            int[] inputArr = tree1.randomperm(20);
            for (int i = 0; i < inputArr.length; i++) {
                tree1.insert(inputArr[i]);
                System.out.print(inputArr[i] + "  ");
            }
            System.out.println("are inserted\n");
            //print them in ascending order
            System.out.print("in sorted order:  ");
            tree1.printsorted(tree1.root);
            System.out.println("\n");

            //output the LaTeX code for the graph
            tree1.addGraph(graph1);
        }

        // ***** 1 COMMAND LINE ARGUMENT *****
        else if(args.length ==  1){
            if(Integer.parseInt(args[0]) > 0) {

                //print header
                graph1.outheader();

                int[] inputArr = tree1.randomperm(Math.abs(Integer.parseInt(args[0])));
                for (int i = 0; i < inputArr.length; i++) {
                    tree1.insert(inputArr[i]);
                    System.out.print(inputArr[i] + "  ");
                }
                System.out.println("are inserted\n");
                //print them in ascending order
                System.out.print("in sorted order:  ");
                tree1.printsorted(tree1.root);
                System.out.println("\n");

                tree1.addGraph(graph1);
             //to test permutation method with 1 negative command line argument
            }else if(Integer.parseInt(args[0]) < 0){

                tree1.randomtest(Math.abs(Integer.parseInt(args[0])));

             //i.e. if command line input is simply 0.
            }else{
                System.out.println("To create a random permutation. Please enter a number other than 0.");
                return;
            }
        }

        // ***** 2 COMMAND LINE ARGUMENTS *****
        else if(args.length == 2) {

            int n = Integer.parseInt(args[0]);
            int d = Integer.parseInt(args[1]);

            //do not allow a first argument to be less than the second otherwise
            //array selection will go out of bounds
            if(n<d){
                System.out.println("Please enter a first argument greater than or equal to the second.");
                return;
            }

            if(d<=0){
                System.out.println("Please enter a positive 2nd argument.");
                return;
            }

            graph1.outheader();

            //generate random permutation a[1...(n+d)]
            int[] a = tree1.randomperm(n+d);

            //insert the first n numbers into the tree and print them
            for(int i = 0; i < n; i++){
                tree1.insert(a[i]);
                System.out.print(a[i] + "  ");
            }
            System.out.println("are inserted\n");
            System.out.print("in sorted order:  ");
            tree1.printsorted(tree1.root);
            System.out.println();
            System.out.println();

            tree1.addGraph(graph1);

            //generate random perm q[1...n]
            int[] q = tree1.randomperm(n);

            //alternate deletions and insertions
            for(int i = 0; i < d; i++){
                tree1.delete(a[q[i]]);
                tree1.insert(a[n+i]);
            }

            System.out.println("— alternate delete/insert: —\n");
            //print numbers deleted
            for(int i = 0; i < d; i++){
                System.out.print(a[q[i]] + "  ");
            }
            System.out.println("are deleted\n");

            //print numbers inserted
            for(int i = 0; i < d; i++){
                System.out.print(a[n+i] + "  ");
            }
            System.out.println("are newly inserted\n");

            System.out.print("in sorted order:  ");
            tree1.printsorted(tree1.root);
            System.out.println();
            System.out.println();

            //print new graph
            tree1.addGraph(graph2);
        }

        // ***** 3 COMMAND LINE ARGUMENTS *****
        else if(args.length == 3){

            int n = Integer.parseInt(args[0]);
            int d = Integer.parseInt(args[1]);
            int r = Integer.parseInt(args[2]);

            //do not allow a first argument to be less than the second otherwise
            //array selection will go out of bounds
            if(n<d){
                System.out.println("Please enter a first argument greater than or equal to the second.");
                return;
            }

            if(d<=0){
                System.out.println("Please enter a positive 2nd argument.");
                return;
            }

            //print header
            graph1.outheader();
            //print table of depth comparisons
            tree1.depthComparisons(n, d , r, tree1);
        }

        // ***** MORE THAN 3 COMMAND LINE ARGUMENTS *****
        else{

            //output header
            graph1.outheader();

            //insert items into the tree and print each one
            for (int i = 0; i < args.length; i++) {
                tree1.insert(Integer.parseInt(args[i]));
                System.out.print(args[i] + "  ");
            }
            System.out.println("are inserted\n");

            //print them in ascending order
            System.out.print("in sorted order:  ");
            tree1.printsorted(tree1.root);
            System.out.println("\n");

            tree1.addGraph(graph1);
        }

        //output header
        if(args.length == 0 || Integer.parseInt(args[0]) > 0) {
            graph1.outfooter();
        }
    }
}
