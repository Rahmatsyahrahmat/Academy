package com.rahmatsyah.academy.ui.bookmark;

import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;

interface BookmarkFragmentCallback {
    void onShareClick(CourseEntity courseEntity);
}
