package com.example.taha.alrehab.BusinessEntities;

import java.util.Date;
/**
 * Created by taha on 12/28/15.
 */
public class AlrehabNotification {
    private int _id;
    private String _title;

    private Date _publishdate;

    private String _imageUrl;
    private String _imageThumbUrl;

    private short _type;

    public AlrehabNotification() {
    }

    public AlrehabNotification(int id,
                               String title,
                               Date publishdate,
                               Date expirationdate,
                               String imageUrl,
                               String imageThumbUrl,
                               short type
    ) {
        this._id = id;
        this._title = title;
        this._publishdate = publishdate;
        this._imageUrl = imageUrl;
        this._imageThumbUrl = imageThumbUrl;
        this._type = type;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }


    public Date get_publishdate() {
        return _publishdate;
    }

    public void set_publishdate(Date _publishdate) {
        this._publishdate = _publishdate;
    }


    public String get_imageUrl() {
        return _imageUrl;
    }

    public void set_imageUrl(String _imageUrl) {
        this._imageUrl = _imageUrl;
    }

    public String get_imageThumbUrl() {
        return _imageThumbUrl;
    }

    public void set_imageThumbUrl(String _imageThumbUrl) {
        this._imageThumbUrl = _imageThumbUrl;
    }

    public short get_type() {
        return _type;
    }

    public void set_type(short _type) {
        this._type = _type;
    }
}
