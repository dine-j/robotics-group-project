package main.term2Challenges;


public class LocalizationStrip {

	public LocalizationStrip() {
		final boolean[] stripIsBlue = new boolean[] { true, false, true, false, false, true, true, false, true, true, 
				false, false, true, true, true, false, true, true, true, false, false,
				true, true, true, false, false, true, true, true, false, false, false, false,
				true, true, true, true, false, false, false};
		
		float[] bayesianProbs = new float[stripIsBlue.length];
		
		
		float initialProb = 1 / bayesianProbs.length;
		for( int i = 0; i < bayesianProbs.length; ++i){
			bayesianProbs[i] = initialProb;
		}
	
	}
	
	
	
}
