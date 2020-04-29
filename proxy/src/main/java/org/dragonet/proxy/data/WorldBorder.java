package org.dragonet.proxy.data;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import lombok.*;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.util.TextFormat;

@Getter
@Setter
@RequiredArgsConstructor
public class WorldBorder {
    @NonNull private Vector2f center;
    @NonNull private double radius;
    @NonNull private double oldRadius;
    @NonNull private double newRadius;
    @NonNull private long speed;
    @NonNull private int warningTime;
    @NonNull private int warningBlocks;

    private double minX, minZ, maxX, maxZ;

    /**
     * Called every tick to tell the player if they are near the border edge.
     */
    public void onTick(ProxySession session) {
        CachedPlayer player = session.getCachedEntity();
        if(isNearEdge(player)) {
            session.sendActionBar(TextFormat.BOLD + "" + TextFormat.RED + "You are near the world border (" + (int) getDistanceToEdge(player) + " blocks)", 0);
        }
    }

    /**
     * Updates the min and max positions of the world border.
     * This should be called every time there is a modifcation to either the center coordinates or the radius.
     */
    public void update() {
        this.minX = Math.max(center.getX() - newRadius / 2.0D, -newRadius);
        this.minZ = Math.max(center.getY() - newRadius / 2.0D, -newRadius);
        this.maxX = Math.min(center.getX() + newRadius / 2.0D, newRadius);
        this.maxZ = Math.min(center.getY() + newRadius / 2.0D, newRadius);
    }

    /**
     * Checks if an entity is within the warning distance to the edge of the world border.
     * https://wiki.vg/Protocol#World_Border
     */
    public boolean isNearEdge(CachedEntity entity) {
        double distance = Math.max(Math.min(speed * 1000 * warningTime, Math.abs(newRadius - oldRadius)), warningBlocks);

        float entityDistance = (float) getDistanceToEdge(entity);

        if ((double) entityDistance < distance) {
            return true;
        }
        return false;
    }

    /**
     * Checks if an entity is inside the world border.
     *
     * This method needs to be improved as it doesn't account for when the world border
     * is currently changing size, it only accounts for the target size.
     *
     * Something similar to the method above should work.
     */
    public boolean isInsideBorder(CachedEntity entity) {
        return entity.getPosition().getX() > minX && entity.getPosition().getX() < maxX && entity.getPosition().getZ() > minZ && entity.getPosition().getZ() < maxZ;
    }

    /**
     * Calculates how close the entity is to the edge of the world border.
     */
    public double getDistanceToEdge(CachedEntity entity) {
        Vector3f pos = entity.getPosition();

        double minPosZ = pos.getZ() - minZ;
        double maxPosZ = maxZ - pos.getZ();
        double minPosX = pos.getX() - minX;
        double maxPosX = maxX - pos.getX();

        return Math.min(Math.min(Math.min(minPosX, maxPosX), minPosZ), maxPosZ);
    }
}
