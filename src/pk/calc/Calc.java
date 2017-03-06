package pk.calc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Calc implements ActionListener
{
	public static final int ADD_BUTTON = 0;
	public static final int SUB_BUTTON = 1;
	public static final int MUL_BUTTON = 2;
	public static final int DIV_BUTTON = 3;
	
	public static final String STORY = "Nothin' else matters";
	private JFrame m_pFrame;
	private JTextField m_pDisplay;
	private JButton m_pCalc;
	private JButton[] m_pKeyButtons;
	private JButton[] m_pOperatorButtons;
	private JButton m_pClear, m_pClearE;
	private JButton m_pPI, m_pE;
	
	public Calc()
	{
		final Font pFont = new Font("Arial", Font.PLAIN, 20);
		
		m_pFrame = new JFrame("MyCulator");
		m_pFrame.setPreferredSize(new Dimension(348, 348));
		m_pFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		m_pDisplay = new JTextField();
		//m_pDisplay.setToolTipText(STORY);
		m_pDisplay.setFont(pFont);
		
		m_pCalc = new JButton("=");
		m_pCalc.addActionListener(this);
		m_pCalc.setFont(pFont);
		
		m_pClear = new JButton("C");
		m_pClear.addActionListener(this);
		m_pClear.setFont(pFont);
		
		m_pClearE = new JButton("CE");
		m_pClearE.addActionListener(this);
		m_pClearE.setFont(pFont);
		
		m_pPI = new JButton("\u03C0");
		m_pPI.addActionListener(this);
		m_pPI.setFont(pFont);
		
		m_pE = new JButton("e");
		m_pE.addActionListener(this);
		m_pE.setFont(pFont);
		
		m_pKeyButtons = new JButton[4 * 3 - 1];
		for(int i = 0; i < 4 * 3 - 1; ++i)
		{
			m_pKeyButtons[i] = new JButton(String.valueOf(9 - i));
			m_pKeyButtons[i].addActionListener(this);
			m_pKeyButtons[i].setFont(pFont);
		}
		m_pKeyButtons[10].setText(".");
		SwapButtons(m_pKeyButtons, 0, 2);
		SwapButtons(m_pKeyButtons, 3, 5);
		SwapButtons(m_pKeyButtons, 6, 8);
		
		m_pOperatorButtons = new JButton[4];
		m_pOperatorButtons[ADD_BUTTON] = new JButton("+");
		m_pOperatorButtons[SUB_BUTTON] = new JButton("-");
		m_pOperatorButtons[MUL_BUTTON] = new JButton("*");
		m_pOperatorButtons[DIV_BUTTON] = new JButton("/");
		for(JButton b : m_pOperatorButtons)
		{
			b.addActionListener(this);
			b.setFont(pFont);
		}
		
		JPanel pButtonMatrix = new JPanel();
		pButtonMatrix.setLayout(new GridLayout(4, 3));
		
		JPanel pOperatorMatrix = new JPanel();
		pOperatorMatrix.setLayout(new GridLayout(4, 1));
		
		JPanel pClearMatrix = new JPanel();
		pClearMatrix.setLayout(new GridLayout(5, 1));
		pClearMatrix.add(m_pClear);
		pClearMatrix.add(m_pClearE);
		pClearMatrix.add(m_pPI);
		pClearMatrix.add(m_pE);
		pClearMatrix.add(new JLabel((char)169 + " PK") { private static final long serialVersionUID = 1L; { setFont(pFont); setForeground(new Color(0x4F9B98)); }});
		
		for(JButton b : m_pKeyButtons)
			pButtonMatrix.add(b);
		pButtonMatrix.add(m_pCalc);
		
		for(JButton b : m_pOperatorButtons)
			pOperatorMatrix.add(b);
		
		JPanel pDisplay = new JPanel();
		pDisplay.setLayout(new BorderLayout());
		pDisplay.add(m_pDisplay, BorderLayout.CENTER);
		
		Container cp = m_pFrame.getContentPane();
		cp.add(pDisplay, BorderLayout.NORTH);
		cp.add(pButtonMatrix, BorderLayout.CENTER);
		cp.add(pOperatorMatrix, BorderLayout.EAST);
		cp.add(pClearMatrix, BorderLayout.WEST);
		
		pButtonMatrix.getRootPane().setDefaultButton(m_pCalc);
		
		m_pFrame.pack();
		m_pFrame.setLocationByPlatform(true);
		m_pFrame.setVisible(true);
	}

	private void SwapButtons(JButton[] buttons, int i1, int i2)
	{
		JButton tmp = buttons[i1];
		buttons[i1] = buttons[i2];
		buttons[i2] = tmp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == m_pCalc)
		{
			if(m_pDisplay.getText().isEmpty())
				return;
			
			MathParser mp = new MathParser();
			if(mp.Parse(m_pDisplay.getText()))
			{
				MathExecuter me = new MathExecuter();
				
				double res = me.Execute(mp.GetNums(), mp.GetOps());
				
				m_pDisplay.setText(String.valueOf(res));
			}
			else
				m_pDisplay.setText("ERROR");
		}
		else if(e.getSource() == m_pClearE)
			m_pDisplay.setText("");
		else if(e.getSource() == m_pClear)
		{
			String displayText = m_pDisplay.getText();
			if(!displayText.isEmpty())
			{
				String newString = displayText.substring(0, displayText.length() - 1);
				m_pDisplay.setText(newString);
			}
		}
		else
			m_pDisplay.setText(m_pDisplay.getText() + ((JButton)e.getSource()).getText());
	}
	
	public static void main(String... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Calc();
	}
}
