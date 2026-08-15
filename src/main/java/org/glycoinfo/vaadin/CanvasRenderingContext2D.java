package org.glycoinfo.vaadin;

import java.io.Serializable;

import com.vaadin.flow.dom.Element;

/**
 * Base class that renders shapes and images on a canvas using the 2D context.
 * It is a Java wrapper for the <a href="https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D">same client-side API</a>.
 *
 * <p>Originally provided by the {@code org.vaadin.pekkam:canvas-java} add-on. It was absorbed into this
 * add-on so the only external dependency could be removed. The only behavioural change from the original
 * is {@link #runScript(String)}: it now uses {@link Element#executeJs(String, Serializable...)} instead of
 * the {@code Page.executeJavaScript} API that was removed in later Vaadin versions. The canvas element is
 * passed as {@code $0} so the generated scripts keep their exact meaning.</p>
 */
public class CanvasRenderingContext2D {
    /**
     * Canvas that owns this rendering context.
     */
    private Canvas canvas;

    /**
     * Creates a rendering context bound to the given canvas.
     * @param canvas Canvas that owns this context.
     */
    protected CanvasRenderingContext2D(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setFillStyle(String fillStyle) {
        setProperty("fillStyle", fillStyle);
    }

    public void setStrokeStyle(String strokeStyle) {
        setProperty("strokeStyle", strokeStyle);
    }

    public void setLineWidth(double lineWidth) {
        setProperty("lineWidth", lineWidth);
    }

    public void setFont(String font) {
        setProperty("font", font);
    }

    public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean antiClockwise) {
        callJsMethod("arc", x, y, radius, startAngle, endAngle, antiClockwise);
    }

    public void beginPath() {
        callJsMethod("beginPath");
    }

    public void clearRect(double x, double y, double width, double height) {
        callJsMethod("clearRect", x, y, width, height);
    }

    public void closePath() {
        callJsMethod("closePath");
    }

    public void drawImage(String src, double x, double y) {
        runScript(String.format(
            "var img = new Image();img.onload = function () {$0.getContext('2d').drawImage(img, %s, %s);};img.src='%s';",
            x, y, src));
    }

    public void drawImage(String src, double x, double y, double width, double height) {
        runScript(String.format(
            "var img = new Image();img.onload = function () {$0.getContext('2d').drawImage(img, %s, %s, %s, %s);};img.src='%s';",
            x, y, width, height, src));
    }

    public void fill() {
        callJsMethod("fill");
    }

    public void fillRect(double x, double y, double width, double height) {
        callJsMethod("fillRect", x, y, width, height);
    }

    public void fillText(String text, double x, double y) {
        callJsMethod("fillText", text, x, y);
    }

    public void lineTo(double x, double y) {
        callJsMethod("lineTo", x, y);
    }

    public void moveTo(double x, double y) {
        callJsMethod("moveTo", x, y);
    }

    public void rect(double x, double y, double width, double height) {
        callJsMethod("rect", x, y, width, height);
    }

    public void restore() {
        callJsMethod("restore");
    }

    public void rotate(double angle) {
        callJsMethod("rotate", angle);
    }

    public void save() {
        callJsMethod("save");
    }

    public void scale(double x, double y) {
        callJsMethod("scale", x, y);
    }

    public void stroke() {
        callJsMethod("stroke");
    }

    public void strokeRect(double x, double y, double width, double height) {
        callJsMethod("strokeRect", x, y, width, height);
    }

    public void strokeText(String text, double x, double y) {
        callJsMethod("strokeText", text, x, y);
    }

    public void translate(double x, double y) {
        callJsMethod("translate", x, y);
    }

    /**
     * Sets the value of a property on the 2D context.
     * @param propertyName Property name of the 2D context.
     * @param value Value to assign to the property.
     */
    protected void setProperty(String propertyName, Serializable value) {
        runScript(String.format("$0.getContext('2d').%s='%s'", propertyName, value));
    }

    /**
     * Calls a method on the 2D context.
     * @param methodName Method name to call on the 2D context.
     * @param parameters Parameters to pass to the method.
     */
    protected void callJsMethod(String methodName, Serializable... parameters) {
        canvas.getElement().callJsFunction("getContext('2d')." + methodName, parameters);
    }

    /**
     * Runs a raw script against the canvas. The canvas element is passed as {@code $0}.
     * @param script Script to run.
     */
    private void runScript(String script) {
        Element element = canvas.getElement();
        element.executeJs(script, element);
    }
}
