package com.gsxxx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.gsxxx.game.Enemies.Spearman;
import com.gsxxx.game.projectiles.ProjectilePrototype;

import java.util.LinkedList;

import static com.badlogic.gdx.Input.Keys.U;
import static com.badlogic.gdx.Input.Keys.Y;

public class PlayScreen implements Screen {
    //reference to the game
    MammothGame game;
    //starting objects
    private Spearman spearman;
    private Ribbon ribbon;

    public static World world;
    public static LinkedList<ProjectilePrototype> projectilesToRender;
    public static LinkedList<ProjectilePrototype> projectilesToDestroy;
    private Box2DDebugRenderer debugRenderer;
    static public OrthographicCamera camera;
    private LinkedList<MyContactListener.StickInfo> thingsToStick;

    public PlayScreen(MammothGame game) {
        this.game = game;

        initializePhysics();
        initializeCamera();
        Ground ground = Ground.getInstance();//??

        //initialize objects
        ribbon = new Ribbon();
        spearman = new Spearman();

        Gdx.input.setInputProcessor(new GestureDetector(new MyGesturesListener(ribbon)));
    }

    @Override
    public void render(float delta) {
        EndlessScrollingBackground.getInstance().render();
        spearman.render();
        ribbon.render();
        for (ProjectilePrototype projectile : projectilesToRender) {
            projectile.render();
        }
        Panel.getInstance().render();
        Mammoth.getInstance().render();

//        debugRenderer.render(world, camera.combined);

        removeUnneededSpears();
        stickProjectileToMammoth();
        spearmanStateUpdate();

        world.step(1 / 45f, 6, 2);
    }


    private void spearmanStateUpdate() {
        if (Gdx.input.isKeyJustPressed(U)) {
            spearman.shoot();
        } else if (Gdx.input.isKeyJustPressed(Y)) {
            while (projectilesToRender.size() > 1) {
                projectilesToRender.get(0).destroyThisProjectile();
            }
        }
    }

    private void initializePhysics() {
        Box2D.init();
        thingsToStick = new LinkedList<MyContactListener.StickInfo>();
        projectilesToRender = new LinkedList<ProjectilePrototype>();
        projectilesToDestroy = new LinkedList<ProjectilePrototype>();
        world = new World(new Vector2(0f, -9.81f), false);
        world.setContactListener(new MyContactListener(thingsToStick));
        debugRenderer = new Box2DDebugRenderer();
    }

    private void initializeCamera() {
        final int PPM = 200; // physic world scale variable
        camera = new OrthographicCamera((float) Gdx.graphics.getWidth() / PPM, (float) Gdx.graphics.getHeight() / PPM);
        camera.position.set((float) Gdx.graphics.getWidth() / PPM / 2, (float) Gdx.graphics.getHeight() / PPM / 2, 0);
        camera.update();
    }

    private void stickProjectileToMammoth() {
        while (thingsToStick.size() > 0) {
            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.bodyA = thingsToStick.get(0).getMammoth();
            weldJointDef.bodyB = thingsToStick.get(0).getProjectile();
            weldJointDef.collideConnected = true;
            weldJointDef.frequencyHz = 0;
            weldJointDef.dampingRatio = 0;
            weldJointDef.referenceAngle = weldJointDef.bodyB.getAngle() - weldJointDef.bodyA.getAngle();
            weldJointDef.initialize(thingsToStick.get(0).getMammoth(), thingsToStick.get(0).getProjectile(), thingsToStick.get(0).getContactPoints()[0]);
            PlayScreen.world.createJoint(weldJointDef);
            thingsToStick.remove(0);
        }
    }

    private void removeUnneededSpears() {
        while (projectilesToDestroy.size() > 0) {
            projectilesToDestroy.get(0).dispose();
            projectilesToDestroy.remove(0);
        }
    }

    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        EndlessScrollingBackground.getInstance().dispose();
        Mammoth.getInstance().dispose();
        Panel.getInstance().dispose();
        spearman.dispose();
        for (ProjectilePrototype projectile : projectilesToRender) {
            projectile.dispose();
        }
        removeUnneededSpears();
        world.dispose();
    }
}