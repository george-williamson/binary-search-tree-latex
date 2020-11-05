//CANDIDATE NUMBER: 35946


public class Graph {

    private final int maxcoordinate = 100;

    private int[] xArray = new int[0];
    private int[] yArray = new int[0];
    private int[] labelArray = new int[0];

    private int coord1_labelPosition;
    private int coord2_labelPosition;

    private int[] edgeArray1 = new int[0];
    private int[] edgeArray2 = new int[0];

    //do not make textwidth larger than 21
    private double textwidth = 17.0;
    private double oddsidemargin = -(2.54 - (21.0 - textwidth) / 2.0);
    private double minXcoord;
    private double maxXcoord;
    private double gridwidth;

    //finds min value of array
    public int arrayMin(int[] arr){

        if(arr == null){
            System.out.println("% array given to arrayMin method was null.\n");
            return 0;
        }

        int min = arr[0];

        for(int i=1; i<arr.length; i++){
            if(arr[i] < min){
                min = arr[i];
            }
        }

        return min;
    }

    //finds max value of array
    public int arrayMax(int[] arr){

        if(arr == null){
            System.out.println("% array given to arrayMin method was null.\n");
            return 0;
        }

        int max = arr[0];

        for(int i = 1; i < arr.length; i++){
            if(arr[i] > max){
                max = arr[i];
            }
        }

        return max;
    }

    //sets grid width between 0.3cm and 0.6cm
    //depending on X coordinate range of the graph nodes
    private void gridAdjustment(){

        if(maxXcoord - minXcoord == 0){
            System.out.println("% grid adjustment failed. Min X coord is equal to Max X coord.\n");
        }

        double gridWidthValue = textwidth / Math.abs(maxXcoord - minXcoord);

        if(gridWidthValue > 0.6){
            gridwidth = 0.600;
        }else if(gridWidthValue < 0.3){
            gridwidth = 0.300;
        }else{
            gridwidth = gridWidthValue;
        }
    }

    //outputs the LaTeX preamble
    public void outheader() {
        System.out.println(
                "\\documentclass[a4paper,11pt]{article}\n" +
                "\\usepackage{mathpazo}\n" +
                "\\usepackage{tikz}\n" +
                "\\usetikzlibrary{shapes}\n" +
                "\\oddsidemargin " + oddsidemargin + "cm\n" +
                "\\textwidth " + textwidth + "cm\n" +
                "\\textheight 24cm\n" +
                "\\topmargin -1.3cm\n" +
                "\\parindent 0pt\n" +
                "\\parskip 1ex\n" +
                "\\pagestyle{empty}\n" +
                "\\begin{document}\n" +
                "\\medskip\\hrule\\medskip\n");
    }

    //outputs the LaTex footer
    public void outfooter() {
        System.out.println("\\end{document}");
    }

    //adds nodes to internal array data structure
    public void addNode(int x, int y, int label) {

        if(xArray.length != yArray.length && xArray.length != labelArray.length){
            System.out.println("% internal array data structures not of same size. Failed to add node.");
            return;
        }

        if (Math.abs(x) > maxcoordinate) {
            System.out.println("% coordinate" + x + " in node" + "(" + x + "," + y + ") too large)");
            return;
        }

        if (Math.abs(y) > maxcoordinate) {
            System.out.println("% coordinate" + y + " in node" + "(" + x + "," + y + ") too large)");
            return;
        }

        /* NOTE TO MARKER: If we were allowed to use libraries then this method
        of copying and redefining arrays could have been heavily simplified by using Java's
        ArrayList class.
         */


        //to initialise the arrays with first set of values
        if(this.xArray.length == 0){
            this.xArray = new int[1];
            this.yArray = new int[1];
            this.labelArray = new int[1];
            this.xArray[0] = x;
            this.yArray[0] = y;
            this.labelArray[0] = label;

            return;
        }

        int size = this.xArray.length;

        //check for duplicate coordinates
        for (int i = 0; i < size; i++) {
            if (this.xArray[i] == x && this.yArray[i] == y) {
                labelArray[i] = label;
                return;
            }
        }

        int[] xArray_temp = new int[size];
        int[] yArray_temp = new int[size];
        int[] labelArray_temp = new int[size];

        //copy values into temp arrays
        for (int i = 0; i < size; i++) {
            xArray_temp[i] = this.xArray[i];
            yArray_temp[i] = this.yArray[i];
            labelArray_temp[i] = this.labelArray[i];
        }

        //resize the old arrays to make way for new value
        this.xArray = new int[size + 1];
        this.yArray = new int[size + 1];
        this.labelArray = new int[size + 1];

        //add new coords to the updated array **except the last one**
        for (int i = 0; i < size; i++) {
            this.xArray[i] = xArray_temp[i];
            this.yArray[i] = yArray_temp[i];
            this.labelArray[i] = labelArray_temp[i];
        }

        //add new value to the end of arrays
        this.xArray[size] = x;
        this.yArray[size] = y;
        this.labelArray[size] = label;

    }

