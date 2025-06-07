package com.example.wms.bottomsheet;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wms.R;
import com.example.wms.adapters.ProductViewAdapter;
import com.example.wms.data.DBHelper;
import com.example.wms.models.ICReason;
import com.example.wms.models.Product;
import com.example.wms.models.Inventory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InventoryActionBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private TextView select_action,select_reason;
    private Button inventory_in,inventory_out,inventory_ic;
    private TextInputLayout edt_enter_quantity,edt_enter_price,edt_notes;
    private MaterialButton inventory_btn_save;

    private ArrayList<Inventory> inventoryArrayList;
    private ArrayList<Product> productArrayList;
    private int product_position;
    private DBHelper dbHelper;

    private int INVENTORY_IN = 1;
    private int INVENTORY_OUT = 2;
    private int INVENTORY_IC = 3;
    private int INVENTORY_IC_PRICE = 0;


    private ProductViewAdapter.productdataupdate productdataupdate;
    private Dialog dialog;
    private ArrayList<ICReason> icReasonArrayList = new ArrayList<>();
    private Integer ic_reason_id;


    public InventoryActionBottomSheet(ArrayList<Product> product,int position,ProductViewAdapter.productdataupdate productdataupdate){
        this.productdataupdate = productdataupdate;
        this.productArrayList = product;
        this.product_position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.bottom_sheet_inventory_details,container,false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return bottomSheetDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        select_action = view.findViewById(R.id.select_action);
        select_reason = view.findViewById(R.id.select_reason);
        inventory_in = view.findViewById(R.id.inventory_in);
        inventory_out = view.findViewById(R.id.inventory_out);
        inventory_ic = view.findViewById(R.id.inventory_ic);
        edt_enter_quantity = view.findViewById(R.id.edt_enter_quantity);
        edt_enter_price = view.findViewById(R.id.edt_enter_price);
        edt_notes = view.findViewById(R.id.edt_notes);
        inventory_btn_save = view.findViewById(R.id.inventory_btn_save);
        inventoryArrayList = new ArrayList<>();
        dbHelper = new DBHelper(getContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        inventory_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_enter_quantity.setVisibility(View.VISIBLE);
                edt_enter_price.setVisibility(View.VISIBLE);
                edt_notes.setVisibility(View.VISIBLE);
                inventory_btn_save.setVisibility(View.VISIBLE);
                select_reason.setVisibility(View.GONE);
                select_action.setText("Inventory In");

                inventory_btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (validateIn()){

                            //Data get in product table
                            int product_id = productArrayList.get(product_position).getId();
                            int product_qty_on = productArrayList.get(product_position).getQty_on_hand();
                            String product_title_name = productArrayList.get(product_position).getProduct_name();

                            //data insert by user
                            String inventory_date = sdf.format(new Date());
                            int inventory_quantity = Integer.parseInt(edt_enter_quantity.getEditText().getText().toString());
                            int inventory_price = Integer.parseInt(edt_enter_price.getEditText().getText().toString());
                            String inventory_note = edt_notes.getEditText().getText().toString();

                            // update qty_oh in product table and inventory qty_oh
                            int qty_on_hand = product_qty_on+inventory_quantity;


                            boolean result = dbHelper.insert_inventory_data(product_id,INVENTORY_IN,inventory_date,inventory_quantity,inventory_price,qty_on_hand,inventory_note,null,inventory_date);
                            if(result){
                                Toast.makeText(getContext(), "Inventory added successfully!", Toast.LENGTH_SHORT).show();

                                boolean res = dbHelper.update_product_qty(qty_on_hand,product_id);
                                if (res){
                                    Toast.makeText(getContext(), product_title_name+" Quantity added successfully!", Toast.LENGTH_SHORT).show();
                                    productdataupdate.isInventoryInserted(true);
                                    dismiss();
                                }else{
                                    Toast.makeText(getContext(), "Quantity could not be added", Toast.LENGTH_SHORT).show();
                                    productdataupdate.isInventoryInserted(false);
                                    dismiss();
                                }

                            }else{
                                Toast.makeText(getContext(), "Inventory could not be added", Toast.LENGTH_SHORT).show();
                            }

                            edt_enter_price.getEditText().setText("");
                            edt_enter_quantity.getEditText().setText("");
                            edt_notes.getEditText().setText("");
                        }
                    }
                });
            }
        });


        inventory_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_enter_price.setVisibility(View.VISIBLE);
                edt_enter_quantity.setVisibility(View.VISIBLE);
                edt_notes.setVisibility(View.VISIBLE);
                inventory_btn_save.setVisibility(View.VISIBLE);
                select_reason.setVisibility(View.GONE);
                select_action.setText("Inventory Out");

                inventory_btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if (validateOut()){
                                //Data get in product table
                                int product_id = productArrayList.get(product_position).getId();
                                int product_qty_on = productArrayList.get(product_position).getQty_on_hand();
                                String product_title_name = productArrayList.get(product_position).getProduct_name();

                                //data insert by user
                                String inventory_date = sdf.format(new Date());
                                int inventory_quantity = Integer.parseInt(edt_enter_quantity.getEditText().getText().toString());
                                int inventory_price = Integer.parseInt(edt_enter_price.getEditText().getText().toString());
                                String inventory_note = edt_notes.getEditText().getText().toString();


                                    // update qty_oh in product table and inventory qty_oh
                                    int qty_on_hand = product_qty_on-inventory_quantity;


                                    boolean result = dbHelper.insert_inventory_data(product_id,INVENTORY_OUT,inventory_date,inventory_quantity,inventory_price,qty_on_hand,inventory_note,null,inventory_date);
                                    if(result){
                                        Toast.makeText(getContext(), "Inventory out successfully!", Toast.LENGTH_SHORT).show();
                                        boolean res = dbHelper.update_product_qty(qty_on_hand,product_id);
                                        if (res){
                                            Toast.makeText(getContext(), product_title_name+" Quantity Updated successfully!", Toast.LENGTH_SHORT).show();
                                            productdataupdate.isInventoryUpdated(true);
                                            dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Quantity could not be updated", Toast.LENGTH_SHORT).show();
                                            productdataupdate.isInventoryUpdated(false);
                                            dismiss();
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "Inventory could not be out", Toast.LENGTH_SHORT).show();
                                    }

                                    edt_enter_price.getEditText().setText("");
                                    edt_enter_quantity.getEditText().setText("");
                                    edt_notes.getEditText().setText("");


                            }
                    }
                });
            }
        });


        inventory_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_enter_price.setVisibility(View.GONE);

                edt_enter_quantity.setVisibility(View.VISIBLE);
                inventory_btn_save.setVisibility(View.VISIBLE);
                select_reason.setVisibility(View.VISIBLE);
                edt_notes.setVisibility(View.VISIBLE);
                select_action.setText("Inventory Check");

                select_reason.setOnClickListener(new View.OnClickListener() {
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

                        TextView select_ic_text = dialog.findViewById(R.id.select_supplier_text);
                        EditText edit_search_supplier = dialog.findViewById(R.id.edit_search_supplier);
                        ListView listView = dialog.findViewById(R.id.supplier_list_view);
                        select_ic_text.setText("Select Reason");

//                        String ic_reason[] = getResources().getStringArray(R.array.ic_reason);

                        Cursor cursor1 = dbHelper.getICReasonData();
                        icReasonArrayList.clear();
                        while (cursor1.moveToNext()){
                            int ic_id = cursor1.getInt(0);
                            String ic_name = cursor1.getString(1);
                            icReasonArrayList.add(new ICReason(ic_id,ic_name));
                        }

                        ArrayAdapter<ICReason> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,icReasonArrayList);

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

                                ICReason ic_reason = (ICReason) listView.getItemAtPosition(i);
                                select_reason.setText(ic_reason.getReason());
                                ic_reason_id = ic_reason.getId();
                                dialog.dismiss();
                            }
                        });

                    }
                });



                inventory_btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (validateIC()){

                            //Data get in product table
                            int product_id = productArrayList.get(product_position).getId();
                            int product_qty_on = productArrayList.get(product_position).getQty_on_hand();
                            String product_title_name = productArrayList.get(product_position).getProduct_name();

                            // Data Enter by User
                            String inventory_date = sdf.format(new Date());
                            int inventory_quantity = Integer.parseInt(edt_enter_quantity.getEditText().getText().toString());
                            String inventory_note = edt_notes.getEditText().getText().toString();
                            String select_res = select_reason.getText().toString();

                            // update qty_oh in product table and inventory qty_oh
                            int qty_on_hand = product_qty_on-inventory_quantity;

                            boolean result = dbHelper.insert_inventory_data(product_id,INVENTORY_IC,inventory_date,inventory_quantity,INVENTORY_IC_PRICE,qty_on_hand,inventory_note,ic_reason_id,inventory_date);
                            if(result){
                                Toast.makeText(getContext(), "Inventory Check successfully!", Toast.LENGTH_SHORT).show();
                                boolean res = dbHelper.update_product_qty(qty_on_hand,product_id);
                                if (res){
                                    Toast.makeText(getContext(), product_title_name+" Quantity check successfully!", Toast.LENGTH_SHORT).show();
                                    productdataupdate.isInventoryIC(true);
                                    dismiss();
                                }else{
                                    Toast.makeText(getContext(), "Quantity could not be check", Toast.LENGTH_SHORT).show();
                                    productdataupdate.isInventoryIC(false);
                                    dismiss();
                                }
                            }else{
                                Toast.makeText(getContext(), "Inventory could not be check", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });



            }
        });

    }

    private boolean validateOut() {
        boolean isDataValid = true;
        edt_enter_quantity.setError(null);
        edt_notes.setError(null);
        edt_enter_price.setError(null);
        if (TextUtils.isEmpty(edt_enter_quantity.getEditText().getText().toString())) {
            isDataValid = false;
            edt_enter_quantity.setError("Required");
        } else if (TextUtils.isEmpty(edt_enter_price.getEditText().getText().toString())) {
            isDataValid = false;
            edt_enter_price.setError("Required");
        } else if (TextUtils.isEmpty(edt_notes.getEditText().getText().toString())){
            isDataValid = false;
            edt_notes.setError("Required");
        }
        return isDataValid;
    }

    private boolean validateIn() {
        boolean isDataValid = true;
        edt_enter_quantity.setError(null);
        edt_notes.setError(null);
        edt_enter_price.setError(null);
        if (TextUtils.isEmpty(edt_enter_quantity.getEditText().getText().toString())) {
            isDataValid = false;
            edt_enter_quantity.setError("Required");
        } else if (TextUtils.isEmpty(edt_enter_price.getEditText().getText().toString())) {
            isDataValid = false;
            edt_enter_price.setError("Required");
        } else if (TextUtils.isEmpty(edt_notes.getEditText().getText().toString())){
            isDataValid = false;
            edt_notes.setError("Required");
        }
        return isDataValid;
    }

    private boolean validateIC() {
        boolean isDataValid = true;
        if (TextUtils.isEmpty(edt_enter_quantity.getEditText().getText().toString())) {
            isDataValid = false;
            edt_enter_quantity.setError("Required");
        }else if (ic_reason_id==0){
            isDataValid = false;
            select_reason.setError("Required");
        }
        return isDataValid;
    }








}
