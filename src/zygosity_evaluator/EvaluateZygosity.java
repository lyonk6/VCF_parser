package zygosity_evaluator;

public class EvaluateZygosity 
{
	public static void main(String[]args)
	{
		ZygosityEvaluator myEvaluator 
			= new ZygosityEvaluator(args[0].trim(), args[1].trim(), args[2].trim());
	}
}
