package com.example.nurbekkabylbai.sch.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Nurbek Kabylbay on 27.01.2018.
 */
@Entity
class Class() : Serializable {

    @PrimaryKey
    var classId: Int = 0

    @SerializedName("teacherId")
    @Expose
    var teacherId: String? = null

    @SerializedName("hours")
    @Expose
    var hours: Int = 0

    @SerializedName("groupId")
    @Expose
    var groupId: String? = null

    @SerializedName("isEquiped")
    @Expose
    var isEquipped: Boolean = false

    @SerializedName("courseId")
    @Expose
    var courseId: String? = null

    @SerializedName("startHour")
    @Expose
    lateinit var startHour: String

    @SerializedName("roomId")
    @Expose
    lateinit var roomId: String

    constructor(classId: Int, startHour: String) : this() {
        this.classId = classId
        this.startHour = startHour
    }
}