package bn.ApproxInference;
import java.util.*;
import bn.core.*;
import java.io.*;
import bn.parser.*;

public class LikelihoodWeighting {

	public static void main(String[] args) {

	}//end main method


	public double likelihood_weighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] W = new double[X.getDomain().size()];

		for(int j = 1; j <= N; j++) {
			//event, weight = weighted_sample(bn, e);
			//W[italics x] = w[italics x] + weight where italics x is value of X in event
		}

		return normalize(W);

	}//end likelihood_weighting method


	public header







	public double[] normalize(double[] counts) {

		//want to make all the variables add up to 1

		//add up all things, and then divide 1 by sum and then multiply by that number

		double sum = 0;

		for(int i = 0; i < counts.length; i++){
			sum += counts[i];
		}

		for(int i = 0; i < counts.length; i++){
			counts[i] = counts[i]/sum;
		}

		return counts;

	}//end normalize method


}//end of class