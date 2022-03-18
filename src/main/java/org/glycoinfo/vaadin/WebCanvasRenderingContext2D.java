package org.glycoinfo.vaadin;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.CanvasRenderingContext2D;

/**
 * Extended class of base class that renders shapes and images on a canvas using the context.
 * This is a Java wrapper for the <a href="https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D">same client-side API</a>.
 */
public class WebCanvasRenderingContext2D extends CanvasRenderingContext2D {
    /**
     * History of JavaScript method commands (e.g. strokeStyle, fillRect etc) to redraw executed drawing processes so far.
     */
    private List<JSMethodCommand> jsMethodCommands = new ArrayList<JSMethodCommand>();

    /**
     * Passes Canvas instance to super class.
     * @param canvas canvas instance.
     */
    protected WebCanvasRenderingContext2D(Canvas canvas) {
        super(canvas);
    }

    /**
     * Sets stroke style from Color instance.
     * @param color Color instance.
     */
    public void setStrokeStyle(Color color) {
        setStrokeStyle(convertToHexColorCodeFromColor(color));
    }

    /**
     * Sets fill style from Color instance.
     * @param color Color instance.
     */
    public void setFillStyle(Color color) {
        setFillStyle(convertToHexColorCodeFromColor(color));
    }

    /**
     * Converts to a hex color code from Color instance.
     * @param color Color instance.
     * @return Returns a hex color code.
     */
    private String convertToHexColorCodeFromColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    /**
     * Sets alpha to rectangle.
     * @param alpha Alpha value.
     */
    public void setGlobalAlpha(double alpha) {
        setProperty("globalAlpha", alpha);
    }

    /**
     * Sets text align.
     * @param textAlign String that specifies the position of the text.
     */
    public void setTextAlign(String textAlign) {
        setProperty("textAlign", textAlign);
    }

    /**
     * Sets text baseline.
     * @param textBaseline String that specifies the position of the text baseline.
     */
    public void setTextBaseline(String textBaseline) {
        setProperty("textBaseline", textBaseline);
    }

    /**
     * Draws text on the specified point.
     * @param text Text to draw.
     * @param x X coordinate of where to draw the text.
     * @param y Y coordinate of where to draw the text.
     * @param maxWidth Text of maximum allowable width.
     */
    public void fillText(String text, double x, double y, double maxWidth) {
        callJsMethod("fillText", text, x, y, maxWidth);
    }

    /**
     * Calculates text width.
     * @param text Text to calculate width.
     * @param scale Scale of text.
     * @return
     */
    public double calculateTextWidth(String text, double scale) {
        double textWidth = 0.0;
        for (int i = 0; i < text.length(); i++) {
            // TODO textWidth += font.get(text.charAt(i)).getWidth();
        }
        return textWidth * scale;
    }

    /**
     * Draws bezier curve.
     * @param cp1x X coordinate of the first control point.
     * @param cp1y Y coordinate of the first control point.
     * @param cp2x X coordinate of the second control point.
     * @param cp2y Y coordinate of the second control point.
     * @param x X coordinate of where to draw the line.
     * @param y Y coordinate of where to draw the line.
     */
    public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
        callJsMethod("bezierCurveTo", cp1x, cp1y, cp2x, cp2y, x ,y);
    }

    /**
     * Redraws shapes on the canvas using history of executed drawing processes.
     */
    public void redraw() {
        for (JSMethodCommand jsMethodCommand : jsMethodCommands) {
            jsMethodCommand.execute();
        }
    }

    /**
     * Clears the canvas at the specified shape size.
     * @param x X coordinate of where to draw the shape.
     * @param y Y coordinate of where to draw the shape.
     * @param width Width of the shape.
     * @param height Height of the shape.
     */
    public void clear(double x, double y, double width, double height) {
        clearRect(x, y, width, height);
        clearJSMethodCommands();
    }

    /**
     * Sets value of property name to context2D.
     * @param propertyName Property name of context2D.
     * @param value Value to substitute to a property.
     */
    @Override
    protected void setProperty(String propertyName, Serializable value) {
        runJSMethodCommand(new JSMethodCommand() {
            @Override
            public void execute() {
                setPropertyOfParent(propertyName, value);
            }
        });
    }

    /**
     * Sets value of property name to context2D using parent method.
     * @param propertyName Property name of context2D.
     * @param value Value to substitute to a property.
     */
    private void setPropertyOfParent(String propertyName, Serializable value) {
        super.setProperty(propertyName, value);
    }

    /**
     * Calls JavaScript method.
     * @param methodName Method name to call on JavaScript.
     * @param parameters Parameters to be passed the specified method.
     */
    @Override
    protected void callJsMethod(String methodName, Serializable... parameters) {
        runJSMethodCommand(new JSMethodCommand() {
            @Override
            public void execute() {
                callJsMethodOfParent(methodName, parameters);
            }
        });
    }

    /**
     * Calls JavaScript method using parent method.
     * @param methodName Method name to call on JavaScript.
     * @param parameters Parameters to be passed the specified method.
     */
    private void callJsMethodOfParent(String methodName, Serializable... parameters) {
        super.callJsMethod(methodName, parameters);
    }

    /**
     * Runs specified JavaScript method command.
     * @param jsMethodCommand JavaScript method command.
     */
    private void runJSMethodCommand(JSMethodCommand jsMethodCommand) {
        if (jsMethodCommands.add(jsMethodCommand)) jsMethodCommand.execute();
    }

    /**
     * Clears JavaScript method commands.
     */
    private void clearJSMethodCommands() {
        jsMethodCommands.clear();
    }
}
