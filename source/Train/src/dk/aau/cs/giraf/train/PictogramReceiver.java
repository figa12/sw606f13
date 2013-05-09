package dk.aau.cs.giraf.train;

public interface PictogramReceiver {
    public void receivePictograms(long[] pictogramIds, int requestCode);
}
