package com.acme.controller;

import com.acme.model.Equipment;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;
import java.util.UUID;

@MicronautTest
public class EquipmentControllerTest {
    @Inject
    @Client("/")
    private RxHttpClient client;

    @Test
    void canListEquipment() {
        ListResponse<Equipment> body = getEquipmentList();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(2, body.getData().size());
    }

    @Test
    void canGetSingleEquipment() {
        ListResponse<Equipment> list = getEquipmentList();

        UUID id = list.getData().get(0).getId();

        HttpRequest<Equipment> request = HttpRequest.GET(String.format("/api/equipment/%s", list.getData().get(0).getId()));
        Equipment equipment = client.toBlocking().retrieve(request, Argument.of(Equipment.class));

        Assertions.assertNotNull(equipment);
        Assertions.assertEquals(id, equipment.getId());
    }

    private ListResponse<Equipment> getEquipmentList() {
        HttpRequest<ListResponse<Equipment>> request = HttpRequest.GET("/api/equipment");
        return client.toBlocking().retrieve(request, Argument.of(ListResponse.class, Equipment.class));
    }
}
