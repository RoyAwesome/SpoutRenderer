package org.spout.client.renderer;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import gnu.trove.list.array.*;


import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;
import org.spout.api.math.Vector4;
import org.spout.client.renderer.shader.EmptyShader;
import org.spout.client.renderer.shader.Shader;
import org.newdawn.slick.opengl.Texture;


public abstract class BatchVertexRenderer {

	public static BatchModes GLMode = BatchModes.GL30;
	
	public static BatchVertexRenderer constructNewBatch(int renderMode){
		if(GLMode == BatchModes.GL11) return new GL11BatchVertexRenderer(renderMode);
		if(GLMode == BatchModes.GL30) return new GL30BatchVertexRenderer(renderMode);
		if(GLMode == BatchModes.GLES20) return new GLES20BatchVertexRenderer(renderMode);
		throw new IllegalArgumentException("GL Mode:" + GLMode + " Not reconized");
	}
	
	
	
	boolean batching = false;
	boolean flushed = false;
	
	int renderMode;
	
	//Using FloatArrayList because I need O(1) access time
	//and fast ToArray()
	TFloatArrayList vertexBuffer = new TFloatArrayList();
	TFloatArrayList colorBuffer = new TFloatArrayList();
	TFloatArrayList normalBuffer = new TFloatArrayList();
	TFloatArrayList uvBuffer = new TFloatArrayList();
	
	
	int numVerticies = 0;
	
	boolean useColors = false;
	boolean useNormals = false;
	boolean useTextures = false;
	
	Shader activeShader = null;
	
	public BatchVertexRenderer(int mode){
		renderMode = mode;
	}
	
	/**
	 * Begin batching render calls
	 */
	public void begin(){
		if(batching) throw new IllegalStateException("Already Batching!");
		batching = true;
		flushed = false;		
		vertexBuffer.clear();
		colorBuffer.clear();
		normalBuffer.clear();
		uvBuffer.clear();
		
		
		numVerticies = 0;
	}
	/**
	 * Ends batching and flushes cache to the GPU
	 */
	public void end(){
		if(!batching) throw new IllegalStateException("Not Batching!");
		batching = false;
		flush();
	}
	
	/**
	 * Flushes the contents of the cache to the GPU
	 * 
	 */
	public final void flush(){
		if( vertexBuffer.size() % 4 != 0) throw new IllegalStateException("Vertex Size Mismatch (How did this happen?)");
		if( useColors){
			if(colorBuffer.size() % 4 != 0) throw new IllegalStateException("Color Size Mismatch (How did this happen?)");
			if(colorBuffer.size() / 4 != numVerticies) throw new IllegalStateException("Color Buffer size does not match numVerticies");
	
		}
		if(useNormals){
			if(normalBuffer.size() % 4 != 0) throw new IllegalStateException("Normal Size Mismatch (How did this happen?)");
			if(normalBuffer.size() / 4 != numVerticies) throw new IllegalStateException("Normal Buffer size does not match numVerticies");
			
		}
		if(useTextures){
			if(uvBuffer.size() % 2 != 0) throw new IllegalStateException("UV size Mismatch (How did this happen?)");
			if(uvBuffer.size() / 2 != numVerticies) throw new IllegalStateException("UV Buffer size does not match numVerticies");
			
		}
		
		
		//Call the overriden flush
		doFlush();
		
		//clean up after flush
		postFlush();
		
	}
	
	protected abstract void doFlush();
	
	protected void postFlush(){
		
		flushed = true;
		
	}
	
	/**
	 * The act of drawing.  The Batch will check if it's possible to render
	 * as well as setup for rendering.  If it's possible to render, it will call doRender()
	 * 
	 *  
	 */
	protected abstract void doRender();
	
	
	/**
	 * Renders the batch. 
	 */
	public final void render(){
		checkRender();
		
		doRender();
		
		
	}
	
	protected void checkRender(){
		if(batching) throw new IllegalStateException("Cannot Render While Batching");
		if(!flushed) throw new IllegalStateException("Cannon Render Without Flushing the Batch");		
	}
	
	

	public void addVertex(float x, float y, float z, float w){
		vertexBuffer.add(x);
		vertexBuffer.add(y);
		vertexBuffer.add(z);
		vertexBuffer.add(w);
		
		numVerticies++;
	}
	public void addVertex(float x, float y, float z){
		addVertex(x,y,z,1.0f);
	}
	public void addVertex(float x, float y){
		addVertex(x,y,0.0f,1.0f);
	}
	
	public void addVertex(Vector3 vertex){
		addVertex(vertex.getX(), vertex.getY(), vertex.getZ());
	}
	public void AddVertex(Vector2 vertex){
		addVertex(vertex.getX(), vertex.getY());
	}
	public void AddVertex(Vector4 vertex){
		addVertex(vertex.getX(), vertex.getY(), vertex.getZ(), vertex.getZ());
	}
	
	public void addColor(float r, float g, float b){
		addColor(r,g,b,1.0f);
	}
	public void addColor(float r, float g, float b, float a){
		colorBuffer.add(r);
		colorBuffer.add(g);
		colorBuffer.add(b);
		colorBuffer.add(a);
	}
	
	public void addNormal(float x, float y, float z, float w){
		normalBuffer.add(x);
		normalBuffer.add(y);
		normalBuffer.add(z);
		normalBuffer.add(w);
	}
	public void addNormal(float x, float y, float z){
		addNormal(x,y,z,1.0f);
	}

	public void addNormal(Vector3 vertex){
		addNormal(vertex.getX(), vertex.getY(), vertex.getZ());
	}
	public void addNormal(Vector4 vertex){
		addNormal(vertex.getX(), vertex.getY(), vertex.getZ(), vertex.getZ());
	}
	
	public void addTexCoord(float u, float v){
		uvBuffer.add(u);
		uvBuffer.add(v);
	}
	
	
	public void setShader(Shader shader){
		if(shader == null){		
			try {
				activeShader = new EmptyShader();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else activeShader = shader;
	}
	
	
	public void enableColors(){
		useColors = true;
	}
	
	public void enableNormals(){
		useNormals = true;
	}
	
	public void enableTextures(){
		useTextures = true;
	}
	
}
