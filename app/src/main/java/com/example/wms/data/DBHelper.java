package com.example.wms.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper {


    private static final String DATABASE_NAME="WHS";
    private static  final String TABLE_PRODUCT ="products";
    private static  final String TABLE_SUPPLIER="suppliers";
    private static  final String TABLE_INVENTORY="inventory";
    private static  final String TABLE_INVENTORY_CHECK="inventory_check_reasons";
    public static final String TABLE_CATEGORY ="category" ;


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table "+ TABLE_PRODUCT +"(id INTEGER PRIMARY KEY AUTOINCREMENT ,product_name TEXT,sku TEXT,description TEXT,id_category INTEGER,image BLOB,id_supplier INTEGER,qty_on_hand INTEGER DEFAULT 0,FOREIGN KEY(id_supplier) REFERENCES suppliers(id),FOREIGN KEY(id_category) REFERENCES category(id))");
        sqLiteDatabase.execSQL("create table "+TABLE_SUPPLIER+"(id INTEGER PRIMARY KEY AUTOINCREMENT ,supplier_name TEXT,code TEXT,phone TEXT,email TEXT)");
        sqLiteDatabase.execSQL("create table "+TABLE_INVENTORY+"(id INTEGER PRIMARY KEY AUTOINCREMENT,id_product INTEGER,inv_type INTEGER ,inv_date TEXT,qty INTEGER,price NUMERIC,qty_oh INTEGER,notes TEXT,id_ic_reason INTEGER,modify_by INTEGER DEFAULT 1,modify_date TEXT,FOREIGN KEY(id_product) REFERENCES products(id),FOREIGN KEY(id_ic_reason) REFERENCES inventory_check_reasons(id))");
        sqLiteDatabase.execSQL("create table "+TABLE_CATEGORY+"(id INTEGER PRIMARY KEY AUTOINCREMENT,category_name TEXT)");
        sqLiteDatabase.execSQL("create table "+TABLE_INVENTORY_CHECK+"(id INTEGER PRIMARY KEY AUTOINCREMENT,reason TEXT,is_active INTEGER DEFAULT 1)");


        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (1,'Appliances');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (2,'Electronics');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (3,'Vehicles');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (4,'Furniture');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (5,'Office Products');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (6,'Sports');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (7,'Toys');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (8,'Books');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (9,'Clothing,shoes,jewelry');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (10,'Home and Kitchen');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (11,'Industrial and Scientific');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (12,'Tools and Home improvement');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (13,'Computer Accessories');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (14,'Grocery and Gourmet foods');");
        sqLiteDatabase.execSQL("insert into "+ TABLE_CATEGORY+" values (15,'Other');");

        sqLiteDatabase.execSQL("insert into "+ TABLE_INVENTORY_CHECK+" values (1,'Product is missing',1);");
        sqLiteDatabase.execSQL("insert into "+ TABLE_INVENTORY_CHECK+" values (2,'Damage product',1);");
        sqLiteDatabase.execSQL("insert into "+ TABLE_INVENTORY_CHECK+" values (3,'Other',1);");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_SUPPLIER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_INVENTORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_INVENTORY_CHECK);
        onCreate(sqLiteDatabase);
    }

    public boolean insert_product_data(String product_name, String product_sku, String product_description, int product_category, byte[] product_img, Integer supplier_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("product_name",product_name);
        cv.put("sku",product_sku);
        cv.put("description",product_description);
        cv.put("id_category",product_category);
        cv.put("image",product_img);
        cv.put("id_supplier",supplier_id);
        long ans = db.insert(TABLE_PRODUCT,null,cv);

        if(ans == -1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor get_product_Data(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_PRODUCT,null);
        return res;
    }

    public Cursor get_separate_data(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }


    public boolean update_product_data(String id, String product_name, String product_sku, String product_description, int product_category, byte[] product_img, Integer supplier_id, Integer qty_oh){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("product_name",product_name);
        cv.put("sku",product_sku);
        cv.put("description",product_description);
        cv.put("id_category",product_category);
        cv.put("image",product_img);
        cv.put("id_supplier",supplier_id);
        cv.put("qty_on_hand",qty_oh);
        cv.put("id",id);
        db.update(TABLE_PRODUCT,cv,"ID = ?",new String[]{id});
        return true;
    }


    public Integer delete_product_data(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCT,"ID = ?", new String[]{id});
    }





    public boolean insert_supplier(String supplier_name,String supplier_code,String supplier_phone,String supplier_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("supplier_name",supplier_name);
        cv.put("code",supplier_code);
        cv.put("phone",supplier_phone);
        cv.put("email",supplier_email);
        long ans = db.insert(TABLE_SUPPLIER,null,cv);

        if(ans == -1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor getSupplier_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_SUPPLIER,null);
        return res;
    }

    public boolean update_product_qty(int qty_oh,int p_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("qty_on_hand",qty_oh);
        cv.put("id",p_id);
        db.update(TABLE_PRODUCT,cv,"ID = ?",new String[]{String.valueOf(p_id)});
        return true;
    }


//    UPDATE product_details SET qty_oh = 10 WHERE id = 3




    public boolean update_supplier_data(String id,String supplier_name,String supplier_code,String supplier_phone,String supplier_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("supplier_name",supplier_name);
        cv.put("code",supplier_code);
        cv.put("phone",supplier_phone);
        cv.put("email",supplier_email);
        cv.put("id",id);
        db.update(TABLE_SUPPLIER,cv,"ID = ?",new String[]{id});
        return true;
    }


    public Integer delete_supplier_Data(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SUPPLIER,"ID = ?", new String[]{id});
    }




    public boolean insert_inventory_data(int id_product,int inv_type,String inv_date,int qty,int price,int qty_oh,String notes,Integer id_ic_reason,String modify_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_product ",id_product);
        cv.put("inv_type",inv_type);
        cv.put("inv_date",inv_date);
        cv.put("qty",qty);
        cv.put("price",price);
        cv.put("qty_oh",qty_oh);
        cv.put("notes",notes);
        cv.put("id_ic_reason",id_ic_reason);
        cv.put("modify_date ",modify_date);
        long ans = db.insert(TABLE_INVENTORY,null,cv);
        if(ans == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getCategory_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_CATEGORY,null);
        return res;
    }



    public Cursor getICReasonData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_INVENTORY_CHECK,null);
        return res;
    }


    @SuppressLint("Range")
    public String getSupplierName(int supplierId) {
        String supplierName = "";
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"supplier_name"};
        String selection = "products.id_supplier = ? AND suppliers.id = products.id_supplier";
        String[] selectionArgs = {String.valueOf(supplierId)};
        Cursor cursor = db.query("suppliers INNER JOIN products", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            supplierName = cursor.getString(cursor.getColumnIndex("supplier_name"));
        }
        cursor.close();
        return supplierName;
    }

    @SuppressLint("Range")
    public String getCategoryName(int categoryId) {
        String categoryName = "";
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"category_name"};
        String selection = "products.id_category = ? AND category.id = products.id_category";
        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor cursor = db.query("category INNER JOIN products", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(cursor.getColumnIndex("category_name"));
        }
        cursor.close();
        return categoryName;
    }



}
