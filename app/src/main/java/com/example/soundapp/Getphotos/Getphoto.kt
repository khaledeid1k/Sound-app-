package com.example.soundapp.Getphotos

import wseemann.media.FFmpegMediaMetadataRetriever

class Getphoto {
    companion object {
        fun getalbumsart(uri: String?): ByteArray? {
            val mediaMetadataRetriever = FFmpegMediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(uri)
            val embeddedPicture = mediaMetadataRetriever.embeddedPicture
            mediaMetadataRetriever.release()
            return embeddedPicture
        }
    }
}