package com.acme.repository;

import com.acme.model.Equipment;
import com.acme.model.data.EquipmentData;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.UUID;

public interface IEquipmentRepository {
    public Observable<Equipment> list(Integer limit);

    public Maybe<Equipment> get(UUID id);

    public Single<Equipment> create(EquipmentData equipment);

    public Single<Long> deleteAll();

    public Single<Long> generateTestData();
}
