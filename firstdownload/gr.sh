#!/bin/bash
# set -x
export GR_HOME=`dirname "$(readlink -f "$0")"`/..

copy_file(){
    export filename=$1
	if [ ! -f ${GR_HOME}/libs/copy/${filename} ] ; then
		cp ${GR_HOME}/libs/origin/${filename}  ${GR_HOME}/libs/copy/${filename}
	fi
}

if [ ! -d ${GR_HOME}/libs/copy ] ; then
  mkdir ${GR_HOME}/libs/copy
fi

if [ ! -f ${GR_HOME}/firstdownload/javarunner.sh ] ; then
    cp ${GR_HOME}/firstdownload/javarunner_sample.sh  ${GR_HOME}/firstdownload/javarunner.sh
    chmod u+rwx ${GR_HOME}/firstdownload/javarunner.sh
fi


copy_file jremoterun.jar
copy_file groovy_custom.jar
copy_file groovy.jar


${GR_HOME}/firstdownload/javarunner.sh  $JAVA_OPTS $GROOVY_OPTS -javaagent:${GR_HOME}/libs/copy/jremoterun.jar -classpath ${GR_HOME}/libs/copy/groovy_custom.jar:${GR_HOME}/libs/copy/groovy.jar groovy.ui.GroovyMain ${GR_HOME}/firstdownload/groovyrunner.groovy 2 $*

