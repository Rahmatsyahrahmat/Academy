package com.rahmatsyah.academy.data.source.local.entity;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseWithModule {

    @Embedded
    public CourseEntity courseEntity;

    @Relation(parentColumn = "courseId",entityColumn = "courseId")
    public List<ModuleEntity> moduleEntities;

}
