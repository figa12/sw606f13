package dk.aau.cs.giraf.train.opengl;

/**
 * Sometimes we need to load something at runtime.
 * {@link GameDrawable}s should implement this interface if they need it.
 * The {@link GameDrawer} should be designed to load the RuntimeLoaders when they are ready to load.
 * This should also allow for other threads other than the Gl thread to request a texture load on the Gl thread.
 * @author Jesper Rimer Andersen
 * @see GameDrawer
 * @see GameDrawable
 */
public interface RuntimeLoader {
    public void runtimeLoad();
    public boolean isReadyToLoad();
}
