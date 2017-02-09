package main.term2Challenges;

public class LocalizationStrip {
	
	private final boolean[] stripIsBlue;
	private float[] bayesianProbs;

	public LocalizationStrip() {
		stripIsBlue = new boolean[] { true, false, true, false, false, true, true, false, true, true, 
				false, false, true, true, true, false, true, true, true, false, false,
				true, true, true, false, false, true, true, true, false, false, false, false,
				true, true, true, true, false, false, false};
		
		bayesianProbs = new float[stripIsBlue.length];
		
		
		float initialProb = 1 / stripIsBlue.length;
		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = initialProb;
		}

	}
	
	
	public void updateProbs(boolean movedFoward, boolean readBlue) { 
		
		
		
	}

	/*
	 * Return highest probability
	 */
	public int getLocation() {
		float max = bayesianProbs[0];
		int index = 0;

		for(int i = 1; i < bayesianProbs.length; ++i) {
			if(bayesianProbs[i] > max) {
				max = bayesianProbs[i];
				index = i;
			}
		}

		return index;
	}

    public void printBayesianResults() {
        System.out.print("|");
        for(int index = 0; index < stripIsBlue.length; ++index) {
            System.out.print("   " + stripIsBlue[index] + "   ");
            System.out.print("|");
        }
        System.out.println();
        System.out.print("|");
        for(int index = 0; index < stripIsBlue.length; ++index) {
            System.out.print(" " + bayesianProbs[index] + " ");
            System.out.print("|");
        }
    }

    public void reinitalizeProbabilities() {
		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = 1 / stripIsBlue.length;
		}
	}
}
