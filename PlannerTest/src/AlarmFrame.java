import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmFrame extends JFrame {
	
	private DefaultTableModel model;
	private JTable table;
	private String fileName;
	private String times[] = new String[10];

	
	public AlarmFrame(String title, int year, int month, int day, String name) {
		setTitle(title);
		setSize(300, 300);
		fileName = name.split("\\.", 2)[0] + "alarm.txt";
		
		JPanel timePanel = new JPanel();
		JLabel jobLabel = new JLabel("Job"); timePanel.add(jobLabel);
		JTextField jobText = new JTextField(2); timePanel.add(jobText);
		JLabel hourLabel = new JLabel("Hour"); timePanel.add(hourLabel);
		JTextField hourText = new JTextField(10); timePanel.add(hourText);
		JLabel minuteLabel = new JLabel("Min"); timePanel.add(minuteLabel);
		JTextField minuteText = new JTextField(10); timePanel.add(minuteText);
		add(timePanel, BorderLayout.NORTH);
		
		JPanel tablePanel = new JPanel();
		Object contents[][] = MemoFrame.makeContents(fileName, 2);
		String columnNames[] = {"우선순위", "시각"};
		
		table = new JTable();
		model = new DefaultTableModel(contents, columnNames);
		table.setModel(model);
		
		if(table.getValueAt(0, 0).equals(""))
		{
			model.removeRow(0);
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(350, 350));
		tablePanel.add(scrollPane);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		
		table.setEnabled(false);
		
		add(tablePanel);
		
		JPanel commandPanel = new JPanel();
		JButton addButton = new JButton("추가");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				model.addRow(new Object[]{jobText.getText(), hourText.getText() + ":" +
						minuteText.getText()});
				StringBuilder txt = new StringBuilder();
				for(int i=0; i<table.getRowCount(); i++) // table scope??
				{
					for(int j=0; j<table.getColumnCount(); j++)
					{
						txt.append(table.getValueAt(i, j) + "\t");
					}
					txt.append("\n");
				}
				MemoFrame.writeTxt(String.valueOf(txt), fileName);
				
				//makeTimesArray();
			}
		});
		addButton.setFocusPainted(false);
		addButton.setContentAreaFilled(false);
		commandPanel.add(addButton);
		JButton removeButton = new JButton("삭제");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				model.removeRow(model.getRowCount()-1);
				StringBuilder txt = new StringBuilder();
				for(int i=0; i<table.getRowCount(); i++) // table scope??
				{
					for(int j=0; j<table.getColumnCount(); j++)
					{
						txt.append(table.getValueAt(i, j) + "\t");
					}
					txt.append("\n");
				}
				MemoFrame.writeTxt(String.valueOf(txt), fileName);
				
				//makeTimesArray();
			}
		});
		removeButton.setFocusPainted(false);
		removeButton.setContentAreaFilled(false);
		commandPanel.add(removeButton);
		add(commandPanel, BorderLayout.SOUTH);
		
		pack();
		
		makeTimesArray();
		
		operateAlarm();
	}
	
	/* times라는 공유자원에 race condition이 발생했었지만, 
	 * 이 함수 호출을 operateAlarm에서 만들어진 스레드에서만 호출되는걸로 수정함 */
	private void makeTimesArray() {
		for(int i=0; i<table.getRowCount(); i++)
		{
			times[i] = (String)table.getValueAt(i, 1);
		}
	}
	
	/* times, time 주기적으로 업데이트 해줘야함 */
	private void operateAlarm() {
		Runnable r = new Runnable() {
			public void run() {
				while(true)
				{
					makeTimesArray();
					for(int i=0; i<table.getRowCount(); i++)
					{
						int hour = Integer.valueOf(times[i].split(":")[0]);
						int minute = Integer.valueOf(times[i].split(":")[1]);
						
						long milis = System.currentTimeMillis();
						
						int cHour = Integer.valueOf(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(milis).split(" ")[1].split(":")[0]);
						int cMinute = Integer.valueOf(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(milis).split(" ")[1].split(":")[1]);
						
						if(hour == cHour && minute == cMinute)
						{
							System.out.println("띠리링~");
							JOptionPane.showMessageDialog(null, "띠리링~");
							try {
								Thread.sleep(1000);
							} catch(InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					
					try {
						Thread.sleep(500);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}

