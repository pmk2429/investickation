package com.sfsu.image;

import android.os.Environment;

import java.io.File;

/**
 * Holds the path to the base album directory where the images are stored in the phone's storage.
 */
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + albumName);
    }
}
