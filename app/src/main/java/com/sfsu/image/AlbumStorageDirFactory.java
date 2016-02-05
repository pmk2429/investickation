package com.sfsu.image;

import java.io.File;

/**
 * Defines the album storage directory of the images captured using the Camera..
 */
public abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
