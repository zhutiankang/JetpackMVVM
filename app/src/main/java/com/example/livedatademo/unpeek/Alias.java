package com.example.livedatademo.unpeek;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

/**
 * Alias Event for {@link LiveDataManager}.
 *
 * @author zhutiankang
 */
@StringDef({Alias.EVENT_EXAM, Alias.EVENT_HISTORY})
@Retention(RetentionPolicy.SOURCE)
public @interface Alias {

    String EVENT_EXAM = "eventExam";

    String EVENT_HISTORY = "eventHistory";
}