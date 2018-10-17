#!/bin/bash
# set -x
#set -o pipefailed
export GR_HOME=$(dirname "$(readlink -f "$0")")/..

JRR_UNAME_OUT="$(uname -s)"

if [ "$JRR_CUSTOM_FILE_GENERAL" != "" ] ; then
      echo loading custom script  "$JRR_CUSTOM_FILE_GENERAL"
      . "$JRR_CUSTOM_FILE_GENERAL"
   else
      if [ ! -f "${GR_HOME}/firstdownload/gr_custom.sh" ] ; then
          dos2unix "${GR_HOME}/firstdownload/gr_custom_sample.sh"
          cp "${GR_HOME}/firstdownload/gr_custom_sample.sh"  "${GR_HOME}/firstdownload/gr_custom.sh"
          chmod u+rwx "${GR_HOME}/firstdownload/gr_custom.sh"
          
      fi  
      chmod u+x "${GR_HOME}/firstdownload/gr_custom.sh"
      . "${GR_HOME}/firstdownload/gr_custom.sh"    
fi



jrr_pre0
# begin constanats
JRR_CONSTANT_jrrrunnerprintcmd=jrrrunnerPrintCmd
JRR_CONSTANT_jrrrunnertest=jrrrunnerEchoTest
JRR_CONSTANT_pauseOnExit=jrrrunnerPauseOnExit
JRR_CONSTANT_sleep5sOnExit=jrrrunnerSleep5OnExit
JRR_CONSTANT_jrrrunnerplaysound=jrrrunnerPlaySoundOnFinish
# https://unix.stackexchange.com/questions/3886/difference-between-nohup-disown-and
JRR_CONSTANT_jrrrunnerfork=jrrrunnerFork
JRR_CONSTANT_jrrrunnernofork=jrrrunnerNoFork
JRR_CONSTANT_jstd=jstd
JRR_CONSTANT_jw=jw
JRR_CONSTANT_jxmx=jxmx
JRR_CONSTANT_jdebug=jdebug
JRR_CONSTANT_jrrParam=jrrVmParam
JRR_CONSTANT_jpr=jpr
JRR_CONSTANT_sound="TextFunction : p1"

JRR_CONSTANT_prefixDelim="-D"
JRR_CONSTANT_DelimValue="="
JRR_PREFIX_CMD=
JRR_ECHO_TEST=0
JRR_JAVA_PATH=java
JRR_JAVATYPE=2
JRR_FULL_CMD=
JRR_JAVA8_USED=1
JRR_PRINT_JAVA_DEFAULT_ARGS=0
JRR_PRINT_JAVA_DEFAULT_CMD_ARGS="-XX:+PrintCommandLineFlags"
JRR_PRINT_CMD=0
JRR_PRINT_CURRENT_DIR=0
JRR_ADD_JMX_UNSAFE=0
JRR_AFTER_FINISH_SET_PRINT_OFF=1
JRR_FIRST_JVM_OPTS=
JRR_PROGA_ARGS_LAST=
JRR_JVM_OPTS=
JRR_MEMORY_XMX=
JRR_IS_NEED_PLAYSOUND_TEXT_ON_FINISH="gr finished"
JRR_IS_NEED_PLAYSOUND_ON_FINISH=0
JRR_jrrclasspath=
JRR_PRINT_JAVA_HOME_FOUND=0
JRR_PRINT_JAVA_HOME_NOTFOUND=1
JRR_DEFAULT_JVM11_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED -Djdk.reflect.useDirectMethodHandle=false"
JRR_JAVASTDFORCE=0
JRR_JAVAW=
JRR_ERROR_LEVEL=

# custom unix props
JRR_ORIGINAL_ARGS=$@
JRR_HOSTNAME=$(hostname)
JRR_SEPARATOR_CLASSPATH=
JRR_ALWAYS_OPTS=
JRR_CLASSPATH=
JRR_CURRENT_DIR=$(pwd)
JRR_AFTER_FINISH_PAUSE=0
JRR_SLEEP_TIME_IN_SEC=5
JRR_SLEEP_FINISH=0
JRR_CURRENT_PROCESS_PID=$$
JRR_FORKED_PROCESS_PID=
JRR_FORK_CMD=0
JRR_FORK_DISOWN=0


# ends constanats

jrr_f_set_java_home(){
  if [ -d "$1" ]; then
     JRR_JAVA_PATH="$1/bin/java"
  fi 
}

jrr_set_my_java_home(){
    if [  -d "$1" ] ; then
        JRR_JAVA_PATH="$1/bin/java"
        if [ "$JRR_PRINT_JAVA_HOME_FOUND" = 1 ]; then
          echo "Using java from ${1}"
        fi
    else
       if [ "$JRR_PRINT_JAVA_HOME_NOTFOUND" = 1 ]; then
          echo "java home not found ${1}"
       fi
    fi
}


