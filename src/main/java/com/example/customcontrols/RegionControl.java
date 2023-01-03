package com.example.customcontrols;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.function.Consumer;

public class RegionControl extends Region {
        public enum Type { CLOSE, MINIMIZE, ZOOM }

        private static final double PREFERRED_WIDTH = 12;
        private static final double PREFERRED_HEIGHT = 12;
        private static final double MINIMUM_WIDTH = 12;
        private static final double MINIMUM_HEIGHT = 12;
        private static final double MAXIMUM_WIDTH = 12;
        private static final double MAXIMUM_HEIGHT = 12;
        private static final PseudoClass CLOSE_PSEUDO_CLASS = PseudoClass.getPseudoClass("close");
        private static final PseudoClass MINIMIZE_PSEUDO_CLASS = PseudoClass.getPseudoClass("minimize");
        private static final PseudoClass ZOOM_PSEUDO_CLASS = PseudoClass.getPseudoClass("zoom");
        private static final PseudoClass HOVERED_PSEUDO_CLASS = PseudoClass.getPseudoClass("hovered");
        private static final PseudoClass PRESSED_PSEUDO_CLASS = PseudoClass.getPseudoClass("pressed");
        private BooleanProperty hovered;
        private static String userAgentStyleSheet;
        private ObjectProperty<Type> type;
        private double size;
        private double width;
        private double height;
        private Circle circle;
        private Region symbol;
        private Consumer<MouseEvent> mousePressedConsumer;
        private Consumer<MouseEvent> mouseReleasedConsumer;
    public RegionControl() {
        this(Type.CLOSE);
    }
    public RegionControl(final Type type) {
        this.type = new ObjectPropertyBase<>(type) {
            @Override protected void invalidated() {
                switch(get()) {
                    case CLOSE -> {
                        pseudoClassStateChanged(CLOSE_PSEUDO_CLASS, true);
                        pseudoClassStateChanged(MINIMIZE_PSEUDO_CLASS, false);
                        pseudoClassStateChanged(ZOOM_PSEUDO_CLASS, false);
                    }
                    case MINIMIZE -> {
                        pseudoClassStateChanged(CLOSE_PSEUDO_CLASS, false);
                        pseudoClassStateChanged(MINIMIZE_PSEUDO_CLASS, true);
                        pseudoClassStateChanged(ZOOM_PSEUDO_CLASS, false);
                    }
                    case ZOOM -> {
                        pseudoClassStateChanged(CLOSE_PSEUDO_CLASS, false);
                        pseudoClassStateChanged(MINIMIZE_PSEUDO_CLASS, false);
                        pseudoClassStateChanged(ZOOM_PSEUDO_CLASS, true);
                    }
                }
            }
            @Override public Object getBean() { return RegionControl.this; }
            @Override public String getName() { return "type"; }
        };
        this.hovered = new BooleanPropertyBase() {
            @Override protected void invalidated() { pseudoClassStateChanged(HOVERED_PSEUDO_CLASS, get()); }
            @Override public Object getBean() { return RegionControl.this; }
            @Override public String getName() { return "hovered"; }
        };

        pseudoClassStateChanged(CLOSE_PSEUDO_CLASS, Type.CLOSE == type);
        pseudoClassStateChanged(MINIMIZE_PSEUDO_CLASS, Type.MINIMIZE == type);
        pseudoClassStateChanged(ZOOM_PSEUDO_CLASS, Type.ZOOM == type);

        initGraphics();
        registerListeners();
    }
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
                Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        getStyleClass().add("region-based");

        circle = new Circle();
        circle.getStyleClass().add("circle");
        circle.setStrokeType(StrokeType.INSIDE);

        symbol = new Region();
        symbol.getStyleClass().add("symbol");

        getChildren().setAll(circle, symbol);
    }

    private void registerListeners() {
        //He editado resize
        //widthProperty().addListener(o -> resize());
        //heightProperty().addListener(o -> resize());
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            pseudoClassStateChanged(PRESSED_PSEUDO_CLASS, true);
            if (null == mousePressedConsumer) { return; }
            mousePressedConsumer.accept(e);
        });
        addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            pseudoClassStateChanged(PRESSED_PSEUDO_CLASS, false);
            if (null == mouseReleasedConsumer) { return; }
            mouseReleasedConsumer.accept(e);
        });
    }
}
