package com.acme.repository;

import com.acme.model.EStatus;
import com.acme.model.Equipment;
import com.google.common.collect.Maps;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.inject.Singleton;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Singleton
public class EquipmentRepository implements IEquipmentRepository {
    public static final String TABLE_NAME = "Equipment";
    public static final String KEY_ID = "Id";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_CONTRACT_START_TIME = "ContractStartTime";
    public static final String KEY_CONTRACT_END_TIME = "ContractEndTime";
    public static final String KEY_STATUS = "Status";

    private DynamoDbAsyncClient dynamoDb;

    public EquipmentRepository() {
        this.dynamoDb = DynamoDbAsyncClient.create();
    }

    @Override
    public Observable<Equipment> list(Integer limit) {
        return Single.fromFuture(
                this.dynamoDb.scan(
                        ScanRequest.builder()
                                .tableName(TABLE_NAME)
                                .limit(limit)
                                .build()
                )
        )
                .flattenAsObservable(queryResponse -> queryResponse.items())
                .map(this::mapToEquipment);
    }

    @Override
    public Maybe<Equipment> get(UUID id) {
        return Single.fromFuture(
                this.dynamoDb
                        .getItem(GetItemRequest.builder()
                                .tableName(TABLE_NAME)
                                .key(Collections.singletonMap("Id", AttributeValue.builder().s(id.toString()).build()))
                                .build()
                        )
        )
                .flatMapMaybe(r -> r.hasItem() ? Maybe.just(r.item()) : Maybe.empty())
                .map(this::mapToEquipment);
    }

    @Override
    public Single<Equipment> create(Equipment equipment) {
        return Single.just(equipment)
                .map(this::equipmentToMap)
                .flatMap(item ->
                        Single.fromFuture(
                                this.dynamoDb.putItem(PutItemRequest.builder()
                                        .tableName(TABLE_NAME)
                                        .item(item)
                                        .build()
                                )
                        )
                )
                .map($ -> equipment);
    }

    @Override
    public Single<Long> deleteAll() {
        return list(Integer.MAX_VALUE)
                .flatMapSingle(item ->
                        Single.fromFuture(
                                this.dynamoDb.deleteItem(
                                        DeleteItemRequest.builder()
                                                .key(Collections.singletonMap("Id", AttributeValue.builder().s(item.getId().toString()).build()))
                                                .tableName(TABLE_NAME)
                                                .build()
                                )
                        )
                )
                .count();
    }

    @Override
    public Single<Long> generateTestData() {
        return Observable.range(0, 100)
                .map($ -> createRandomEquipment())
                .flatMapSingle(this::create)
                .count();
    }

    private Equipment createRandomEquipment() {
        Equipment equipment = new Equipment();

        equipment.setId(UUID.randomUUID());
        equipment.setAddress(String.format("ACME Street %d", (int) Math.floor(Math.random() * 10000)));
        equipment.setContractStartTime(OffsetDateTime.now().minusDays((int) Math.floor(Math.random() * 1000)));
        equipment.setContractEndTime(OffsetDateTime.now().plusDays((int) Math.floor(Math.random() * 1000)));
        equipment.setStatus(EStatus.values()[(int) Math.floor(Math.random() * EStatus.values().length)]);

        return equipment;
    }


    private Equipment mapToEquipment(Map<String, AttributeValue> values) {
        Equipment equipment = new Equipment();
        equipment.setId(UUID.fromString(values.get(KEY_ID).s()));
        equipment.setAddress(values.get(KEY_ADDRESS).s());
        equipment.setContractStartTime(timestampToUTCDateTime(Long.parseLong(values.get(KEY_CONTRACT_START_TIME).n())));
        equipment.setContractEndTime(timestampToUTCDateTime(Long.parseLong(values.get(KEY_CONTRACT_END_TIME).n())));
        equipment.setStatus(EStatus.valueOf(values.get(KEY_STATUS).s()));
        return equipment;
    }

    private Map<String, AttributeValue> equipmentToMap(Equipment equipment) {
        Map<String, AttributeValue> map = Maps.newHashMap();

        map.put(KEY_ID, AttributeValue.builder().s(equipment.getId().toString()).build());
        map.put(KEY_ADDRESS, AttributeValue.builder().s(equipment.getAddress()).build());
        map.put(KEY_CONTRACT_START_TIME, AttributeValue.builder().n(dateTimeToTimestamp(equipment.getContractStartTime()).toString()).build());
        map.put(KEY_CONTRACT_END_TIME, AttributeValue.builder().n(dateTimeToTimestamp(equipment.getContractEndTime()).toString()).build());
        map.put(KEY_STATUS, AttributeValue.builder().s(equipment.getStatus().toString()).build());

        return map;
    }

    private OffsetDateTime timestampToUTCDateTime(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
    }

    private Long dateTimeToTimestamp(OffsetDateTime dateTime) {
        return Long.valueOf(dateTime.toInstant().toEpochMilli());
    }
}
