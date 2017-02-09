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

		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = 1 / stripIsBlue.length;
		}

	}

	/*
	 * Only considers the fact that the robot is moving forward
	 * TODO: implement also for moving backwards
	 */
	public void updateProbs(boolean movedFoward, boolean readBlue, double sensorProba) {
		// sensing update
		float normalization = 0;
		for(int i = 0; i < bayesianProbs.length; ++i) {
			if((stripIsBlue[i] && readBlue) || (!stripIsBlue[i] && !readBlue)) { // sensor is right
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
		float lastProba = bayesianProbs[bayesianProbs.length - 1];
		for(int i = 1; i < bayesianProbs.length; ++i) {
			bayesianProbs[i] = bayesianProbs[i - 1];
		}
		bayesianProbs[0] = lastProba;
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
