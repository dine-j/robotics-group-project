package main.term2Challenges;

public class LocalizationStrip {
    
    private final boolean[] stripIsBlue;
    private double[] bayesianProbs;

    public LocalizationStrip() {
        stripIsBlue = new boolean[] {
                true, false, true, false, false,
                true, true, false, true, true,
                false, false, true, true, true,
                false, true, true, true, false,
                false, true, true, true, false,
                false, false, true, true, true,
                true, false, false };
        
        bayesianProbs = new double[stripIsBlue.length];

        for(int i = 0; i < 7; ++i){
            bayesianProbs[i] = 0; // Because the robot can only start to read the 8th square
        }

        for(int i = 7; i < bayesianProbs.length; ++i){
            bayesianProbs[i] = 1.0 / (stripIsBlue.length - 8);
        }
    }

    /**
     * Update probabilities after:
     * First, sensing the color at current location
     * Second, moving in a given direction (forward or backward)
     */
    public void updateProbabilities(boolean movedFoward, boolean readBlue, double sensorProba, double moveProba) {
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
        normalize(normalization);

        normalization = 0.0;

        // move update
        if(movedFoward) {
            for (int i = bayesianProbs.length - 1; i > 0; --i) {
                bayesianProbs[i] = bayesianProbs[i - 1] * moveProba + bayesianProbs[i] * (1 - moveProba);
                normalization += bayesianProbs[i];
            }
            bayesianProbs[0] = bayesianProbs[bayesianProbs.length - 1] * moveProba + bayesianProbs[0] * (1 - moveProba);
            normalization += bayesianProbs[0];
        } else {
            for (int i = 0; i < bayesianProbs.length - 1; ++i) {
                bayesianProbs[i] = bayesianProbs[i + 1] * moveProba + bayesianProbs[i] * (1 - moveProba);
                normalization += bayesianProbs[i];
            }
            bayesianProbs[bayesianProbs.length - 1] = bayesianProbs[0] * moveProba + bayesianProbs[bayesianProbs.length - 1] * (1 - moveProba);
            normalization += bayesianProbs[bayesianProbs.length - 1];
        }

        // normalization
        normalize(normalization);
    }

    /**
     * Normalize probabilities with normalization coefficient
     * @param normalization Coefficient for normalization
     */
    private void normalize(double normalization) {
        for(int i = 0; i < bayesianProbs.length; ++i) {
            bayesianProbs[i] /= normalization;
        }
    }

    /**
     * Print probabilities for testing
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

    /**
     * Reinitialize probabilities
     * Useful for testing
     */
    public void reinitializeProbabilities() {
        for( int i = 0; i < bayesianProbs.length; ++i){
            bayesianProbs[i] = 1.0 / stripIsBlue.length;
        }
    }

    /**
     * Get position on hardocded strip
     * @return Position with highest probability
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

    /**
     * Get length of strip
     * @return Length of strip
     */
    public int getLength() {
        return bayesianProbs.length;
    }

    /**
     * Get highest probability
     * @return Highest probability
     */
    public double getHighestProbability() {
        return bayesianProbs[getLocation()];
    }
}
