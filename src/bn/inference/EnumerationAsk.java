package bn.inference;
import java.util.*;
import java.io.*;
import bn.core.*;
import bn.parser.*;


public class EnumerationAsk implements Inferencer{

	public static void main(String[] args) {
		String filename = args[0];
		String queryVariable = args[1];
		HashMap<String, Boolean> evidenceVariables= new HashMap<String, Boolean>();
		BayesianNetwork network;

		for (int i = 2; i< args.length; i+=2){
			evidenceVariables.put(args[i], Boolean.valueOf(args[i+1]));
		}


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
		// EnumerationAsk e = new EnumerationAsk();
		// Distribution x = e.enumeration_ask(new RandomVariable("hi"), new Assignment(), new BayesianNetwork());
	}//end main



	public Distribution enumeration_ask(RandomVariable X, Assignment e, BayesianNetwork bn){

		Distribution Q = new Distribution(X);
		// for(Object Xi : X.getDomain()) {
		// 	e.set(X, Xi);
		// 	Q.put(Xi, enumerate_all(bn.getVariableList(), e));

		// }

		// Q.normalize();
		return Q;

	}//end of enumeration_ask method

	// public double enumerate_all(List<RandomVariable> vars, Assignment e) {
	// 	if(vars.isEmpty()) {
	// 		return 1.0;
	// 	}
	// 	RandomVariable Y = vars.get(0);
	// 	//need what y is wtf
	// 	if(Y has value y in e) {
	// 		return 
	// 	}


	// }//end enumerate_all method



	public ArrayList<RandomVariable> rest(List<RandomVariable> vars) {
		ArrayList<RandomVariable> copy = new ArrayList<RandomVariable>();

		for(RandomVariable r : vars) {
			copy.add(r);
		}

		copy.remove(0);

		return copy;


	}//end rest method



}//end of class