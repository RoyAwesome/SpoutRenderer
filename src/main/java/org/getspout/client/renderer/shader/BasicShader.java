package org.getspout.client.renderer.shader;


import java.nio.FloatBuffer;

import org.getspout.api.math.Matrix;
import org.getspout.client.renderer.shader.variables.Mat4ShaderVariable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class BasicShader extends Shader {
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);
	
	public BasicShader() {
		super(null, null);
		
	}
	
	public void assign(boolean compatabilityMode){
		if(!variables.containsKey("Projection"))throw new IllegalStateException("Basic Shader must have a projection matrix assigned");
		if(!variables.containsKey("View")) throw new IllegalStateException("Basic Shader must have a view matrix assigned");
		
		if(compatabilityMode){
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			matrixBuffer.position(0);
			matrixBuffer.put(getProjectionMatrix().toArray());
			matrixBuffer.flip();
			
			GL11.glLoadMatrix(matrixBuffer);
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			matrixBuffer.position(0);
			matrixBuffer.put(getViewMatrix().toArray());
			matrixBuffer.flip();
			
			GL11.glLoadMatrix(matrixBuffer);
			
		}
		
	}
	
	public void setViewMatrix(Matrix mat){
		SetUniform("View", mat);
	}
	
	public Matrix getViewMatrix()	{
		return ((Mat4ShaderVariable)variables.get("View")).get();
		
	}
	public Matrix getProjectionMatrix()	{
		return ((Mat4ShaderVariable)variables.get("Projection")).get();		
	}
	
	
	public void setProjectionMatrix(Matrix mat){
		SetUniform("Projection", mat);
	}
}
