/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 *
 * AbstractShellTool.java Create on 2013-7-11 上午10:13:29
 */
package com.github.runner.support;

import javax.management.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.runner.ServerStartup;
import com.github.runner.util.JMXClient;
import com.github.runner.util.SysProperties;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
abstract public class AbstractShellTool extends ShellTool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartup.class);
	
	@Override
    public Object doMain(String methodName) throws Exception {
        String host = SysProperties.getString("host", "127.0.0.1");
        int port = SysProperties.getInt("port", 4001);

        JMXClient jmxClient = JMXClient.getJMXClient(host, port);
        LOGGER.debug("connected to " + jmxClient.getAddressAsString());
        ObjectInstance brokerInstance = jmxClient.queryMBeanForOne(this.getBrokerName());
        
        Object result = null;
        if (brokerInstance != null) {
        	result = jmxClient.invoke(brokerInstance.getObjectName(), methodName, new Object[0], new String[0]);
            jmxClient.close();
            LOGGER.debug("invoke " + brokerInstance.getClassName() + "#" + methodName + " success");
        }
        else {
        	LOGGER.warn("没有找到 " + this.getBrokerName());
        }
        return result;
    }

}
