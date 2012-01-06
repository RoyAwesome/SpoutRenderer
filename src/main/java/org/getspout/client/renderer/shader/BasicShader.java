package org.getspout.client.renderer.shader;

import java.io.FileNotFoundException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.royawesome.renderer.shader.variables.Mat4ShaderVariable;

public class BasicShader extends Shader {
	
	
	public BasicShader() {
		super(null, null);
		
	}
	
	public void assign(boolean compatabilityMode){
		if(!variables.containsKey("Projection"))throw new IllegalStateException("Basic Shader must have a projection matrix assigned");
		if(!variables.containsKey("View")) throw new IllegalStateException("Basic Shader must have a view matrix assigned");
		
		if(compatabilityMode){
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			FloatBuffer buff = BufferUtils.createFloatBuffer(4*4);
			getProjectionMatrix().store(buff);
			buff.flip();
			
			GL11.glLoadMatrix(buff);
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			buff = BufferUtils.createFloatBuffer(4*4);
			getViewMatrix().store(buff);
			buff.flip();
			
			GL11.glLoadMatrix(buff);
			
		}
		
	}
	
	public void setViewMatrix(Matrix4f mat){
		SetUniform("View", mat);
	}
	
	public Matrix4f getViewMatrix()	{
		return ((Mat4ShaderVariable)variables.get("View")).get();
		
	}
	public Matrix4f getProjectionMatrix()	{
		return ((Mat4ShaderVariable)variables.get("Projection")).get();		
	}
	
	
	public void setProjectionMatrix(Matrix4f mat){
		SetUniform("Projection", mat);
	}
}
