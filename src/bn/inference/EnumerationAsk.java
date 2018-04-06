package bn.inference;
import java.util.*;
import java.io.*;
import bn.core.*;
import bn.parser.*;


public class EnumerationAsk implements Inferencer{

	public static void main(String[] args) {
		String filename = args[0];					//Take in command line args
		String queryVariable = args[1];
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

		for (int i = 2; i< args.length; i+=2){
			evidenceVariables.set(network.getVariableByName(args[i]), args[i+1]);
		}


		EnumerationAsk e = new EnumerationAsk();
		//Do work
		Distribution x = e.enumeration_ask(network.getVariableByName(queryVariable), evidenceVariables, network);
		x.print();			//Print out result

	}//end main



	public Distribution enumeration_ask(RandomVariable X, Assignment e, BayesianNetwork bn){

		Distribution Q = new Distribution(X);
		for(Object Xi : X.getDomain()) {		//Go through domain
			e.set(X, Xi);
			Q.put(Xi, enumerate_all(bn.getVariableListTopologicallySorted(), e, bn));	//enumerate all

		}

		Q.normalize();
		return Q;

	}//end of enumeration_ask method

	public double enumerate_all(List<RandomVariable> vars, Assignment e, BayesianNetwork bn) {
		if(vars.isEmpty()) {		//if empty
			return 1.0;	
		}
		RandomVariable Y = vars.get(0);		//get 1st variable

		if(e.containsKey(Y)){ //is th
			Assignment pAssign = e.copy();

			return bn.getProb(Y,pAssign) * enumerate_all(rest(vars),e, bn);	
		}
		else{
			double summation=0;

			for(Object Yi : Y.getDomain()) {
				Assignment pAssign = e.copy();
				pAssign.set(Y, Yi);
				summation += bn.getProb(Y,pAssign) * enumerate_all(rest(vars),pAssign ,bn);

			}

			return summation;
		}
		
		
	} //end enumerate_all method


	public ArrayList<RandomVariable> rest(List<RandomVariable> vars) {		//return tail of list (index 1 through end)
		ArrayList<RandomVariable> copy = new ArrayList<RandomVariable>();

		for(RandomVariable r : vars) {
			copy.add(r);
		}

		copy.remove(0);

		return copy;


	}//end rest method



}//end of class