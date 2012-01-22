package org.spout.client.mesh;

import java.util.ArrayList;

import org.spout.client.renderer.vertexformat.*;
import org.spout.api.model.renderer.RenderEffect;
import org.spout.api.render.Renderer;

public abstract class BaseMesh {
	ArrayList<PositionNormalTexture> verts = new ArrayList<PositionNormalTexture>();
	
	ArrayList<RenderEffect> effects = new ArrayList<RenderEffect>();
	
	boolean dirty = false;
	
	public void addRenderEffect(RenderEffect effect){
		effects.add(effect);
	}
	public void removeRenderEffect(RenderEffect effect){
		effects.remove(effect);
	}
	public RenderEffect[] getEffects(){
		return (RenderEffect[])effects.toArray();
	}
	
	
	private void preBatch(Renderer batcher){
		for(RenderEffect effect : effects){
			effect.preBatch();
		}
	}
	
	private void postBatch(Renderer batcher){
		for(RenderEffect effect: effects){
			effect.postBatch();
		}
		
	}
	
	protected void batch(Renderer batcher){		
		for(PositionNormalTexture vert : verts){
			batcher.addTexCoord(vert.getTexture());
			batcher.addNormal(vert.getNormal());
			batcher.addVertex(vert.getPosition());
		}		
	}
	
	private void preRender(Renderer batcher){
		for(RenderEffect effect : effects){
			effect.preDraw();
		}
	}
	private void postRender(Renderer batcher){
		for(RenderEffect effect : effects){
			effect.postDraw();
		}		
	}
	
	protected void render(Renderer batcher){			
		batcher.render();		
	}
	
	
	public void draw(Renderer batcher){
		this.preBatch(batcher);
		this.batch(batcher);
		this.postBatch(batcher);
		
		this.preRender(batcher);
		this.render(batcher);	
		this.postRender(batcher);
		
	}
	
	
	
	
	
}
