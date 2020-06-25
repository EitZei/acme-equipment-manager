package com.acme.controller;

import com.acme.model.Equipment;
import com.acme.model.data.EquipmentData;
import com.acme.repository.IEquipmentRepository;
import com.acme.repository.NonUniqueItemException;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;
import java.util.UUID;

@Controller("/api/equipment")
public class EquipmentController {
    private IEquipmentRepository equipmentRepository;

    public EquipmentController(IEquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * List equipment.
     *
     * @param limit Limit result size to specified count.
     * @return List of equipment.
     */
    @Get("/{?limit}")
    @Schema(name = "EquipmentList")
    public Single<ListResponse<Equipment>> list(@QueryValue @Positive Optional<Integer> limit) {
        // There is a bug in Micronaut which causes it to not validate wrapped parameters
        // https://github.com/micronaut-projects/micronaut-core/issues/3511
        if (limit.isPresent() && limit.get() < 1) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Limit should be greater or equal to 1");
        }

        return this.equipmentRepository
                .list(limit.orElse(100))
                .toList()
                .map(ListResponse::new);
    }

    /**
     * Get equipment.
     *
     * @param id Unique identifier of the equipment
     * @return Equipment data.
     */
    @Get("/{id}")
    public Maybe<Equipment> get(UUID id) {
        return this.equipmentRepository.get(id);
    }

    /**
     * Create an equipment.
     *
     * @param equipment equipment to be created.
     * @return Created equipment.
     */
    @Post
    public Single<Equipment> create(@Valid @Body EquipmentData equipment) {
        if (!equipment.getContractStartTime().isBefore(equipment.getContractEndTime())) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Contract start time must be before end time.");
        }

        return this.equipmentRepository.create(equipment).onErrorResumeNext(error -> {
            if (error instanceof NonUniqueItemException) {
                return Single.error(new HttpStatusException(HttpStatus.CONFLICT, error.getMessage()));
            } else {
                return Single.error(new RuntimeException(error));
            }
        });
    }

    /**
     * Delete all equipment.
     *
     * @return Number of equipment deleted.
     */
    @Delete
    public Single<Long> deleteAll() {
        return this.equipmentRepository.deleteAll();
    }


    /**
     * Generate test data.
     *
     * @return Number of equipment created.
     */
    @Post("/generate")
    public Single<Long> generateTestData() {
        return this.equipmentRepository.generateTestData();
    }
}
