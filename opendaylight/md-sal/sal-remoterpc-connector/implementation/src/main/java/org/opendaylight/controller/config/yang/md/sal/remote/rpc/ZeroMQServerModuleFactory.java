/**
* Generated file

* Generated from: yang module name: odl-sal-dom-rpc-remote-cfg  yang module local name: remote-zeromq-rpc-server
* Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
* Generated at: Thu Dec 05 14:25:21 CET 2013
*
* Do not modify this file unless it is present under src/main directory
*/
package org.opendaylight.controller.config.yang.md.sal.remote.rpc;

import org.opendaylight.controller.config.api.DependencyResolver;
import org.opendaylight.controller.config.api.DynamicMBeanWithInstance;
import org.opendaylight.controller.config.spi.Module;
import org.osgi.framework.BundleContext;

/**
*
*/
public class ZeroMQServerModuleFactory extends org.opendaylight.controller.config.yang.md.sal.remote.rpc.AbstractZeroMQServerModuleFactory
{

    @Override
    public Module createModule(String instanceName, DependencyResolver dependencyResolver, BundleContext bundleContext) {
        ZeroMQServerModule module = (ZeroMQServerModule) super.createModule(instanceName, dependencyResolver, bundleContext);
        module.setBundleContext(bundleContext);
        return module;
    }
    
    @Override
    public Module createModule(String instanceName, DependencyResolver dependencyResolver,
            DynamicMBeanWithInstance old, BundleContext bundleContext) throws Exception {
        ZeroMQServerModule module = (ZeroMQServerModule) super.createModule(instanceName, dependencyResolver, old,bundleContext);
        module.setBundleContext(bundleContext);
        return module;
    }
}
