

rem echo custom : %*
call:%*%
goto EXIT1

:jrr_not_used
goto EXIT1

:jrr_pre0
goto EXIT1


:jrr_pre1
rem time /T
rem set JRR_ADD_JMX_UNSAFE=1
goto EXIT1


:jrr_pre_onarg
if "%1"=="j15" (
  call:jrr_set_my_java_home 0 c:\somepath
  set JRR_JAVA8_USED=0
 ) ELSE (
  set JRR_FULL_CMD=!JRR_FULL_CMD! %1
 )
goto EXIT1


:jrr_pre2
goto EXIT1


:jrr_post1
rem time /T
goto EXIT1


:jrr_post_finished_fine
echo finished fine
rem call:jrr_sound finished fine
goto EXIT1


:jrr_post_finished_with_error
echo finished with error %1
rem call:jrr_sound finsihed with error
goto EXIT1


:jrr_set_my_java_ver_and_home
IF EXIST "%~2" (
    set "JRR_JAVA_PATH=%~2\bin\java"
    set JRR_JAVA8_USED=%~1
    if "%JRR_PRINT_JAVA_HOME_FOUND%"=="1" (
       echo using java from %~2
    )
) ELSE (
    if "%JRR_PRINT_JAVA_HOME_NOTFOUND%"=="1" (
       echo java home not found %~2
    )
)
goto EXIT1


:jrr_copy_gr_to_new_location
xcopy /H /K /E /Y /I %GR_HOME% %1
GR_HOME=%1
goto EXIT1


:jrr_sound
echo %JRR_CONSTANT_sound% %*
goto EXIT1



:EXIT1


