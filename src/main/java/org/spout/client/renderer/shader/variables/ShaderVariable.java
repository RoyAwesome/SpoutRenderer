package org.spout.client.renderer.shader.variables;

import org.lwjgl.opengl.GL20;

public abstract class ShaderVariable {
	int program;
	int location;
	public ShaderVariable(int program, String name){
		this.program = program;
		this.location = GL20.glGetUniformLocation(program, name);
	}
	public abstract void assign();
	
}
