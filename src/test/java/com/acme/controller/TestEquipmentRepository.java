package com.acme.controller;

import com.acme.model.Equipment;
import com.acme.model.data.EquipmentData;
import com.acme.repository.IEquipmentRepository;
import com.google.common.collect.Maps;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TestEquipmentRepository implements IEquipmentRepository {
    private Map<UUID, Equipment> store = Maps.newHashMap();

    @Override
    public Observable<Equipment> list(Integer limit) {
        return Observable.fromIterable(() -> this.store.values().stream().limit(limit).iterator());
    }

    @Override
    public Maybe<Equipment> get(UUID id) {
        return Optional.ofNullable(this.store.get(id)).map(Maybe::just).orElse(Maybe.empty());
    }

    @Override
    public Single<Equipment> create(EquipmentData data) {
        Equipment equipment = Equipment.fromData(data);

        this.store.put(equipment.getId(), equipment);

        return Single.just(equipment);
    }

    @Override
    public Single<Long> deleteAll() {
        int size = this.store.size();
        this.store.clear();

        return Single.just((long) size);
    }

    @Override
    public Single<Long> generateTestData() {
        throw new UnsupportedOperationException("Generating test data not implemented");
    }
}
