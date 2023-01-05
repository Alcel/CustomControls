package com.example.customcontrols;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Random;
import java.util.function.Consumer;

public class ImageParticle {
    private final Random rnd = new Random();
    private final double velocityFactorX = 1.0;
    private final double velocityFactorY = 1.0;
    private final Image image;
    private double  x;
    private double  y;
    private double  vx;
    private double  vy;
    private double  opacity;
    private double  size;
    private double  width;
    private double  height;
    private boolean active;
    // ******************** Constructor ***********************************
    public ImageParticle(final double width, final double height, final Image image) {
        this.width = width;
        this.height = height;
        this.image = image;
        this.active = true;
        init();
    }
    // ******************** Methods **************************************
    public void init() {
        // Position
        x = rnd.nextDouble() * width;
        y = height + image.getHeight();
        // Random Size
        size = (rnd.nextDouble() * 0.5) + 0.1;
        // Velocity
        vx = ((rnd.nextDouble() * 0.5) - 0.25) * velocityFactorX;
        vy = ((-(rnd.nextDouble() * 2) - 0.5) * size) * velocityFactorY;
        // Opacity
        opacity = (rnd.nextDouble() * 0.6) + 0.4;
    }
    public void adjustToSize(final double width, final double height) {
        this.width = width;
        this.height = height;
        x = rnd.nextDouble() * width;
        y = height + image.getHeight();
    }
    public void update() {
        x += vx;
        y += vy;
        // Respawn particle if needed
        if(y < -image.getHeight()) {
            if (active) { respawn(); }
        }
    }
    public void respawn() {
        x = rnd.nextDouble() * width;
        y = height + image.getHeight();
        opacity = (rnd.nextDouble() * 0.6) + 0.4;
    }

}