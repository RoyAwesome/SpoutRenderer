package org.getspout.client.renderer;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package org.royawesome.renderer;

import java.nio.ByteOrder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;

*//**
 *
 * @author simplyianm
 *//*
public class BatchTesselator implements MinecraftTessellator {

    private BatchVertexRenderer renderer;

    //Brightness
    private int brightness;

    //Colors
    private float r;

    private float g;

    private float b;

    private float a;
    
    private boolean colorEnabled;

    //Textures
    private float u;

    private float v;
    
    private boolean texturesEnabled = false;
    
    //Normals
    private float nx;
    
    private float ny;
    
    private float nz;
    
    private boolean normalsEnabled = false;

    //Translation
    private float tx = 0;

    private float ty = 0;

    private float tz = 0;

    public BatchTesselator(){
    	this(GL11.GL_QUADS);
    }
    
    public BatchTesselator(int mode) {
    	this.renderer = BatchVertexRenderer.constructNewBatch(mode);
        colorEnabled = renderer.useColors;
        texturesEnabled = renderer.useTextures;
        normalsEnabled = renderer.useNormals;
    }

    public int getMCTexture(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void draw() {
        renderer.end();
        renderer.render();
    }

    public void startDrawingQuads() {
        startDrawing(GL11.GL_QUADS);
    }

    public void startDrawing(int drawMode) {
        renderer.begin();
        renderer.renderMode = drawMode;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setTextureUV(double u, double v) {
        this.u = (float) u;
        this.v = (float) v;
    }

    public void setColorOpaqueFloat(float r, float g, float b) {
        setColorRGBAFloat(r, g, b, 0xff);
    }

    public void setColorRGBAFloat(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setColorOpaque(int r, int g, int b) {
        setColorRGBA(r, g, b, 0xff);
    }

    public void setColorRGBA(int r, int g, int b, int a) {
        setColorRGBAFloat((float) (r / 255.0f), (float) (g / 255.0f), (float) (b / 255.0f), (float) (a / 255.0f));
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4) {
        setTextureUV(d3, d4);
        addVertex(d, d1, d2);
    }

    public void addVertex(double x, double y, double z) {
        if (colorEnabled) {
            renderer.AddColor(r, g, b, a);
        }
        if (normalsEnabled) {
            renderer.AddNormal(nx, ny, nz);
        }
        if (texturesEnabled) {
            renderer.AddTexCoord(u, v);
        }
        renderer.AddVertex((float) x + tx, (float) y + ty, (float) z + tz);
    }

    public void setColorOpaqueInt(int color) {
        setColorRGBAInt(color, 1);
    }

    public void setColorRGBAInt(int color, int alpha) {
        //Not sure if I have to check the byte order
        r = color & 0xff0000 >> 16;
        g = color & 0x00ff00 >> 8;
        b = color & 0x0000ff;
        a = alpha & 0x0000ff;
    }

    public void disableColor() {
        colorEnabled = false;
    }

    public void setNormal(float x, float y, float z) {
        nx = x;
        ny = y;
        nz = z;
    }

    public void setTranslation(double x, double y, double z) {
        tx = (float) x;
        ty = (float) y;
        tz = (float) z;
    }

}
*/