jrr_set_my_java_ver_and_home(){
    if [  -d "$2" ] ; then
        JRR_JAVA_PATH="$2/bin/java"
        JRR_JAVA8_USED="$1"
        if [ "$JRR_PRINT_JAVA_HOME_FOUND" = 1 ]; then
          echo "Using java from ${2}"
        fi
    else
       if [ "$JRR_PRINT_JAVA_HOME_NOTFOUND" = 1 ]; then
          echo "java home not found ${2}"
       fi
    fi
}


jrr_sound(){
  jrr_donothing
  echo $JRR_CONSTANT_sound $@
}

jrr_f_copy_file(){
    export filename="$1"
	if [ ! -f "${GR_HOME}/libs/copy/${filename}" ] ; then
		cp "${GR_HOME}/libs/origin/${filename}"  "${GR_HOME}/libs/copy/${filename}"
	fi
}

jrr_set_classpath_separator(){
    case "${JRR_UNAME_OUT}" in
        Linux*)     JRR_SEPARATOR_CLASSPATH=:;;
        Darwin*)    JRR_SEPARATOR_CLASSPATH=:;;
        CYGWIN*)    JRR_SEPARATOR_CLASSPATH=";";;
        MINGW*)     JRR_SEPARATOR_CLASSPATH=";";;
        *)          JRR_SEPARATOR_CLASSPATH="UNKNOWN_SEPARATOR_${JRR_UNAME_OUT}"
    esac
}

jrr_copy_gr_to_new_location(){
   cp -r "$GR_HOME" "$1"
   GR_HOME="$1"
}

jrr_update_gr_home(){
    case "${JRR_UNAME_OUT}" in
        Linux*)     ;;
        Darwin*)    ;;
        CYGWIN*)    GR_HOME=$(echo "${GR_HOME}"|sed 'sf/cygdrive/\(.\)f\1:fg');;
        MINGW*)     GR_HOME=$(echo "${GR_HOME}"|sed 'sf/cygdrive/\(.\)f\1:fg');;
        *)          GR_HOME="UNKNOWN_GR_HOME_${JRR_UNAME_OUT}"
    esac
}

jrr_f_pause_execution(){
  read -r -p "Press any key to resume ..."
}

jrr_f_sleep_execution(){
  read -r -t "$1" -p "I am going to wait for $1 seconds only ..."
}

jrr_f_sleep_and_pause(){
    if [ "$JRR_SLEEP_FINISH" = 1 ]; then
       jrr_f_sleep_execution $JRR_SLEEP_TIME_IN_SEC
    fi
    
    if [ "$JRR_AFTER_FINISH_PAUSE" = 1 ]; then
       jrr_f_pause_execution
    fi
}

jrr_f_check_exit_code(){
  exit_code=$?
  if [ "$exit_code" != 0 ]; then
     echo "ERROR bad exit code $exit_code"
	 exit "$exit_code"
  fi
}


