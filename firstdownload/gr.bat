@echo off
set GR_HOME=%~dp0..

rem echo on

IF NOT EXIST %GR_HOME%\firstdownload\gr_custom.bat copy  %GR_HOME%\firstdownload\gr_custom_sample.bat  %GR_HOME%\firstdownload\gr_custom.bat

IF NOT EXIST %GR_HOME%\libs\copy\jremoterun.jar (
     echo copying jars ..
     xcopy /H /K /S /Y /I /F %GR_HOME%\libs\origin %GR_HOME%\libs\copy
     SET JRR_COPYINITJAR_ERROR_LEVEL=%ERRORLEVEL%
     if "%JRR_COPYINITJAR_ERROR_LEVEL%"=="0" ( 
         rem echo init jars copied ok
     )ELSE (
         echo failed copy init jars, error code = %JRR_COPYINITJAR_ERROR_LEVEL%
     )

)

setlocal EnableDelayedExpansion
set JRR_CUSTOM_FILE_THIS=%GR_HOME%\firstdownload\gr_custom.bat
IF EXIST "%JRR_CUSTOM_FILE_GENERAL%" set "JRR_CUSTOM_FILE_THIS=%JRR_CUSTOM_FILE_GENERAL%"
call %JRR_CUSTOM_FILE_THIS% jrr_pre0

rem # begin constanats
set JRR_CONSTANT_jrrrunnerprintcmd=jrrrunnerPrintCmd
set JRR_CONSTANT_jrrrunnertest=jrrrunnerEchoTest
set JRR_CONSTANT_pauseOnExit=jrrrunnerPauseOnExit
set JRR_CONSTANT_sleep5sOnExit=jrrrunnerSleep5OnExit
set JRR_CONSTANT_jrrrunnerplaysound=jrrrunnerPlaySoundOnFinish
set JRR_CONSTANT_jstd=jstd
set JRR_CONSTANT_jw=jw
set JRR_CONSTANT_jxmx=jxmx
set JRR_CONSTANT_jdebug=jdebug
set JRR_CONSTANT_jrrParam=jrrVmParam
set JRR_CONSTANT_jpr=jpr
set JRR_CONSTANT_sound=TextFunction : p1
rem # custom windows constants
set JRR_CONSTANT_prefixDelim=-D
set JRR_CONSTANT_DelimValue==


set JRR_PREFIX_CMD=
set JRR_ECHO_TEST=0
set JRR_JAVA_PATH=java
SET JRR_JAVATYPE=2
SET JRR_FULL_CMD=
SET JRR_JAVA8_USED=1
SET JRR_PRINT_JAVA_DEFAULT_ARGS=0
SET JRR_PRINT_JAVA_DEFAULT_CMD_ARGS=-XX:+PrintCommandLineFlags
SET JRR_PRINT_CMD=0
SET JRR_PRINT_CURRENT_DIR=0
SET JRR_ADD_JMX_UNSAFE=0
set JRR_AFTER_FINISH_SET_PRINT_OFF=1
SET JRR_FIRST_JVM_OPTS=
set JRR_PROGA_ARGS_LAST=
SET JRR_JVM_OPTS=
SET JRR_MEMORY_XMX=
rem # path separated by coma
SET JRR_IS_NEED_PLAYSOUND_TEXT_ON_FINISH=gr finished
SET JRR_IS_NEED_PLAYSOUND_ON_FINISH=0
SET JRR_jrrclasspath=
SET JRR_PRINT_JAVA_HOME_FOUND=0
SET JRR_PRINT_JAVA_HOME_NOTFOUND=1
set JRR_DEFAULT_JVM11_OPTS= --add-opens=java.base/java.lang=ALL-UNNAMED -Djdk.reflect.useDirectMethodHandle=false
set JRR_JAVASTDFORCE=0
set JRR_JAVAW=
set JRR_ERROR_LEVEL=

