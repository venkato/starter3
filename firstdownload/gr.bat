@echo off
set GR_HOME=%~dp0\..

IF NOT EXIST %GR_HOME%\libs\copy mkdir %GR_HOME%\libs\copy


IF NOT EXIST %GR_HOME%\firstdownload\javarunner.bat copy  %GR_HOME%\firstdownload\javarunner_sample.bat  %GR_HOME%\firstdownload\javarunner.bat

call:copy_file jremoterun.jar
call:copy_file groovy_custom.jar
call:copy_file groovy.jar

rem -javaagent:%GR_HOME%\libs\copy\jremoterun.jar

%GR_HOME%\firstdownload\javarunner.bat %GROOVY_OPTS% -javaagent:%GR_HOME%\libs\copy\jremoterun.jar -classpath %GR_HOME%\libs\copy\groovy_custom.jar;%GR_HOME%\libs\copy\groovy.jar groovy.ui.GroovyMain %GR_HOME%\firstdownload\groovyrunner.groovy 2 %*%

if %ERRORLEVEL% == 0 goto:eof
EXIT /B 1


goto:eof


:copy_file
IF NOT EXIST %GR_HOME%\libs\copy\%~1 copy  %GR_HOME%\libs\origin\%~1  %GR_HOME%\libs\copy\%~1
goto:eof

