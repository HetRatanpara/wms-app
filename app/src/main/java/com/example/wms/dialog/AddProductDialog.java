package com.example.wms.dialog;

import static com.example.wms.activity.MainActivity.mImageViewToByte;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wms.R;
import com.example.wms.data.DBHelper;
import com.example.wms.fragments.ProductFragment;
import com.example.wms.models.Category;
import com.example.wms.models.Supplier;
import com.google.android.material.textfield.TextInputLayout;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;




import java.util.ArrayList;

public class AddProductDialog extends DialogFragment implements View.OnClickListener {


    TextInputLayout edt_product_name,edt_product_sku,edt_product_description;
    TextView select_supplier,product_category;
    ImageView imageView;
    ImageButton close,save;
    View view;
    final int REQUEST_CODE_GALLERY = 999;
    DBHelper dbHelper;


    Dialog dialog;
    int product_supplier_id = 0;
    ArrayList<Supplier> supplierList = new ArrayList<>();

    int product_category_id = 0;
    ArrayList<Category> categoryArrayList = new ArrayList<>();
    Dialog category_select;

    boolean isDataEntered = false;
    private static ProductFragment.ProductDataCallback productDataCallback;


    public static AddProductDialog newInstance(ProductFragment.ProductDataCallback productDataCallback) {
        AddProductDialog.productDataCallback = productDataCallback;
        return new AddProductDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_product_dialog,container,false);

        dbHelper = new DBHelper(getContext());

        close = view.findViewById(R.id.fullscreen_dialog_close);
        save = view.findViewById(R.id.fullscreen_dialog_save);
        edt_product_name = view.findViewById(R.id.product_name);
        edt_product_sku = view.findViewById(R.id.sku_number);
        edt_product_description = view.findViewById(R.id.product_description);
        imageView = view.findViewById(R.id.imageView);
        product_category = view.findViewById(R.id.product_category);
        select_supplier = view.findViewById(R.id.select_supplier);


        Cursor cursor = dbHelper.getSupplier_data();
        supplierList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String s_name = cursor.getString(1);
            String s_code = cursor.getString(2);
            String s_email = cursor.getString(3);
            String s_phone = cursor.getString(4);
            supplierList.add(new Supplier(id,s_name,s_code,s_email,s_phone));
        }

        ArrayAdapter<Supplier> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, supplierList);

        select_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog=new Dialog(getContext());

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(650,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();


                EditText edit_search_supplier = dialog.findViewById(R.id.edit_search_supplier);
                ListView listView = dialog.findViewById(R.id.supplier_list_view);


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
                        select_supplier.setText(supplier.getSupplier_name());
                        product_supplier_id = supplier.getId();

                        dialog.dismiss();
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


        ArrayAdapter<Category> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoryArrayList);



        product_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                category_select = new Dialog(getContext());

                // set custom dialog
                category_select.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                category_select.getWindow().setLayout(650,800);

                // set transparent background
                category_select.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                category_select.show();

                TextView category_title = category_select.findViewById(R.id.select_supplier_text);
                EditText edit_search_category = category_select.findViewById(R.id.edit_search_supplier);
                ListView listView_category = category_select.findViewById(R.id.supplier_list_view);

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
                        product_category.setText(category.getCategory_name());
                        product_category_id = category.getId();

                        category_select.dismiss();
                    }
                });
            }

        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_GALLERY);

              /* if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_GALLERY);
               } else {
                   // permission not granted, request for permission
                  ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                 }*/
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    String edt_p_name = edt_product_name.getEditText().getText().toString();
                    String edt_p_sku = edt_product_sku.getEditText().getText().toString();
                    String edt_p_desc = edt_product_description.getEditText().getText().toString();
                    int p_c_id = product_category_id;
                    int p_s_id = product_supplier_id;
                    byte[] add_p_image = mImageViewToByte(imageView);

                    boolean result = dbHelper.insert_product_data(edt_p_name,edt_p_sku,edt_p_desc,p_c_id,add_p_image,p_s_id);

                    if (result == true){
                        Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                        productDataCallback.isInserted(true);
                        dismiss();
                    }else {
                        productDataCallback.isInserted(false);
                        Toast.makeText(getContext(), "Product could not be added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        close.setOnClickListener(this::onClick);

        return view;
    }

    private boolean validateData() {
        boolean isDataValid = true;
        edt_product_name.setError(null);
        edt_product_sku.setError(null);
        edt_product_description.setError(null);
        select_supplier.setError(null);
        product_category.setError(null);
        if (TextUtils.isEmpty(edt_product_name.getEditText().getText().toString())) {
            isDataValid = false;
            edt_product_name.setError("Required");
        } else if (TextUtils.isEmpty(edt_product_sku.getEditText().getText().toString())) {
            isDataValid = false;
            edt_product_sku.setError("Required");
        } else if (TextUtils.isEmpty(edt_product_description.getEditText().getText().toString())) {
            isDataValid = false;
            edt_product_description.setError("Required");
        }else if (product_category_id==0){
            isDataValid = false;
            product_category.setError("Required");
        }else if(product_supplier_id==0){
            isDataValid = false;
            select_supplier.setError("Required");
        }
        return isDataValid;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.fullscreen_dialog_close:
                dismiss();
            break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_CODE_GALLERY)
        {
            if(requestCode == REQUEST_CODE_GALLERY && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getContext(), "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_GALLERY && resultCode== Activity.RESULT_OK) {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(requireContext(), this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== Activity.RESULT_OK)
            {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }

        }
    }
}
