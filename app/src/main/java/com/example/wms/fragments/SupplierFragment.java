package com.example.wms.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wms.R;
import com.example.wms.activity.BarcodeActivity;
import com.example.wms.adapters.SupplierViewAdapter;
import com.example.wms.bottomsheet.ScanNfcBottomSheet;
import com.example.wms.data.DBHelper;
import com.example.wms.dialog.AddSupplierDialog;
import com.example.wms.models.Supplier;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;


public class SupplierFragment extends Fragment {


    RecyclerView recyclerView;
    DBHelper dbHelper;
    FloatingActionButton floatingActionButton;
    SupplierViewAdapter supplierViewAdapter;
    ArrayList<Supplier> supplierList;
    private SearchView searchView;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supplier, container, false);
        dbHelper = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerview_suppiler);
        floatingActionButton = view.findViewById(R.id.btn_add_suppiler);
        supplierList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        supplierViewAdapter = new SupplierViewAdapter(getContext(), supplierList);
        recyclerView.setAdapter(supplierViewAdapter);

        displaydata();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = AddSupplierDialog.newInstance(new SupplierDataCallback() {
                    @Override
                    public void isInserted(boolean isInserted) {
                        if (isInserted) {
                            displaydata();
                        }
                    }
                });
                dialogFragment.show(getChildFragmentManager(), "Supplier");
            }
        });
        return view;
    }


    private void displaydata() {
        Cursor cursor = dbHelper.getSupplier_data();
        supplierList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String s_name = cursor.getString(1);
            String s_code = cursor.getString(2);
            String s_email = cursor.getString(3);
            String s_phone = cursor.getString(4);
            supplierList.add(new Supplier(id, s_name, s_code, s_email, s_phone));
        }
       supplierViewAdapter.updateDataSet(supplierList);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mRecyclerViewAdapter.getFilter().filter(newText);
                filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void filter(String newText) {
        ArrayList<Supplier> filteredlist = new ArrayList<Supplier>();

        // running a for loop to compare elements.
        for (Supplier item : supplierList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getSupplier_name().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||item.getCode().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
//            Toast.makeText(getContext(), "No Supplier Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            supplierViewAdapter.filterList(filteredlist);
        }
    }

    public void onResume() {
        super.onResume();
        // Refresh the Fragment here
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.qr_code:
                Intent i = new Intent(getContext(), BarcodeActivity.class);
                this.startActivityForResult(i, 2002);
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

                scanNfcBottomSheet.show(getFragmentManager(), "Supplier Fragment");


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2002 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("product_name")) {
                String QrData = data.getExtras().getString("product_name");
                if (searchView != null) {
                    searchView.setQuery(QrData, true);
                }
            }
        }
    }


    public interface SupplierDataCallback {
        public void isInserted(boolean isInserted);
    }
}