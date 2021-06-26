import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

public class PlannerFrame extends JFrame {
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 300;
	private boolean onTop = false;
	private JMenuItem topItem;
	private JFrame alarmFrame;
	
	public PlannerFrame(String title) {
		setTitle(title);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		/* Set Icon Image */
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("k.png");
		setIconImage(img);
		
		PlannerPanel panel = new PlannerPanel();
		add(panel);
		
		JMenu menuMenu = new JMenu("메뉴");
		//menuMenu.add(new AlarmAction("알람"));
		//menuMenu.add(new InfoAction("정보"));
		/* 밑에처럼도 구현 가능하다 */
		JMenuItem infoItem = menuMenu.add("정보");
		infoItem.addActionListener(new InfoAction());
		topItem = new JMenuItem(new TopAction("고정"));
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(menuMenu);
		menuBar.add(topItem);
		
	}
	
	private class AlarmAction extends AbstractAction {
		public AlarmAction(String name) {
			super(name);
		}
		
		public void actionPerformed(ActionEvent event) {
			//JOptionPane.showMessageDialog(null, getValue(Action.NAME) + "은 개발중입니다." );
			alarmFrame.setVisible(true);
		}
	}
	
	private class InfoAction extends AbstractAction {
		/*
		public InfoAction(String name) {
			super(name);
		}
		*/
		public void actionPerformed(ActionEvent event) {
			JOptionPane.showMessageDialog(null, "버전 : 3\n개발자 : 김용진");
		}
	}
	
	private class TopAction extends AbstractAction {
		public TopAction(String name) {
			super(name);
		}
		public void actionPerformed(ActionEvent event) {
			if(onTop)
			{
				System.out.println("해제");
				setAlwaysOnTop(false);
				onTop = false;
				topItem.setForeground(Color.BLACK);
			}
			else
			{
				System.out.println("작동");
				setAlwaysOnTop(true);
				onTop = true;
				topItem.setForeground(Color.RED);
			}
		}
	}
}

class PlannerPanel extends JPanel {
	private JButton date, left, right;
	private JPanel datePanel;
	private JPanel panel;
	private Calendar time = Calendar.getInstance();
	private int year, month, day;
	
	public PlannerPanel() {
		year = time.get(Calendar.YEAR);
		month = time.get(Calendar.MONTH)+1;
		day = time.get(Calendar.DAY_OF_MONTH);
		
		setLayout(new BorderLayout());
		
		drawDate();
		
		drawCal();
	
		
	}
	
	private void addButton(String label) {
		JButton button = new JButton(label);
		button.setEnabled(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		panel.add(button);
	}
	
	private void addButton(String label, ActionListener listener) {
		JButton button = new JButton(label);
		button.addActionListener(listener);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		panel.add(button);
		
		if(Integer.valueOf(label) == day && time.get(Calendar.MONTH)+1 == month &&
				time.get(Calendar.YEAR) == year)
		{
			button.setContentAreaFilled(true);
			button.setBackground(Color.BLUE);
			button.setForeground(Color.WHITE);
		}
	}
	
	private boolean isLeapYear(int y) {
		if( (y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0) )
		{
			return true;
		}
		return false;
	}
	
	private void drawDate() {
		datePanel = new JPanel();
		add(datePanel, BorderLayout.NORTH);
		datePanel.setLayout(new GridBagLayout());
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		
		left = new JButton("<");
		left.setContentAreaFilled(false);
		left.setFocusPainted(false);
		left.setBorderPainted(false);
		left.addActionListener( (ActionEvent event) -> {
			if(month == 1)
			{
				year--;
				month = 12;
			}
			else
			{
				month--;
			}
			updateDate();
		});
		gbc[0] = new GridBagConstraints();
		gbc[0].fill = GridBagConstraints.BOTH;
		gbc[0].gridx = 0; gbc[0].gridy = 0;
		datePanel.add(left, gbc[0]);
		
		date = new JButton(String.valueOf(year) + ". "
				+ String.valueOf(month) + ".");
		date.setEnabled(false);
		date.setBorderPainted(false);
		date.setFont(new Font("Serif", Font.PLAIN, 20));
		gbc[1] = new GridBagConstraints();
		gbc[1].gridx = 1; gbc[1].gridy = 0;
		gbc[1].gridwidth = 1; gbc[1].gridheight = 1;
		datePanel.add(date, gbc[1]);
	
		
		right = new JButton(">");
		right.setFocusPainted(false);
		right.setContentAreaFilled(false);
		right.setBorderPainted(false);
		right.addActionListener( (ActionEvent event) -> {
			if(month == 12)
			{
				year++;
				month = 1;
			}
			else
			{
				month++;
			}
			updateDate();
		});
		gbc[2] = new GridBagConstraints();
		gbc[2].gridx = 6; gbc[0].gridy = 0;
		datePanel.add(right, gbc[2]);
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
	}
	
	private void drawCal() {
		int calDay = rawCal();
		int daysOfMonth;
		int row;
		if(month == 2)
		{
			if( isLeapYear(year) ) // 윤년 2월달이 29일
			{
				daysOfMonth = 29;
				row = 5;
			}
			else // 2월달이 28일이면
			{
				if(calDay == 0) // 1일이 일요일이면
				{
					daysOfMonth = 28;
					row = 4;
				}
				else // 1일이 일요일이 아니면
				{
					daysOfMonth = 28;
					row = 5;
				}
			}
		}
		else // 2월이 아니면
		{
			if( is31Days(month) )
			{
				daysOfMonth = 31;
				if(calDay == 5 || calDay == 6)
				{
					row = 6;
				}
				else
				{
					row = 5;
				}
			}
			else
			{
				daysOfMonth = 30;
				if(calDay == 6)
				{
					row = 6;
				}
				else
				{
					row = 5;
				}
			}
		}
		
		panel.setLayout(new GridLayout(row+1, 7));
		addButton("일"); addButton("월"); addButton("화"); addButton("수");
		addButton("목"); addButton("금"); addButton("토");
		
		int dayCount = 1;
		for(int i=0; i<row; i++)
		{
			for(int j=0; j<7; j++)
			{
				if(i == 0 && j < calDay)
				{
					addButton("");
				}
				else if(dayCount > daysOfMonth)
				{
					addButton("");
				}
				else
				{
					ActionListener memo = new MemoAction();
					addButton(String.valueOf(dayCount++), memo);
				}
			}
		}
	}
	
	private int rawCal() {
		int preYear = year-1;
		int numOfDays = preYear*365 + (preYear/4 - preYear/100 + preYear/400);
		
		int[] monthArray = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		for(int a=0; a<month-1; a++)
		{
			numOfDays += monthArray[a];
		}
		
		if(month >= 3 && isLeapYear(year))
		{
			numOfDays++;
		}
		
		numOfDays += 1;
		
		return numOfDays % 7; // 0 이면 일요일
	}
	
	private boolean is31Days(int m) {
		if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8
				|| m == 10 || m == 12)
		{
			return true;
		}
		return false;
	}
	
	private class MemoAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame memoFrame = new MemoFrame("TO DO LIST", year, month, 
					Integer.valueOf(((JButton)event.getSource()).getText()));
			memoFrame.setVisible(true);
			memoFrame.setResizable(false);
		}
	}
	
	private void updateDate() {
		date.setText(String.valueOf(year) + ". "
				+ String.valueOf(month) + ".");
		panel.removeAll();
		drawCal();
	}
}