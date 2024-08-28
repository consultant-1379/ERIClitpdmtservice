/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.litp.dmt.test.operators;

import javax.inject.Singleton;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.RestTool;


@Operator(context = Context.REST)
@Singleton
public class DmtRestOperator implements DmtOperator{
	
	@Override
	public RestTool retrieveRestData() {
		return callRestOperation();
	}
  
	
	private RestTool callRestOperation() {
		
		final Host restServer = DataHandler.getHostByName("dmt");
		
		final RestTool restTool = new RestTool(restServer);

		return restTool;
	}
	

}