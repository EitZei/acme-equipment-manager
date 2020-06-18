package com.acme.controller;

import com.acme.model.Equipment;
import com.acme.repository.IEquipmentRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
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
     * @param offset Start returning values starting from specified index.
     * @param limit  Limit result size to specified count.
     * @return
     */
    @Get("/{?limit,offset}")
    public Single<ListResponse<Equipment>> list(@QueryValue @PositiveOrZero Optional<Integer> offset, @QueryValue @PositiveOrZero Optional<Integer> limit) {
        // There is a bug in Micronaut which causes it to not validate wrapped parameters
        // https://github.com/micronaut-projects/micronaut-core/issues/3511
        if (offset.isPresent() && offset.get() < 0) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Offset should be greater or equal to 0");
        }
        if (limit.isPresent() && limit.get() < 0) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Limit should be greater or equal to 0");
        }

        return this.equipmentRepository
                .list(limit.orElse(100))
                .toList()
                .map(ListResponse::new);
    }

    @Get("/{id}")
    public Maybe<Equipment> get(UUID id) {
        return this.equipmentRepository.get(id);
    }

    @Post
    public Single<Equipment> create(@Valid @Body Equipment equipment) {
        return this.equipmentRepository.create(equipment);
    }

    @Delete
    public Single<Long> deleteAll() {
        return this.equipmentRepository.deleteAll();
    }


    @Post("/generate")
    public Single<Long> genereteTestData() {
        return this.equipmentRepository.generateTestData();
    }
}
