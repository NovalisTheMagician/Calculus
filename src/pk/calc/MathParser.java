package pk.calc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pk.calc.MathExecuter.Operator;

public final class MathParser 
{
	public static final int NUMS_ONLY 	= 1 << 0;
	public static final int OPS_ONLY 	= 1 << 1;
	public static final int BRACS_ONLY 	= 1 << 2;
	
	public static final String OPS = "\\+|\\-|\\*|\\/";
	
	private double[] m_pNums;
	private Operator[] m_pOps;
	
	public MathParser()
	{
		
	}
	
	private final boolean CheckOps(char c)
	{	
		if(c == '+' || c == '-' || c == '*' || c == '/')
			return true;
		return false;
	}
	
	private final boolean CheckNums(char c)
	{
		if(c >= 48 || c <= 57 || c == '.')
			return true;
		return false;
	}
	
	private final boolean Check(char c, int constrain)
	{
		boolean res1 = true, res2 = true;
		if((constrain & NUMS_ONLY) > 0)
			res1 = CheckNums(c);
		if((constrain & OPS_ONLY) > 0)
			res2 = CheckOps(c);
		
		return res1 || res2;
	}
	
	private final Operator GetOp(char op)
	{
		switch(op)
		{
		case '+': return Operator.ADD;
		case '-': return Operator.SUBTRACT;
		case '*': return Operator.MULTIPLY;
		case '/': return Operator.DIVIDE;
		}
		return Operator.UNDEF;
	}
	
	private final int IndexOf(String str, String regex)
	{
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if(m.find())
		   return m.start();
		return -1;
	}
	
	public final double[] GetNums()
	{
		return m_pNums;
	}
	
	public final Operator[] GetOps()
	{
		return m_pOps;
	}
	
	public final boolean Parse(String szMathString)
	{
		szMathString = szMathString.trim();
		szMathString = szMathString.replaceAll("\\s+", "");
		
		szMathString = szMathString.replaceAll("e", String.valueOf(Math.E));
		szMathString = szMathString.replaceAll("\u03C0", String.valueOf(Math.PI));
		
		int cursor = 0, count = 0;
		int checkFlag = NUMS_ONLY | OPS_ONLY;
		while(cursor != szMathString.length())
		{
			char c = szMathString.charAt(cursor);
			
			if(!Check(c, checkFlag))
				return false;
			
			if(CheckOps(c))
				count++;
			
			cursor++;
		}
		
		if(count <= 0)
			return false;
		
		m_pOps = new Operator[count];
		m_pNums = new double[count + 1];
		
		cursor = 0;
		int loc1 = -1;
		
		String sub = new String(szMathString);
		
		String[] nums = sub.split(OPS);
		
		if(nums.length != count + 1)
			return false;
		try
		{
			for(int i = 0; i < m_pNums.length; ++i)
				m_pNums[i] = Float.parseFloat(nums[i]);
		}
		catch(java.lang.NumberFormatException ex)
		{
			return false;
		}
		
		while(cursor < count)
		{
			loc1 = IndexOf(sub, OPS);
			m_pOps[cursor] = GetOp(sub.charAt(loc1));
			sub = sub.substring(loc1 + 1);
			cursor++;
		}
		
		return true;
	}
}
