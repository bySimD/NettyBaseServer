package com.simd.pvp.protocol.base;

import com.simd.pvp.repository.UserSessionManager;
import com.simd.pvp.service.session.UserSessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;


@Controller
public abstract class AbstractProtocolController
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected ProtocolService protocolService;

	@Autowired
	protected UserSessionManager wsUserSessionManager;

	@PostConstruct
	protected void postConstruct() {
		protocolService.addProtocol(this.getClass().getSimpleName(), this);
	}

	public void service(UserSessionData userSessionData)
	{

		serviceInternal(userSessionData);

	}
	
	protected abstract void serviceInternal(UserSessionData userSessionData);
}