rem custom windows props
set JRR_ORIGINAL_ARGS=%*
set JRR_HOSTNAME=%COMPUTERNAME%
set JRR_SEPARATOR_CLASSPATH=;
SET JRR_ALWAYS_OPTS=-javaagent:%GR_HOME%/libs/copy/jremoterun.jar -javaagent:%GR_HOME%/libs/copy/java11base.jar
SET JRR_CLASSPATH=%GR_HOME%/libs/copy/groovy_custom.jar;%GR_HOME%/libs/copy/groovy.jar;%GR_HOME%/libs/copy/java11base.jar
set JRR_CURRENT_DIR=%CD%
set JRR_AFTER_FINISH_PAUSE=0
set JRR_SLEEP_TIME_IN_SEC=5
set JRR_SLEEP_FINISH=0
set JRR_JOINT_NEXT_ARG=0
rem # ends constanats

call %JRR_CUSTOM_FILE_THIS% jrr_pre1

rem https://ss64.com/nt/for.html separator : space comma semicomma or equals
rem echo parsing args .. may bee shift https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/shift
for %%a in (%*) do (
  set JRR_ARG=%%a
  rem echo JRR_JOINT_NEXT_ARG 1 = %JRR_JOINT_NEXT_ARG%
  rem echo JRR_JOINT_NEXT_ARG 2 = !JRR_JOINT_NEXT_ARG!
  if "!JRR_ARG:~0,4!"=="%JRR_CONSTANT_jxmx%" (
     set JRR_MEMORY_XMX=-Xmx!JRR_ARG:~4!
     rem set JRR_PRINT_CMD=1    
  ) ELSE if "!JRR_JOINT_NEXT_ARG!"=="1" (       
       set JRR_JOINT_NEXT_ARG=0
       set JRR_JVM_OPTS=!JRR_JVM_OPTS!%JRR_CONSTANT_DelimValue%!JRR_ARG!
  ) ELSE if "!JRR_ARG:~0,12!"=="%JRR_CONSTANT_jrrParam%%JRR_CONSTANT_prefixDelim%" (       
       rem echo test JRR_ARG = !JRR_ARG!
       set JRR_JOINT_NEXT_ARG=1
       set JRR_JVM_OPTS=!JRR_JVM_OPTS! !JRR_ARG:~10!
  ) ELSE if "!JRR_ARG:~0,10!"=="%JRR_CONSTANT_jrrParam%" (       
       rem echo test JRR_ARG = !JRR_ARG!
       set JRR_JVM_OPTS=!JRR_JVM_OPTS! !JRR_ARG:~10!
  ) ELSE if "!JRR_ARG:~0,6!"=="%JRR_CONSTANT_jdebug%" (       
       rem sample yn1088 means server=y,suspend=n,address=1088
       rem sample ny127.0.0.1:1088 means server=n,suspend=y,address=127.0.0.1:1088
       set JRR_JVM_OPTS=!JRR_JVM_OPTS! -agentlib:jdwp=transport=dt_socket,server=!JRR_ARG:~6,1!,suspend=!JRR_ARG:~7,1!,address=!JRR_ARG:~8!
  ) ELSE if "%%a"=="%JRR_CONSTANT_jrrrunnerprintcmd%" (       
       set JRR_PRINT_CMD=1
       set JRR_PRINT_JAVA_DEFAULT_ARGS=1
  ) ELSE if "%%a"=="%JRR_CONSTANT_jrrrunnerplaysound%" (       
       set JRR_IS_NEED_PLAYSOUND_ON_FINISH=1
  ) ELSE if "%%a"=="%JRR_CONSTANT_jrrrunnertest%" (       
       set JRR_ECHO_TEST=1
  ) ELSE if "%%a"=="%JRR_CONSTANT_jpr%" (       
       set JRR_JVM_OPTS= %JRR_PRINT_JAVA_DEFAULT_CMD_ARGS% !JRR_JVM_OPTS!
  ) ELSE if "%%a"=="%JRR_CONSTANT_jstd%" (
       set JRR_JAVATYPE=2
       set JRR_JAVASTDFORCE=1
  ) ELSE if "%%a"=="%JRR_CONSTANT_jw%" (
     if "!JRR_JAVASTDFORCE!"=="0" (
       set JRR_JAVATYPE=3
     )       
  ) ELSE if "%%a"=="%JRR_CONSTANT_pauseOnExit%" (
       set JRR_AFTER_FINISH_PAUSE=1
  ) ELSE if "%%a"=="%JRR_CONSTANT_sleep5sOnExit%" (
       set JRR_SLEEP_FINISH=1
  )ELSE (  
     call %JRR_CUSTOM_FILE_THIS% jrr_pre_onarg !JRR_ARG!
  )
)

