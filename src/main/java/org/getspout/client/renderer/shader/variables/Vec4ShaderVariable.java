package org.getspout.client.renderer.shader.variables;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class Vec4ShaderVariable extends ShaderVariable {
	Vector4f value;
	public Vec4ShaderVariable(int program, String name, Vector4f value) {
		super(program, name);
		this.value = value;
	}

	@Override
	public void assign() {
		GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
	}

}
