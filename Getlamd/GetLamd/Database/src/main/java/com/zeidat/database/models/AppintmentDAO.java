package com.zeidat.database.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.Locale;

@Entity(nameInDb = "APPOINTMENTS")
public class AppintmentDAO {

    @Id(autoincrement = true)
    public long id;

    public Date date ;
    public Locale startTime ;
    public Locale endTime ;


}