    //adds edges to internal array data structure
    public void addEdge(int x1, int y1, int x2, int y2){

        //do not allow the same coordinates to be given to the method
        if(x1 == x2 && y1 == y2){
            System.out.println("% edge from (" + x1 + "," + y1 + ") was not drawn. " +
                    "Coordinate pairs given to addEdge were the same.");
            return;
        }

        //arrays have to be of same size
        if(this.xArray.length != this.yArray.length && this.xArray.length != this.labelArray.length
                && this.edgeArray1.length != this.edgeArray2.length) {

            System.out.println("% internal array data structures not of same size. Failed to add edge.");
            return;
        }

        int nArraySize = this.xArray.length;

        boolean coord1 = false;
        boolean coord2 = false;

        //check existence of first set of coords
        for(int i=0; i < nArraySize; i++){

            if(this.xArray[i] == x1 && this.yArray[i] == y1) {
                coord1 = true;
                coord1_labelPosition = i;
                break;
            }
        }

        //return if it was not found
        if(!coord1){
            System.out.println("% The edge between (" + x1 + "," + y1 + ") and (" + x2 + "," + y2 + ")" +
                    " could not be added. The node (" + x1 + ", " + y1 + ") is not present.");
            return;
        }

        //check existence of second set of coords
        for(int i=0; i < nArraySize; i++){

            if(this.xArray[i] == x2 && this.yArray[i] == y2) {
                coord2 = true;
                coord2_labelPosition = i;
                break;
            }
        }

        //return if it was not found
        if(!coord2){
            System.out.println("% The edge between (" + x1 + "," + y1 + ") and (" + x2 + "," + y2 + ")" +
                    " could not be added. The node (" + x2 + ", " + y2 + ") is not present.");
            return;
        }

        if(this.edgeArray1.length == 0){

            this.edgeArray1 = new int[1];
            this.edgeArray2 = new int[1];

            this.edgeArray1[0] = this.labelArray[coord1_labelPosition];
            this.edgeArray2[0] = this.labelArray[coord2_labelPosition];

            return;
        }

        int eArraySize = this.edgeArray1.length;

        //resize temp arrays so that they are ready for copying values
        int[] edgeArray1_temp = new int[eArraySize];
        int[] edgeArray2_temp = new int[eArraySize];

        //copy existing edge to temp edge array
        for(int i = 0; i < eArraySize; i++){

            edgeArray1_temp[i] = this.edgeArray1[i];
            edgeArray2_temp[i] = this.edgeArray2[i];
        }

        //resize old arrays ready for new value
        this.edgeArray1 = new int[eArraySize + 1];
        this.edgeArray2 = new int[eArraySize + 1];

        //add all old label pairs back, except last one
        for(int i = 0; i < eArraySize;  i++){

            this.edgeArray1[i] = edgeArray1_temp[i];
            this.edgeArray2[i] = edgeArray2_temp[i];

        }

        //add new label pairs to the end of each array
        this.edgeArray1[eArraySize] = this.labelArray[coord1_labelPosition];
        this.edgeArray2[eArraySize] = this.labelArray[coord2_labelPosition];

    }

    //clears the internal array data structures
    public void clear(){
        int[] xArray = null;
        int[] yArray = null;
        int[] labelArray = null;
    }

    //outputs the grid measurements
    public void outgrid(){

        int minYcoord = arrayMin(yArray);
        int maxYcoord = arrayMax(yArray);

        System.out.println("\\draw [help lines, color=green] (" + minXcoord + "," + minYcoord + ") " +
                "grid (" + maxXcoord + "," + maxYcoord +");");

    }

    public void outgraph() {

        minXcoord = arrayMin(xArray);
        maxXcoord = arrayMax(xArray);
        gridAdjustment();

        System.out.println("\n\\begin{tikzpicture}[scale=" + gridwidth  +"]");
        outgrid();

        //output LaTeX code for node output
        for(int i = 0; i < xArray.length; i++){
            System.out.println("\\draw [thick] (" + xArray[i] + "," + yArray[i] +
                    ") node[draw, rounded rectangle] (" + labelArray[i] + ") {" + labelArray[i] + "};");
        }

        //output LaTeX code for edge output
        for(int i = 0; i < edgeArray1.length; i++){
            System.out.println("\\draw [->, thick] (" + edgeArray1[i] + ") to (" + edgeArray2[i] + ");");
        }

        System.out.println("\\end{tikzpicture}");
        System.out.println();

        System.out.println("\\medskip\\hrule\\medskip");

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

        Graph testGraph = new Graph();

        int testLabel = 1;

        for(int i=0; i < args.length; i+=2){
            testGraph.addNode(Integer.parseInt(args[i]), Integer.parseInt(args[i+1]), testLabel);
            testLabel++;
        }

        for(int i=0; i < args.length; i+=2){
            testGraph.addEdge(Integer.parseInt(args[i]), Integer.parseInt(args[i+1]),
                    Integer.parseInt(args[i+2]), Integer.parseInt(args[i+3]));

            if(i+3 == args.length -1){
                break;
            }
        }

        testGraph.outheader();
        testGraph.outgraph();
        testGraph.outfooter();
    }
}
