package rafasconquest.client.map;

/**
 * The map's camera: which part of the world the viewport shows, and how large. Converts between world blocks and
 * screen (GUI) pixels; north (-z) is up.
 */
public final class MapView {
    public int left;
    public int top;
    public int right;
    public int bottom;
    /** World position at the middle of the viewport. */
    public double centerX;
    public double centerZ;
    /** Screen pixels per block. */
    public double scale = 0.1;
    public double minScale = 0.01;
    public double maxScale = 1.0;

    public void setViewport(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int width() {
        return right - left;
    }

    public int height() {
        return bottom - top;
    }

    public double screenX(double worldX) {
        return (left + right) / 2.0 + (worldX - centerX) * scale;
    }

    public double screenY(double worldZ) {
        return (top + bottom) / 2.0 + (worldZ - centerZ) * scale;
    }

    public double worldX(double screenX) {
        return centerX + (screenX - (left + right) / 2.0) / scale;
    }

    public double worldZ(double screenY) {
        return centerZ + (screenY - (top + bottom) / 2.0) / scale;
    }

    public boolean contains(double screenX, double screenY) {
        return screenX >= left && screenX < right && screenY >= top && screenY < bottom;
    }

    /** True if a point is on screen, or within {@code margin} pixels of it. */
    public boolean near(double screenX, double screenY, double margin) {
        return screenX >= left - margin && screenX < right + margin && screenY >= top - margin && screenY < bottom + margin;
    }

    public void pan(double dx, double dy) {
        centerX -= dx / scale;
        centerZ -= dy / scale;
    }

    /** Zooms by {@code factor}, keeping the world point under (screenX, screenY) where it is. */
    public void zoomAt(double screenX, double screenY, double factor) {
        double wx = worldX(screenX);
        double wz = worldZ(screenY);
        scale = Math.max(minScale, Math.min(maxScale, scale * factor));
        centerX = wx - (screenX - (left + right) / 2.0) / scale;
        centerZ = wz - (screenY - (top + bottom) / 2.0) / scale;
    }
}
