package fxml;

import JavaSrc.Data.UserInfo;

public interface SceneController
{
	void clear();
	void error(String msg);
	void updateInfo(UserInfo info);
}
