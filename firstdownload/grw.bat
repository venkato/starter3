@echo off
set GR_HOME=%~dp0\..

IF NOT EXIST %GR_HOME%\libs\copy mkdir %GR_HOME%\libs\copy

IF NOT EXIST %GR_HOME%\firstdownload\javarunnerw.bat copy  %GR_HOME%\firstdownload\javarunnerw_sample.bat  %GR_HOME%\firstdownload\javarunnerw.bat

call:copy_file jremoterun.jar
call:copy_file groovy_custom.jar
call:copy_file groovy.jar


%GR_HOME%\firstdownload\javarunnerw.bat  %JAVA_OPTS% %GROOVY_OPTS% -javaagent:%GR_HOME%\libs\copy\jremoterun.jar  -Dexception.swing=true -classpath %GR_HOME%\libs\copy\groovy_custom.jar;%GR_HOME%\libs\copy\groovy.jar groovy.ui.GroovyMain %GR_HOME%\firstdownload\groovyrunner.groovy 3 %*%

if %ERRORLEVEL% == 0 goto:eof
EXIT /B 1


:copy_file
IF NOT EXIST %GR_HOME%\libs\copy\%~1 copy  %GR_HOME%\libs\origin\%~1  %GR_HOME%\libs\copy\%~1
goto:eof

