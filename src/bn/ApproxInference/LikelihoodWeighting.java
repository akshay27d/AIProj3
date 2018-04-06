package bn.ApproxInference;
import java.util.*;
import bn.core.*;
import java.io.*;
import bn.parser.*;

public class LikelihoodWeighting {

	public static void main(String[] args) {
		int numSamples = Integer.parseInt(args[0]);			//Take in command line args
		String filename = args[1];
		String queryVariable = args[2];
		Assignment evidenceVariables= new Assignment();
		BayesianNetwork network = null;

		if (filename.endsWith(".xml")){					//parse file
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

			LikelihoodWeighting rs = new LikelihoodWeighting();		//Do work
		double[] output = rs.likelihood_weighting(network.getVariableByName(queryVariable), evidenceVariables, network, numSamples);

		ArrayList<Object> domain = network.getVariableByName(queryVariable).getDomain();


		for(int i = 0; i < output.length; i++) {				//Print out result
			System.out.println(domain.get(i) + " = " + output[i]);
		}

	}//end main method


	public double[] likelihood_weighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {

		double[] W = new double[X.getDomain().size()];
		ArrayList<Object> list = X.getDomain();

		for(int j = 1; j <= N; j++) {
			EventWeight ew = weighted_sample(bn, e, X);
			HashMap<RandomVariable, Object> x = ew.event;	//value of X in event
			Object d = x.get(X);
			W[list.indexOf(d)] = W[list.indexOf(d)] + ew.weight;//W.put(X, W.get(X) + ew.weight);
		}

		return normalize(W);

	}//end likelihood_weighting method


	public EventWeight weighted_sample(BayesianNetwork bn, Assignment e, RandomVariable X){
		double w = 1;
		HashMap<RandomVariable,Object> x = new HashMap<RandomVariable,Object>();
		Assignment check = e.copy();

		for (RandomVariable rv : e.variableSet()){			//initialize elements	
			x.put(rv, e.receive(rv));
		}

		int i =0;
		for (RandomVariable rv : bn.getVariableListTopologicallySorted()){	//go through variables

			for(Object dom : rv.getDomain()){
				if (e.receive(rv) != null && e.receive(rv).equals(dom)){	//if Xi has value xi

					e.set(rv, dom);
					double tempW = bn.getProb(rv,e);

					w = w*tempW;			//w = w+ P(Xi=xi | parents(Xi))
				}
					
				else{

					ArrayList<Double> probabilities = new ArrayList<Double>();
					for (Object d : X.getDomain()){

						e.set(rv,d);
						double prob = bn.getProb(rv, e);	//P(Xi| parents(Xi))
						probabilities.add(prob);

					}

					//Assign value
					int idx = assign_value(probabilities);
					x.put(rv, rv.getDomain().get(idx));
				}
			}

			i++;
		}

		return new EventWeight(x, w);		//Returns an event and weight

	}//end weighter_sample 



	public int assign_value(ArrayList<Double> probs){		
		Random r = new Random();	
		double value = (double)r.nextInt(11)/10.0;		////pick number 0.0 to 1.0

		double y=0;
		for (int i =0; i < probs.size(); i++){			//Have boundaries according to probabilities to assign a value
			if (value >=y && value <= y+probs.get(i)){
				return i;
			}
			y = y+probs.get(i);
		}

		return 0;
	}



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