rem echo args parsed
rem echo on 

if "%JRR_JAVA8_USED%"=="0" ( 
  set JRR_JVM_OPTS=%JRR_JVM_OPTS% %JRR_DEFAULT_JVM11_OPTS%
)

if "%JRR_JAVATYPE%"=="3" ( 
  set JRR_JAVAW=w
)

call %JRR_CUSTOM_FILE_THIS% jrr_pre2
rem echo on

if "%JRR_ADD_JMX_UNSAFE%"=="1" (
  set JRR_JVM_OPTS=%JRR_JVM_OPTS% -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.ssl=false -Dcom_sun_management_jmxremote_authenticate=false
)

if "%JRR_PRINT_JAVA_DEFAULT_ARGS%"=="1" (
   set JRR_JVM_OPTS=%JRR_JVM_OPTS% %JRR_PRINT_JAVA_DEFAULT_CMD_ARGS%
)

if "%JRR_jrrclasspath%" neq "" (
  set JRR_JVM_OPTS=%JRR_JVM_OPTS% -Djrrclasspath=%JRR_jrrclasspath%
)

if "%JRR_PRINT_CURRENT_DIR%"=="1" (
   echo current dir : %CD%
)

if "%JRR_ECHO_TEST%"=="1" (
   set JRR_PREFIX_CMD=echo %JRR_PREFIX_CMD%
   rem echo echo mode on
)

if "%JRR_PRINT_CMD%"=="1" (
   set JRR_JVM_OPTS=%JRR_JVM_OPTS% -showversion
   @echo on
)


%JRR_PREFIX_CMD% "%JRR_JAVA_PATH%%JRR_JAVAW%" %JRR_FIRST_JVM_OPTS% %JRR_MEMORY_XMX% %JRR_JVM_OPTS% %JAVA_OPTS% %JRR_ALWAYS_OPTS% %GROOVY_OPTS% -classpath %JRR_CLASSPATH% groovy.ui.GroovyMain "%GR_HOME%/firstdownload/groovyrunner.groovy" %JRR_JAVATYPE% %JRR_FULL_CMD% %JRR_PROGA_ARGS_LAST%

@SET JRR_ERROR_LEVEL=%ERRORLEVEL%

@if "%JRR_AFTER_FINISH_SET_PRINT_OFF%"=="1" (
   @echo off
)

call %JRR_CUSTOM_FILE_THIS% jrr_post1 %JRR_ERROR_LEVEL%


@if "%JRR_IS_NEED_PLAYSOUND_ON_FINISH%"=="1" (
   call %JRR_CUSTOM_FILE_THIS% jrr_sound %JRR_IS_NEED_PLAYSOUND_TEXT_ON_FINISH%
)

if "!JRR_ERROR_LEVEL!"=="0" ( 
    call %JRR_CUSTOM_FILE_THIS% jrr_post_finished_fine
)ELSE (
    call %JRR_CUSTOM_FILE_THIS% jrr_post_finished_with_error %JRR_ERROR_LEVEL%
)

rem needed if sometimes need pause
@if "%JRR_SLEEP_FINISH%"=="1" (
   C:\Windows\System32\timeout.exe /t %JRR_SLEEP_TIME_IN_SEC%
   rem C:\Windows\System32\timeout.exe /t %JRR_SLEEP_TIME_IN_SEC% /nobreak
)

@if "%JRR_AFTER_FINISH_PAUSE%"=="1" (
   time /T
   pause
)

exit /B %JRR_ERROR_LEVEL%

endlocal



