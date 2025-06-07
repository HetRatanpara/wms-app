package com.example.wms.adapters;


import static com.example.wms.activity.MainActivity.mImageViewToByte;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wms.bottomsheet.InventoryActionBottomSheet;
import com.example.wms.data.DBHelper;
import com.example.wms.R;
import com.example.wms.interfaces.ItemSelectInterface;
import com.example.wms.models.Category;
import com.example.wms.models.Product;
import com.example.wms.models.Supplier;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.MyViewHolder> {

    private final Context mContext;
    private ArrayList<Product> mProductList;
    private DBHelper dbHelper;
    private ItemSelectInterface itemSelectInterface;

    private Dialog select_supplier_dialog,select_category_dialog;
    int product_supplier_id,product_category_id;
    ArrayList<Supplier> supplierArrayList = new ArrayList<>();
    ArrayList<Category> categoryArrayList = new ArrayList<>();

    public ImageView imageView;
    private ImageButton fullscreen_dialog_save,fullscreen_dialog_close;
    private TextInputLayout product_name_set,sku_number_set,product_description_set,product_qty_oh_set;
    private TextView select_supplier,product_category_set;
    public Dialog dialog;


    public ProductViewAdapter(Context context, ArrayList<Product> mProductList,ItemSelectInterface itemSelectInterface){
        this.mContext = context;
        this.mProductList = mProductList;
        this.itemSelectInterface = itemSelectInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_row,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    public void filterList(ArrayList<Product> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        mProductList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public void updateproductdata(ArrayList<Product> productList){
        this.mProductList = productList;
        notifyDataSetChanged();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {


        int position = holder.getAdapterPosition();
        Integer txt_product_id = mProductList.get(position).getId();
        String txt_product_name = mProductList.get(position).getProduct_name();
        String txt_product_sku = mProductList.get(position).getSku();
        String txt_product_desc = mProductList.get(position).getDescription();
        Integer txt_product_qty_oh = mProductList.get(position).getQty_on_hand();
        int txt_product_category_id = mProductList.get(position).getId_category();
        int txt_product_suppiler_id  = mProductList.get(position).getId_supplier();
        String txt_supplier_name = dbHelper.getSupplierName(txt_product_suppiler_id);
        String txt_category_name = dbHelper.getCategoryName(txt_product_category_id);


        holder.txt_product_name.setText(txt_product_name);
        holder.txt_product_sku.setText(txt_product_sku);
        holder.txt_product_category.setText(txt_category_name);

        byte[] recordImage = mProductList.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0, recordImage.length);
        holder.product_image.setImageBitmap(bitmap);

//        holder.product_image.setImageResource(mProductList.get(position).getProduct_image());
//        byte[] recordImage = mProductList.get(position).getProduct_image();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0, recordImage.length);
//        holder.product_image.setImageBitmap(bitmap);


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.add_product_dialog);


                imageView = dialog.findViewById(R.id.imageView);
                fullscreen_dialog_close = dialog.findViewById(R.id.fullscreen_dialog_close);
                fullscreen_dialog_save = dialog.findViewById(R.id.fullscreen_dialog_save);

                TextView add_product_dialog = dialog.findViewById(R.id.add_product_dialog);
                product_name_set = dialog.findViewById(R.id.product_name);
                sku_number_set = dialog.findViewById(R.id.sku_number);
                product_description_set = dialog.findViewById(R.id.product_description);
                product_category_set = dialog.findViewById(R.id.product_category);
                product_qty_oh_set = dialog.findViewById(R.id.product_qty_oh);
                select_supplier = dialog.findViewById(R.id.select_supplier);


                product_name_set.setEnabled(false);
                sku_number_set.setEnabled(false);
                product_description_set.setEnabled(false);
                product_description_set.getEditText().setMaxLines(10);
                product_category_set.setEnabled(false);
                select_supplier.setEnabled(false);
                product_qty_oh_set.setEnabled(false);
                fullscreen_dialog_save.setVisibility(View.GONE);
                product_qty_oh_set.setVisibility(View.VISIBLE);


                add_product_dialog.setText(txt_product_name);
                product_name_set.getEditText().setText(txt_product_name);
                sku_number_set.getEditText().setText(txt_product_sku);
                product_description_set.getEditText().setText(txt_product_desc);
                product_category_set.setText(txt_category_name);
                select_supplier.setText(txt_supplier_name);
                product_qty_oh_set.getEditText().setText(txt_product_qty_oh.toString());
                imageView.setImageBitmap(bitmap);

                fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });




        holder.btn_product_list_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog  = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.add_product_dialog);
                TextView add_product_dialog = dialog.findViewById(R.id.add_product_dialog);

                imageView = dialog.findViewById(R.id.imageView);
                fullscreen_dialog_close = dialog.findViewById(R.id.fullscreen_dialog_close);
                fullscreen_dialog_save = dialog.findViewById(R.id.fullscreen_dialog_save);
                product_name_set = dialog.findViewById(R.id.product_name);
                sku_number_set = dialog.findViewById(R.id.sku_number);
                product_description_set = dialog.findViewById(R.id.product_description);
                product_category_set = dialog.findViewById(R.id.product_category);
                select_supplier = dialog.findViewById(R.id.select_supplier);


                add_product_dialog.setText(txt_product_name);
                product_name_set.getEditText().setText(txt_product_name);
                sku_number_set.getEditText().setText(txt_product_sku);
                product_description_set.getEditText().setText(txt_product_desc);
                product_category_set.setText(txt_category_name);
                select_supplier.setText(txt_supplier_name);
                imageView.setImageBitmap(bitmap);



                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemSelectInterface.onItemSelect(view,position,mProductList.get(position));
                    }
                });


                Cursor cursor = dbHelper.getSupplier_data();
                supplierArrayList.clear();
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String s_name = cursor.getString(1);
                    String s_code = cursor.getString(2);
                    String s_email = cursor.getString(3);
                    String s_phone = cursor.getString(4);
                    supplierArrayList.add(new Supplier(id,s_name,s_code,s_email,s_phone));
                }


                ArrayAdapter<Supplier> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, supplierArrayList);

                select_supplier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        select_supplier_dialog=new Dialog(mContext);

                        // set custom dialog
                        select_supplier_dialog.setContentView(R.layout.dialog_searchable_spinner);

                        // set custom height and width
                        select_supplier_dialog.getWindow().setLayout(650,800);

                        // set transparent background
                        select_supplier_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        // show dialog
                        select_supplier_dialog.show();


                        EditText edit_search_supplier = select_supplier_dialog.findViewById(R.id.edit_search_supplier);
                        ListView listView = select_supplier_dialog.findViewById(R.id.supplier_list_view);


                        listView.setAdapter(adapter);

                        edit_search_supplier.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Supplier supplier = (Supplier) listView.getItemAtPosition(i);
                                //print supplier name into the textview
                                select_supplier.setText(supplier.getSupplier_name());
                                //find id into the table for the display data
                                product_supplier_id = supplier.getId();

                                select_supplier_dialog.dismiss();
                            }
                        });

                    }
                });


                Cursor cursor1 = dbHelper.getCategory_data();
                categoryArrayList.clear();
                while (cursor1.moveToNext()){
                    int c_id = cursor1.getInt(0);
                    String c_name = cursor1.getString(1);
                    categoryArrayList.add(new Category(c_id,c_name));
                }



                ArrayAdapter<Category> adapter1 = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, categoryArrayList);



                product_category_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        select_category_dialog = new Dialog(mContext);

                        // set custom dialog
                        select_category_dialog.setContentView(R.layout.dialog_searchable_spinner);

                        // set custom height and width
                        select_category_dialog.getWindow().setLayout(650,800);

                        // set transparent background
                        select_category_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        select_category_dialog.show();

                        TextView category_title = select_category_dialog.findViewById(R.id.select_supplier_text);
                        EditText edit_search_category = select_category_dialog.findViewById(R.id.edit_search_supplier);
                        ListView listView_category = select_category_dialog.findViewById(R.id.supplier_list_view);

                        category_title.setText("Select Category");

                        listView_category.setAdapter(adapter1);



                        edit_search_category.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapter1.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        listView_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Category category = (Category) listView_category.getItemAtPosition(i);
                                product_category_set.setText(category.getCategory_name());
                                product_category_id = category.getId();

                                select_category_dialog.dismiss();
                            }
                        });
                    }

                });


                fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                fullscreen_dialog_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (validateData()){
                            String edt_id = String.valueOf(mProductList.get(position).getId());
                            String edt_p_name = product_name_set.getEditText().getText().toString();
                            String edt_p_sku = sku_number_set.getEditText().getText().toString();
                            String edt_p_desc = product_description_set.getEditText().getText().toString();
                            int qty_on = mProductList.get(position).getQty_on_hand();
                            byte[] add_p_image = mImageViewToByte(imageView);

                            boolean resupdate = dbHelper.update_product_data(edt_id,edt_p_name,edt_p_sku,edt_p_desc,product_category_id,add_p_image,product_supplier_id,qty_on);

                            if (resupdate == true){
                                Toast.makeText(mContext, "Product Update Successfully!", Toast.LENGTH_SHORT).show();

                                mProductList.set(position,new Product(mProductList.get(position).getId(),edt_p_name,edt_p_sku,edt_p_desc,product_category_id,add_p_image,product_supplier_id,qty_on));
                                notifyItemChanged(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(mContext, "Product could not be update", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });



        holder.btn_product_list_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt_id = String.valueOf(txt_product_id);
                String product_title = mProductList.get(position).getProduct_name();
                AlertDialog.Builder dialogDelete= new AlertDialog.Builder(mContext);
                dialogDelete.setTitle("Warning");
                dialogDelete.setMessage(Html.fromHtml("Are you sure you want to delete <b>"+ product_title+"</b>?"));
                dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int ans=  dbHelper.delete_product_data(edt_id);
                        if (ans>0){
                            Toast.makeText(view.getContext(), "Product is Deleted", Toast.LENGTH_SHORT).show();
                            mProductList.remove(position);
                            notifyItemRemoved(position);
                        }else {
                            Toast.makeText(view.getContext(), "Product is not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogDelete.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialogDelete.show();
            }
        });


        holder.btn_product_list_item_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InventoryActionBottomSheet inventoryActionBottomSheet = new InventoryActionBottomSheet(mProductList, position, new productdataupdate() {
                    @Override
                    public void isInventoryInserted(boolean isInventoryInserted) {
                        if (isInventoryInserted){
                            productUpdate();
                        }
                    }

                    @Override
                    public void isInventoryUpdated(boolean isInventoryUpdated) {
                        if (isInventoryUpdated){
                            productUpdate();
                        }
                    }

                    @Override
                    public void isInventoryIC(boolean isInventoryIC) {
                        if (isInventoryIC){
                            productUpdate();
                        }
                    }
                });

                inventoryActionBottomSheet.show(((FragmentActivity)mContext).getSupportFragmentManager(),inventoryActionBottomSheet.getTag());
            }
        });
    }



    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView txt_product_name,txt_product_category,txt_product_sku;
        private final ImageView product_image;
        private final CardView main_layout;
        private final Button btn_product_list_item_delete,btn_product_list_item_edit,btn_product_list_item_action;
        private final ConstraintLayout constraintLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            dbHelper = new DBHelper(mContext);
            txt_product_name = itemView.findViewById(R.id.text_product_item_name);
            txt_product_category = itemView.findViewById(R.id.text_product_item_category);
            txt_product_sku = itemView.findViewById(R.id.text_product_item_sku);
            product_image = itemView.findViewById(R.id.preview_product_image);
            main_layout = itemView.findViewById(R.id.main_layout);
            btn_product_list_item_delete = itemView.findViewById(R.id.btn_product_list_item_delete);
            btn_product_list_item_edit = itemView.findViewById(R.id.btn_product_list_item_edit);
            btn_product_list_item_action = itemView.findViewById(R.id.btn_product_list_item_action);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Product product = mProductList.get(position);
        }


    }


    public void productUpdate() {
        Cursor cursor = dbHelper.get_product_Data();
        mProductList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String p_name = cursor.getString(1);
            String p_sku = cursor.getString(2);
            String p_desc = cursor.getString(3);
            int p_cate = cursor.getInt(4);
            byte[] p_image = cursor.getBlob(5);
            int p_s_id = cursor.getInt(6);
            int qty_oh= cursor.getInt(7);
            mProductList.add(new Product(id,p_name,p_sku,p_desc,p_cate,p_image,p_s_id,qty_oh));
        }
        updateproductdata(mProductList);
           /* if(mListProduct.size()==0) {
            Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }*/
    }

    public interface productdataupdate{
        public void isInventoryInserted(boolean isInventoryInserted);
        public void isInventoryUpdated(boolean isInventoryUpdated);
        public void isInventoryIC(boolean isInventoryIC);
    }


    private boolean validateData() {
        boolean isDataValid = true;
        product_name_set.setError(null);
        sku_number_set.setError(null);
        product_description_set.setError(null);
        select_supplier.setError(null);
        product_category_set.setError(null);
        if (TextUtils.isEmpty(product_name_set.getEditText().getText().toString())) {
            isDataValid = false;
            product_name_set.setError("Required");
        } else if (TextUtils.isEmpty(sku_number_set.getEditText().getText().toString())) {
            isDataValid = false;
            sku_number_set.setError("Required");
        } else if (TextUtils.isEmpty(product_description_set.getEditText().getText().toString())) {
            isDataValid = false;
            product_description_set.setError("Required");
        }else if (product_category_id==0){
            isDataValid = false;
            product_category_set.setError("Required");
        }else if(product_supplier_id==0){
            isDataValid = false;
            select_supplier.setError("Required");
        }
        return isDataValid;
    }



}







