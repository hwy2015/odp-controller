package org.opendaylight.controller.frm.meter

import org.opendaylight.controller.frm.AbstractTransaction
import org.opendaylight.controller.md.sal.common.api.data.DataModification
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.AddMeterInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.RemoveMeterInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.SalMeterService
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.UpdateMeterInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.meter.update.OriginalMeterBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.meter.update.UpdatedMeterBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.Meter
import org.opendaylight.yangtools.yang.binding.DataObject
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterRef

class MeterTransaction extends AbstractTransaction {
    
    @Property
    val SalMeterService salMeterService;
    
    new(DataModification<InstanceIdentifier<? extends DataObject>, DataObject> modification,SalMeterService salMeterService) {
        super(modification)
        _salMeterService = salMeterService;
    }
    
    override remove(InstanceIdentifier<?> instanceId, DataObject obj) {
        if(obj instanceof Meter) {
            val meter = (obj as Meter)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new RemoveMeterInputBuilder(meter);
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setMeterRef(new MeterRef(instanceId));
            _salMeterService.removeMeter(builder.build());            
        }
    }
    
    override update(InstanceIdentifier<?> instanceId, DataObject originalObj, DataObject updatedObj) {
        if(originalObj instanceof Meter && updatedObj instanceof Meter) {
            val originalMeter = (originalObj as Meter)
            val updatedMeter = (updatedObj as Meter)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new UpdateMeterInputBuilder();
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setMeterRef(new MeterRef(instanceId));
            val ufb = new UpdatedMeterBuilder(updatedMeter);
            builder.setUpdatedMeter((ufb.build()));
            val ofb = new OriginalMeterBuilder(originalMeter);
            builder.setOriginalMeter(ofb.build());      
            _salMeterService.updateMeter(builder.build());
           
        }
    }
    
    override add(InstanceIdentifier<?> instanceId, DataObject obj) {
        if(obj instanceof Meter) {
            val meter = (obj as Meter)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new AddMeterInputBuilder(meter);
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setMeterRef(new MeterRef(instanceId));
            _salMeterService.addMeter(builder.build());            
        }
    }
    
    override validate() throws IllegalStateException {
        MeterTransactionValidator.validate(this)
    }  
}