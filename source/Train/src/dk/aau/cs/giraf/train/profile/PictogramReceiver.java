package dk.aau.cs.giraf.train.profile;

public interface PictogramReceiver {
    public void receivePictograms(long[] pictogramIds, int requestCode);
}
