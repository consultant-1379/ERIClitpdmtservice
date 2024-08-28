#!/bin/bash
# ********************************************************************
# Ericsson LMI                                    SCRIPT
# ********************************************************************
#
# (c) Ericsson LMI 2013 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property
# of Ericsson LMI. The programs may be used
# and/or copied only with the written permission from Ericsson LMI or 
# in accordance with the terms and conditions stipulated
# in the agreement/contract under which the program(s) have been
# supplied.
#
# ********************************************************************
# Name    : <dmt_postinstall.sh>
# Date    : 01/05/2013
# Revision: <R1A>
# Purpose : The script is responsible for carrying out the post install 
#           steps like setting up jboss configuration.
#           Once set up then it starts the dmt service 
#           and restart the apache gracefully. 
#   
# ********************************************************************

DMT_ROOT="/opt/ericsson/dmt_jboss"
INIT_DIR="/etc/init.d"
DMT_LOG="/var/log/dmt"

# copy init script and create create service &  symlinks
cp $DMT_ROOT/script/dmt_service  /etc/init.d/dmt_service
chmod 755 $INIT_DIR/dmt_service
chkconfig --add dmt_service
chkconfig --level 234 dmt_service on

# create dmt log folder
if [ ! -d "$DMT_LOG" ]; then
    mkdir $DMT_LOG
    chmod 755 $DMT_LOG
    chown -R litp_jboss $DMT_LOG
fi


echo "Starting DMT Service this may take approx 30's...."
service dmt_service start 
if [[ $? -ne 0 ]]; then
	echo "Unable to start DMT Service"
	exit 1
fi
