import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.*;

public class MemoFrame extends JFrame{
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
	private JPanel panel;
	private String fileName;
	private JTable table;
	private JFrame alarmFrame;
	
	private DefaultTableCellRenderer dcr = new DefaultTableCellRenderer()
	{
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			JCheckBox box = new JCheckBox();
			box.setSelected(((Boolean)value).booleanValue());
			box.setHorizontalAlignment(JLabel.CENTER);
			return box;
		}
	};
	
	
	public MemoFrame(String title, int year, int month, int day) {
		fileName = year%100 + String.format("%02d", month) 
		+ String.format("%02d", day) + ".txt";
		setTitle(title + " " + fileName);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		/* Set Icon Image */
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("k.png");
		setIconImage(img);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(panel);
		
		designPanel();
	
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		alarmFrame = new AlarmFrame("알람", year, month, day, fileName);
		alarmFrame.setVisible(false);
		alarmFrame.setResizable(false);
		
		JMenu menuMenu = new JMenu("메뉴");
		JMenuItem alarmItem = new JMenuItem("알람");
		alarmItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				alarmFrame.setVisible(true);
			}
		});
		menuMenu.add(alarmItem);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuMenu);
		setJMenuBar(menuBar);
		pack();
	}
	
	public static Object[][] makeContents(String fileName, int colNum) {
		StringBuilder buffer = new StringBuilder();
		try {
			File file = new File(fileName);
			FileReader file_reader = new FileReader(file);
			int cur = 0;
			while( (cur = file_reader.read()) != -1 )
			{
				buffer.append( (char)cur );
			}
			file_reader.close();
		} catch(FileNotFoundException e) {
			System.out.println("Do not Found file..!");
		} catch(IOException e) {
			System.out.println(e.getStackTrace());
		}

		String content[] = String.valueOf(buffer).split("\n");
		boolean zeroBuffer = false;
		if(String.valueOf(buffer).equals(""))
		{
			zeroBuffer = true;
		}
		Object contents[][] = new Object[content.length][colNum];
		if(colNum == 2)
		{
			for(int i=0; i<contents.length; i++)
			{
				if(zeroBuffer)
				{
					Object[] temp = {"", ""};
					contents[0] = temp;
				}
				else
				{
					contents[i][0] = content[i].split("\t",4)[0];
					contents[i][1] = content[i].split("\t",4)[1];
				}
			}
		}
		else if(colNum == 3)
		{
			for(int i=0; i<contents.length; i++)
			{
				if(zeroBuffer)
				{
					Object[] temp = {"", "", false};
					contents[0] = temp;
				}
				else
				{
					contents[i][0] = content[i].split("\t",4)[0];
					contents[i][1] = content[i].split("\t",4)[1];
					String checkStr = content[i].split("\t",4)[2];
					if(checkStr.equals("true"))
					{
						contents[i][2] = true;
					}
					else
					{
						contents[i][2] = false;
					}
				}
			}
		}
		return contents;
	}
	
	private void designPanel() {
		Object contents[][] = makeContents(fileName, 3);

		String columnNames[] = {"우선순위", "할 일", "결과"};
		
		table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel(contents, columnNames);
		table.setModel(tableModel);
		
		/* input checkbox */
		JCheckBox checkBox = new JCheckBox();
		table.getColumn("결과").setCellRenderer(dcr);
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("결과").setCellEditor(new DefaultCellEditor(checkBox));
		/******************/
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		
		
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(2).setPreferredWidth(30);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		
		
		JPanel buttonPanel = new JPanel();
		
		JButton addButton = new JButton("추가");
		addButton.setFocusPainted(false);
		addButton.setContentAreaFilled(false);
		addButton.setFont(new Font("Serif", Font.PLAIN, 14));
		buttonPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[]{"", "", false});
			}
		});
		
		JButton removeButton = new JButton("삭제");
		removeButton.setFocusPainted(false);
		removeButton.setContentAreaFilled(false);
		removeButton.setFont(new Font("Serif", Font.PLAIN, 14));
		buttonPanel.add(removeButton);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.removeRow(table.getRowCount()-1);
			}
		});
		
		
		JButton saveButton = new JButton("저장");
		saveButton.setFocusPainted(false);
		saveButton.setContentAreaFilled(false);
		saveButton.setFont(new Font("Serif", Font.PLAIN, 14));
		buttonPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				StringBuilder txt = new StringBuilder();
				for(int i=0; i<table.getRowCount(); i++) // table scope??
				{
					for(int j=0; j<table.getColumnCount(); j++)
					{
						txt.append(table.getValueAt(i, j) + "\t");
					}
					txt.append("\n");
				}
				writeTxt(String.valueOf(txt), fileName);
			}
		});
		
		JButton quitButton = new JButton("끄기");
		quitButton.setFocusPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.setFont(new Font("Serif", Font.PLAIN, 14));
		buttonPanel.add(quitButton);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		
		panel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static void writeTxt(String txt, String fileName) {
		File file = new File(fileName);
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(txt);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}