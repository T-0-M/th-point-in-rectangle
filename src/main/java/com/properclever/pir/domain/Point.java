package com.properclever.pir.domain;

public record Point(double x, double y) {
    public Point(int x, int y) {
        this((double) x, (double) y);
    }
}