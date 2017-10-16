package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.CameraNode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

/**
 * Main
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    protected Node playerNode;
    protected Spatial player;
    protected Geometry coin;

    @Override
    public void simpleInitApp() {
        
        // Create Terrain node
        Node terrainNode = new Node("world");
        
        Spatial terrain = assetManager.loadModel(
                "Models/Terrain/Terrain.mesh.xml");
        
        // Create Terrain Material
        Material mat_terrain = new Material(assetManager,
            "Common/MatDefs/Terrain/Terrain.j3md");

        // Add Terrain Alpha Map
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
            "Textures/Terrain/splat/alpha1.png"));
        
        // Load Textures
        Texture dirt = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");
        Texture grass = assetManager.loadTexture(
            "Textures/Terrain/splat/grass.jpg");
        
        // Set Texture Wrap
        grass.setWrap(WrapMode.Repeat);
        dirt.setWrap(WrapMode.Repeat);
        
        // Apply Terrain Textures
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);
        mat_terrain.setTexture("Tex2", grass);
        mat_terrain.setFloat("Tex2Scale", 64f);
        mat_terrain.setTexture("Tex3", dirt);
        mat_terrain.setFloat("Tex3Scale", 128f);
        
        // Add Terrain
        terrain.setMaterial(mat_terrain);
        terrain.scale(50);
        terrain.setLocalTranslation(0,-1,0);
        terrainNode.attachChild(terrain);
        
        // Attatch to rootNode
        rootNode.attachChild(terrainNode);
        
        // Create playerNode
        playerNode = new Node();
        
        // Create Player
        player = assetManager.loadModel(
                "Models/HoverTank/Tank2.mesh.xml");
        player.scale(0.1f);
        
        // New Quaternion
        Quaternion q = new Quaternion();
        q.fromAngles(0, 1.5708f, 0);
        
        // Player Position
        player.setLocalTranslation(0,-0.5f,0);
        player.setLocalRotation(q);
        
        // Attatch to playerNode
        playerNode.attachChild(player);
        
        // Create Player Camera
        flyCam.setEnabled(false);
        CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        playerNode.attachChild(camNode);
        
        // Set Camera Position
        camNode.setLocalTranslation(new Vector3f(-4,.35f,0));
        camNode.setLocalRotation(q);
        
        // Set Player Location
        playerNode.setLocalTranslation(new Vector3f(0,0,10));
        
        // Attatch to rootNode
        rootNode.attachChild(playerNode);
        
        // Create Collectible
        Box c = new Box(.25f, .25f, .25f);
        coin = new Geometry("gold cube", c);
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        coin.setMaterial(mat);
        coin.setLocalTranslation(10, .1f, 5);
        rootNode.attachChild(coin);
        
        // Create Lighting
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1f, -1f, -1.0f));
        rootNode.addLight(sun);
        
        // Add Skybox
        rootNode.attachChild(
                SkyFactory.createSky(getAssetManager(), 
                "Textures/Sky/Bright/BrightSky.dds", 
                SkyFactory.EnvMapType.CubeMap));
        
        // Add Controls
        initKeys();
    }
    
    private void initKeys() {
        inputManager.addMapping("Forward",  new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("RotateL",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Back",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("RotateR",  new KeyTrigger(KeyInput.KEY_D));
        
        // Add Action Listeners
        inputManager.addListener(analogListener,"Forward", "RotateL", "Back", "RotateR");
    }
    
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            Vector3f v = playerNode.getLocalRotation().getRotationColumn(0);
            speed = 10;
            
            if (name.equals("Forward")) {
                playerNode.move(v.mult(tpf).mult(speed));
            }
            if (name.equals("RotateL")) {
                playerNode.rotate(0, value*speed, 0);
            }
            if (name.equals("Back")) {
                playerNode.move(v.mult(tpf).mult(-speed));
            }
            if (name.equals("RotateR")) {
                playerNode.rotate(0, -value*speed, 0);
            }
        }
    };
    

    @Override
    public void simpleUpdate(float tpf) {
        coin.rotate(-0.25f*tpf, 0.25f*tpf, -0.25f*tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
