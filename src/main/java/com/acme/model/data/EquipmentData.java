package com.acme.model.data;

import com.acme.model.EStatus;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * The equipment is the primary concept of the ACME Equipment manager.
 */
@Introspected
@Schema(description = "The equipment that is to be leased to a customer.")
public class EquipmentData {
    /**
     * Address of the equipment.
     */
    @Schema(description = "Address of the equipment", example = "ACME Street 10")
    @NotBlank
    private String address;

    /**
     * Start time of the equipment leasing contract.
     */
    @Schema(description = "Start time of the equipment leasing contract.", format = "date-time", example = "2017-07-21T17:32:28Z")
    @NotNull
    private OffsetDateTime contractStartTime;

    /**
     * End time of the equipment leasing contract.
     */
    @Schema(description = "End time of the equipment leasing contract.", format = "date-time", example = "2017-07-21T17:32:28Z")
    @NotNull
    private OffsetDateTime contractEndTime;

    /**
     * Running status of the equipment.
     */
    @Schema(description = "Running status of the equipment.")
    @NotNull
    private EStatus status;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OffsetDateTime getContractStartTime() {
        return this.contractStartTime;
    }

    public void setContractStartTime(OffsetDateTime contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public OffsetDateTime getContractEndTime() {
        return this.contractEndTime;
    }

    public void setContractEndTime(OffsetDateTime contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public EStatus getStatus() {
        return this.status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }
}
