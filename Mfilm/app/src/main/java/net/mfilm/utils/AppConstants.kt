/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.mfilm.utils

import android.support.annotation.IntDef

//import java.lang.annotation.Retention
//import java.lang.annotation.RetentionPolicy

/**
 * Created by amitshekhar on 08/01/17.
 */

object AppConstants {

    val STATUS_CODE_SUCCESS = "success"
    val STATUS_CODE_FAILED = "failed"

    val API_STATUS_CODE_LOCAL_ERROR = 0

    val DB_NAME = "mindorks_mvp.db"
    val PREF_NAME = "mindorks_pref"

    const val NULL_INDEX = -1L

    val SEED_DATABASE_OPTIONS = "seed/options.json"
    val SEED_DATABASE_QUESTIONS = "seed/questions.json"

    val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"


    //This is Type Toast
    const val TYPE_TOAST_SUCCESS = 1
    const val TYPE_TOAST_INFOR = 2
    const val TYPE_TOAST_ERROR = -1
    const val TYPE_TOAST_NOMART = 0

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_TOAST_SUCCESS.toLong(), TYPE_TOAST_INFOR.toLong(), TYPE_TOAST_ERROR.toLong(), TYPE_TOAST_NOMART.toLong())
    annotation class TypeToast
    //End Type toast

    // Timer
    const val TIME_DELAY_ON_FINISH: Long = 3000

    // End Timer

    // KEY - SENDATA
    const val EXTRA_DATA = "extra_data"
    const val EXTENSION_EXTRA = "extension"

    //ACTION VIEW VIDEO
    const val ACTION_VIEW = "VIEW"
    const val ACTION_VIEW_LIST = "VIEW_LIST"
}// This utility class is not publicly instantiable
