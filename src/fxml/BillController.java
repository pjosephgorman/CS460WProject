package fxml;

import JavaSrc.Data.UserInfo;
import javafx.scene.control.TextArea;

public class BillController implements SceneController
{
	public TextArea itemizedBill;

	public void createBill(String patName, TextArea textArea)
	{
			try
			{
				this.itemizedBill = textArea;
				itemizedBill.setText(patName);
				System.out.println(patName);
			}
			catch(NullPointerException e)
			{
				System.out.println("NullPointer Exception");
			}
	}
	
	@Override
	public void clear()
	{
	
	}
	
	@Override
	public void error(String msg)
	{
	
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
}
