package bn.inference;
import java.util.List;

import bn.core.*;

public class EnumerationAsk implements Inferencer{

	public static void main(String[] args) {

		EnumerationAsk e = new EnumerationAsk();
		Distribution x = e.enumeration_ask(new RandomVariable("hi"), new Assignment(), new BayesianNetwork());
	}//end main



	public Distribution enumeration_ask(RandomVariable X, Assignment e, BayesianNetwork bn){

		Distribution Q = new Distribution(X);
		for(Object Xi : X.getDomain()) {
			e.set(X, Xi);
			Q.put(Xi, enumerate_all(bn.getVariableList(), e));

		}

		Q.normalize();
		return Q;

	}//end of enumeration_ask method

	public double enumerate_all(List<RandomVariable> vars, Assignment e) {
		if(vars.isEmpty()) {
			return 1.0;
		}
		RandomVariable Y = vars.get(0);
		//need what y is wtf
		if(Y has value y in e) {
			return 
		}


	}//end enumerate_all method



	public List<RandomVariable> rest(List<RandomVariable> vars) {
		List<RandomVariable> copy = new List<RandomVariable>();

		for(RandomVariable r : vars) {
			copy.add(r);
		}

		copy.remove(0);

		return copy;


	}//end rest method



}//end of class