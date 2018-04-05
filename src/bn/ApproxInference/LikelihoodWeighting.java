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


	public EventWeight weighted_sample(BayesianNetwork bn, Assignment e){
		double w = 1;
		boolean[] x = new boolean[e.getVariableList().size()];

		int i =0;
		for (RandomVariable rv : bn.getVariableListTopologicallySorted()){
			if (e.containsKey(rv)){
				w = w * /* P(rv=x | parents(rv))*/
			}
			else{
				x[i] = random_sample(rv, /* parents(rv*/);
			}
			i++;
		}

		return new EventWeight(x, w);
	}

	public boolean random_sample(RandomVariable rv, BayesianNetwork bn, Assignment e) {

		Random r = new Random();
		double value = ((double)r.nextInt(11))/10.0;

		e.set(rv, true);
		double prob=bn.getProb(rv, e/*get_parents(rv,bn)*/);


		if(value <= prob) {
			return true;
		}
		else{
			return false;
		}


	}//end random_sample method







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