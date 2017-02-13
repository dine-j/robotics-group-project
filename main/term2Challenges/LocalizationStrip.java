package main.term2Challenges;

public class LocalizationStrip {
	
	private final boolean[] stripIsBlue;
	private double[] bayesianProbs;

	public LocalizationStrip() {
		stripIsBlue = new boolean[] { true, false, true, false, false, true, true, false, true, true, 
				false, false, true, true, true, false, true, true, true, false, false,
				true, true, true, false, false, true, true, true, false, false, false, false,
				true, true, true, true, false, false, false};
		
		bayesianProbs = new double[stripIsBlue.length];

		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = 1.0 / stripIsBlue.length;
		}

	}

	/*
	 * Update probabilities after:
	 * First, sensing the color at current location
	 * Second, moving in a given direction (forward or backward)
	 * TODO: probabilities for motion are needed because if beginning of strip, might not be able to move backwards
	 */
	public void updateProbs(boolean movedFoward, boolean readBlue, double sensorProba) {
		// sensing update
		double normalization = 0.0;
		for(int i = 0; i < bayesianProbs.length; ++i) {
			if(stripIsBlue[i] == readBlue) { // sensor is right
				bayesianProbs[i] *= sensorProba;
			} else { // sensor is wrong
				bayesianProbs[i] *= 1 - sensorProba;
			}
			normalization += bayesianProbs[i];
		}

		// normalization
		for(int i = 0; i < bayesianProbs.length; ++i) {
			bayesianProbs[i] /= normalization;
		}

		// move update
		if(movedFoward) {
			double lastProba = bayesianProbs[bayesianProbs.length - 1];
			double previous = bayesianProbs[0];
			for (int i = 1; i < bayesianProbs.length; ++i) {
				double swap = bayesianProbs[i];
				bayesianProbs[i] = previous;
				previous = swap;
			}
			bayesianProbs[0] = lastProba;
		} else {
			double lastProba = bayesianProbs[0];
			double previous = bayesianProbs[bayesianProbs.length - 1];
			for (int i = bayesianProbs.length - 2; i > 0; --i) {
				double swap = bayesianProbs[i];
				bayesianProbs[i] = previous;
				previous = swap;
			}
			bayesianProbs[bayesianProbs.length - 1] = lastProba;
		}
	}

	/*
	 * Return index of highest probability
	 */
	public int getLocation() {
		double max = bayesianProbs[0];
		int index = 0;

		for(int i = 1; i < bayesianProbs.length; ++i) {
			if(bayesianProbs[i] > max) {
				max = bayesianProbs[i];
				index = i;
			}
		}

		return index;
	}

	/*
	 * For testing
	 */
    public void printBayesianResults() {
        for(int index = 0; index < stripIsBlue.length; ++index) {
			if(stripIsBlue[index])
            	System.out.println(index + " (B) " + Math.floor(bayesianProbs[index] * 100) / 100 + " ");
			else
				System.out.println(index + " (W) " + Math.floor(bayesianProbs[index] * 100) / 100 + " ");
        }
        System.out.println("Highest proba: " + Math.floor(bayesianProbs[getLocation()] * 100) / 100 + " at " + getLocation());
    }

    /*
     * Useful for testing
     */
    public void reinitializeProbabilities() {
		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = 1.0 / stripIsBlue.length;
		}
	}
}
