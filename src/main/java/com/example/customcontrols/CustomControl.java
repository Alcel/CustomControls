package com.example.customcontrols;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.css.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

import java.util.List;

public class CustomControl extends Control {


    public boolean isOn() {
        return true;
    }

    public Observable onProperty() {
        return null;
    }

    public enum SkinType { LED, SWITCH }

    private static final StyleablePropertyFactory<CustomControl> FACTORY = new StyleablePropertyFactory<>(Control.getClassCssMetaData());

    // CSS pseudo classes
    private static final PseudoClass ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");
    private BooleanProperty state;

    // CSS Styleable property
    private static final CssMetaData<CustomControl, Color> COLOR = FACTORY.createColorCssMetaData("-color", s -> s.color, Color.RED, false);
    private final StyleableProperty<Color> color;

    private static String defaultUserAgentStyleSheet;
    private static String switchUserAgentStyleSheet;

    // Properties
    private SkinType skinType;

    // ******************** Constructors **************************************
    public CustomControl() {
        this(SkinType.LED);
    }
    public CustomControl(final SkinType skinType) {
        getStyleClass().add("custom-control");
        this.skinType = skinType;
        this.state = new BooleanPropertyBase(false) {
            @Override protected void invalidated() { pseudoClassStateChanged(ON_PSEUDO_CLASS, get()); }
            @Override public Object getBean() { return this; }
            @Override public String getName() { return "state"; }
        };
        this.color    = new SimpleStyleableObjectProperty<>(COLOR, this, "color");
    }

    // ******************** Methods *******************************************
    public boolean getState() { return state.get(); }
    public void setState(final boolean state) { this.state.set(state); }
    public BooleanProperty stateProperty() { return state; }

    // ******************** CSS Styleable Properties **************************
    public Color getColor() { return color.getValue(); }
    public void setColor(final Color color) { this.color.setValue(color); }
    public ObjectProperty<Color> colorProperty() { return (ObjectProperty<Color>) color; }

    // ******************** Style related *************************************
    @Override protected Skin createDefaultSkin() {
        switch(skinType) {
            case SWITCH: return new SwitchSkin(CustomControl.this);
            case LED   :
            default    : return new LedSkin(CustomControl.this);
        }
    }

    @Override public String getUserAgentStylesheet() {
        switch(skinType) {
            case SWITCH:
                if (null == switchUserAgentStyleSheet) { switchUserAgentStyleSheet = CustomControl.class.getResource("switch.css").toExternalForm(); }
                return switchUserAgentStyleSheet;
            case LED   :
            default    :
                if (null == defaultUserAgentStyleSheet) { defaultUserAgentStyleSheet = CustomControl.class.getResource("custom-control.css").toExternalForm(); }
                return defaultUserAgentStyleSheet;
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() { return FACTORY.getCssMetaData(); }
    @Override public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() { return FACTORY.getCssMetaData(); }
}