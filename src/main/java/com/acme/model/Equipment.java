package com.acme.model;

import com.acme.model.data.EquipmentData;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * The equipment is the primary concept of the ACME Equipment manager.
 */
@Introspected
@Schema(description = "The equipment that is to be leased to a customer.", allOf = EquipmentData.class)
public class Equipment extends EquipmentData implements IIdentified {
    /**
     * UUID identifier for the equipment.
     */
    @Schema(description = "Unique identifier for the equipment.", example = "e19cabb7-5485-42ed-a1a2-6c44172d34f1")
    @NotNull
    private UUID id;

    public static Equipment fromData(EquipmentData data) {
        Equipment equipment = new Equipment();
        equipment.setId(UUID.randomUUID());
        equipment.setAddress(data.getAddress());
        equipment.setContractStartTime(data.getContractStartTime());
        equipment.setContractEndTime(data.getContractEndTime());
        equipment.setStatus(data.getStatus());
        return equipment;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
