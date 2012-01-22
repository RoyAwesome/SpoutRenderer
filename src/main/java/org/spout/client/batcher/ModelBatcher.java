package org.spout.client.batcher;

import org.lwjgl.opengl.GL11;
import org.spout.api.render.Renderer;
import org.spout.client.mesh.BaseMesh;
import org.spout.client.renderer.BatchVertexRenderer;

public class ModelBatcher {
	
	Renderer renderer;
	
	public ModelBatcher(){
		renderer = BatchVertexRenderer.constructNewBatch(GL11.GL_TRIANGLES);
		renderer.enableNormals();
		renderer.enableTextures();
	}
	
	public void drawMesh(BaseMesh mesh){
		
	}
	
	
	
	
	
}
