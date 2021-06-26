/*
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class PlannerMain {
	public static void main(String[] args) {
		Runnable r1 = new Runnable() {
			
			public void run() {
				// �ڵ� ������Ʈ 
				String rDir = "/html/planner/";
				String lDir = "";
				FTPClient ftpClient = new FTPClient();
				try {
					ftpClient.setControlEncoding("UTF-8");
					ftpClient.connect("tigerz.dothome.co.kr", 21);
					int resultCode = ftpClient.getReplyCode();
					if(!FTPReply.isPositiveCompletion(resultCode))
					{
						System.out.println("FTP server refused connection..!");
						return;
					}
					else
					{
						ftpClient.setSoTimeout(1000);
						if(!ftpClient.login("tigerz", "ehlswkd56!7"))
						{
							System.out.println("Login Error!");
							return;
						}
						
						if(!ftpClient.changeWorkingDirectory(rDir))
						{
							return;
						}
						
						String[] ftpFiles = ftpClient.listNames();
						File getFile;
						FileOutputStream outputStream;
						
						for(int i=0; i<ftpFiles.length; i++)
						{
							if(ftpFiles[i].equals("version.txt"))
							{
								getFile = new File(lDir + ftpFiles[i]);
								outputStream = new FileOutputStream(getFile);
								ftpClient.retrieveFile(rDir + ftpFiles[i], outputStream);
								outputStream.close();
								
								FileReader filereader = new FileReader(getFile);
								BufferedReader bufReader = new BufferedReader(filereader);
								String line = "";
								line = bufReader.readLine();
								bufReader.close();
								
								if(!line.equals("2"))
								{
									JOptionPane.showMessageDialog(null, "������Ʈ ���� �ٿ�ε带 �����մϴ�.");
									
									for(int j=0; j<ftpFiles.length; j++)
									{
										if(ftpFiles[j].equals("KYJPlanner.exe"))
										{
											try {
												getFile = new File(lDir + ftpFiles[j]);
												outputStream = new FileOutputStream(getFile); // �߰� �̻��ϴ�?
												// �̹� Planner.exe�� ����� ���¿��� Planner.exe�� stream���� open�ϴϱ�
												// ������ ����°Ű�, �� ������ catch ������ϴµ�
												// ���࿡ catch�� ���� �����ָ� ���Ѱ����� catch�� �Ǿ
												// 100���ٿ� �ִ� ���Ϲ��� ������ ���� �ʴ°��̴�. �׷���, frame�� �����Ǵ� �帧�� Ž
												ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
												ftpClient.retrieveFile(rDir + ftpFiles[j], outputStream);
												outputStream.close();
												break;
											} catch(Exception e) {
												JOptionPane.showMessageDialog(null, e.getStackTrace());
											}
											
										}
									}
									ftpClient.logout();
									ftpClient.disconnect();
									
									try {
										Runtime.getRuntime().exec("cmd /c update.exe");
									} catch(IOException e) {
										e.printStackTrace();
									}
									
									return;
								}
								break;
								
							}
						}
						
						ftpClient.logout();
						
					}
				}
				
				catch(Throwable e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getStackTrace());
				}
				
				finally
				{
					// ���� ������ �Ǹ� �ȵǴµ�??
					//JOptionPane.showMessageDialog(null, "����");
					try
					{
						if(ftpClient.isConnected())
						{
							ftpClient.disconnect();
						}
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
				
				
				//////////////////////////////////////////////
				JFrame frame = new PlannerFrame("KYJ PLANNER");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
				
				frame.pack();
			}
		};
		Thread thread = new Thread(r1);
		thread.start();
		
		//while(thread.isAlive()) { }
		
		return;
	}
}
*/

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class PlannerMain {
	public static void main(String[] args) {
		
		Runnable r1 = new Runnable() {
			public void run() {
				
				/* �ڵ� ������Ʈ */
				
				String rDir = "/html/planner/";
				String lDir = "";
				FTPClient ftpClient = new FTPClient();
				try {
					ftpClient.setControlEncoding("UTF-8");
					ftpClient.connect("ftp ����", 21);
					int resultCode = ftpClient.getReplyCode();
					if(!FTPReply.isPositiveCompletion(resultCode))
					{
						System.out.println("FTP server refused connection..!");
						return;
					}
					else
					{
						ftpClient.setSoTimeout(1000);
						if(!ftpClient.login("id", "password"))
						{
							System.out.println("Login Error!");
							return;
						}
						
						if(!ftpClient.changeWorkingDirectory(rDir))
						{
							return;
						}
						
						String[] ftpFiles = ftpClient.listNames();
						File getFile;
						FileOutputStream outputStream;
						
						for(int i=0; i<ftpFiles.length; i++)
						{
							if(ftpFiles[i].equals("version.txt"))
							{
								getFile = new File(lDir + ftpFiles[i]);
								outputStream = new FileOutputStream(getFile);
								ftpClient.retrieveFile(rDir + ftpFiles[i], outputStream);
								outputStream.close();
								
								FileReader filereader = new FileReader(getFile);
								BufferedReader bufReader = new BufferedReader(filereader);
								String line = "";
								line = bufReader.readLine();
								bufReader.close();
								
								if(!line.equals("3"))
								{
									JOptionPane.showMessageDialog(null, "������Ʈ ���� �ٿ�ε带 �����մϴ�.");
									
									for(int j=0; j<ftpFiles.length; j++)
									{
										if(ftpFiles[j].equals("KYJPlanner.exe"))
										{
											try {
												Runtime.getRuntime().exec("cmd /c update.exe");
											} catch(IOException e) {
												e.printStackTrace();
											}
											break;
										}
									}
									
									ftpClient.logout();
									ftpClient.disconnect();
									
									
									return;
								}
								break;
							}
						}
						
						ftpClient.logout();
						
					}
				}
				catch(Throwable e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(ftpClient.isConnected())
						{
							ftpClient.disconnect();
						}
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
				
				/******************************************/
				
				JFrame frame = new PlannerFrame("KYJ PLANNER");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
				
				frame.pack();
			}
		};
		Thread thread = new Thread(r1);
		thread.start();
		
		//while(thread.isAlive()) { }
		
		return;
	}

}

