module CS460WProject {
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
	requires com.microsoft.sqlserver.jdbc;
	requires org.junit.jupiter.api;
//	requires org.testng;
	
	opens JavaSrc;
	opens JavaSrc.Exceptions;
	opens fxml;
}
