import java.util.*;

public class RejectionSampling {


	public static void main(String[] args) {

		//parsing stuff - needs to also take in number of samples to be run as first variable

		RejectionSampling rs = new RejectionSampling();

	}//end main method



	public double[] rejection_sampling(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] counts = new double[X.getDomain().size()]; //bolded N
		ArrayList<RandomVariable> list = bn.getVariableListTopologicallySorted();

		for(int i = 1; i <= N; i++) {

			HashMap<RandomVariable, Boolean> x = prior_sample(bn);

			if(is_consistent(x, e)) {
				int y = list.indexOf(X);
				counts[y] = counts[y] + 1;

			}//end if

		}//end for 


		return normalize(counts);

	}//end rejection_sampling method



	public HashMap<RandomVariable, Boolean> prior_sample(BayesianNetwork bn) {

		//boolean[] x = new boolean[bn.getVariableList().size()];
		HashMap<RandomVariable, Boolean> x = new HashMap<RandomVariable, Boolean>();

		ArrayList<RandomVariable> list = bn.getVariableListTopologicallySorted();

		for(int i = 0; i < list.size(); i++) {

			x.put(list.get(i), random_sample(list.get(i), bn));

		}

		return x;

	}//end prior_sample method

	public boolean random_sample(RandomVariable rv, BayesianNetwork bn) {

		Random r = new Random();
		double value = r.nextInt(11)/10;

		double prob = bn.getProb(rv, get_parents(rv, bn));

		if(value <= prob) {
			return true;
		}
		else{
			return false;
		}


	}//end random_sample method

	public Assignment get_parents(RandomVariable rv, BayesianNetwork bn) {

		BayesianNetwork.Node node = bn.getNodeForVariable(rv);
		ArrayList<BayesianNetwork.Node> parents = node.parents;
		Assignment ass = new Assignment();

		for(BayesianNetwork.Node n : parents) {
			ass.set(n.variable, true);
		}

		return ass;

	}//end get_parents method


	public boolean is_consistent(HashMap<RandomVariable, Boolean> x, Assignment e) {

		for(RandomVariable rv : x.keySet()) {
			
			if(e.receive(rv) != x.get(rv))
				return false;

		}

		return true;

	}//end is_consistent method


	public double[] normalize(double[] counts) {




	}//end normalize method




}//end of class