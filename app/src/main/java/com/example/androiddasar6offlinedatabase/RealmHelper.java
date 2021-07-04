package com.example.androiddasar6offlinedatabase;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {
    private Context context;
    private Realm realm;

    public RealmHelper(Context context) {
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    //insert
    public void insertData (CatatanModel catatan) {
        realm.beginTransaction();
        realm.copyToRealm(catatan);
        realm.commitTransaction();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
        realm.close();
    }

    //next id
    public long getNextId() {
        if (realm.where(CatatanModel.class).count() != 0) {
            long id = realm.where(CatatanModel.class).max("id").longValue();
            return id +1;
        } else {
            return 1;
        }
    }

    //menampilkan data
    public List<CatatanModel> showData() {
        return realm.where(CatatanModel.class).findAll();
    }

    //menampilkan satu data
    public CatatanModel showOneData(Integer id) {
        CatatanModel data = realm.where(CatatanModel.class).equalTo("id", id).findFirst();
        return data;
    }

    //update
    public void updateData(CatatanModel catatan) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(catatan);
        realm.commitTransaction();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Toast.makeText(context, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            }
        });
        realm.close();
    }

    //deleteOneData
    public void deleteData(Integer id) {
        realm.beginTransaction();
        CatatanModel catatan = realm.where(CatatanModel.class).equalTo("id", id).findFirst();
        catatan.deleteFromRealm();
        realm.commitTransaction();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
