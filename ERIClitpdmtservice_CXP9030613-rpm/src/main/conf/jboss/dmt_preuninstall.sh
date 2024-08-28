#!/bin/bash

if [ $1 -eq  0 ]; then

	echo "Stopping dmt_service...."
	service dmt_service stop 2>/dev/null
	RETVAL=$?
	if [ $RETVAL -ne "0" ]; then
	   echo "killing leftover processes...."
	   PID=`ps aux | grep -v grep | grep dmt | grep -v sh | awk '{ print $2 }'`
	 
	   if [[ $PID -ne "0" ]]; then 
		  echo "pid to kill $PID...."
		  kill -9 $PID
		  RETVAL=$?
		  sleep 5
		  echo "kill returns $RETVAL...."
		  if [[ $REVAL -eq "0" ]]; then
			 echo "Remaining processes stopped...."
		  else
			 echo "Could not stop remaining processes...."
		  fi
	   fi
	fi

fi