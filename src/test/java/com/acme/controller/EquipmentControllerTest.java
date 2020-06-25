package com.acme.controller;

import com.acme.model.EStatus;
import com.acme.model.Equipment;
import com.acme.model.data.EquipmentData;
import com.acme.repository.IEquipmentRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.UUID;

@MicronautTest
public class EquipmentControllerTest {
    @Inject
    IEquipmentRepository equipmentRepository;

    @Inject
    @Client("/")
    private RxHttpClient client;

    @BeforeEach
    void cleanDb() {
        this.equipmentRepository.deleteAll();
    }


    @Test
    void canListEquipment() {
        addEquipment(getRandomEquipmentData());

        ListResponse<Equipment> body = getEquipmentList();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(1, body.getData().size());
    }

    @Test
    void canGetSingleEquipment() {
        Equipment equipment = addEquipment(getRandomEquipmentData());

        UUID id = equipment.getId();

        HttpRequest<Equipment> request = HttpRequest.GET(String.format("/api/equipment/%s", id));
        equipment = this.client.toBlocking().retrieve(request, Argument.of(Equipment.class));

        Assertions.assertNotNull(equipment);
        Assertions.assertEquals(id, equipment.getId());
    }

    @Test
    void equipmentAddressMustBeUnique() {
        String address = "The Address";

        EquipmentData data = getRandomEquipmentData();
        data.setAddress(address);

        // First one should succeed
        addEquipment(data);

        try {
            addEquipment(data);
            Assertions.fail("Trying to add same data twice should fail uniqueness.");
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.CONFLICT, e.getStatus());
        } catch (Exception e) {
            Assertions.fail("Should fail with 409");
        }
    }

    private ListResponse<Equipment> getEquipmentList() {
        HttpRequest<ListResponse<Equipment>> request = HttpRequest.GET("/api/equipment");
        return this.client.toBlocking().retrieve(request, Argument.of(ListResponse.class, Equipment.class));
    }

    private Equipment addEquipment(EquipmentData data) {
        HttpRequest<EquipmentData> request = HttpRequest.POST("/api/equipment", data);

        Equipment equipment = this.client.toBlocking().retrieve(request, Argument.of(Equipment.class));

        return equipment;
    }

    private EquipmentData getRandomEquipmentData() {
        EquipmentData data = new Equipment();

        data.setAddress(String.format("ACME Street %d", (int) Math.floor(Math.random() * 10000)));
        data.setContractStartTime(OffsetDateTime.now().minusDays((int) Math.floor(Math.random() * 1000)));
        data.setContractEndTime(OffsetDateTime.now().plusDays((int) Math.floor(Math.random() * 1000)));
        data.setStatus(EStatus.values()[(int) Math.floor(Math.random() * EStatus.values().length)]);
        return data;
    }
}
