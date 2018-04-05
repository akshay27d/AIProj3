package bn.ApproxInference;
import java.util.*;
import bn.core.*;
import java.io.*;
import bn.parser.*;

public class LikelihoodWeighting {

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

		LikelihoodWeighting rs = new LikelihoodWeighting();
		double[] output = rs.likelihood_weighting(network.getVariableByName(queryVariable), evidenceVariables, network, numSamples);

		ArrayList<Object> domain = network.getVariableByName(queryVariable).getDomain();


		for(int i = 0; i < output.length; i++) {
			System.out.println(domain.get(i) + " = P(" + output[i]+")");
		}

	}//end main method


	public double[] likelihood_weighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] W = new double[X.getDomain().size()];

		for(int j = 1; j <= N; j++) {
			EventWeight ew = weighted_sample(bn, e, X);
			int x = ;//value of X in event
			W[x] = W[x] + ew.weight;
		}

		return normalize(W);

	}//end likelihood_weighting method


	public EventWeight weighted_sample(BayesianNetwork bn, Assignment e, RandomVariable X){
		double w = 1;
		Object[] x = new Object[e.variableSet().size()];
		
		int i = 0;
		for(RandomVariable rv : e.variableSet()) {
			x[i] = e.receive(X);
		}

		i =0;
		for (RandomVariable rv : bn.getVariableListTopologicallySorted()){
			if (e.containsKey(rv)){
				w = w;/* P(rv=x | parents(rv))*/
			}
			else{
				x[i] = random_sample(rv, bn, e);
			}
			i++;
		}

		return new EventWeight(x, w);

	}//end weighter_sample 

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