jrr_f_runjava(){

  jrr_update_gr_home
  jrr_set_classpath_separator
  
  if [ ! -d "${GR_HOME}/libs/copy" ] ; then
    mkdir "${GR_HOME}/libs/copy"
  fi


  jrr_f_copy_file jremoterun.jar
  jrr_f_copy_file groovy_custom.jar
  jrr_f_copy_file groovy.jar
  jrr_f_copy_file jrrassist.jar
  jrr_f_copy_file java11base.jar

  JRR_ALWAYS_OPTS="-javaagent:${GR_HOME}/libs/copy/jremoterun.jar -javaagent:${GR_HOME}/libs/copy/java11base.jar"
  JRR_CLASSPATH="${GR_HOME}/libs/copy/groovy_custom.jar${JRR_SEPARATOR_CLASSPATH}${GR_HOME}/libs/copy/groovy.jar${JRR_SEPARATOR_CLASSPATH}${GR_HOME}/libs/copy/java11base.jar"

  jrr_pre1

#    if [ $JRR_ARG = "j11" ]; then
#      JRR_JAVA_PATH=java11
#      JRR_JAVA8_USED=0    
  #echo parsing args ..
  
  for JRR_ARG in "$@"; do
    if [ "${JRR_ARG:0:4}" = "${JRR_CONSTANT_jxmx}" ]; then
      JRR_MEMORY_XMX="-Xmx${JRR_ARG:4}"
    elif [ "${JRR_ARG:0:6}" = "${JRR_CONSTANT_jdebug}" ]; then
       # sample yn1088 means server=y,suspend=n,address=1088
       # sample ny127.0.0.1:1088 means server=n,suspend=y,address=127.0.0.1:1088    
      JRR_JVM_OPTS="$JRR_JVM_OPTS -agentlib:jdwp=transport=dt_socket,server=${JRR_ARG:6:1},suspend=${JRR_ARG:7:1},address=${JRR_ARG:8}"
    elif [ "${JRR_ARG:0:10}" = "${JRR_CONSTANT_jrrParam}" ]; then
      JRR_JVM_OPTS="$JRR_JVM_OPTS ${JRR_ARG:10}"
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jrrrunnerprintcmd}" ]; then
      JRR_PRINT_CMD=1
      JRR_PRINT_JAVA_DEFAULT_ARGS=1
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jrrrunnertest}" ]; then
      JRR_ECHO_TEST="1"      
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jrrrunnerplaysound}" ]; then
      JRR_IS_NEED_PLAYSOUND_ON_FINISH=1      
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jpr}" ]; then
      JRR_JVM_OPTS=" $JRR_PRINT_JAVA_DEFAULT_CMD_ARGS $JRR_JVM_OPTS"
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jrrrunnernofork}" ]; then
      JRR_FORK_CMD=0
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jrrrunnerfork}" ]; then
      JRR_FORK_CMD=1
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_pauseOnExit}" ]; then
      JRR_AFTER_FINISH_PAUSE=1
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_sleep5sOnExit}" ]; then
      JRR_SLEEP_FINISH=1
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jw}" ]; then
      # do nothing
      jrr_donothing
    elif [ "$JRR_ARG" = "${JRR_CONSTANT_jstd}" ]; then
      # do nothing
      jrr_donothing
    else
      jrr_pre_onarg "$JRR_ARG"
      #JRR_FULL_CMD="${JRR_FULL_CMD} ${JRR_ARG}"
    fi
  done
  
  #echo args parsed
  
  if [ "$JRR_JAVA8_USED" = 0 ]; then
     JRR_JVM_OPTS="$JRR_JVM_OPTS $JRR_DEFAULT_JVM11_OPTS"
  fi

  if [ "$JRR_ADD_JMX_UNSAFE" = 1 ]; then
     JRR_JVM_OPTS="$JRR_JVM_OPTS -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.ssl=false -Dcom_sun_management_jmxremote_authenticate=false"
  fi  
  
  jrr_pre2
  
  if [ "$JRR_PRINT_JAVA_DEFAULT_ARGS" = 1 ]; then
     JRR_JVM_OPTS="$JRR_JVM_OPTS $JRR_PRINT_JAVA_DEFAULT_CMD_ARGS"
  fi  
  
  if [ "$JRR_jrrclasspath" != "" ]; then
     JRR_JVM_OPTS="$JRR_JVM_OPTS -Djrrclasspath=${JRR_jrrclasspath}"
  fi  

  if [ $JRR_ECHO_TEST = 1 ]; then
     JRR_PREFIX_CMD="echo $JRR_PREFIX_CMD"
  fi  

  if [ $JRR_PRINT_CURRENT_DIR = 1 ]; then
     echo current dir : $(pwd)
  fi

  if [ $JRR_PRINT_CMD = 1 ]; then
     JRR_JVM_OPTS="$JRR_JVM_OPTS -showversion"
     set -x
  fi

  if [ $JRR_FORK_CMD = 1 ]; then
    $JRR_PREFIX_CMD $JRR_JAVA_PATH $JRR_FIRST_JVM_OPTS $JRR_MEMORY_XMX $JRR_JVM_OPTS $JAVA_OPTS $JRR_ALWAYS_OPTS $GROOVY_OPTS -classpath $JRR_CLASSPATH groovy.ui.GroovyMain ${GR_HOME}/firstdownload/groovyrunner.groovy $JRR_JAVATYPE $JRR_FULL_CMD $JRR_PROGA_ARGS_LAST &
    JRR_FORKED_PROCESS_PID=$!
    if [ $JRR_FORK_DISOWN = 1 ]; then
      disown
    fi
    JRR_ERROR_LEVEL=0
    jrr_post_forked
    
    jrr_f_sleep_and_pause
    
    exit $JRR_ERROR_LEVEL
  else
    $JRR_PREFIX_CMD $JRR_JAVA_PATH $JRR_FIRST_JVM_OPTS $JRR_MEMORY_XMX $JRR_JVM_OPTS $JAVA_OPTS $JRR_ALWAYS_OPTS $GROOVY_OPTS -classpath $JRR_CLASSPATH groovy.ui.GroovyMain ${GR_HOME}/firstdownload/groovyrunner.groovy $JRR_JAVATYPE $JRR_FULL_CMD $JRR_PROGA_ARGS_LAST

    JRR_ERROR_LEVEL=$?

    if [ "$JRR_AFTER_FINISH_SET_PRINT_OFF" = 1 ]; then
       set +x
    fi

    if [ $JRR_IS_NEED_PLAYSOUND_ON_FINISH = 1 ]; then
       jrr_sound "$JRR_IS_NEED_PLAYSOUND_TEXT_ON_FINISH"
    fi

    jrr_post1 "$JRR_ERROR_LEVEL"
    

    if [ $JRR_ERROR_LEVEL != 0 ]; then
       jrr_post_finished_with_error "$JRR_ERROR_LEVEL"
    else
       jrr_post_finished_fine
    fi
    
    jrr_f_sleep_and_pause
    
    exit "$JRR_ERROR_LEVEL"
  fi
}

jrr_f_runjava $@


