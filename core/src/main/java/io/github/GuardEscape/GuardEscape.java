package io.github.GuardEscape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.GuardEscape.Entities.BaseEntity;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.Pathfinding.Node;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GuardEscape extends ApplicationAdapter {

    // Map and camera objects
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Batch batch;

    // Map properties
    private float worldWidth, worldHeight;
    private float tileWidth, tileHeight;
    private float unitScale;
    public Array<Rectangle> wallHitboxes;
    private HashMap<Integer, Node> nodeMap;

    // Entities
    private Array<BaseEntity> entities;
    private Player player;
    private Guard guard;

    // Debug
    ShapeRenderer fovRenderer;

    @Override
    public void create() {
        loadMap("tileMaps\\world.tmx");
        loadObjects();
        loadGraph();

        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        batch = renderer.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        player = new Player(16, 20);
        guard = new Guard(20, 20, this, nodeMap.get(Node.getHash(20, 20)));

        fovRenderer = new ShapeRenderer();
        fovRenderer.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 6.0f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.translateX(speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.translateX(-speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.translateY(speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.translateY(-speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        // Player logic
        if (player.getX() <= 0) { player.setX(0); }
        else if (player.getX() >= worldWidth - player.getWidth()) { player.setX(worldWidth - player.getWidth()); }
        if (player.getY() <= 0) { player.setY(0); }
        else if (player.getY() >= worldHeight - player.getHeight()) { player.setY(worldHeight - player.getHeight()); }

        Rectangle playerHitbox = player.getHitbox();
        float playerHitboxLeft = playerHitbox.getX();
        float playerHitboxRight = playerHitbox.getX() + playerHitbox.getWidth();
        float playerHitboxBottom = playerHitbox.getY();
        float playerHitboxTop = playerHitbox.getY() + playerHitbox.getHeight();
        for (Rectangle wall : wallHitboxes) {
            float wallLeft = wall.getX();
            float wallRight = wall.getX() + wall.getWidth();
            float wallBottom = wall.getY();
            float wallTop = wall.getY() + wall.getHeight();

            if (wall.overlaps(playerHitbox)) {
                // Whatever direction the overlap claims to be is the side of the wall being contacted
                float overlapRight = wallRight - playerHitboxLeft;
                float overlapLeft = playerHitboxRight - wallLeft;
                float overlapBottom = playerHitboxTop - wallBottom;
                float overlapTop = wallTop - playerHitboxBottom;

                float minOverlap = Math.min(
                    Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom)
                );

                if (minOverlap == overlapLeft) {
                    player.setX(wall.getX() - playerHitbox.getWidth());
                }
                else if (minOverlap == overlapRight) {
                    player.setX(wallRight);
                }
                else if (minOverlap == overlapBottom) {
                    player.setY(wallBottom - playerHitbox.getHeight());
                }
                else if (minOverlap == overlapTop) {
                    player.setY(wallTop);
                }

            }
        }

        // Guard logic
        guard.update(player, delta);

        Rectangle guardHitbox = guard.getHitbox();
        float guardHitboxLeft = guardHitbox.getX();
        float guardHitboxRight = guardHitbox.getX() + guardHitbox.getWidth();
        float guardHitboxBottom = guardHitbox.getY();
        float guardHitboxTop = guardHitbox.getY() + guardHitbox.getHeight();
        for (Rectangle wall : wallHitboxes) {
            float wallLeft = wall.getX();
            float wallRight = wall.getX() + wall.getWidth();
            float wallBottom = wall.getY();
            float wallTop = wall.getY() + wall.getHeight();

            if (wall.overlaps(guardHitbox)) {
                // Whatever direction the overlap claims to be is the side of the wall being contacted
                float overlapRight = wallRight - guardHitboxLeft;
                float overlapLeft = guardHitboxRight - wallLeft;
                float overlapBottom = guardHitboxTop - wallBottom;
                float overlapTop = wallTop - guardHitboxBottom;

                float minOverlap = Math.min(
                    Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom)
                );

                if (minOverlap == overlapLeft) {
                    guard.setX(wall.getX() - guardHitbox.getWidth());
                }
                else if (minOverlap == overlapRight) {
                    guard.setX(wallRight);
                }
                else if (minOverlap == overlapBottom) {
                    guard.setY(wallBottom - guardHitbox.getHeight());
                }
                else if (minOverlap == overlapTop) {
                    guard.setY(wallTop);
                }

            }
        }
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        renderer.setView(camera);

        renderer.render();

        batch.begin();
        player.getSprite().draw(batch);
        guard.getSprite().draw(batch);
        batch.end();

        Vector2 baseVec = guard.getOrientation().cpy().nor().scl(50);
        Vector2 bound1 = baseVec.cpy().rotateDeg(60);
        Vector2 bound2 = baseVec.cpy().rotateDeg(-60);

        fovRenderer.begin(ShapeRenderer.ShapeType.Line);
        fovRenderer.setColor(Color.RED);
        fovRenderer.line(
            guard.getX() + (guard.getWidth() / 2),
            guard.getY() + (guard.getWidth() / 2),
            guard.getX() + bound1.x,
            guard.getY() + bound1.y
        );
        fovRenderer.line(
            guard.getX() + (guard.getWidth() / 2),
            guard.getY() + (guard.getHeight() / 2),
            guard.getX() + bound2.x,
            guard.getY() + bound2.y
        );
        fovRenderer.end();
    }

    private void loadMap(String mapPath) {
        map = new TmxMapLoader().load(mapPath);
        MapProperties properties = map.getProperties();

        worldWidth = properties.get("width", Integer.class);
        worldHeight = properties.get("height", Integer.class);
        tileWidth = properties.get("tilewidth", Integer.class);
        tileHeight = properties.get("tileheight", Integer.class);

        // Check to ensure that the tile pixel width and height are the exact same
        // If not, unitScale calculation becomes more complicated
        if (tileWidth == tileHeight)
            unitScale = 1f / tileHeight;
        else
            throw new IllegalArgumentException("ERROR: map .tmx file must have tile size width and height equal!");
    }

    /**
     * Method responsible for loading the object data from a preloaded tmx file
     * Hitboxes of walls and furniture are stored in two separate arrays, allowing for differing logic between the two
     */
    private void loadObjects() {
        wallHitboxes = new Array<>();

        MapLayer wallsLayer = map.getLayers().get("WallHitbox");
        MapObjects walls = wallsLayer.getObjects();

        for (MapObject wall : walls) {
            Rectangle hitbox = ((RectangleMapObject) wall).getRectangle();
            float scaledX = hitbox.getX() * unitScale;
            float scaledY = hitbox.getY() * unitScale;
            float scaledWidth = hitbox.getWidth() * unitScale;
            float scaledHeight = hitbox.getHeight() * unitScale;
            hitbox = new Rectangle(scaledX, scaledY, scaledWidth, scaledHeight);
            wallHitboxes.add(hitbox);
        }
    }

    // Loading is fucked, calling new Node() destroys any relation, need to load nodes into a HashMap, not Array
    // Also when populating neighbors you cannot call new Node(), instead access in hashmap based on coordinates and Node.getHash() method
    private void loadGraph() {
        nodeMap = new HashMap<>();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Wall");

        // Get all valid nodes
        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                if (layer.getCell(i, j) == null) {
                    Node node = new Node(i, j);
                    nodeMap.put(Node.getHash(i, j), node);
                }
            }
        }

        // Populate node neighbors
        for (Node node : nodeMap.values()) {
            int x = node.getX();
            int y = node.getY();
            for (int i = -1; i <= 1; i += 2) {
                if ((x+i) < 0 || (x+i) > 29) continue;
                Node neighbor = new Node(x+i, y);
                if (nodeMap.containsValue(neighbor)) {
                    node.addNeighbor(nodeMap.get(Node.getHash(neighbor.getX(), neighbor.getY())));
                }
            }
            for (int j = -1; j <= 1; j += 2) {
                if ((y+j) < 0 || (y+j) > 29) continue;
                Node neighbor = new Node(x, y+j);
                if (nodeMap.containsValue(neighbor)) {
                    node.addNeighbor(nodeMap.get(Node.getHash(neighbor.getX(), neighbor.getY())));
                }
            }
        }
    }

    public Node getGuardPosition() {
        return nodeMap.get(Node.getHash((int) guard.getX(), (int) guard.getY()));
    }
    public Node getGuardHome() {
        return guard.getHome();
    }

    @Override
    public void dispose() {

    }
}
