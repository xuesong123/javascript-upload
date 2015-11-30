@ECHO OFF
rd /s /q "webapp\WEB-INF\ayada"

@IF exist "E:\WorkSpace\finder" copy "docs\conf\server-local.xml" "D:\Tomcat-7.0.37\conf\server.xml"
@IF exist "d:\workspace2\finder" copy "docs\conf\server.xml" "D:\Tomcat-7.0.37\conf\server.xml"

del "D:\logs\generator.log"
cd /d "D:\Tomcat-7.0.37\bin"
startup.bat