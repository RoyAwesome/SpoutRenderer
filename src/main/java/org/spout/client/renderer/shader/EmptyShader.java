package org.spout.client.renderer.shader;

import java.io.FileNotFoundException;


/**
 * Empty Shader for 1.1 only.  Do not use this for 3.0 ever.  
 * @author RoyAwesome
 *
 */
public class EmptyShader extends ClientShader {

	public EmptyShader()
			throws FileNotFoundException {
		super(null, null);
		
	}

	public void assign(){
		return;
	}
}
