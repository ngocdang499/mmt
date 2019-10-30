package data;

import java.io.Serializable;

import Protocol.Tags;

@SuppressWarnings("serial")
public class FileData implements Serializable{

    @SuppressWarnings("unused")
    private String openTags = Tags.FILE_DATA_OPEN_TAG;
    @SuppressWarnings("unused")
    private String closeTags = Tags.FILE_DATA_CLOSE_TAG;
    public byte[] data;

    public FileData() {
        data = new byte[1024];
    }

    public FileData(int size) {
        data = new byte[size];
    }
}