import java.io.File;


import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.*;

public class PlannerUpdate {

	public static void main(String[] args) {
		String lDir = "";
		String rDir = "/html/planner/";
		FTPClient ftpClient = new FTPClient();
		
		try {
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.connect("ftp 서버", 21); // 개인 서버 기입
			int resultCode = ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(resultCode))
			{
				System.out.println("FTP server refused connection..!");
				return;
			}
			else
			{
				ftpClient.setSoTimeout(1000);
				if(!ftpClient.login("id", "password")) // id password 기입
				{
					System.out.println("Login Error!");
					return;
				}
				
				if(!ftpClient.changeWorkingDirectory(rDir))
				{
					return;
				}
				
				try {
					File getFile = new File(lDir + "KYJPlanner.exe");
					FileOutputStream outputStream = new FileOutputStream(getFile);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.retrieveFile(rDir + "KYJPlanner.exe", outputStream);
					outputStream.close();
					
					Runtime.getRuntime().exec("KYJPlanner.exe");
				} catch(Exception e) {
					e.printStackTrace();
				}
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
	}
}
