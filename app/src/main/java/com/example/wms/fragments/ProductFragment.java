package com.example.wms.fragments;



import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.wms.bottomsheet.ScanNfcBottomSheet;
import com.example.wms.data.DBHelper;
import com.example.wms.R;
import com.example.wms.activity.BarcodeActivity;
import com.example.wms.adapters.ProductViewAdapter;
import com.example.wms.dialog.AddProductDialog;
import com.example.wms.interfaces.ItemSelectInterface;
import com.example.wms.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.ArrayList;


public class ProductFragment extends Fragment implements ItemSelectInterface {


    private ArrayList<Product> mListProduct;
    private RecyclerView mRecyclerView;
    private View rootView;
    private FloatingActionButton floatingActionButton;
    private ProductViewAdapter productViewAdapter;
    private DBHelper dbHelper;
    private SearchView searchView;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_product, container, false);
        dbHelper = new DBHelper(getContext());
        mRecyclerView = rootView.findViewById(R.id.recyclerview_product);
        mListProduct = new ArrayList<>();
        floatingActionButton = rootView.findViewById(R.id.btn_add_product);
        productViewAdapter = new ProductViewAdapter(getContext(),mListProduct,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(productViewAdapter);

        displaydata();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogFragment = AddProductDialog.newInstance(new ProductDataCallback() {
                    @Override
                    public void isInserted(boolean isInserted) {
                        if (isInserted){
                            displaydata();
                        }
                    }
                });
                dialogFragment.show(getFragmentManager(),"Product");
            }
        });

        return rootView;
    }



    private void displaydata() {
        Cursor cursor = dbHelper.get_product_Data();
        mListProduct.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String p_name = cursor.getString(1);
            String p_sku = cursor.getString(2);
            String p_desc = cursor.getString(3);
            int p_cate = cursor.getInt(4);
            byte[] p_image = cursor.getBlob(5);
            int p_s_id = cursor.getInt(6);
            int qty_oh= cursor.getInt(7);
            mListProduct.add(new Product(id,p_name,p_sku,p_desc,p_cate,p_image,p_s_id,qty_oh));
        }
        productViewAdapter.updateproductdata(mListProduct);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
     //   SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Product> filteredlist = new ArrayList<Product>();

        // running a for loop to compare elements.
        for (Product item : mListProduct) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getProduct_name().toLowerCase().contains(text.toLowerCase())||item.getSku().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
//           Toast.makeText(getContext(), "No Product Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            productViewAdapter.filterList(filteredlist);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.qr_code:
                Intent i = new Intent(getContext(), BarcodeActivity.class);
                this.startActivityForResult(i,2001);
                return true;

            case R.id.nfc_tag:
                ScanNfcBottomSheet scanNfcBottomSheet = new ScanNfcBottomSheet(new ScanNfcBottomSheet.NfcScanListener() {
                    @Override
                    public void onNfcScan(boolean isSuccess, @Nullable String serialNo, @Nullable String tagText, String message) {
                        if (searchView != null && tagText!=null) {
                            searchView.setQuery(tagText, true);
                        }
                    }
                });
                scanNfcBottomSheet.show(getFragmentManager(), "Product Fragment");

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("product_name")) {
                String QrData = data.getExtras().getString("product_name");
                if (searchView != null) {
                    searchView.setQuery(QrData,true);
                }
            }
        } else if (requestCode == 888){
                Uri imageUri=data.getData();
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(requireContext(), this);
        }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== Activity.RESULT_OK)
            {
                Uri resultUri = result.getUri();
                productViewAdapter.imageView.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }

        }
    }

    @Override
    public void onItemSelect(View view, int position, Object object) {
        if (object instanceof Product) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            this.startActivityForResult(Intent.createChooser(intent, "Select Image"), 888);
        }
    }

    public interface ProductDataCallback {
        public void isInserted(boolean isInserted);
    }


}