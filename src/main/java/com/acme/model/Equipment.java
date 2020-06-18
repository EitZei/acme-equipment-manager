package com.acme.model;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Introspected
public class Equipment {
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String address;

    @NotNull
    private OffsetDateTime contractStartTime;

    @NotNull
    private OffsetDateTime contractEndTime;

    @NotNull
    private EStatus status;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
