package org.spout.client.renderer.shader.variables;

import org.lwjgl.opengl.GL20;
import org.spout.api.util.Color;

public class ColorShaderVariable extends ShaderVariable {
	
	Color value;

	public ColorShaderVariable(int program, String name, Color value) {
		super(program, name);
		this.value = value;
	}

	@Override
	public void assign() {
		GL20.glUniform4f(this.location, value.getRedF(), value.getGreenF(), value.getBlueF(), value.getAlphaF());

	}

}
