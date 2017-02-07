public final class BayesianDebug {
    public static void printBayesianResults() {
        System.out.print("|");
        for(int index = 0; index < stripIsBlue.length; ++index) {
            System.out.print("   " + stripIsBlue[index] + "   ");
            System.out.print("|");
        }
        System.out.println();
        System.out.print("|");
        for(int index : bayesianProbs) {
            System.out.print(" " + bayesianProbs[index] + " ");
            System.out.print("|");
        }
    }
}
