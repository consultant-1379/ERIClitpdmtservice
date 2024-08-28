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
# Name    : <dmt_postuninstall.sh>
# Date    : 01/05/2013
# Revision: <R1A>
# Purpose : Post uninstall clean up script for DMT Service.
#           Removes directory structure as RPM handles file. 
#			Removes DMT init script and deletes symlinks using chkconfig --del
#   
# ********************************************************************

DMT_ROOT_DIR="/opt/ericsson/dmt_jboss"
DMT_SERV="dmt_service"

if [ $1 -eq 0 ]; then 

	echo "Removing symlinks...."
	# Remove symlinks
	/sbin/chkconfig --del $DMT_SERV 2>/dev/null

	echo "Removing leftover files and cleaning up......"
	# Delete leftover dmt_jbosss directory
	echo "Removing DMT root dir...." 
	rm -fr $DMT_ROOT_DIR 2>/dev/null

	echo "Removing DMT init script...."
	# Remove init script
	rm /etc/init.d/$DMT_SERV 2>/dev/null

fi