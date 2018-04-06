package bn.ApproxInference;
import java.util.*;
import bn.core.*;
import java.io.*;
import bn.parser.*;

public class RejectionSampling {


	public static void main(String[] args) {
		
		int numSamples = Integer.parseInt(args[0]);		//Take in command line args
		String filename = args[1];
		String queryVariable = args[2];
		Assignment evidenceVariables= new Assignment();
		BayesianNetwork network = null;


		if (filename.endsWith(".xml")){						//parse file
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

		RejectionSampling rs = new RejectionSampling();			//Do work
		double[] output = rs.rejection_sampling(network.getVariableByName(queryVariable), evidenceVariables, network, numSamples);

		ArrayList<Object> domain = network.getVariableByName(queryVariable).getDomain();


		for(int i = 0; i < output.length; i++) {		//Print out result
			System.out.println(domain.get(i) + " = " + output[i]);
		}


	}//end main method



	public double[] rejection_sampling(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] counts = new double[X.getDomain().size()]; //bolded N
		ArrayList<Object> domain = X.getDomain();
		List<RandomVariable> list = bn.getVariableListTopologicallySorted();
		Assignment initialGiven = e.copy();

		for(int i = 1; i <= N; i++) {

			HashMap<RandomVariable, Object> x = prior_sample(bn, e);	//get sample

			if(is_consistent(x, initialGiven)) {		//Check if consistant
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

		for(RandomVariable X : list) {			//go through variables
			ArrayList<Double> probabilities = new ArrayList<Double>();
			for (Object d : X.getDomain()){

				e.set(X,d);					//Set to val of domain
				double prob = bn.getProb(X, e);	//Get Probability
				probabilities.add(prob);

			}

			//Assign value
			int idx = assign_value(probabilities);
			x.put(X, X.getDomain().get(idx));

		}
		return x;

	}//end prior_sample method

	public int assign_value(ArrayList<Double> probs){
		Random r = new Random();
		double value = (double)r.nextInt(11)/10.0;	//pick number 0.0 to 1.0

		double y=0;
		for (int i =0; i < probs.size(); i++){			//Have boundaries according to probabilities to assign a value
			if (value >=y && value <= y+probs.get(i)){
				return i;
			}
			y = y+probs.get(i);
		}

		return 0;
	}


	public boolean is_consistent(HashMap<RandomVariable, Object> x, Assignment e) {

		for(RandomVariable rv : e.variableSet()) {		
			if(!x.get(rv).equals(e.receive(rv)))		//Checks if x clashes with e
				return false;

		}
		// System.exit(0);
		return true;

	}//end is_consistent method


	public double[] normalize(double[] counts) {

		//want to make all the variables add up to 1

		//add up all things, and then divide by sum 

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