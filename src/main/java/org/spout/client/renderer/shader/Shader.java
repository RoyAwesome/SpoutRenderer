package org.spout.client.renderer.shader;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.opengl.Texture;
import org.spout.client.renderer.shader.variables.AttributeShaderVariable;
import org.spout.client.renderer.shader.variables.FloatShaderVariable;
import org.spout.client.renderer.shader.variables.IntShaderVariable;
import org.spout.client.renderer.shader.variables.Mat2ShaderVariable;
import org.spout.client.renderer.shader.variables.Mat3ShaderVariable;
import org.spout.client.renderer.shader.variables.Mat4ShaderVariable;
import org.spout.client.renderer.shader.variables.ShaderVariable;
import org.spout.client.renderer.shader.variables.TextureSamplerShaderVariable;
import org.spout.client.renderer.shader.variables.Vec2ShaderVariable;
import org.spout.client.renderer.shader.variables.Vec3ShaderVariable;
import org.spout.client.renderer.shader.variables.Vec4ShaderVariable;
import org.spout.api.math.*;



/**
 * Represents a Shader Object in OpenGL
 * @author RoyAwesome
 *
 */
public class Shader {
	int program;
	int textures = 0;
	
	HashMap<String, ShaderVariable> variables = new HashMap<String, ShaderVariable>();
	
	
	
	public Shader(String vertexShader, String fragmentShader){

		
		//Create a new Shader object on the GPU
		program = GL20.glCreateProgram();
		
		//Compile the vertex shader
		String vshader;
		if(vertexShader == null)  vshader = fallbackVertexShader;
		else{
			try{
				vshader = readShaderSource(vertexShader);			
			}
			catch(FileNotFoundException e){
				vshader = fallbackVertexShader;
			}
		}
		int vShader = compileShader(vshader , GL20.GL_VERTEX_SHADER);
		GL20.glAttachShader(program, vShader);
		String fshader;
		if(fragmentShader == null) fshader = fallbackFragmentShader;
		else{
			try{
				fshader = readShaderSource(fragmentShader);			
			}
			catch(FileNotFoundException e){
				fshader = fallbackFragmentShader;
			}
		}	
		
		int fShader = compileShader(fshader, GL20.GL_FRAGMENT_SHADER);
		GL20.glAttachShader(program, fShader);
		
		GL20.glLinkProgram(program);
		
		int status = GL20.glGetProgram(program, GL20.GL_LINK_STATUS);
		if(status != GL11.GL_TRUE){
			String error = GL20.glGetProgramInfoLog(program, 255);
			throw new ShaderCompileException("Link Error: "+error);
		}
		
		
		
	}
	
	
	public void setUniform(String name, int value){
		variables.put(name, new IntShaderVariable(program, name, value));
	}
	public void setUniform(String name, float value){
		variables.put(name, new FloatShaderVariable(program, name, value));
	}
	public void setUniform(String name, Vector2 value){
		variables.put(name, new Vec2ShaderVariable(program, name, value));
	}
	public void setUniform(String name, Vector3 value){
		variables.put(name, new Vec3ShaderVariable(program, name, value));
	}
	public void setUniform(String name, Vector4 value){
		variables.put(name, new Vec4ShaderVariable(program, name, value));
	}
	public void SetUniform(String name, Matrix value){
		if(value.getDimension() == 2){
			variables.put(name, new Mat2ShaderVariable(program, name, value));
		}else if(value.getDimension() == 3){
			variables.put(name, new Mat3ShaderVariable(program, name, value));
		}else if(value.getDimension() == 4){
			variables.put(name, new Mat4ShaderVariable(program, name, value));
		}
		
	}
	public void setUniform(String name, Texture value){
		if(variables.containsKey(name)){
			ShaderVariable texture = variables.get(name);
			if(!(texture instanceof TextureSamplerShaderVariable)) throw new IllegalStateException(name + " is not a texture!");
			TextureSamplerShaderVariable t = (TextureSamplerShaderVariable)texture;
			t.set(value);
		}else{
			textures++;
			variables.put(name, new TextureSamplerShaderVariable(program, name, value, textures));
		}		
		
	}
	
	public void enableAttribute(String name, int size, int type, int stride,  long offset){
		variables.put(name, new AttributeShaderVariable(program, name, size, type,stride,  offset));
	}
	
	public void assign(){
		GL20.glUseProgram(program);
		for(ShaderVariable v : variables.values()){
			v.assign();
		}
	}
	
	
	
	
	
	
	private int compileShader(String source, int type){
		
		
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);
		int status = GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS);
		if(status != GL11.GL_TRUE){
			String error = GL20.glGetShaderInfoLog(shader, 255);
			throw new ShaderCompileException("Compile Error in " + ((type == GL20.GL_FRAGMENT_SHADER)? "Fragment Shader": "VertexShader") + ": "+error);
		}
		return shader;
	}
	
	
	private String readShaderSource(String file) throws FileNotFoundException{

		FileInputStream in = new FileInputStream(file);
		Scanner scan = new Scanner(in);
		
		StringBuilder src = new StringBuilder();
		
		while(scan.hasNextLine()){
			src.append(scan.nextLine() + "\n");
		}
		
		return src.toString();
		
	}
	
	String fallbackVertexShader = "attribute vec4 vPosition;\n" +
								  "attribute vec4 vColor;\n" +
								  "attribute vec2 vTexCoord; \n"+
								  "varying vec4 color;\n" +
								  "varying vec2 uvcoord; \n" +
								  "uniform mat4 Projection; \n"+
								  "uniform mat4 View; \n" +
								  "void main() \n" +
								  "{\n    gl_Position = Projection * View * vPosition; \n" +
								  "	uvcoord = vTexCoord; \n" +
								  "color = vColor; \n"+
								  "} \n";
 
	String fallbackFragmentShader = "varying vec4 color;  //in \n" +
									"varying vec2 uvcoord; \n" +
									"uniform sampler2D texture; \n" +
									"void main()\n{\n" +
									"gl_FragColor =  color; \n} \n";

	
}
