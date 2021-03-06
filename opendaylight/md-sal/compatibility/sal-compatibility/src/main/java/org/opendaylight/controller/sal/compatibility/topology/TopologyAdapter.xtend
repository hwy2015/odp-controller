package org.opendaylight.controller.sal.compatibility.topology

import org.opendaylight.controller.sal.binding.api.data.DataProviderService
import org.opendaylight.controller.sal.topology.IPluginInTopologyService
import org.opendaylight.controller.sal.topology.IPluginOutTopologyService
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev130712.NetworkTopology
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev130712.TopologyId
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev130712.network.topology.Topology
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev130712.network.topology.TopologyKey
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier

import static extension org.opendaylight.controller.sal.compatibility.topology.TopologyMapping.*
import java.util.List
import org.opendaylight.controller.sal.topology.TopoEdgeUpdate
import java.util.Collections

class TopologyAdapter implements IPluginInTopologyService {
    
    @Property
    DataProviderService dataService;
    
    @Property
    IPluginOutTopologyService topologyPublisher;
    
    override sollicitRefresh() {
        val path = InstanceIdentifier.builder(NetworkTopology).child(Topology,new TopologyKey(new TopologyId("flow:1"))).toInstance;
        val topology =  (dataService.readOperationalData(path) as Topology);
        topologyPublisher.edgeUpdate(topology.toADEdgeUpdates)
    }
    
}