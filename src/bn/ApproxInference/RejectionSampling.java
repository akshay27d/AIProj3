package bn.ApproxInference;
import java.util.*;
import bn.core.*;
import java.io.*;
import bn.parser.*;

public class RejectionSampling {


	public static void main(String[] args) {
		
		int numSamples = Integer.parseInt(args[0]);
		String filename = args[1];
		String queryVariable = args[2];
		Assignment evidenceVariables= new Assignment();
		BayesianNetwork network = null;


		if (filename.endsWith(".xml")){
			XMLBIFParser parser = new XMLBIFParser();
			try{
				network = parser.readNetworkFromFile("bn/examples/"+filename);
			} catch(Exception e){
				System.out.println("Error parsing Bayesian Network 1");
				System.exit(0);
			}
		}
		else{
			BIFParser parser = null;
			try{
				parser = new BIFParser(new FileInputStream("bn/examples/"+filename));
			} catch(Exception e){
				System.out.println("Error parsing Bayesian Network 2");
				System.exit(0);
			}

			try{
				network = parser.parseNetwork();
			} catch(Exception e){
				System.out.println("Error parsing Bayesian Network 3");
				System.exit(0);
			}
		}

		for (int i = 3; i< args.length; i+=2){
			evidenceVariables.set(network.getVariableByName(args[i]), args[i+1]);
		}

		RejectionSampling rs = new RejectionSampling();
		double[] output = rs.rejection_sampling(network.getVariableByName(queryVariable), evidenceVariables, network, numSamples);

		ArrayList<Object> domain = network.getVariableByName(queryVariable).getDomain();


		for(int i = 0; i < output.length; i++) {
			System.out.println(domain.get(i) + " = P(" + output[i]+")");
		}


	}//end main method



	public double[] rejection_sampling(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] counts = new double[X.getDomain().size()]; //bolded N
		ArrayList<Object> domain = X.getDomain();
		List<RandomVariable> list = bn.getVariableListTopologicallySorted();
		Assignment initialGiven = e.copy();

		for(int i = 1; i <= N; i++) {

			HashMap<RandomVariable, Object> x = prior_sample(bn, e);

			if(is_consistent(x, initialGiven)) {
				Object val = x.get(X);
				int y = domain.indexOf(val);
				counts[y] = counts[y] + 1;
			}//end if

		}//end for 


		return normalize(counts);

	}//end rejection_sampling method



	public HashMap<RandomVariable, Object> prior_sample(BayesianNetwork bn, Assignment e) {

		HashMap<RandomVariable, Object> x = new HashMap<RandomVariable, Object>();

		List<RandomVariable> list = bn.getVariableListTopologicallySorted();

		for(RandomVariable X : list) {
			ArrayList<Double> probabilities = new ArrayList<Double>();
			for (Object d : X.getDomain()){

				e.set(X,d);
				double prob = bn.getProb(X, e);
				probabilities.add(prob);

			}

			//Assign value
			int idx = assignValue(probabilities);
			x.put(X, X.getDomain().get(idx));

		}
		// System.exit(0);
		return x;

	}//end prior_sample method

	public int assignValue(ArrayList<Double> probs){
		Random r = new Random();
		double value = (double)r.nextInt(11)/10.0;

		double y=0;
		for (int i =0; i < probs.size(); i++){
			if (value >=y && value <= y+probs.get(i)){
				return i;
			}
			y = y+probs.get(i);
		}

		return 0;
	}

	// public Assignment get_parents(RandomVariable rv, BayesianNetwork bn) {

	// 	BayesianNetwork.Node node = bn.getNodeForVariable(rv);
	// 	List<BayesianNetwork.Node> parents = node.parents;
	// 	Assignment ass = new Assignment();

	// 	for(BayesianNetwork.Node n : parents) {
	// 		ass.set(n.variable, true);
	// 	}

	// 	return ass;

	// }//end get_parents method


	public boolean is_consistent(HashMap<RandomVariable, Object> x, Assignment e) {

		for(RandomVariable rv : e.variableSet()) {	
			// System.out.println(rv + " = " + e.receive(rv));		
			if(!x.get(rv).equals(e.receive(rv)))
				return false;

		}
		// System.exit(0);
		return true;

	}//end is_consistent method


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