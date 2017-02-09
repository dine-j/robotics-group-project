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
	 * Only considers the fact that the robot is moving forward
	 * TODO: implement also for moving backwards
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
		double lastProba = bayesianProbs[bayesianProbs.length - 1];
		double previous = bayesianProbs[0];
		for(int i = 1; i < bayesianProbs.length; ++i) {
			double swap = bayesianProbs[i];
			bayesianProbs[i] = previous;
			previous = swap;
		}
		bayesianProbs[0] = lastProba;
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

    public void printBayesianResults() {
        for(int index = 0; index < stripIsBlue.length; ++index) {
			if(stripIsBlue[index])
            	System.out.println(index + " (B) " + Math.floor(bayesianProbs[index] * 100) / 100 + " ");
			else
				System.out.println(index + " (W) " + Math.floor(bayesianProbs[index] * 100) / 100 + " ");
        }
        System.out.println("Highest proba: " + Math.floor(bayesianProbs[getLocation()] * 100) / 100 + " at " + getLocation());
    }

    public void reinitializeProbabilities() {
		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = 1.0 / stripIsBlue.length;
		}
	}
}
