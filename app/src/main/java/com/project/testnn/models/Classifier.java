package com.project.testnn.models;


public interface Classifier {
    String name();

    Classification recognize(final float[] pixels);
}
