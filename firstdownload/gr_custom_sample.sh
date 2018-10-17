#!/bin/bash
# set -x

jrr_donothing(){
  # aaa
  #echo test123 > /dev/null
  test 1
}

jrr_pre0(){
  jrr_donothing
}


jrr_pre1(){
  jrr_donothing
  command -v java
  #date
}


jrr_pre_onarg(){
  if [ "$JRR_ARG" = "jeeeee" ]; then
      jrr_donothing  
    else
      JRR_FULL_CMD="${JRR_FULL_CMD} ${JRR_ARG}"
    fi
}


jrr_pre2(){
  jrr_donothing
}


jrr_post1(){
  jrr_donothing
  #jrr_sound finished
}


jrr_post_finished_fine(){
  echo finished fine
  jrr_donothing
}


jrr_post_finished_with_error(){
  jrr_donothing
  echo "finished with error $1"
}


jrr_post_forked(){
  echo "process started pid = $JRR_FORKED_PROCESS_PID"
}

