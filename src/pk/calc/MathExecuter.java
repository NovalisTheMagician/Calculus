package pk.calc;

public final class MathExecuter 
{
	
	public static enum Operator
	{
		ADD,
		SUBTRACT,
		MULTIPLY,
		DIVIDE,
		UNDEF
	}
	
	public MathExecuter()
	{
		
	}
	
	private double Calculate(Operator op, double numLHS, double numRHS)
	{
		System.out.print(numLHS + " " + op + " " + numRHS);
		
		switch(op)
		{
		case ADD: 		return numLHS + numRHS;
		case SUBTRACT: 	return numLHS - numRHS;
		case MULTIPLY: 	return numLHS * numRHS;
		case DIVIDE: 	return numLHS / numRHS;
		case UNDEF:		return 0.0f;
		}
		return 0.0f;
	}
	
	private int GetPrecedence(Operator[] operators)
	{
		int result = -1;
		
		for(int i = 0; i < operators.length; ++i)
		{
			if(operators[i] == Operator.MULTIPLY || operators[i] == Operator.DIVIDE)
				result = i;
		}
		
		if(result == -1)
		{
			for(int i = 0; i < operators.length; ++i)
			{
				if(operators[i] == Operator.ADD || operators[i] == Operator.SUBTRACT)
					result = i;
			}
		}
		
		return result;
	}
	
	public double Execute(double[] numbers, Operator[] operators)
	{	
		int index = GetPrecedence(operators);
		double res = Calculate(operators[index], numbers[index], numbers[index + 1]);
		System.out.println(" = " + res);
		
		if(operators.length != 1)
		{
			double[] newNums = new double[numbers.length - 1];
			Operator[] newOps = new Operator[operators.length - 1];
			
			for(int i = 0, j = 0; i < operators.length; ++i)
			{
				if(i == index)
					continue;
				newOps[j] = operators[i];
				j++;
			}
			
			for(int i = 0, j = 0; i < numbers.length; ++i)
			{
				if(i == index)
					newNums[i] = res;
				else if(i == index + 1)
					continue;
				else
					newNums[j] = numbers[i];
				j++;
			}
			
			return Execute(newNums, newOps);
		}
		
		return res;
	}
}
