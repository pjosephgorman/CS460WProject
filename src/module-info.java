module CS460WProject {
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
	requires com.microsoft.sqlserver.jdbc;
	
	opens JavaSrc;
	opens JavaSrc.Exceptions;
	opens fxml;
}
