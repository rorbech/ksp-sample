package org.example.kspsample.consumer

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class ModelObject: RealmObject {
    @PrimaryKey
    var id: String = "Realm"
}
